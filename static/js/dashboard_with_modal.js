// 전역 변수로 차트 데이터 저장
let chartData = {};
let isLoadingActivity = false;
let hasMoreActivity = true;
let currentPage = 1;
let totalActivityData = []; // 모든 활동 데이터를 저장

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
                labels: ['월', '화', '수', '목', '금', '토', '일'],
                data: [2, 1, 3, 0, 4, 1, 2]
            }
        };
    }
}

// 차트 생성 함수들
function createAuthorChart() {
    const ctx = document.getElementById('authorChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: chartData.author.labels,
            datasets: [
                {
                    label: '위반 건수',
                    data: chartData.author.violations,
                    backgroundColor: 'rgba(250, 112, 154, 0.8)',
                    borderColor: 'rgba(250, 112, 154, 1)',
                    borderWidth: 2,
                    borderRadius: 8,
                    borderSkipped: false,
                },
                {
                    label: '해결 건수',
                    data: chartData.author.resolutions,
                    backgroundColor: 'rgba(75, 172, 254, 0.8)',
                    borderColor: 'rgba(75, 172, 254, 1)',
                    borderWidth: 2,
                    borderRadius: 8,
                    borderSkipped: false,
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                intersect: false,
                mode: 'index'
            },
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        usePointStyle: true,
                        padding: 20,
                        font: {
                            size: 12,
                            weight: '600'
                        }
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    borderColor: '#ddd',
                    borderWidth: 1,
                    cornerRadius: 8,
                    displayColors: true
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)',
                        drawBorder: false
                    },
                    ticks: {
                        font: {
                            size: 11
                        }
                    }
                },
                x: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        font: {
                            size: 11
                        }
                    }
                }
            },
            onClick: (event, elements) => {
                console.log('Author chart clicked:', event, elements); // 디버깅용
                if (elements.length > 0) {
                    const elementIndex = elements[0].index;
                    const authorName = chartData.author.labels[elementIndex];
                    const violations = chartData.author.violations[elementIndex];
                    const resolutions = chartData.author.resolutions[elementIndex];
                    console.log('Author clicked:', authorName); // 디버깅용
                    showAuthorDetails(authorName, violations, resolutions);
                }
            }
        }
    });
}

function createTrendChart() {
    const ctx = document.getElementById('trendChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: chartData.trend.labels,
            datasets: [{
                label: '월별 위반 건수',
                data: chartData.trend.data,
                borderColor: 'rgba(102, 126, 234, 1)',
                backgroundColor: 'rgba(102, 126, 234, 0.1)',
                borderWidth: 3,
                fill: true,
                tension: 0.4,
                pointBackgroundColor: 'rgba(102, 126, 234, 1)',
                pointBorderColor: '#fff',
                pointBorderWidth: 2,
                pointRadius: 6,
                pointHoverRadius: 8
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                intersect: false,
                mode: 'index'
            },
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    borderColor: '#ddd',
                    borderWidth: 1,
                    cornerRadius: 8
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)',
                        drawBorder: false
                    },
                    ticks: {
                        font: {
                            size: 11
                        }
                    }
                },
                x: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        font: {
                            size: 11
                        }
                    }
                }
            },
            onClick: (event, elements) => {
                console.log('Trend chart clicked:', event, elements); // 디버깅용
                if (elements.length > 0) {
                    const elementIndex = elements[0].index;
                    const monthLabel = chartData.trend.labels[elementIndex];
                    const violationCount = chartData.trend.data[elementIndex];
                    console.log('Month clicked:', monthLabel, violationCount); // 디버깅용
                    showTrendDetails(monthLabel, violationCount);
                }
            }
        }
    });
}

