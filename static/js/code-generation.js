// Code Generation 페이지 JavaScript

let currentCode = '';
let chatHistory = [];

// 템플릿 예제들
const templates = {
    entity: `User Entity 클래스를 생성해주세요.
필드: id (Long), username (String), email (String), createdAt (LocalDateTime)
JPA 어노테이션을 포함하고 KTDS 개발 가이드라인을 준수해주세요.`,
    
    controller: `User 관리를 위한 REST Controller를 생성해주세요.
엔드포인트: GET /users, POST /users, PUT /users/{id}, DELETE /users/{id}
Spring Boot 어노테이션과 KTDS 명명규칙을 준수해주세요.`,
    
    service: `User Service 클래스를 생성해주세요.
메서드: findAll(), findById(Long id), save(User user), deleteById(Long id)
KTDS 개발 가이드라인과 트랜잭션 처리를 포함해주세요.`,
    
    repository: `User Repository 인터페이스를 생성해주세요.
JpaRepository를 상속받고 사용자 이름과 이메일로 검색하는 메서드를 포함해주세요.
KTDS 개발 표준을 준수해주세요.`,
    
    kafka: `Kafka 메시지 producer와 consumer를 생성해주세요.
Spring Boot Kafka 설정과 메시지 처리 로직을 포함해주세요.`,
    
    config: `Spring Boot 애플리케이션 설정 클래스를 생성해주세요.
Database, Security, Kafka 등의 설정을 포함해주세요.`
};

document.addEventListener('DOMContentLoaded', function() {
    // 채팅 폼 이벤트
    const chatForm = document.getElementById('chatForm');
    const messageInput = document.getElementById('messageInput');
    const sendButton = document.getElementById('sendButton');
    const chatMessages = document.getElementById('chatMessages');
    const codePreview = document.getElementById('codePreview');

    // 폼 제출 이벤트
    chatForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const message = messageInput.value.trim();
        if (message) {
            sendMessage(message);
            messageInput.value = '';
            adjustTextareaHeight(messageInput);
        }
    });

    // 엔터 키 이벤트 (Shift+Enter는 줄바꿈)
    messageInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            chatForm.dispatchEvent(new Event('submit'));
        }
    });

    // 텍스트 영역 자동 높이 조절
    messageInput.addEventListener('input', function() {
        adjustTextareaHeight(this);
    });

    // 초기 높이 설정
    adjustTextareaHeight(messageInput);
});

// 텍스트 영역 높이 자동 조절
function adjustTextareaHeight(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = Math.min(textarea.scrollHeight, 120) + 'px';
}

// 메시지 전송
function sendMessage(message) {
    // 사용자 메시지 추가
    addMessage(message, 'user');
    
    // 로딩 인디케이터 표시
    showTypingIndicator();
    
    // 버튼 비활성화
    const sendButton = document.getElementById('sendButton');
    sendButton.disabled = true;
    sendButton.innerHTML = '<i class="bi bi-hourglass-split"></i>';
    
    // 채팅 히스토리에 추가
    chatHistory.push({ role: 'user', content: message });
    
    // AI API 호출
    generateCode(message);
}

// 메시지 추가
function addMessage(content, sender) {
    const chatMessages = document.getElementById('chatMessages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}-message`;
    
    const currentTime = new Date().toLocaleTimeString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit'
    });
    
    const avatar = sender === 'user' ? 
        '<i class="bi bi-person-circle"></i>' : 
        '<i class="bi bi-robot"></i>';
    
    const senderName = sender === 'user' ? 'You' : 'AI Assistant';
    
    messageDiv.innerHTML = `
        <div class="message-avatar">
            ${avatar}
        </div>
        <div class="message-content">
            <div class="message-header">
                <span class="message-sender">${senderName}</span>
                <span class="message-time">${currentTime}</span>
            </div>
            <div class="message-text">
                ${content.replace(/\n/g, '<br>')}
            </div>
        </div>
    `;
    
    chatMessages.appendChild(messageDiv);
    scrollToBottom();
}

