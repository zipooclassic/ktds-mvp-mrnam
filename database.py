import sqlite3

def init_db():
    conn = sqlite3.connect('validation_results.db')
    cursor = conn.cursor()
    
    # 기존 테이블 삭제
    cursor.execute("DROP TABLE IF EXISTS validations")
    
    # 확장된 스키마로 테이블 재생성
    cursor.execute("""
    CREATE TABLE validations (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        file_path TEXT NOT NULL,
        
        -- 위반 발생 정보
        violation_commit_hash TEXT NOT NULL,
        author_name TEXT NOT NULL,
        violation_details TEXT,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        
        -- 조치 정보
        status TEXT NOT NULL DEFAULT 'open', -- 'open' 또는 'closed'
        resolved_commit_hash TEXT,
        resolved_by_author TEXT,
        resolved_at DATETIME
    )
    """)
    
    print("'validations' 테이블이 새로운 스키마로 성공적으로 생성되었습니다.")
    
    conn.commit()
    conn.close()

if __name__ == '__main__':
    init_db()