function createViolationTypeChart() {
    const ctx = document.getElementById('violationTypeChart');
    if (!ctx) return;

    const chart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: chartData.violation_type.labels,
            datasets: [{
                data: chartData.violation_type.data,
                backgroundColor: [
                    'rgba(250, 112, 154, 0.8)',
                    'rgba(102, 126, 234, 0.8)',
                    'rgba(75, 172, 254, 0.8)',
                    'rgba(255, 206, 84, 0.8)'
                ],
                borderColor: [
                    'rgba(250, 112, 154, 1)',
                    'rgba(102, 126, 234, 1)',
                    'rgba(75, 172, 254, 1)',
                    'rgba(255, 206, 84, 1)'
                ],
                borderWidth: 2,
                hoverBorderWidth: 3
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        usePointStyle: true,
                        padding: 15,
                        font: {
                            size: 11,
                            weight: '600'
                        }
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    borderColor: '#ddd',
                    borderWidth: 1,
                    cornerRadius: 8,
                    callbacks: {
                        label: function(context) {
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = ((context.parsed / total) * 100).toFixed(1);
                            return `${context.label}: ${context.parsed}건 (${percentage}%)`;
                        }
                    }
                }
            },
            onClick: (event, elements) => {
                console.log('Chart clicked:', event, elements); // 디버깅용
                if (elements.length > 0) {
                    const elementIndex = elements[0].index;
                    const labelClicked = chartData.violation_type.labels[elementIndex];
                    console.log('Label clicked:', labelClicked); // 디버깅용
                    showViolationTypeDetails(labelClicked);
                }
            }
        }
    });
}

function createDailyResolutionChart() {
    const ctx = document.getElementById('resolutionChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: chartData.daily_resolution.labels,
            datasets: [{
                label: '일별 해결 건수',
                data: chartData.daily_resolution.data,
                backgroundColor: 'rgba(75, 172, 254, 0.8)',
                borderColor: 'rgba(75, 172, 254, 1)',
                borderWidth: 2,
                borderRadius: 8,
                borderSkipped: false,
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    borderColor: '#ddd',
                    borderWidth: 1,
                    cornerRadius: 8
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)',
                        drawBorder: false
                    },
                    ticks: {
                        font: {
                            size: 11
                        }
                    }
                },
                x: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        font: {
                            size: 11
                        }
                    }
                }
            },
            onClick: (event, elements) => {
                console.log('Daily resolution chart clicked:', event, elements); // 디버깅용
                if (elements.length > 0) {
                    const elementIndex = elements[0].index;
                    const dayLabel = chartData.daily_resolution.labels[elementIndex];
                    const resolutionCount = chartData.daily_resolution.data[elementIndex];
                    console.log('Day clicked:', dayLabel, resolutionCount); // 디버깅용
                    showDailyResolutionDetails(dayLabel, resolutionCount);
                }
            }
        }
    });
}