// 타이핑 인디케이터 표시
function showTypingIndicator() {
    const chatMessages = document.getElementById('chatMessages');
    const typingDiv = document.createElement('div');
    typingDiv.className = 'typing-indicator';
    typingDiv.id = 'typingIndicator';
    
    chatMessages.appendChild(typingDiv);
    scrollToBottom();
}

// 타이핑 인디케이터 제거
function hideTypingIndicator() {
    const typingIndicator = document.getElementById('typingIndicator');
    if (typingIndicator) {
        typingIndicator.remove();
    }
}

// 채팅 하단으로 스크롤
function scrollToBottom() {
    const chatMessages = document.getElementById('chatMessages');
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// 코드 생성 API 호출
async function generateCode(message) {
    try {
        const response = await fetch('/api/generate-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                message: message,
                history: chatHistory
            })
        });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || '코드 생성 중 오류가 발생했습니다.');
        }
        
        const data = await response.json();
        
        // 타이핑 인디케이터 제거
        hideTypingIndicator();
        
        if (data.success) {
            // AI 응답 메시지 구성
            let responseMessage = data.explanation || '코드가 생성되었습니다.';
            
            // 템플릿 사용 정보 추가
            if (data.templates_used) {
                responseMessage += `\n\n📚 ${data.templates_used}개의 개발 템플릿을 참고하여 코드를 생성했습니다.`;
            }
            
            // 의존성 정보 추가
            if (data.dependencies && data.dependencies.length > 0) {
                responseMessage += '\n\n📦 필요한 의존성:\n';
                data.dependencies.forEach(dep => {
                    responseMessage += `• ${dep}\n`;
                });
            }
            
            // 사용 예시 추가
            if (data.usage_example) {
                responseMessage += '\n\n💡 사용 예시:\n' + data.usage_example;
            }
            
            // AI 응답 메시지 추가
            addMessage(responseMessage, 'assistant');
            
            // 생성된 코드가 있으면 코드 미리보기 업데이트
            if (data.code) {
                updateCodePreview(data.code, data.language || 'java');
                currentCode = data.code;
                
                // 다운로드 및 복사 버튼 활성화
                document.getElementById('downloadBtn').disabled = false;
                document.getElementById('copyBtn').disabled = false;
            }
            
            // 채팅 히스토리에 추가
            chatHistory.push({ role: 'assistant', content: responseMessage });
            
        } else {
            throw new Error(data.error || '코드 생성에 실패했습니다.');
        }
        
    } catch (error) {
        console.error('Error:', error);
        hideTypingIndicator();
        addMessage(`❌ 오류: ${error.message}`, 'assistant');
    } finally {
        // 버튼 활성화
        const sendButton = document.getElementById('sendButton');
        sendButton.disabled = false;
        sendButton.innerHTML = '<i class="bi bi-send"></i>';
    }
}

// 코드 미리보기 업데이트
function updateCodePreview(code, language = 'java') {
    currentCode = code;
    const codePreview = document.getElementById('codePreview');
    
    codePreview.innerHTML = `
        <pre><code class="language-${language}">${escapeHtml(code)}</code></pre>
    `;
    
    // Prism.js로 코드 하이라이팅
    if (typeof Prism !== 'undefined') {
        Prism.highlightAll();
    }
    
    // 다운로드 및 복사 버튼 활성화
    document.getElementById('downloadBtn').disabled = false;
    document.getElementById('copyBtn').disabled = false;
}

// HTML 이스케이프
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 템플릿 사용
function useTemplate(templateType) {
    const template = templates[templateType];
    if (template) {
        const messageInput = document.getElementById('messageInput');
        messageInput.value = template;
        adjustTextareaHeight(messageInput);
        messageInput.focus();
    }
}

