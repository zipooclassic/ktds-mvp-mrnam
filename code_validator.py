import os
import re
import shutil

from git import Repo
from openai import AzureOpenAI
from azure.core.credentials import AzureKeyCredential
from azure.search.documents import SearchClient
from azure.search.documents.models import VectorizedQuery
import sqlite3

try:
    from dotenv import load_dotenv
    load_dotenv()
except ImportError:
    print("dotenv 모듈이 설치되지 않았습니다. os.environ을 사용하여 환경 변수를 설정하세요.")

# --- 1. 설정 (Configuration) ---

# Git 리포지토리 설정
REPO_URL = os.getenv("REPO_URL", "https://github.com/zipooclassic/ktds-mvp-mrnam.git")
LOCAL_REPO_PATH = os.getenv("LOCAL_REPO_PATH", "./cloned_repo/ktds-mvp-mrnam")

# Azure AI Search (Vector DB) 정보
SEARCH_ENDPOINT = os.getenv("SEARCH_ENDPOINT", os.environ.get("SEARCH_ENDPOINT"))
SEARCH_KEY = os.getenv("SEARCH_KEY",os.environ.get("SEARCH_KEY"))
INDEX_NAME = os.getenv("INDEX_NAME",os.environ.get("INDEX_NAME"))  # Code Validator용 인덱스

# Azure OpenAI (Generative AI) 정보
AZURE_OPENAI_ENDPOINT = os.getenv("AZURE_OPENAI_ENDPOINT",os.environ.get("AZURE_OPENAI_ENDPOINT"))
AZURE_OPENAI_KEY = os.getenv("AZURE_OPENAI_KEY", os.environ.get("AZURE_OPENAI_KEY"))
AZURE_OPENAI_DEPLOYMENT = os.getenv("AZURE_OPENAI_DEPLOYMENT",os.environ.get("AZURE_OPENAI_DEPLOYMENT", "mrnam-text-embedding-3-small"))
OPENAI_API_VERSION = os.getenv("OPENAI_API_VERSION", "2024-02-01")

# 벡터 변환 모델 (Azure OpenAI 임베딩 사용)
EMBEDDING_MODEL = os.getenv("EMBEDDING_MODEL", "text-embedding-3-small")
# model = SentenceTransformer(MODEL_NAME)  # Azure OpenAI 임베딩으로 변경

# --- 2. 핵심 함수 정의 ---
def get_latest_changed_files():
    """Git 리포지토리에서 최신 커밋의 변경된 파일 목록을 가져옵니다."""
    if os.path.exists(LOCAL_REPO_PATH):
        repo = Repo(LOCAL_REPO_PATH)
        print("기존 리포지토리에서 최신 코드를 가져옵니다 (git pull)...")
        repo.remotes.origin.pull()
    else:
        print(f"'{REPO_URL}' 리포지토리를 '{LOCAL_REPO_PATH}'에 복제합니다 (git clone)...")
        Repo.clone_from(REPO_URL, LOCAL_REPO_PATH)
        repo = Repo(LOCAL_REPO_PATH)

    latest_commit = repo.head.commit
    parent_commit = latest_commit.parents[0] if latest_commit.parents else None
    
    # if not parent_commit:
        # print("비교할 부모 커밋이 없습니다. 리포지토리의 모든 .java 파일을 대상으로 합니다.")
        # 모든 Java 파일을 찾는 로직 (예시)
    java_files = []
    for root, _, files in os.walk(LOCAL_REPO_PATH):
        for file in files:
            if file.endswith(".java"):
                java_files.append(os.path.join(root, file))
    return java_files

    # print(f"\n최신 커밋 '{latest_commit.hexsha[:7]}'의 변경사항을 분석합니다.")
    # changed_files = []
    # for diff_item in latest_commit.diff(parent_commit):
    #     # 변경되거나 추가된 파일만 대상으로 함
    #     if diff_item.change_type in ['A', 'M'] and diff_item.a_path.endswith(".java"):
    #         full_path = os.path.join(repo.working_dir, diff_item.a_path)
    #         changed_files.append(full_path)
    #         print(f"  - 변경된 파일 감지: {diff_item.a_path}")
            
    return changed_files

def get_embedding_vector(text):
    """Azure OpenAI를 사용하여 텍스트를 벡터로 변환합니다."""
    client = AzureOpenAI(
        api_key=AZURE_OPENAI_KEY,
        azure_endpoint=AZURE_OPENAI_ENDPOINT,
        api_version=OPENAI_API_VERSION,
    )
    
    response = client.embeddings.create(
        input=text,
        model=EMBEDDING_MODEL
    )
    # print(f"임베딩 벡터 생성 완료: {len(response.data[0])} ")
    return response.data[0].embedding

