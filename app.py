from flask import Flask, render_template, redirect, url_for, request, jsonify
import sqlite3
import json
from datetime import datetime, timedelta
import code_validator  # Assuming this is the module with your validation logic
from code_generator_service import get_code_generator
import logging

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)

# 코드 생성 서비스 인스턴스 가져오기
code_generator_service = get_code_generator()

if not code_generator_service:
    logger.error("CodeGeneratorService 초기화 실패")
else:
    logger.info("CodeGeneratorService 초기화 성공")

def get_db_connection():
    return sqlite3.connect('validation_results.db')

@app.route('/')
def index():
    # 기본 홈페이지는 Code Inspection으로 리다이렉트
    return redirect(url_for('code_inspection'))

@app.route('/code-generation')
def code_generation():
    return render_template('code_generation.html')

@app.route('/code-inspection')
def code_inspection():
    conn = get_db_connection()
    thirty_days_ago_str = (datetime.now() - timedelta(days=30)).strftime('%Y-%m-%d %H:%M:%S')

    # 기본값 설정
    stats = {
        "total_commits": 0,
        "total_violations": 0,
        "total_resolutions": 0,
        "avg_resolution_days": 0
    }
    
    author_stats = {
        "labels": [],
        "violations": [],
        "resolutions": [],
    }
    
    trend_stats = {
        "labels": [],
        "data": [],
    }
    
    violation_type_stats = {
        "labels": ["명명규칙", "CQRS 패턴", "클래스 구조", "기타"],
        "data": [40, 30, 20, 10]
    }
    
    daily_resolution_stats = {
        "labels": [],
        "data": []
    }
    
    history_log = []
    
    try:
        # --- 1. KPI 통계 계산 ---
        stats = {
            "total_commits": conn.execute("SELECT COUNT(DISTINCT violation_commit_hash) FROM validations WHERE created_at >= ?", (thirty_days_ago_str,)).fetchone()[0] or 0,
            "total_violations": conn.execute("SELECT COUNT(id) FROM validations WHERE created_at >= ?", (thirty_days_ago_str,)).fetchone()[0] or 0,
            "total_resolutions": conn.execute("SELECT COUNT(id) FROM validations WHERE status = 'closed' AND resolved_at >= ?", (thirty_days_ago_str,)).fetchone()[0] or 0,
        }
        avg_days_row = conn.execute("SELECT AVG(julianday(resolved_at) - julianday(created_at)) FROM validations WHERE status = 'closed' AND resolved_at IS NOT NULL").fetchone()
        stats['avg_resolution_days'] = avg_days_row[0] if avg_days_row and avg_days_row[0] is not None else 0

        # --- 2. 개발자별 통계 (차트용) ---
        author_violations = dict(conn.execute("SELECT author_name, COUNT(id) FROM validations GROUP BY author_name").fetchall())
        author_resolutions = dict(conn.execute("SELECT resolved_by_author, COUNT(id) FROM validations WHERE status = 'closed' GROUP BY resolved_by_author").fetchall())
        all_authors = sorted(list(set(author_violations.keys()) | set(author_resolutions.keys())))
        
        if all_authors:
            author_stats = {
                "labels": all_authors,
                "violations": [author_violations.get(author, 0) for author in all_authors],
                "resolutions": [author_resolutions.get(author, 0) for author in all_authors],
            }

        # --- 3. 월별 위반 트렌드 (차트용) ---
        trend_data = conn.execute("SELECT strftime('%Y-%m', created_at) as month, COUNT(id) FROM validations GROUP BY month ORDER BY month").fetchall()
        if trend_data:
            trend_stats = {
                "labels": [row[0] for row in trend_data],
                "data": [row[1] for row in trend_data],
            }
        
        # --- 4. 위반 유형별 분포 (상세 데이터 포함) ---
        # 키워드 기반으로 위반 유형별 데이터 조회
        violation_type_stats = {
            "labels": ["명명규칙", "CQRS 패턴", "클래스 구조", "기타"],
            "data": [0, 0, 0, 0],
            "details": {
                "명명규칙": [],
                "CQRS 패턴": [],
                "클래스 구조": [],
                "기타": []
            }
        }
        
        # 각 위반 유형별로 직접 데이터베이스 조회
        
        # 1. 명명규칙 위반
        naming_violations = conn.execute("""
            SELECT violation_details, file_path, author_name, created_at, status, violation_commit_hash
            FROM validations 
            WHERE created_at >= ? 
            AND (violation_details LIKE '%명명%' OR violation_details LIKE '%네이밍%' OR violation_details LIKE '%이름%' 
                 OR violation_details LIKE '%Naming%' OR violation_details LIKE '%name%')
            ORDER BY created_at DESC
        """, (thirty_days_ago_str,)).fetchall()
        
        for row in naming_violations:
            violation_type_stats["details"]["명명규칙"].append({
                "file_path": row[1] if row[1] else "",
                "author": row[2] if row[2] else "",
                "created_at": row[3] if row[3] else "",
                "status": row[4] if row[4] else "",
                "commit_hash": row[5] if row[5] else "",
                "description": row[0][:200] + "..." if row[0] and len(row[0]) > 200 else (row[0] or "")
            })
        
        # 2. CQRS 패턴 위반
        cqrs_violations = conn.execute("""
            SELECT violation_details, file_path, author_name, created_at, status, violation_commit_hash
            FROM validations 
            WHERE created_at >= ? 
            AND (violation_details LIKE '%CQRS%' OR violation_details LIKE '%Command%' OR violation_details LIKE '%Query%'
                 OR violation_details LIKE '%Cmd%' OR violation_details LIKE '%Qry%')
            ORDER BY created_at DESC
        """, (thirty_days_ago_str,)).fetchall()
        
        for row in cqrs_violations:
            violation_type_stats["details"]["CQRS 패턴"].append({
                "file_path": row[1] if row[1] else "",
                "author": row[2] if row[2] else "",
                "created_at": row[3] if row[3] else "",
                "status": row[4] if row[4] else "",
                "commit_hash": row[5] if row[5] else "",
                "description": row[0][:200] + "..." if row[0] and len(row[0]) > 200 else (row[0] or "")
            })
        
        # 3. 클래스 구조 위반
        class_violations = conn.execute("""
            SELECT violation_details, file_path, author_name, created_at, status, violation_commit_hash
            FROM validations 
            WHERE created_at >= ? 
            AND (violation_details LIKE '%클래스%' OR violation_details LIKE '%구조%' OR violation_details LIKE '%아키텍처%'
                 OR violation_details LIKE '%class%' OR violation_details LIKE '%structure%' OR violation_details LIKE '%responsibility%')
            ORDER BY created_at DESC
        """, (thirty_days_ago_str,)).fetchall()
        
        for row in class_violations:
            violation_type_stats["details"]["클래스 구조"].append({
                "file_path": row[1] if row[1] else "",
                "author": row[2] if row[2] else "",
                "created_at": row[3] if row[3] else "",
                "status": row[4] if row[4] else "",
                "commit_hash": row[5] if row[5] else "",
                "description": row[0][:200] + "..." if row[0] and len(row[0]) > 200 else (row[0] or "")
            })
        
        # 4. 기타 위반 (위의 3개 카테고리에 속하지 않는 것들)
        other_violations = conn.execute("""
            SELECT violation_details, file_path, author_name, created_at, status, violation_commit_hash
            FROM validations 
            WHERE created_at >= ? 
            AND NOT (violation_details LIKE '%명명%' OR violation_details LIKE '%네이밍%' OR violation_details LIKE '%이름%' 
                     OR violation_details LIKE '%Naming%' OR violation_details LIKE '%name%')
            AND NOT (violation_details LIKE '%CQRS%' OR violation_details LIKE '%Command%' OR violation_details LIKE '%Query%'
                     OR violation_details LIKE '%Cmd%' OR violation_details LIKE '%Qry%')
            AND NOT (violation_details LIKE '%클래스%' OR violation_details LIKE '%구조%' OR violation_details LIKE '%아키텍처%'
                     OR violation_details LIKE '%class%' OR violation_details LIKE '%structure%' OR violation_details LIKE '%responsibility%')
            ORDER BY created_at DESC
        """, (thirty_days_ago_str,)).fetchall()
        
        for row in other_violations:
            violation_type_stats["details"]["기타"].append({
                "file_path": row[1] if row[1] else "",
                "author": row[2] if row[2] else "",
                "created_at": row[3] if row[3] else "",
                "status": row[4] if row[4] else "",
                "commit_hash": row[5] if row[5] else "",
                "description": row[0][:200] + "..." if row[0] and len(row[0]) > 200 else (row[0] or "")
            })
        
        # 실제 데이터로 차트 데이터 업데이트
        violation_type_stats["data"] = [
            len(violation_type_stats["details"]["명명규칙"]),
            len(violation_type_stats["details"]["CQRS 패턴"]),
            len(violation_type_stats["details"]["클래스 구조"]),
            len(violation_type_stats["details"]["기타"])
        ]
        
        # 디버깅용 출력
        print(f"위반 유형별 통계:")
        print(f"  - 명명규칙: {len(violation_type_stats['details']['명명규칙'])}건")
        print(f"  - CQRS 패턴: {len(violation_type_stats['details']['CQRS 패턴'])}건")
        print(f"  - 클래스 구조: {len(violation_type_stats['details']['클래스 구조'])}건")
        print(f"  - 기타: {len(violation_type_stats['details']['기타'])}건")
        
        # --- 5. 일별 조치 현황 (최근 7일) ---
        daily_resolution_data = []
        daily_resolution_labels = []
        for i in range(6, -1, -1):
            date = datetime.now() - timedelta(days=i)
            date_str = date.strftime('%Y-%m-%d')
            count = conn.execute("SELECT COUNT(id) FROM validations WHERE status = 'closed' AND DATE(resolved_at) = ?", (date_str,)).fetchone()[0] or 0
            daily_resolution_data.append(count)
            daily_resolution_labels.append(date.strftime('%m/%d'))
        
        daily_resolution_stats = {
            "labels": daily_resolution_labels,
            "data": daily_resolution_data
        }
        
        # --- 6. 최근 활동 로그 ---
        recent_violations = conn.execute("SELECT created_at, 'open' as status, file_path, author_name, violation_commit_hash, violation_details FROM validations ORDER BY created_at DESC LIMIT 10").fetchall()
        recent_resolutions = conn.execute("SELECT resolved_at, 'closed' as status, file_path, resolved_by_author, resolved_commit_hash, 'Fixed' as details FROM validations WHERE status = 'closed' ORDER BY resolved_at DESC LIMIT 10").fetchall()
        
        # 두 리스트를 합치고 시간 순으로 정렬
        for row in recent_violations:
            if row[0]:  # created_at이 None이 아닌 경우
                try:
                    timestamp = datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
                    history_log.append({
                        'timestamp': timestamp, 
                        'status': row[1], 
                        'file_path': row[2], 
                        'author': row[3], 
                        'commit_hash': row[4], 
                        'details': row[5]
                    })
                except ValueError:
                    continue
                    
        for row in recent_resolutions:
            if row[0]:  # resolved_at이 None이 아닌 경우
                try:
                    timestamp = datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
                    history_log.append({
                        'timestamp': timestamp, 
                        'status': row[1], 
                        'file_path': row[2], 
                        'author': row[3], 
                        'commit_hash': row[4], 
                        'details': row[5]
                    })
                except ValueError:
                    continue
        
        history_log.sort(key=lambda x: x['timestamp'], reverse=True)
        
    except Exception as e:
        print(f"데이터베이스 조회 중 오류: {e}")
    finally:
        conn.close()
    
    return render_template('code_inspection.html', 
                         stats=stats, 
                         author_stats=author_stats, 
                         trend_stats=trend_stats, 
                         violation_type_stats=violation_type_stats,
                         daily_resolution_stats=daily_resolution_stats,
                         history=history_log[:15])