// 위반 유형별 상세 정보 모달 표시
function showViolationTypeDetails(violationType) {
    console.log('showViolationTypeDetails called with:', violationType); // 디버깅용
    console.log('Chart data details:', chartData.violation_type.details); // 디버깅용
    
    const details = chartData.violation_type.details[violationType];
    console.log('Details found:', details); // 디버깅용
    
    if (!details || details.length === 0) {
        alert(`${violationType}에 대한 상세 정보가 없습니다.`);
        return;
    }

    const modal = document.getElementById('chartModal');
    const modalTitle = document.getElementById('chartModalLabel');
    const modalBody = document.getElementById('modalContent');

    modalTitle.innerHTML = `
        <i class="bi bi-pie-chart me-2"></i>${violationType} 상세 정보 
        <span class="badge bg-primary ms-2">${details.length}건</span>
    `;

    let content = `
        <div class="mb-3">
            <div class="row">
                <div class="col-md-3">
                    <div class="card bg-light">
                        <div class="card-body text-center">
                            <h5 class="card-title text-primary">${details.length}</h5>
                            <p class="card-text">총 위반사항</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-light">
                        <div class="card-body text-center">
                            <h5 class="card-title text-danger">${details.filter(d => d.status === 'open').length}</h5>
                            <p class="card-text">미해결</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-light">
                        <div class="card-body text-center">
                            <h5 class="card-title text-success">${details.filter(d => d.status === 'closed').length}</h5>
                            <p class="card-text">해결완료</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-light">
                        <div class="card-body text-center">
                            <h5 class="card-title text-info">${[...new Set(details.map(d => d.author))].length}</h5>
                            <p class="card-text">관련 개발자</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
            <table class="table table-hover table-sm">
                <thead class="table-dark sticky-top">
                    <tr>
                        <th style="width: 25%;">파일</th>
                        <th style="width: 15%;">작성자</th>
                        <th style="width: 15%;">생성일</th>
                        <th style="width: 10%;">상태</th>
                        <th style="width: 35%;">위반 내용</th>
                    </tr>
                </thead>
                <tbody>
    `;

    details.forEach((detail, index) => {
        const statusBadge = detail.status === 'open' 
            ? '<span class="badge bg-danger"><i class="bi bi-exclamation-triangle me-1"></i>미해결</span>' 
            : '<span class="badge bg-success"><i class="bi bi-check-circle me-1"></i>해결</span>';
        
        const fileName = detail.file_path ? detail.file_path.split('/').pop() : 'N/A';
        const createdDate = detail.created_at ? new Date(detail.created_at).toLocaleDateString('ko-KR') : 'N/A';
        
        // 활동 상세 정보를 위한 데이터 객체 생성
        const activityData = {
            timestamp: detail.created_at,
            status: detail.status,
            file_path: detail.file_path,
            author: detail.author,
            commit_hash: detail.commit_hash,
            details: detail.description
        };
        
        content += `
            <tr class="${index % 2 === 0 ? 'table-light' : ''}">
                <td>
                    <button class="btn btn-link p-0 text-start violation-file-btn" 
                            data-activity='${JSON.stringify(activityData).replace(/'/g, "&apos;")}' 
                            title="${detail.file_path || 'N/A'}" 
                            style="border: none; background: none; text-decoration: none;">
                        <small class="font-monospace text-primary">
                            <i class="bi bi-file-code me-1"></i>${fileName}
                            <i class="bi bi-eye-fill ms-1" style="font-size: 0.8em;"></i>
                        </small>
                    </button>
                </td>
                <td>
                    <small class="text-muted">
                        <i class="bi bi-person me-1"></i>${detail.author || 'N/A'}
                    </small>
                </td>
                <td>
                    <small class="text-muted">
                        <i class="bi bi-calendar me-1"></i>${createdDate}
                    </small>
                </td>
                <td>${statusBadge}</td>
                <td>
                    <small class="text-wrap" style="word-break: break-word;">
                        ${detail.description ? detail.description.substring(0, 100) + (detail.description.length > 100 ? '...' : '') : '상세 내용 없음'}
                    </small>
                    ${detail.commit_hash ? `
                        <br><small class="text-muted font-monospace">
                            <i class="bi bi-git me-1"></i>${detail.commit_hash.substring(0, 8)}
                        </small>
                    ` : ''}
                </td>
            </tr>
        `;
    });

    content += `
                </tbody>
            </table>
        </div>
        
        <div class="mt-3 text-muted">
            <small>
                <i class="bi bi-info-circle me-1"></i>
                총 ${details.length}개의 ${violationType} 관련 위반사항이 발견되었습니다.
            </small>
        </div>
    `;

    modalBody.innerHTML = content;

    // Bootstrap Modal 표시
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
}

