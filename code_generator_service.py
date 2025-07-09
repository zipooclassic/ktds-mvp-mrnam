import os
import json
from typing import List, Dict, Any, Optional


from azure.core.credentials import AzureKeyCredential
from azure.search.documents import SearchClient
from azure.search.documents.models import VectorizedQuery
from openai import AzureOpenAI
import logging

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)



# 환경 변수 로드 (local에서는 .env 파일 사용, Azure에서는 환경 변수 설정)
try:
    from dotenv import load_dotenv
    load_dotenv()   
except ImportError:
    logger.warning("dotenv 모듈이 설치되지 않았습니다. os.environ을 사용하여 환경 변수를 설정하세요.")



class CodeGeneratorService:
    """
    Azure AI Search와 Azure OpenAI를 이용한 코드 생성 서비스
    """
    
    def __init__(self):
        """서비스 초기화 및 Azure 클라이언트 설정"""
        # Azure AI Search 설정
        self.search_endpoint = os.getenv("SEARCH_ENDPOINT",os.environ.get("SEARCH_ENDPOINT"))
        self.search_key = os.getenv("SEARCH_KEY", os.environ.get("SEARCH_KEY")) 
        self.index_name = os.getenv("INDEX_NAME", os.environ.get("INDEX_NAME"))
        
        # Azure OpenAI 설정
        self.azure_openai_endpoint = os.getenv("AZURE_OPENAI_ENDPOINT", os.environ.get("AZURE_OPENAI_ENDPOINT"))
        self.azure_openai_key = os.getenv("AZURE_OPENAI_KEY", os.environ.get("AZURE_OPENAI_KEY"))
        self.azure_openai_deployment = os.getenv("AZURE_OPENAI_DEPLOYMENT", os.environ.get("AZURE_OPENAI_DEPLOYMENT"))
        self.openai_api_version = os.getenv("OPENAI_API_VERSION", os.environ.get("OPENAI_API_VERSION", "2025-01-01-preview"))
        self.embedding_model = os.getenv("EMBEDDING_MODEL", "mrnam-text-embedding-3-small")
        
        # 설정 검증
        self._validate_configuration()
        
        # Azure OpenAI 클라이언트 초기화
        self.openai_client = AzureOpenAI(
            api_key=self.azure_openai_key,
            azure_endpoint=self.azure_openai_endpoint,
            api_version=self.openai_api_version,
        )
        
        # Azure AI Search 클라이언트 초기화
        self.search_client = SearchClient(
            self.search_endpoint, 
            self.index_name, 
            AzureKeyCredential(self.search_key)
        )
        
        logger.info("CodeGeneratorService 초기화 완료")
    
    def _validate_configuration(self) -> None:
        """필수 환경 변수 검증"""
        required_vars = [
            "SEARCH_ENDPOINT", "SEARCH_KEY", "INDEX_NAME",
            "AZURE_OPENAI_ENDPOINT", "AZURE_OPENAI_KEY", "AZURE_OPENAI_DEPLOYMENT"
        ]
        
        missing_vars = []
        for var in required_vars:
            if not os.getenv(var):
                missing_vars.append(var)
        
        if missing_vars:
            raise ValueError(f"필수 환경 변수가 누락되었습니다: {', '.join(missing_vars)}")
    
    def get_embedding_vector(self, text: str) -> List[float]:
        """
        텍스트를 Azure OpenAI 임베딩 모델을 사용하여 벡터로 변환
        
        Args:
            text (str): 벡터로 변환할 텍스트
            
        Returns:
            List[float]: 임베딩 벡터
        """
        try:
            response = self.openai_client.embeddings.create(
                input=text,
                model=self.embedding_model
            )
            
            vector = response.data[0].embedding
            logger.info(f"임베딩 벡터 생성 완료: {len(vector)}차원")
            return vector
            
        except Exception as e:
            logger.error(f"임베딩 벡터 생성 실패: {e}")
            raise Exception(f"임베딩 벡터 생성 중 오류 발생: {e}")
    
    def search_development_templates(self, query: str, k: int = 5) -> List[Dict[str, Any]]:
        """
        Azure AI Search에서 개발 템플릿 검색
        
        Args:
            query (str): 검색 쿼리
            k (int): 검색할 최대 결과 수
            
        Returns:
            List[Dict[str, Any]]: 검색된 템플릿 목록
        """
        try:
            logger.info(f"개발 템플릿 검색 시작: '{query}'")
            
            # 쿼리를 벡터로 변환
            query_vector = self.get_embedding_vector(query)
            
            # 벡터 검색 쿼리 생성
            vector_query = VectorizedQuery(
                vector=query_vector, 
                k_nearest_neighbors=k, 
                fields="text_vector"
            )
            
            # Azure AI Search에서 검색 수행
            results = self.search_client.search(
                search_text=None,
                vector_queries=[vector_query],
                select=["chunk_id", "title", "text_vector"],
                top=k
            )
            
            # 검색 결과 처리
            templates = []
            for result in results:
                template = {
                    "content": result.get('text_vector', ''),
                    "source": f"{result.get('title', 'Unknown')} {result.get('chunk_id', '')}페이지",
                    "score": result.get('@search.score', 0.0)
                }
                templates.append(template)
            
            logger.info(f"개발 템플릿 검색 완료: {len(templates)}개 결과")
            return templates
            
        except Exception as e:
            logger.error(f"개발 템플릿 검색 실패: {e}")
            raise Exception(f"개발 템플릿 검색 중 오류 발생: {e}")
    
    def generate_code_with_templates(self, user_request: str, templates: List[Dict[str, Any]]) -> Dict[str, Any]:
        """
        개발 템플릿을 참고하여 코드 생성
        
        Args:
            user_request (str): 사용자 요청
            templates (List[Dict[str, Any]]): 참고할 템플릿 목록
            
        Returns:
            Dict[str, Any]: 생성된 코드 정보
        """
        try:
            logger.info(f"코드 생성 시작: '{user_request[:50]}...'")
            
            # 템플릿을 프롬프트에 포함하기 좋은 형태로 변환
            template_texts = "\n\n".join([
                f"개발 템플릿 ({template['source']}):\n{template['content']}" 
                for template in templates
            ])
            
            # AI에게 전달할 프롬프트 구성
            prompt = self._build_code_generation_prompt(user_request, template_texts)
            
            # Azure OpenAI로 코드 생성
            response = self.openai_client.chat.completions.create(
                model=self.azure_openai_deployment,
                messages=[
                    {
                        "role": "system", 
                        "content": "You are a skilled Java developer who generates high-quality production-ready code based on development templates and KTDS coding standards."
                    },
                    {
                        "role": "user", 
                        "content": prompt
                    }
                ],
                temperature=0.2,  # 일관성 있는 답변을 위해 낮은 온도 설정
                max_tokens=4000,  # 최대 토큰 수 제한
            )
            
            # 응답 처리
            ai_response = response.choices[0].message.content
            result = self._parse_ai_response(ai_response)
            
            logger.info("코드 생성 완료")
            return result
            
        except Exception as e:
            logger.error(f"코드 생성 실패: {e}")
            return {
                "code": f"// 코드 생성 중 오류 발생: {e}",
                "language": "java",
                "explanation": f"Azure OpenAI 호출 중 오류 발생: {e}",
                "dependencies": [],
                "usage_example": ""
            }
    
    def _build_code_generation_prompt(self, user_request: str, template_texts: str) -> str:
        """
        코드 생성을 위한 프롬프트 구성
        
        Args:
            user_request (str): 사용자 요청
            template_texts (str): 템플릿 텍스트
            
        Returns:
            str: 구성된 프롬프트
        """
        return f"""
당신은 숙련된 Java 개발자입니다. 아래의 '개발 템플릿'을 최우선으로 참고하여 사용자의 요청에 맞는 코드를 생성해주세요.

[개발 템플릿 (최우선 참고)]
{template_texts}

[사용자 요청]
{user_request}

[코드 생성 요구사항]
1. 위의 개발 템플릿을 최우선으로 참고하여 코드를 생성하세요.
2. 참고된 템플릿의 내용은 반드시 포함되어야 합니다.
3. 참고된 템플릿의 내용이 없을 경우, 관련된 템플릿을 검색하여 포함하세요.
4. 참고된 템플릿을 사용하지 않고 코드를 생성할 경우, 템플릿을 검색하여 포함하세요.
5. 참고된 템플릿 위치를 알려주세요.
6. 템플릿에서 제공하는 패턴, 구조, 명명규칙을 따르세요.
7. KTDS 개발 가이드라인을 준수하세요:
   - CQRS 패턴 적용 (조회성: Qry, 처리성: Cmd 접두사)
   - 명명규칙 준수 (Controller→Ctrl, Service→Svc, Repository→Repo)
8. 적절한 주석과 설명을 포함하세요.
9. 에러 처리와 예외 상황을 고려하세요.
10. 코드의 확장성과 유지보수성을 고려하세요.
11. 보안과 성능을 고려한 코드를 작성하세요.
12. 참고된 템플릿 소스를 반드시 포함하세요.


[응답 형식]
다음 JSON 형식으로 응답해주세요:
{{
    "code": "생성된 코드 (문자열)",
    "language": "java",
    "explanation": "코드에 대한 상세한 설명",
    "dependencies": ["필요한 의존성 목록"],
    "usage_example": "사용 예시 코드"
}}
"""
    
    def _parse_ai_response(self, ai_response: str) -> Dict[str, Any]:
        """
        AI 응답을 파싱하여 구조화된 데이터로 변환
        
        Args:
            ai_response (str): AI 응답 텍스트
            
        Returns:
            Dict[str, Any]: 파싱된 결과
        """
        try:
            # JSON 파싱 시도
            result = json.loads(ai_response)
            
            # 필수 필드 검증 및 기본값 설정
            return {
                "code": result.get("code", ""),
                "language": result.get("language", "java"),
                "explanation": result.get("explanation", "코드가 생성되었습니다."),
                "dependencies": result.get("dependencies", []),
                "usage_example": result.get("usage_example", "")
            }
            
        except json.JSONDecodeError:
            logger.warning("AI 응답을 JSON으로 파싱할 수 없어 텍스트 형태로 반환합니다.")
            
            # JSON 파싱 실패 시 기본 형태로 반환
            return {
                "code": ai_response,
                "language": "java",
                "explanation": "코드가 생성되었습니다.",
                "dependencies": [],
                "usage_example": ""
            }
    
    def generate_code(self, user_request: str) -> Dict[str, Any]:
        """
        사용자 요청에 따라 코드 생성 (메인 메서드)
        
        Args:
            user_request (str): 사용자 요청
            
        Returns:
            Dict[str, Any]: 생성된 코드 정보
        """
        try:
            if not user_request or not user_request.strip():
                return {
                    "success": False,
                    "error": "요청 내용이 입력되지 않았습니다."
                }
            
            # 개발 템플릿 검색
            templates = self.search_development_templates(user_request)
            
            if not templates:
                return {
                    "success": False,
                    "error": "관련된 개발 템플릿을 찾을 수 없습니다. 다른 키워드로 시도해보세요."
                }
            
            # 템플릿을 참고하여 코드 생성
            result = self.generate_code_with_templates(user_request, templates)
            
            return {
                "success": True,
                "code": result.get("code", ""),
                "language": result.get("language", "java"),
                "explanation": result.get("explanation", ""),
                "dependencies": result.get("dependencies", []),
                "usage_example": result.get("usage_example", ""),
                "templates_used": len(templates)
            }
            
        except Exception as e:
            logger.error(f"코드 생성 처리 중 오류: {e}")
            return {
                "success": False,
                "error": str(e)
            }
    
    def get_service_status(self) -> Dict[str, Any]:
        """
        서비스 상태 확인
        
        Returns:
            Dict[str, Any]: 서비스 상태 정보
        """
        try:
            # Azure OpenAI 연결 테스트
            openai_status = self._test_openai_connection()
            
            # Azure AI Search 연결 테스트
            search_status = self._test_search_connection()
            
            return {
                "status": "healthy" if openai_status and search_status else "unhealthy",
                "azure_openai": "connected" if openai_status else "disconnected",
                "azure_search": "connected" if search_status else "disconnected",
                "deployment": self.azure_openai_deployment,
                "index_name": self.index_name
            }
            
        except Exception as e:
            logger.error(f"서비스 상태 확인 중 오류: {e}")
            return {
                "status": "error",
                "error": str(e)
            }
    
    def _test_openai_connection(self) -> bool:
        """Azure OpenAI 연결 테스트"""
        try:
            response = self.openai_client.chat.completions.create(
                model=self.azure_openai_deployment,
                messages=[{"role": "user", "content": "Hello"}],
                max_tokens=5
            )
            return True
        except Exception as e:
            logger.error(f"Azure OpenAI 연결 테스트 실패: {e}")
            return False
    
    def _test_search_connection(self) -> bool:
        """Azure AI Search 연결 테스트"""
        try:
            # 간단한 검색 테스트
            results = self.search_client.search(
                search_text="test",
                top=1
            )
            list(results)  # 결과를 실제로 가져와서 연결 확인
            return True
        except Exception as e:
            logger.error(f"Azure AI Search 연결 테스트 실패: {e}")
            return False


# 싱글톤 인스턴스 생성
try:
    code_generator = CodeGeneratorService()
    logger.info("CodeGeneratorService 싱글톤 인스턴스 생성 완료")
except Exception as e:
    logger.error(f"CodeGeneratorService 인스턴스 생성 실패: {e}")
    code_generator = None


def get_code_generator() -> Optional[CodeGeneratorService]:
    """
    CodeGeneratorService 인스턴스 반환
    
    Returns:
        Optional[CodeGeneratorService]: 서비스 인스턴스 또는 None
    """
    return code_generator
