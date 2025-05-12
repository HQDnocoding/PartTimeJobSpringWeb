document.addEventListener('DOMContentLoaded', function () {
    const ctx = document.getElementById('myChart').getContext('2d');
    let chart;

    function renderChart(data) {
        if (!data || data.length === 0) {
            if (chart) chart.destroy();
            return;
        }

        const labels = data.map(item => new Date(item.date).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' }));
        const jobData = data.map(item => item.jobCount || 0);
        const candidateData = data.map(item => item.candidateCount || 0);
        const companyData = data.map(item => item.companyCount || 0);

        if (chart) {
            chart.destroy();
        }

        chart = new Chart(ctx, {
            type: 'bar', // Thay đổi từ 'line' thành 'bar'
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Số việc làm',
                        data: jobData,
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        borderColor: 'rgba(255, 99, 132, 1)',
                        borderWidth: 1
                    },
                    {
                        label: 'Số ứng viên',
                        data: candidateData,
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    },
                    {
                        label: 'Số nhà tuyển dụng',
                        data: companyData,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'top' },
                    title: { display: true, text: 'JobHome Statistics' }
                },
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: 'Số lượng' } },
                    x: { title: { display: true, text: 'Ngày' } }
                }
            }
        });
    }

    let initialReportData = /*[[${reportData}]]*/ [];
    if (initialReportData.length > 0) {
        renderChart(initialReportData);
    }

    document.querySelector('form').addEventListener('submit', function (e) {
        e.preventDefault();
        const formData = new FormData(this);
        const contextPath = window.location.pathname.split('/')[1] || '';
        const url = contextPath ? `/${contextPath}/report/data?` + new URLSearchParams(formData).toString() : `/report/data?` + new URLSearchParams(formData).toString();
        fetch(url, {
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.length > 0) {
                    renderChart(data);
                } else if (chart) {
                    chart.destroy();
                }
            });
    });
});