// 개발자별 상세 정보 모달 표시
function showAuthorDetails(authorName, violations, resolutions) {
    console.log('showAuthorDetails called with:', authorName, violations, resolutions); // 디버깅용
    
    const modal = document.getElementById('chartModal');
    const modalTitle = document.getElementById('chartModalLabel');
    const modalBody = document.getElementById('modalContent');

    modalTitle.innerHTML = `
        <i class="bi bi-person-circle me-2"></i>${authorName} 개발자 상세 정보
    `;

    const totalIssues = violations + resolutions;
    const resolutionRate = totalIssues > 0 ? ((resolutions / totalIssues) * 100).toFixed(1) : 0;

    let content = `
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card bg-danger text-white">
                    <div class="card-body text-center">
                        <h4 class="card-title">${violations}</h4>
                        <p class="card-text">위반 건수</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card bg-success text-white">
                    <div class="card-body text-center">
                        <h4 class="card-title">${resolutions}</h4>
                        <p class="card-text">해결 건수</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card bg-info text-white">
                    <div class="card-body text-center">
                        <h4 class="card-title">${totalIssues}</h4>
                        <p class="card-text">총 이슈</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card bg-warning text-white">
                    <div class="card-body text-center">
                        <h4 class="card-title">${resolutionRate}%</h4>
                        <p class="card-text">해결률</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-light">
                        <h6 class="mb-0"><i class="bi bi-exclamation-triangle me-2"></i>위반 현황</h6>
                    </div>
                    <div class="card-body">
                        ${violations > 0 ? `
                            <div class="progress mb-2">
                                <div class="progress-bar bg-danger" role="progressbar" style="width: 100%" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"></div>
                            </div>
                            <small class="text-muted">총 ${violations}건의 위반사항이 발견되었습니다.</small>
                        ` : `
                            <div class="text-center text-muted">
                                <i class="bi bi-check-circle-fill text-success"></i>
                                <p class="mt-2">위반사항이 없습니다.</p>
                            </div>
                        `}
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-light">
                        <h6 class="mb-0"><i class="bi bi-check-circle me-2"></i>해결 현황</h6>
                    </div>
                    <div class="card-body">
                        ${resolutions > 0 ? `
                            <div class="progress mb-2">
                                <div class="progress-bar bg-success" role="progressbar" style="width: 100%" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"></div>
                            </div>
                            <small class="text-muted">총 ${resolutions}건의 이슈를 해결했습니다.</small>
                        ` : `
                            <div class="text-center text-muted">
                                <i class="bi bi-clock-fill text-warning"></i>
                                <p class="mt-2">해결한 이슈가 없습니다.</p>
                            </div>
                        `}
                    </div>
                </div>
            </div>
        </div>
        
        <div class="mt-3 text-muted">
            <small>
                <i class="bi bi-info-circle me-1"></i>
                ${authorName} 개발자의 최근 30일간 활동 통계입니다.
            </small>
        </div>
    `;

    modalBody.innerHTML = content;

    // Bootstrap Modal 표시
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
}

// 일별 해결 상세 정보 모달 표시
function showDailyResolutionDetails(dayLabel, resolutionCount) {
    console.log('showDailyResolutionDetails called with:', dayLabel, resolutionCount); // 디버깅용
    
    const modal = document.getElementById('chartModal');
    const modalTitle = document.getElementById('chartModalLabel');
    const modalBody = document.getElementById('modalContent');

    modalTitle.innerHTML = `
        <i class="bi bi-calendar-check me-2"></i>${dayLabel} 일별 해결 현황
    `;

    let content = `
        <div class="row mb-4">
            <div class="col-md-6 mx-auto">
                <div class="card bg-primary text-white">
                    <div class="card-body text-center">
                        <h2 class="card-title">${resolutionCount}</h2>
                        <p class="card-text">해결된 이슈</p>
                        <small>최근 7일 중 ${dayLabel}에 해결된 건수</small>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-light">
                        <h6 class="mb-0"><i class="bi bi-bar-chart me-2"></i>해결 현황 분석</h6>
                    </div>
                    <div class="card-body">
                        ${resolutionCount > 0 ? `
                            <div class="alert alert-success" role="alert">
                                <i class="bi bi-check-circle-fill me-2"></i>
                                <strong>우수한 해결 성과!</strong> ${dayLabel}에 총 ${resolutionCount}건의 이슈가 해결되었습니다.
                            </div>
                            <div class="progress mb-2" style="height: 20px;">
                                <div class="progress-bar bg-success progress-bar-striped progress-bar-animated" 
                                     role="progressbar" 
                                     style="width: 100%" 
                                     aria-valuenow="100" 
                                     aria-valuemin="0" 
                                     aria-valuemax="100">
                                     ${resolutionCount}건 해결
                                </div>
                            </div>
                        ` : `
                            <div class="alert alert-info" role="alert">
                                <i class="bi bi-info-circle me-2"></i>
                                ${dayLabel}에는 해결된 이슈가 없습니다.
                            </div>
                            <div class="text-center text-muted">
                                <i class="bi bi-calendar-x" style="font-size: 3rem;"></i>
                                <p class="mt-2">해결 활동이 없었던 날입니다.</p>
                            </div>
                        `}
                    </div>
                </div>
            </div>
        </div>
        
        <div class="mt-3 text-muted">
            <small>
                <i class="bi bi-info-circle me-1"></i>
                최근 7일간의 일별 해결 현황을 보여주는 차트입니다.
            </small>
        </div>
    `;

    modalBody.innerHTML = content;

    // Bootstrap Modal 표시
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
}

