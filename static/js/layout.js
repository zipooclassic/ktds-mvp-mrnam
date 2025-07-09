// 레이아웃 관련 JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // 사이드바 요소들
    const sidebar = document.getElementById('sidebar');
    const sidebarOverlay = document.getElementById('sidebarOverlay');
    const sidebarToggle = document.getElementById('sidebarToggle');
    const mobileSidebarToggle = document.getElementById('mobileSidebarToggle');
    const desktopSidebarToggle = document.getElementById('desktopSidebarToggle');
    const mainContent = document.querySelector('.main-content');

    // 로컬 스토리지에서 사이드바 상태 로드
    let isCollapsed = localStorage.getItem('sidebar-collapsed') === 'true';
    
    // 초기 상태 설정
    if (isCollapsed) {
        sidebar.classList.add('collapsed');
        mainContent.classList.add('sidebar-collapsed');
    }
    
    // 토글 버튼 아이콘 업데이트 함수
    function updateToggleButtonIcon() {
        if (desktopSidebarToggle) {
            const toggleIcon = desktopSidebarToggle.querySelector('i');
            if (toggleIcon) {
                if (sidebar.classList.contains('collapsed')) {
                    toggleIcon.className = 'bi bi-chevron-right';
                    desktopSidebarToggle.setAttribute('data-tooltip', '메뉴 펼치기');
                } else {
                    toggleIcon.className = 'bi bi-chevron-left';
                    desktopSidebarToggle.setAttribute('data-tooltip', '메뉴 접기');
                }
            }
        }
    }
    
    // 초기 아이콘 설정
    updateToggleButtonIcon();

    // 데스크탑 사이드바 토글 함수 (접기/펼치기)
    function toggleDesktopSidebar() {
        sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('sidebar-collapsed');
        
        // 상태를 로컬 스토리지에 저장
        isCollapsed = sidebar.classList.contains('collapsed');
        localStorage.setItem('sidebar-collapsed', isCollapsed);
        
        // 토글 버튼 아이콘 변경
        updateToggleButtonIcon();
        
        // 디버깅을 위한 콘솔 로그
        console.log('사이드바 상태 변경:', isCollapsed ? '접힘' : '펼침');
    }

    // 모바일 사이드바 토글 함수 (보이기/숨기기)
    function toggleMobileSidebar() {
        sidebar.classList.toggle('show');
        sidebarOverlay.classList.toggle('show');
        document.body.classList.toggle('sidebar-open');
    }

    // 사이드바 닫기 함수 (모바일용)
    function closeMobileSidebar() {
        sidebar.classList.remove('show');
        sidebarOverlay.classList.remove('show');
        document.body.classList.remove('sidebar-open');
    }

    // 이벤트 리스너
    if (desktopSidebarToggle) {
        desktopSidebarToggle.addEventListener('click', toggleDesktopSidebar);
    }

    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', closeMobileSidebar);
    }

    if (mobileSidebarToggle) {
        mobileSidebarToggle.addEventListener('click', toggleMobileSidebar);
    }

    if (sidebarOverlay) {
        sidebarOverlay.addEventListener('click', closeMobileSidebar);
    }

    // ESC 키로 모바일 사이드바 닫기
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeMobileSidebar();
        }
    });

    // 윈도우 리사이즈 시 모바일 사이드바 상태 초기화
    window.addEventListener('resize', function() {
        if (window.innerWidth >= 992) {
            closeMobileSidebar();
        }
    });

    // 네비게이션 링크 활성화
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function() {
            // 모바일에서는 링크 클릭 시 사이드바 닫기
            if (window.innerWidth < 992) {
                closeSidebar();
            }
        });
    });

    // 스무스 스크롤 효과
    const smoothScrollLinks = document.querySelectorAll('a[href^="#"]');
    smoothScrollLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

    // 페이지 로드 애니메이션
    const contentArea = document.querySelector('.content-area');
    if (contentArea) {
        contentArea.style.opacity = '0';
        setTimeout(() => {
            contentArea.style.transition = 'opacity 0.3s ease';
            contentArea.style.opacity = '1';
        }, 100);
    }

    // 툴팁 초기화
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // 카드 호버 효과
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });
});

// 전역 유틸리티 함수들
window.showNotification = function(message, type = 'info') {
    // 알림 표시 함수
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    alertDiv.style.top = '20px';
    alertDiv.style.right = '20px';
    alertDiv.style.zIndex = '9999';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(alertDiv);
    
    // 5초 후 자동 제거
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.parentNode.removeChild(alertDiv);
        }
    }, 5000);
};

window.showLoadingSpinner = function(element) {
    // 로딩 스피너 표시
    const spinner = document.createElement('div');
    spinner.className = 'text-center';
    spinner.innerHTML = `
        <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    `;
    element.appendChild(spinner);
    return spinner;
};

window.hideLoadingSpinner = function(spinner) {
    // 로딩 스피너 숨기기
    if (spinner && spinner.parentNode) {
        spinner.parentNode.removeChild(spinner);
    }
};
