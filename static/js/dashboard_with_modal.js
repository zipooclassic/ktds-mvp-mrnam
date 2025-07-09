// 전역 변수로 차트 데이터 저장
let chartData = {};

// 데이터 초기화
function initializeData() {
    // 템플릿에서 전달된 데이터 사용
    if (typeof window.chartRawData !== 'undefined') {
        console.log('Raw data from template:', window.chartRawData);
        
        chartData = {
            author: {
                labels: window.chartRawData.author_stats?.labels || [],
                violations: window.chartRawData.author_stats?.violations || [],
                resolutions: window.chartRawData.author_stats?.resolutions || []
            },
            trend: {
                labels: window.chartRawData.trend_stats?.labels || [],
                data: window.chartRawData.trend_stats?.data || []
            },
            violation_type: {
                labels: window.chartRawData.violation_type_stats?.labels || ['명명규칙', 'CQRS 패턴', '클래스 구조', '기타'],
                data: window.chartRawData.violation_type_stats?.data || [0, 0, 0, 0],
                details: window.chartRawData.violation_type_stats?.details || {}
            },
            daily_resolution: {
                labels: window.chartRawData.daily_resolution_stats?.labels || [],
                data: window.chartRawData.daily_resolution_stats?.data || []
            }
        };
        
        console.log('Chart data initialized:', chartData);
        console.log('Violation type details:', chartData.violation_type.details);
    } else {
        console.error('Chart raw data not found, using default data');
        // 기본 데이터 설정
        chartData = {
            author: {
                labels: ['개발자1', '개발자2', '개발자3'],
                violations: [5, 3, 8],
                resolutions: [2, 1, 4]
            },
            trend: {
                labels: ['2024-01', '2024-02', '2024-03'],
                data: [10, 15, 12]
            },
            violation_type: {
                labels: ['명명규칙', 'CQRS 패턴', '클래스 구조', '기타'],
                data: [5, 3, 8, 2],
                details: {}
            },
            daily_resolution: {
                labels: ['월', '화', '수', '목', '금'],
                data: [2, 1, 4, 3, 5]
            }
        };
    }
}