// 월별 트렌드 상세 정보 모달 표시
function showTrendDetails(monthLabel, violationCount) {
    console.log('showTrendDetails called with:', monthLabel, violationCount); // 디버깅용
    
    const modal = document.getElementById('chartModal');
    const modalTitle = document.getElementById('chartModalLabel');
    const modalBody = document.getElementById('modalContent');

    modalTitle.innerHTML = `
        <i class="bi bi-graph-up me-2"></i>${monthLabel} 월별 위반 트렌드
    `;

    // 월 이름 변환
    const monthName = new Date(monthLabel + '-01').toLocaleDateString('ko-KR', { year: 'numeric', month: 'long' });

    let content = `
        <div class="row mb-4">
            <div class="col-md-6 mx-auto">
                <div class="card bg-gradient" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
                    <div class="card-body text-center">
                        <h2 class="card-title">${violationCount}</h2>
                        <p class="card-text">위반 건수</p>
                        <small>${monthName}에 발견된 총 위반사항</small>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-light">
                        <h6 class="mb-0"><i class="bi bi-analytics me-2"></i>트렌드 분석</h6>
                    </div>
                    <div class="card-body">
                        ${violationCount > 0 ? `
                            <div class="alert alert-warning" role="alert">
                                <i class="bi bi-exclamation-triangle me-2"></i>
                                <strong>주의!</strong> ${monthName}에 총 ${violationCount}건의 위반사항이 발견되었습니다.
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <h6 class="text-primary">위반 수준</h6>
                                    <div class="progress mb-3" style="height: 25px;">
                                        ${violationCount <= 5 ? `
                                            <div class="progress-bar bg-success" role="progressbar" style="width: ${(violationCount/20)*100}%" aria-valuenow="${violationCount}" aria-valuemin="0" aria-valuemax="20">
                                                낮음 (${violationCount}건)
                                            </div>
                                        ` : violationCount <= 15 ? `
                                            <div class="progress-bar bg-warning" role="progressbar" style="width: ${(violationCount/20)*100}%" aria-valuenow="${violationCount}" aria-valuemin="0" aria-valuemax="20">
                                                보통 (${violationCount}건)
                                            </div>
                                        ` : `
                                            <div class="progress-bar bg-danger" role="progressbar" style="width: ${(violationCount/20)*100}%" aria-valuenow="${violationCount}" aria-valuemin="0" aria-valuemax="20">
                                                높음 (${violationCount}건)
                                            </div>
                                        `}
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <h6 class="text-primary">개선 권고사항</h6>
                                    <ul class="list-unstyled">
                                        ${violationCount > 10 ? `
                                            <li><i class="bi bi-check-circle text-success me-2"></i>코드 리뷰 프로세스 강화</li>
                                            <li><i class="bi bi-check-circle text-success me-2"></i>개발 가이드라인 교육</li>
                                        ` : `
                                            <li><i class="bi bi-check-circle text-success me-2"></i>현재 수준 유지</li>
                                            <li><i class="bi bi-check-circle text-success me-2"></i>지속적인 모니터링</li>
                                        `}
                                    </ul>
                                </div>
                            </div>
                        ` : `
                            <div class="alert alert-success" role="alert">
                                <i class="bi bi-check-circle me-2"></i>
                                <strong>훌륭합니다!</strong> ${monthName}에는 위반사항이 발견되지 않았습니다.
                            </div>
                            <div class="text-center text-muted">
                                <i class="bi bi-trophy-fill text-warning" style="font-size: 3rem;"></i>
                                <p class="mt-2">코드 품질이 우수한 달입니다!</p>
                            </div>
                        `}
                    </div>
                </div>
            </div>
        </div>
        
        <div class="mt-3 text-muted">
            <small>
                <i class="bi bi-info-circle me-1"></i>
                월별 위반 트렌드를 통해 코드 품질 변화를 추적할 수 있습니다.
            </small>
        </div>
    `;

    modalBody.innerHTML = content;

    // Bootstrap Modal 표시
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
}

