import sqlite3
from datetime import datetime, timedelta
import random

def add_sample_data():
    conn = sqlite3.connect('validation_results.db')
    cursor = conn.cursor()
    
    # 기존 데이터 삭제
    cursor.execute("DELETE FROM validations")
    
    # 샘플 데이터 정의
    authors = ['김개발', '이코드', '박자바', '최파이썬', '정스프링']
    file_paths = [
        'src/main/java/com/kt/icis/BackendApplication.java',
        'src/main/java/com/kt/icis/oder/baseinfo/cbod/command/api/CbodController.java',
        'src/main/java/com/kt/icis/oder/baseinfo/ckbi/command/CkbiService.java',
        'src/main/java/com/kt/icis/oder/baseinfo/common/repository/BaseRepository.java',
        'src/main/java/com/kt/icis/oder/baseinfo/csab/query/CsabQueryHandler.java',
        'src/main/java/com/kt/icis/oder/baseinfo/csnh/command/CsnhCommandHandler.java',
        'src/main/java/com/kt/icis/oder/baseinfo/csnl/command/CsnlCommand.java'
    ]
    
    # 위반 유형별 샘플 데이터
    violation_samples = {
        '명명규칙': [
            '클래스명이 PascalCase를 준수하지 않습니다: cbodController → CbodController',
            '메서드명이 camelCase를 준수하지 않습니다: GetUserData → getUserData',
            '변수명이 camelCase를 준수하지 않습니다: user_name → userName',
            '상수명이 UPPER_SNAKE_CASE를 준수하지 않습니다: maxCount → MAX_COUNT',
            '패키지명에 대문자가 사용되었습니다: com.kt.ICIS → com.kt.icis',
            '인터페이스명에 접두사 I가 사용되었습니다: IUserService → UserService'
        ],
        'CQRS 패턴': [
            'Command와 Query가 같은 클래스에 정의되어 있습니다. 분리가 필요합니다.',
            'CommandHandler에서 데이터 조회 로직이 발견되었습니다. QueryHandler로 분리하세요.',
            'Query 객체에서 데이터 변경 작업이 수행되고 있습니다.',
            'Command 객체가 반환값을 가지고 있습니다. void 또는 CommandResult를 사용하세요.',
            'QueryHandler에서 데이터베이스 쓰기 작업이 수행되고 있습니다.',
            'Command와 Query가 동일한 데이터베이스 연결을 사용하고 있습니다.'
        ],
        '클래스 구조': [
            '클래스의 책임이 너무 많습니다. 단일 책임 원칙(SRP)을 위반합니다.',
            '의존성 주입이 생성자가 아닌 필드에서 이루어지고 있습니다.',
            'public 접근자가 과도하게 사용되었습니다. 캡슐화를 고려하세요.',
            '상속보다는 컴포지션을 사용하는 것이 좋습니다.',
            '인터페이스를 구현하지 않고 구체 클래스에 직접 의존하고 있습니다.',
            '클래스가 너무 큽니다. 여러 클래스로 분리를 고려하세요.'
        ],
        '기타': [
            '주석이 없거나 부적절합니다.',
            '예외 처리가 누락되었습니다.',
            '로그 레벨이 부적절합니다.',
            '하드코딩된 값이 발견되었습니다.',
            '사용되지 않는 import 문이 있습니다.',
            '코드 중복이 발견되었습니다.'
        ]
    }
    
    # 현재 시간 기준으로 지난 30일 동안의 데이터 생성
    current_time = datetime.now()
    
    sample_data = []
    violation_id = 1
    
    for days_ago in range(30, 0, -1):
        # 하루에 1-5개의 위반 발생
        daily_violations = random.randint(1, 5)
        
        for _ in range(daily_violations):
            created_at = current_time - timedelta(days=days_ago, hours=random.randint(0, 23), minutes=random.randint(0, 59))
            
            # 위반 유형 랜덤 선택
            violation_type = random.choice(list(violation_samples.keys()))
            violation_detail = random.choice(violation_samples[violation_type])
            
            # 기본 위반 정보
            violation_data = {
                'file_path': random.choice(file_paths),
                'violation_commit_hash': f'commit_{violation_id:04d}',
                'author_name': random.choice(authors),
                'violation_details': violation_detail,
                'created_at': created_at.strftime('%Y-%m-%d %H:%M:%S'),
                'status': 'open'
            }
            
            # 70% 확률로 해결됨
            if random.random() < 0.7:
                resolved_at = created_at + timedelta(days=random.randint(1, 7), hours=random.randint(0, 23))
                if resolved_at <= current_time:
                    violation_data['status'] = 'closed'
                    violation_data['resolved_commit_hash'] = f'fix_{violation_id:04d}'
                    violation_data['resolved_by_author'] = random.choice(authors)
                    violation_data['resolved_at'] = resolved_at.strftime('%Y-%m-%d %H:%M:%S')
            
            sample_data.append(violation_data)
            violation_id += 1
    
    # 데이터베이스에 삽입
    for data in sample_data:
        if data['status'] == 'closed':
            cursor.execute("""
            INSERT INTO validations (
                file_path, violation_commit_hash, author_name, violation_details, 
                created_at, status, resolved_commit_hash, resolved_by_author, resolved_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                data['file_path'], data['violation_commit_hash'], data['author_name'], 
                data['violation_details'], data['created_at'], data['status'],
                data['resolved_commit_hash'], data['resolved_by_author'], data['resolved_at']
            ))
        else:
            cursor.execute("""
            INSERT INTO validations (
                file_path, violation_commit_hash, author_name, violation_details, 
                created_at, status
            ) VALUES (?, ?, ?, ?, ?, ?)
            """, (
                data['file_path'], data['violation_commit_hash'], data['author_name'], 
                data['violation_details'], data['created_at'], data['status']
            ))
    
    conn.commit()
    
    # 생성된 데이터 통계 출력
    total_count = cursor.execute("SELECT COUNT(*) FROM validations").fetchone()[0]
    open_count = cursor.execute("SELECT COUNT(*) FROM validations WHERE status = 'open'").fetchone()[0]
    closed_count = cursor.execute("SELECT COUNT(*) FROM validations WHERE status = 'closed'").fetchone()[0]
    
    print(f"샘플 데이터 생성 완료:")
    print(f"  - 총 위반 건수: {total_count}")
    print(f"  - 미해결 건수: {open_count}")
    print(f"  - 해결 건수: {closed_count}")
    
    # 위반 유형별 통계 출력
    print(f"\n위반 유형별 통계:")
    for violation_type in violation_samples.keys():
        count = 0
        for data in sample_data:
            if any(keyword in data['violation_details'] for keyword in violation_samples[violation_type]):
                count += 1
        print(f"  - {violation_type}: {count}건")
    
    conn.close()

if __name__ == '__main__':
    add_sample_data()
