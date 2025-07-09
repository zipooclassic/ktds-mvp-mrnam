import sqlite3
from datetime import datetime
import os

# 데이터베이스 파일 확인
if os.path.exists('validation_results.db'):
    conn = sqlite3.connect('validation_results.db')
    cursor = conn.cursor()
    
    # 테이블 존재 확인
    cursor.execute("SELECT name FROM sqlite_master WHERE type='table' AND name='validations'")
    table_exists = cursor.fetchone()
    
    if table_exists:
        print('validations 테이블이 존재합니다.')
        
        # 전체 데이터 개수 확인
        cursor.execute('SELECT COUNT(*) FROM validations')
        total_count = cursor.fetchone()[0]
        print(f'전체 레코드 수: {total_count}')
        
        if total_count > 0:
            # 최근 활동 데이터 확인
            cursor.execute('''
                SELECT id, created_at, status, file_path, author_name, violation_commit_hash, violation_details
                FROM validations 
                ORDER BY created_at DESC 
                LIMIT 5
            ''')
            recent_data = cursor.fetchall()
            
            print('\n최근 활동 데이터 (최대 5개):')
            for row in recent_data:
                print(f'ID: {row[0]}, 생성일: {row[1]}, 상태: {row[2]}, 파일: {row[3]}, 작성자: {row[4]}')
            
            # 해결된 항목 확인
            cursor.execute('''
                SELECT COUNT(*) FROM validations WHERE status = 'closed' AND resolved_at IS NOT NULL
            ''')
            resolved_count = cursor.fetchone()[0]
            print(f'\n해결된 항목 수: {resolved_count}')
            
            if resolved_count > 0:
                cursor.execute('''
                    SELECT id, resolved_at, file_path, resolved_by_author, resolved_commit_hash
                    FROM validations 
                    WHERE status = 'closed' AND resolved_at IS NOT NULL
                    ORDER BY resolved_at DESC 
                    LIMIT 3
                ''')
                resolved_data = cursor.fetchall()
                
                print('\n최근 해결된 항목들:')
                for row in resolved_data:
                    print(f'ID: {row[0]}, 해결일: {row[1]}, 파일: {row[2]}, 해결자: {row[3]}')
        else:
            print('데이터베이스에 데이터가 없습니다.')
    else:
        print('validations 테이블이 존재하지 않습니다.')
    
    conn.close()
else:
    print('validation_results.db 파일이 존재하지 않습니다.')