// 활동 히스토리 스크롤 페이징 초기화
function initializeActivityPagination() {
    const container = document.getElementById('activityTableContainer');
    const loadingIndicator = document.getElementById('loadingIndicator');
    const noMoreData = document.getElementById('noMoreData');
    
    if (!container) return;
    
    // 초기 데이터 저장 - 서버에서 전달된 데이터 변환
    if (typeof window.activityData !== 'undefined') {
        totalActivityData = window.activityData.map(item => ({
            timestamp: item.timestamp,
            status: item.status,
            file_path: item.file_path,
            author: item.author,
            commit_hash: item.commit_hash,
            details: item.details
        }));
        console.log('Initial activity data loaded:', totalActivityData.length); // 디버깅용
    }
    
    // 스크롤 이벤트 리스너 추가
    container.addEventListener('scroll', function() {
        if (isLoadingActivity || !hasMoreActivity) return;
        
        // 스크롤이 바닥에 가까워지면 다음 페이지 로드
        if (container.scrollTop + container.clientHeight >= container.scrollHeight - 50) {
            loadMoreActivity();
        }
    });
}

// 추가 활동 데이터 로드
function loadMoreActivity() {
    if (isLoadingActivity || !hasMoreActivity) return;
    
    isLoadingActivity = true;
    currentPage++;
    
    const loadingIndicator = document.getElementById('loadingIndicator');
    if (loadingIndicator) {
        loadingIndicator.style.display = 'block';
    }
    
    fetch(`/api/activity-history?page=${currentPage}&per_page=10`)
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            if (data.data.length > 0) {
                // 새로운 데이터를 totalActivityData에 추가
                totalActivityData = [...totalActivityData, ...data.data];
                
                // 테이블에 새로운 행들 추가
                appendActivityRows(data.data);
                
                // 더 이상 데이터가 없는지 확인
                hasMoreActivity = data.pagination.has_next;
                
                if (!hasMoreActivity) {
                    const noMoreData = document.getElementById('noMoreData');
                    if (noMoreData) {
                        noMoreData.style.display = 'block';
                    }
                }
            } else {
                hasMoreActivity = false;
                const noMoreData = document.getElementById('noMoreData');
                if (noMoreData) {
                    noMoreData.style.display = 'block';
                }
            }
        } else {
            console.error('활동 히스토리 로드 실패:', data.error);
        }
    })
    .catch(error => {
        console.error('활동 히스토리 로드 중 오류:', error);
        hasMoreActivity = false;
    })
    .finally(() => {
        isLoadingActivity = false;
        if (loadingIndicator) {
            loadingIndicator.style.display = 'none';
        }
    });
}

