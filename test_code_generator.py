#!/usr/bin/env python3
"""
코드 생성 서비스 테스트 스크립트
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from code_generator_service import get_code_generator
import json

def test_code_generator():
    """코드 생성 서비스 테스트"""
    print("=" * 60)
    print("코드 생성 서비스 테스트 시작")
    print("=" * 60)
    
    # 서비스 인스턴스 가져오기
    service = get_code_generator()
    
    if not service:
        print("❌ 서비스 초기화 실패")
        return
    
    print("✅ 서비스 초기화 성공")
    
    # 서비스 상태 확인
    print("\n1. 서비스 상태 확인:")
    print("-" * 40)
    status = service.get_service_status()
    print(json.dumps(status, indent=2, ensure_ascii=False))
    
    # 코드 생성 테스트
    print("\n2. 코드 생성 테스트:")
    print("-" * 40)
    
    test_requests = [
        "User Entity 클래스를 생성해주세요. id, username, email 필드를 포함해주세요.",
        "Spring Boot Controller를 생성해주세요. User 관리 기능을 포함해주세요.",
        "Kafka 연동 코드를 생성해주세요. 메시지 송신과 수신 기능을 포함해주세요."
    ]
    
    for i, request in enumerate(test_requests, 1):
        print(f"\n테스트 {i}: {request}")
        print("-" * 40)
        
        try:
            result = service.generate_code(request)
            
            if result.get('success'):
                print("✅ 코드 생성 성공")
                print(f"📊 사용된 템플릿: {result.get('templates_used', 0)}개")
                print(f"💬 설명: {result.get('explanation', '')[:100]}...")
                
                if result.get('dependencies'):
                    print(f"📦 의존성: {', '.join(result.get('dependencies', []))}")
                
                code = result.get('code', '')
                if code:
                    print(f"📝 코드 길이: {len(code)} 문자")
                    print("📄 코드 미리보기:")
                    print(code[:200] + "..." if len(code) > 200 else code)
                
            else:
                print("❌ 코드 생성 실패")
                print(f"오류: {result.get('error', '알 수 없는 오류')}")
                
        except Exception as e:
            print(f"❌ 테스트 중 오류 발생: {e}")
        
        print()
    
    print("=" * 60)
    print("테스트 완료")
    print("=" * 60)

if __name__ == "__main__":
    test_code_generator()