def search_relevant_guides(query):
    """Azure AI Search에서 쿼리와 관련된 개발 가이드를 검색합니다."""
    search_client = SearchClient(SEARCH_ENDPOINT, INDEX_NAME, AzureKeyCredential(SEARCH_KEY))
    query_vector = get_embedding_vector(query)

    vector_query = VectorizedQuery(vector=query_vector, k_nearest_neighbors=3, fields="text_vector")

    results = search_client.search(
        search_text=None,
        vector_queries=[vector_query],
        select=["chunk_id", "title", "text_vector"]
    )
    
    guides = []
    for result in results:
        guides.append({
            "content": result['text_vector'],
            "source": f"{result['title']} {result['chunk_id']}페이지"
        })
    return guides

def validate_code_with_genai(file_path, code_content, relevant_guides):
    """Azure OpenAI를 사용하여 코드와 가이드라인을 비교하고 검증 결과를 생성합니다."""
    client = AzureOpenAI(
        api_key=AZURE_OPENAI_KEY,
        azure_endpoint=AZURE_OPENAI_ENDPOINT,
        api_version=OPENAI_API_VERSION,
    )

    # 가이드라인을 프롬프트에 포함하기 좋은 형태로 변환
    guide_texts = "\n\n".join([f"참조 가이드 ({g['source']}):\n{g['content']}" for g in relevant_guides])

    # AI에게 전달할 프롬프트
    prompt = f"""
    당신은 숙련된 코드 리뷰어입니다. 아래의 '개발 가이드라인'을 바탕으로 주어진 '소스 코드'가 규칙을 잘 준수했는지 검증해주세요.

    [개발 가이드라인]
    {guide_texts}

    [검증할 소스 코드 파일]
    - 파일 경로: {file_path}
    - 소스 코드 내용:
    ```java
    {code_content}
    ```

    [요청 작업]
    1. 파일명, 클래스명, 주요 메소드명 등이 가이드라인을 따르는지 확인하세요.
       파일명, 클래스명에는 CQRS 패턴이 적용된 명명규칙이 있습니다. 
       조회성은 Qry | 처리성은 Cmd| 접두사를 사용합니다.  
       단 Entity, Dto는 CQRS 패턴을 따르지 않습니다.  
       API, Contoller, Service, Repository, Client 등은 반드시 명명규칙을 따르도록 합니다.
       각각 Api, Ctrl, Svc, Repo, Clnt 등 접미사를 사용합니다.
       예시: `UserController`는 `UserCtrl`, `OrderService`는 `OrderSvc` 등으로 명명합니다.
    2. 만약 규칙을 위반한 사항이 있다면, 어떤 규칙을 어떻게 위반했는지 명확히 설명해주세요.
    3. 위반 사항을 설명할 때는 반드시 참조한 가이드의 출처(예: '개발가이드 2페이지')를 정확히 언급해주세요.
    4. 위반 사항이 있는 사항만 명시 해주세요.
    5. 위반 하지 않은 항목에 대해서는 기술하지 말아주세요.
    6. 모든 규칙을 잘 준수했다면 "코드 가이드라인을 모두 준수했습니다."라고 답변해주세요.
    7. 답변은 결과는 한국어로 작성해주세요. 특수문제는 제거해주세요.

    """
    
    try:
        response = client.chat.completions.create(
            model=AZURE_OPENAI_DEPLOYMENT,
            messages=[
                {"role": "system", "content": "You are a helpful and meticulous code reviewer."},
                {"role": "user", "content": prompt}
            ],
            temperature=0.1, # 일관성 있는 답변을 위해 낮은 온도로 설정
        )
        return response.choices[0].message.content
    except Exception as e:
        return f"Azure OpenAI 호출 중 오류 발생: {e}"