// 차트 생성 함수들
function createAuthorChart() {
    const ctx = document.getElementById('authorChart');
    if (!ctx) {
        console.error('authorChart element not found');
        return;
    }
    
    const chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: chartData.author.labels,
            datasets: [{
                label: '위반 건수',
                data: chartData.author.violations,
                backgroundColor: 'rgba(54, 162, 235, 0.5)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }, {
                label: '해결 건수',
                data: chartData.author.resolutions,
                backgroundColor: 'rgba(75, 192, 192, 0.5)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            onClick: (event, activeElements) => {
                if (activeElements.length > 0) {
                    const index = activeElements[0].index;
                    const author = chartData.author.labels[index];
                    const violations = chartData.author.violations[index];
                    const resolutions = chartData.author.resolutions[index];
                    
                    showModal('개발자별 통계', [
                        {
                            '개발자': author,
                            '위반 건수': violations,
                            '해결 건수': resolutions,
                            '해결률': violations > 0 ? `${((resolutions / violations) * 100).toFixed(1)}%` : '0%'
                        }
                    ]);
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function createTrendChart() {
    const ctx = document.getElementById('trendChart');
    if (!ctx) {
        console.error('trendChart element not found');
        return;
    }
    
    const chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: chartData.trend.labels,
            datasets: [{
                label: '월별 위반 건수',
                data: chartData.trend.data,
                borderColor: 'rgba(255, 99, 132, 1)',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            onClick: (event, activeElements) => {
                if (activeElements.length > 0) {
                    const index = activeElements[0].index;
                    const month = chartData.trend.labels[index];
                    const count = chartData.trend.data[index];
                    
                    showModal('월별 위반 트렌드', [
                        {
                            '월': month,
                            '위반 건수': count
                        }
                    ]);
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function createViolationTypeChart() {
    const ctx = document.getElementById('violationTypeChart');
    if (!ctx) {
        console.error('violationTypeChart element not found');
        return;
    }
    
    const chart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: chartData.violation_type.labels,
            datasets: [{
                data: chartData.violation_type.data,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.8)',
                    'rgba(54, 162, 235, 0.8)',
                    'rgba(255, 205, 86, 0.8)',
                    'rgba(75, 192, 192, 0.8)'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            onClick: (event, activeElements) => {
                if (activeElements.length > 0) {
                    const index = activeElements[0].index;
                    const violationType = chartData.violation_type.labels[index];
                    const count = chartData.violation_type.data[index];
                    const details = chartData.violation_type.details[violationType] || [];
                    
                    console.log('Violation type clicked:', violationType);
                    console.log('Details:', details);
                    
                    if (details.length > 0) {
                        showModal(`위반 유형: ${violationType}`, details);
                    } else {
                        showModal(`위반 유형: ${violationType}`, [
                            {
                                '위반 유형': violationType,
                                '건수': count,
                                '상세 정보': '해당 유형의 상세 정보가 없습니다.'
                            }
                        ]);
                    }
                }
            },
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

function createDailyResolutionChart() {
    const ctx = document.getElementById('resolutionChart');
    if (!ctx) {
        console.error('resolutionChart element not found');
        return;
    }
    
    const chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: chartData.daily_resolution.labels,
            datasets: [{
                label: '일별 해결 건수',
                data: chartData.daily_resolution.data,
                backgroundColor: 'rgba(75, 192, 192, 0.5)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            onClick: (event, activeElements) => {
                if (activeElements.length > 0) {
                    const index = activeElements[0].index;
                    const date = chartData.daily_resolution.labels[index];
                    const count = chartData.daily_resolution.data[index];
                    
                    showModal('일별 해결 현황', [
                        {
                            '날짜': date,
                            '해결 건수': count
                        }
                    ]);
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// 모달 표시 함수
function showModal(title, data) {
    console.log('Showing modal:', title, data);
    
    const modal = document.getElementById('chartModal');
    const modalTitle = document.getElementById('chartModalLabel');
    const modalBody = document.getElementById('modalContent');
    
    if (!modal || !modalTitle || !modalBody) {
        console.error('Modal elements not found');
        return;
    }
    
    modalTitle.textContent = title;
    
    if (data && data.length > 0) {
        let tableHTML = '<div class="table-responsive"><table class="table table-striped table-hover"><thead class="table-dark"><tr>';
        
        // 테이블 헤더 생성
        const headers = Object.keys(data[0]);
        headers.forEach(header => {
            tableHTML += `<th>${header}</th>`;
        });
        tableHTML += '</tr></thead><tbody>';
        
        // 테이블 바디 생성
        data.forEach(row => {
            tableHTML += '<tr>';
            headers.forEach(header => {
                let cellValue = row[header] || '';
                // 긴 텍스트는 truncate
                if (typeof cellValue === 'string' && cellValue.length > 100) {
                    cellValue = cellValue.substring(0, 100) + '...';
                }
                tableHTML += `<td>${cellValue}</td>`;
            });
            tableHTML += '</tr>';
        });
        
        tableHTML += '</tbody></table></div>';
        modalBody.innerHTML = tableHTML;
    } else {
        modalBody.innerHTML = '<p class="text-muted">데이터가 없습니다.</p>';
    }
    
    // Bootstrap Modal 표시
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
}

// KPI 카드 애니메이션
function animateKPICards() {
    const cards = document.querySelectorAll('.kpi-card');
    cards.forEach((card, index) => {
        setTimeout(() => {
            card.style.opacity = '0';
            card.style.transform = 'translateY(20px)';
            card.style.transition = 'all 0.5s ease';
            
            setTimeout(() => {
                card.style.opacity = '1';
                card.style.transform = 'translateY(0)';
            }, 100);
        }, index * 100);
    });
}

// 버튼 로딩 상태 관리
function setupAnalyzeButton() {
    const analyzeForm = document.getElementById('analyzeForm');
    const analyzeBtn = document.getElementById('analyzeBtn');
    
    if (analyzeForm && analyzeBtn) {
        analyzeForm.addEventListener('submit', function(e) {
            // 버튼 로딩 상태로 변경
            analyzeBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>분석 중...';
            analyzeBtn.disabled = true;
            
            // 프로그레스 오버레이 표시
            showProgressOverlay();
        });
    }
}

// 프로그레스 오버레이 표시
function showProgressOverlay() {
    const overlay = document.getElementById('progressOverlay');
    if (overlay) {
        overlay.style.display = 'flex';
        
        // 프로그레스 바 애니메이션
        const progressBar = overlay.querySelector('.progress-bar');
        if (progressBar) {
            let width = 0;
            const interval = setInterval(() => {
                width += Math.random() * 10;
                if (width >= 90) {
                    clearInterval(interval);
                    width = 90;
                }
                progressBar.style.width = width + '%';
            }, 200);
        }
    }
}

// JSON 토글 기능 (전역 함수)
function toggleJson(button) {
    const jsonContent = button.nextElementSibling;
    const jsonFormatted = jsonContent.nextElementSibling;
    
    if (jsonContent.style.display === 'none') {
        jsonContent.style.display = 'block';
        jsonFormatted.style.display = 'none';
        button.textContent = 'JSON 숨기기';
    } else {
        jsonContent.style.display = 'none';
        jsonFormatted.style.display = 'block';
        button.textContent = 'JSON 보기';
    }
}

// 전체 텍스트 토글 함수 (전역 함수)
function toggleFullText(button) {
    const fullText = button.nextElementSibling;
    
    if (fullText.style.display === 'none') {
        fullText.style.display = 'block';
        button.textContent = '접기';
    } else {
        fullText.style.display = 'none';
        button.textContent = '전체 보기';
    }
}

// 테이블 행 상세 정보 토글 함수 (전역 함수)
function toggleRowDetails(button) {
    const container = button.closest('.details-container');
    const preview = container.querySelector('.details-preview');
    const full = container.querySelector('.details-full');
    
    if (preview.style.display === 'none') {
        preview.style.display = 'block';
        full.style.display = 'none';
    } else {
        preview.style.display = 'none';
        full.style.display = 'block';
    }
}

// JSON 토글 설정
function setupJSONToggle() {
    document.querySelectorAll('.json-toggle').forEach(button => {
        button.addEventListener('click', function() {
            const targetId = this.getAttribute('data-target');
            const targetElement = document.getElementById(targetId);
            
            if (targetElement.style.display === 'none' || targetElement.style.display === '') {
                targetElement.style.display = 'block';
                this.textContent = 'JSON 숨기기';
            } else {
                targetElement.style.display = 'none';
                this.textContent = 'JSON 보기';
            }
        });
    });
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM content loaded');
    
    // 데이터 초기화
    initializeData();
    
    // 차트 생성
    createAuthorChart();
    createTrendChart();
    createViolationTypeChart();
    createDailyResolutionChart();
    
    // 기타 기능 초기화
    animateKPICards();
    setupAnalyzeButton();
    setupJSONToggle();
    
    console.log('Dashboard initialized');
});

// 전역 함수들을 window 객체에 추가
window.toggleJson = toggleJson;
window.toggleFullText = toggleFullText;
window.toggleRowDetails = toggleRowDetails;