@app.route('/api/generate-code', methods=['POST'])
def generate_code():
    """코드 생성 API 엔드포인트"""
    try:
        # 코드 생성 서비스 사용 가능 확인
        if not code_generator_service:
            return jsonify({
                'success': False,
                'error': 'CodeGeneratorService가 초기화되지 않았습니다. 서버 설정을 확인해주세요.'
            }), 500
        
        # 요청 데이터 검증
        data = request.json
        if not data:
            return jsonify({
                'success': False,
                'error': 'JSON 데이터가 필요합니다.'
            }), 400
        
        user_message = data.get('message', '')
        if not user_message or not user_message.strip():
            return jsonify({
                'success': False,
                'error': '메시지가 입력되지 않았습니다.'
            }), 400
        
        logger.info(f"코드 생성 요청 접수: {user_message[:100]}...")
        
        # 코드 생성 서비스 호출
        result = code_generator_service.generate_code(user_message)
        
        if result.get('success'):
            logger.info("코드 생성 성공")
            return jsonify(result)
        else:
            logger.error(f"코드 생성 실패: {result.get('error')}")
            return jsonify(result), 404 if '템플릿' in result.get('error', '') else 500
    
    except Exception as e:
        logger.error(f"코드 생성 API 처리 중 오류: {e}")
        return jsonify({
            'success': False,
            'error': f'서버 오류가 발생했습니다: {str(e)}'
        }), 500

@app.route('/api/code-generator/status', methods=['GET'])
def get_code_generator_status():
    """코드 생성 서비스 상태 확인 API"""
    try:
        if not code_generator_service:
            return jsonify({
                'status': 'error',
                'error': 'CodeGeneratorService가 초기화되지 않았습니다.'
            }), 500
        
        status = code_generator_service.get_service_status()
        return jsonify(status)
    
    except Exception as e:
        logger.error(f"서비스 상태 확인 중 오류: {e}")
        return jsonify({
            'status': 'error',
            'error': str(e)
        }), 500


@app.route('/analyze', methods=['POST'])
def analyze():
    try:
        print("최신 커밋 분석 실행 중...")
        code_validator.main()  # Assuming this function runs the validation logic
        
        # 분석 완료 후 메인 페이지로 리다이렉트
        return redirect(url_for('code_inspection'))
    except Exception as e:
        print(f"분석 중 오류 발생: {e}")
        return redirect(url_for('code_inspection'))

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8000)