// 서비스 상태 확인
async function checkServiceStatus() {
    try {
        const response = await fetch('/api/code-generator/status');
        const data = await response.json();
        
        let statusIcon = '';
        let statusColor = '';
        
        if (data.status === 'healthy') {
            statusIcon = '✅';
            statusColor = 'text-success';
        } else if (data.status === 'unhealthy') {
            statusIcon = '⚠️';
            statusColor = 'text-warning';
        } else {
            statusIcon = '❌';
            statusColor = 'text-danger';
        }
        
        const statusMessage = `
            <div class="service-status">
                <h5>${statusIcon} 서비스 상태</h5>
                <div class="status-details">
                    <p><strong>전체 상태:</strong> <span class="${statusColor}">${data.status}</span></p>
                    <p><strong>Azure OpenAI:</strong> <span class="${data.azure_openai === 'connected' ? 'text-success' : 'text-danger'}">${data.azure_openai}</span></p>
                    <p><strong>Azure Search:</strong> <span class="${data.azure_search === 'connected' ? 'text-success' : 'text-danger'}">${data.azure_search}</span></p>
                    ${data.deployment ? `<p><strong>배포 모델:</strong> ${data.deployment}</p>` : ''}
                    ${data.index_name ? `<p><strong>검색 인덱스:</strong> ${data.index_name}</p>` : ''}
                    ${data.error ? `<p><strong>오류:</strong> <span class="text-danger">${data.error}</span></p>` : ''}
                </div>
            </div>
        `;
        
        addMessage(statusMessage, 'assistant');
        
    } catch (error) {
        console.error('서비스 상태 확인 중 오류:', error);
        addMessage('❌ 서비스 상태를 확인할 수 없습니다.', 'assistant');
    }
}

// 대화 초기화
function clearChat() {
    if (confirm('대화 내용을 모두 삭제하시겠습니까?')) {
        const chatMessages = document.getElementById('chatMessages');
        
        // 초기 환영 메시지만 남기고 모두 삭제
        chatMessages.innerHTML = `
            <div class="message assistant-message">
                <div class="message-avatar">
                    <i class="bi bi-robot"></i>
                </div>
                <div class="message-content">
                    <div class="message-header">
                        <span class="message-sender">AI Assistant</span>
                        <span class="message-time">${new Date().toLocaleTimeString('ko-KR', {
                            hour: '2-digit',
                            minute: '2-digit'
                        })}</span>
                    </div>
                    <div class="message-text">
                        안녕하세요! 저는 AI 코드 생성 어시스턴트입니다.<br>
                        어떤 코드를 생성해드릴까요? 다음과 같은 요청을 처리할 수 있습니다:
                        <ul class="mt-2 mb-0">
                            <li>Spring Boot Controller 생성</li>
                            <li>JPA Entity 클래스 생성</li>
                            <li>Service 클래스 생성</li>
                            <li>Repository 인터페이스 생성</li>
                            <li>테스트 코드 생성</li>
                            <li>기타 Java 코드 생성</li>
                        </ul>
                    </div>
                </div>
            </div>
        `;
        
        // 채팅 히스토리 초기화
        chatHistory = [];
        
        // 코드 미리보기 초기화
        clearCodePreview();
        
        showNotification('대화가 초기화되었습니다.', 'success');
    }
}

// 코드 미리보기 초기화
function clearCodePreview() {
    currentCode = '';
    const codePreview = document.getElementById('codePreview');
    codePreview.innerHTML = `
        <div class="text-center text-muted py-5">
            <i class="bi bi-code-slash display-1 mb-3"></i>
            <p>생성된 코드가 여기에 표시됩니다.</p>
        </div>
    `;
    
    // 버튼 비활성화
    document.getElementById('downloadBtn').disabled = true;
    document.getElementById('copyBtn').disabled = true;
}

// 코드 복사
function copyCode() {
    if (currentCode) {
        navigator.clipboard.writeText(currentCode).then(function() {
            showNotification('코드가 클립보드에 복사되었습니다.', 'success');
        }).catch(function(err) {
            console.error('복사 실패:', err);
            showNotification('코드 복사에 실패했습니다.', 'error');
        });
    }
}

// 코드 다운로드
function downloadCode() {
    if (currentCode) {
        const blob = new Blob([currentCode], { type: 'text/plain' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'generated_code.java';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
        
        showNotification('코드가 다운로드되었습니다.', 'success');
    }
}