def analyze_file(file_path,cursor):
    """단일 파일을 분석, 검증하고 결과를 출력합니다."""
    print(f"분석할 파일: {file_path}")
    print("-" * 50)
    print(f"파일 분석 시작: {os.path.basename(file_path)}")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # 1. 파일명, 클래스명 등에서 검증 쿼리 추출
    # 이 예제에서는 간단히 '자바 클래스 명명규칙'을 검색 쿼리로 사용
    # 실제 시스템에서는 더 정교한 파싱(AST 등)을 통해 쿼리를 생성할 수 있음
    validation_query = "자바 클래스 명명 규칙"
    print(f"  - 관련 가이드 검색 중 (쿼리: '{validation_query}')...")
    
    # 2. Azure AI Search에서 관련 가이드 검색
    guides = search_relevant_guides(validation_query)
    if not guides:
        print("  - 관련된 개발 가이드를 찾을 수 없습니다.")
        return

    print(f"  - {len(guides)}개의 관련 가이드를 찾았습니다. AI로 검증을 시작합니다...")

    # 3. GenAI로 코드 검증
    validation_result = validate_code_with_genai(os.path.basename(file_path), content, guides)
    
    is_violation = "위반" in validation_result or "따르지 않" in validation_result

    # 3. 위반 상태에 따른 DB 처리
    # 현재 파일에 대해 'open' 상태인 위반이 있는지 확인
    cursor.execute("SELECT id FROM validations WHERE file_path = ? AND status = 'open'", (file_path,))
    open_violation = cursor.fetchone()
    
    repo = Repo(LOCAL_REPO_PATH)
    # 특정 파일의 마지막 커밋 정보 가져오기
    relative_path = os.path.relpath(file_path, repo.working_dir)
    commits = list(repo.iter_commits(paths=relative_path, max_count=1))
    
    if commits:
        file_last_commit = commits[0]
        file_author = file_last_commit.author.name
        file_commit_hash = file_last_commit.hexsha
    else:
        # 파일이 아직 커밋되지 않은 경우 현재 HEAD 커밋 정보 사용
        latest_commit = repo.head.commit
        file_author = latest_commit.author.name
        file_commit_hash = latest_commit.hexsha
    
    if is_violation:
        # Case 1: 위반 발생. 기존에 열린 위반이 없다면 새로 기록
        if not open_violation:
            print(f"  - [NEW VIOLATION] {os.path.basename(file_path)}")
            cursor.execute(
                """INSERT INTO validations (file_path, violation_commit_hash, author_name, violation_details, status) 
                    VALUES (?, ?, ?, ?, 'open')""",
                (file_path, file_commit_hash, file_author, validation_result)
            )
    else:
        # Case 2: 규칙 준수. 기존에 열린 위반이 있다면 'closed'로 변경
        if open_violation:
            print(f"  - [VIOLATION FIXED] {os.path.basename(file_path)}")
            cursor.execute(
                """UPDATE validations 
                    SET status = 'closed', resolved_commit_hash = ?, resolved_by_author = ?, resolved_at = CURRENT_TIMESTAMP
                    WHERE id = ?""",
                (file_commit_hash, file_author, open_violation[0])
            )
    repo.commit()
    


def main():
    # 설정 값 확인
    if not all([SEARCH_ENDPOINT, SEARCH_KEY, AZURE_OPENAI_ENDPOINT, AZURE_OPENAI_KEY, AZURE_OPENAI_DEPLOYMENT]):
        print("오류: .env 파일에 Azure 서비스 정보가 모두 설정되어 있는지 확인해주세요.")
        print("필요한 환경변수: SEARCH_ENDPOINT, SEARCH_KEY, AZURE_OPENAI_ENDPOINT, AZURE_OPENAI_KEY, AZURE_OPENAI_DEPLOYMENT")
    else:
        try:
            changed_files = get_latest_changed_files()
            print("\ngit repository에서 변경된 파일 목록을 가져왔습니다.")

            if not changed_files:
                print("\n분석할 변경된 .java 파일이 없습니다.")
            else:
                print(f"\n총 {len(changed_files)}개의 변경된 .java 파일을 분석합니다.")
                conn = sqlite3.connect('validation_results.db')
                cursor = conn.cursor()
                # 데이터베이스 연결 및 테이블 생성 (필요시)
                for i, file_path in enumerate(changed_files):
                    if i < 197:
                        print(f"\n[{i+1}/{len(changed_files)}] 파일 분석 중: {file_path}")
                        # 파일 분석 및 검증
                        analyze_file(file_path, cursor)
                        conn.commit()
                    else:
                        break  # 10개까지만 분석

                # 4. DB에 저장된 데이터 건수 조회 및 출력
                cursor.execute("SELECT COUNT(*) FROM validations WHERE status = 'open'")
                open_violations = cursor.fetchone()[0]
                
                cursor.execute("SELECT COUNT(*) FROM validations WHERE status = 'closed'")
                closed_violations = cursor.fetchone()[0]
                
                cursor.execute("SELECT COUNT(*) FROM validations")
                total_violations = cursor.fetchone()[0]   

                print("\n[전체 분석 완료 - DB 저장 현황]")
                print(f"  - 총 위반 기록: {total_violations}건")
                print(f"  - 미해결 위반: {open_violations}건")
                print(f"  - 해결된 위반: {closed_violations}건")
                print("-" * 50 + "\n")

                cursor.execute("SELECT violation_details FROM validations WHERE status = 'open'")
                open_violations_details = cursor.fetchall()

                if open_violations_details:
                    print("미해결 위반 사항:")
                    for detail in open_violations_details:
                        print(f"  - {detail[0]}")
                else:
                    print("미해결 위반 사항이 없습니다.")

                conn.commit()
                conn.close()
                print("\n모든 파일 분석이 완료되어 DB에 결과를 저장했습니다.")
        
        except Exception as e:
            print(f"\n스크립트 실행 중 오류가 발생했습니다: {e}")
        
        finally:
            # 로컬에 클론된 리포지토리 삭제 (선택사항)
            pass
            # if os.path.exists(LOCAL_REPO_PATH):
            #     print(f"\n작업 완료. 로컬 리포지토리 '{LOCAL_REPO_PATH}'를 삭제합니다.")
            #     shutil.rmtree(LOCAL_REPO_PATH)



# --- 3. 메인 실행 로직 ---

if __name__ == "__main__":
    main()
    # 스크립트가 직접 실행될 때만 main() 함수 호출  
    # (모듈로 import될 때는 실행되지 않도록)