// 새로운 활동 행들을 테이블에 추가
function appendActivityRows(activities) {
    const tableBody = document.getElementById('activityTableBody');
    if (!tableBody) return;
    
    // "최근 활동이 없습니다" 메시지가 있다면 제거
    const emptyRow = tableBody.querySelector('td[colspan="6"]');
    if (emptyRow) {
        emptyRow.closest('tr').remove();
    }
    
    activities.forEach((activity, index) => {
        const row = document.createElement('tr');
        // 현재 totalActivityData에서의 인덱스 계산
        const currentIndex = totalActivityData.length - activities.length + index;
        
        console.log(`Adding row ${index}, currentIndex: ${currentIndex}, activity:`, activity); // 디버깅용
        
        row.innerHTML = `
            <td>
                ${activity.status === 'open' ? 
                    '<span class="badge bg-danger"><i class="bi bi-exclamation-triangle me-1"></i>이슈</span>' : 
                    '<span class="badge bg-success"><i class="bi bi-check-circle me-1"></i>해결</span>'
                }
            </td>
            <td>
                <small class="text-muted">
                    ${formatDateTime(activity.timestamp)}
                </small>
            </td>
            <td>
                <small class="text-truncate" style="max-width: 200px; display: inline-block;" title="${activity.file_path || 'N/A'}">
                    ${activity.file_path ? activity.file_path.split('/').pop() : 'N/A'}
                </small>
            </td>
            <td>
                <small class="text-muted">${activity.author || 'N/A'}</small>
            </td>
            <td>
                <small class="font-monospace text-muted">
                    ${activity.commit_hash ? activity.commit_hash.substring(0, 8) : 'N/A'}
                </small>
            </td>
            <td>
                <div class="details-container">
                    ${activity.details && activity.details.length > 60 ? 
                        `<div class="details-preview">
                            <small>${activity.details.substring(0, 60)}...</small>
                            <button class="btn btn-link btn-sm p-0 ms-1 activity-detail-btn" data-activity-index="${currentIndex}" title="자세히 보기">
                                <i class="bi bi-eye"></i>
                            </button>
                        </div>` : 
                        `<small>${activity.details || 'N/A'}</small>
                        ${activity.details ? 
                            `<button class="btn btn-link btn-sm p-0 ms-1 activity-detail-btn" data-activity-index="${currentIndex}" title="자세히 보기">
                                <i class="bi bi-eye"></i>
                            </button>` : ''
                        }`
                    }
                </div>
            </td>
        `;
        
        tableBody.appendChild(row);
    });
}

// 날짜/시간 포맷팅 함수
function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${month}-${day} ${hours}:${minutes}`;
}

// 활동 상세 모달 표시
function showActivityDetails(activity) {
    if (!activity) {
        console.error('활동 데이터가 없습니다.');
        return;
    }

    console.log('Activity data:', activity); // 디버깅용

    // 모달 제목 설정
    const modalTitle = document.getElementById('activityDetailModalLabel');
    if (modalTitle) {
        modalTitle.textContent = '활동 상세 정보';
    }

    // 모달 내용 생성
    const modalBody = document.querySelector('#activityDetailModal .modal-body');
    if (modalBody) {
        const statusBadge = activity.status === 'open' 
            ? '<span class="badge bg-danger"><i class="bi bi-exclamation-triangle me-1"></i>위반</span>'
            : '<span class="badge bg-success"><i class="bi bi-check-circle me-1"></i>해결</span>';

        modalBody.innerHTML = `
            <div class="row">
                <div class="col-md-6">
                    <h6 class="fw-bold text-primary">기본 정보</h6>
                    <table class="table table-sm table-borderless">
                        <tr>
                            <td class="fw-semibold" style="width: 30%;">유형:</td>
                            <td>${statusBadge}</td>
                        </tr>
                        <tr>
                            <td class="fw-semibold">시간:</td>
                            <td>${formatDateTime(activity.timestamp)}</td>
                        </tr>
                        <tr>
                            <td class="fw-semibold">작성자:</td>
                            <td>${activity.author || 'N/A'}</td>
                        </tr>
                        <tr>
                            <td class="fw-semibold">커밋 해시:</td>
                            <td><code class="text-break">${activity.commit_hash || 'N/A'}</code></td>
                        </tr>
                    </table>
                </div>
                <div class="col-md-6">
                    <h6 class="fw-bold text-primary">파일 정보</h6>
                    <div class="card bg-light">
                        <div class="card-body">
                            <small class="fw-semibold text-muted d-block mb-2">파일 경로:</small>
                            <div class="file-path">${activity.file_path || 'N/A'}</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mt-3">
                <div class="col-12">
                    <h6 class="fw-bold text-primary">상세 내용</h6>
                    <div class="card bg-light">
                        <div class="card-body">
                            <pre class="mb-0 text-break">${activity.details || '상세 내용이 없습니다.'}</pre>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    // 모달 표시
    const modalElement = document.getElementById('activityDetailModal');
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
}

// 엑셀 다운로드 기능
function downloadViolationsExcel(event) {
    event.preventDefault();
    
    // 클릭된 요소 찾기 (카드나 버튼일 수 있음)
    const clickedElement = event.currentTarget;
    const card = clickedElement.closest('.violation-download-card');
    
    if (!card) return;
    
    // 원래 상태 저장
    const originalContent = card.innerHTML;
    
    // 로딩 상태로 변경
    card.innerHTML = `
        <div class="card-body text-center">
            <div class="d-flex align-items-center justify-content-center">
                <div class="spinner-border spinner-border-sm text-primary me-2" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <span class="text-primary fw-bold">다운로드 중...</span>
            </div>
        </div>
    `;
    card.style.pointerEvents = 'none';
    
    // 엑셀 파일 다운로드
    const link = document.createElement('a');
    link.href = '/download-violations-excel';
    link.style.display = 'none';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    
    // 3초 후 원래 상태로 복원
    setTimeout(() => {
        card.innerHTML = originalContent;
        card.style.pointerEvents = 'auto';
    }, 3000);
}

// 이벤트 위임을 통한 활동 상세 버튼 클릭 처리
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, setting up event delegation'); // 디버깅용
    
    // 데이터 초기화
    initializeData();
    
    // 차트 생성
    createAuthorChart();
    createTrendChart();
    createViolationTypeChart();
    createDailyResolutionChart();
    
    // 활동 페이징 초기화
    initializeActivityPagination();
    
    // 활동 상세 버튼 이벤트 위임 - 이벤트 위임을 문서 레벨에서 처리
    document.addEventListener('click', function(event) {
        // 활동 상세 버튼 클릭 처리
        const button = event.target.closest('.activity-detail-btn');
        
        if (button) {
            console.log('Activity detail button clicked:', button); // 디버깅용
            event.preventDefault();
            event.stopPropagation();
            
            const activityIndex = parseInt(button.getAttribute('data-activity-index'));
            console.log('Activity index:', activityIndex); // 디버깅용
            console.log('Total activity data length:', totalActivityData.length); // 디버깅용
            
            if (activityIndex >= 0 && activityIndex < totalActivityData.length) {
                const activity = totalActivityData[activityIndex];
                console.log('Activity data:', activity); // 디버깅용
                showActivityDetails(activity);
            } else {
                console.error('잘못된 활동 인덱스:', activityIndex, 'Total length:', totalActivityData.length);
            }
        }
        
        // 위반 유형별 상세 모달에서 파일 클릭 처리
        const fileButton = event.target.closest('.violation-file-btn');
        
        if (fileButton) {
            console.log('Violation file button clicked:', fileButton); // 디버깅용
            event.preventDefault();
            event.stopPropagation();
            
            try {
                const activityDataStr = fileButton.getAttribute('data-activity');
                const activityData = JSON.parse(activityDataStr.replace(/&apos;/g, "'"));
                console.log('Violation file activity data:', activityData); // 디버깅용
                
                // 현재 모달을 닫고 새로운 모달을 열기
                const currentModal = bootstrap.Modal.getInstance(document.getElementById('chartModal'));
                if (currentModal) {
                    currentModal.hide();
                }
                
                // 약간의 지연 후 새 모달 열기 (애니메이션 완료 대기)
                setTimeout(() => {
                    showActivityDetails(activityData);
                }, 300);
                
            } catch (error) {
                console.error('파일 활동 데이터 파싱 오류:', error);
            }
        }
    });
    
    // 위반사항 다운로드 카드 클릭 이벤트
    const downloadCard = document.querySelector('.violation-download-card');
    if (downloadCard) {
        downloadCard.addEventListener('click', downloadViolationsExcel);
    }
});
