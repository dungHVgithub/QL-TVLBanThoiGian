document.addEventListener('DOMContentLoaded', function() {
    google.charts.load('current', {packages: ['corechart', 'bar']});
    google.charts.setOnLoadCallback(drawChart);

    // Lấy giá trị từ URL hoặc mặc định
    const urlParams = new URLSearchParams(window.location.search);
    let type = urlParams.get('type') || 'month';
    let range = urlParams.get('range') || '1';

    // Cập nhật giá trị dropdown
    const typeSelect = document.getElementById('typeSelect');
    const rangeSelect = document.getElementById('rangeSelect');

    if (typeSelect && rangeSelect) {
        typeSelect.value = type;
        rangeSelect.value = range;

        // Cập nhật biểu đồ khi thay đổi dropdown
        typeSelect.addEventListener('change', function() {
            type = this.value;
            updateChart();
        });

        rangeSelect.addEventListener('change', function() {
            range = this.value;
            updateChart();
        });
    } else {
        console.error('Dropdown elements not found');
    }

    // Lấy dữ liệu tổng quan từ API /api/statistics
    fetch('/FindJob/api/statistics')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            document.getElementById('totalJobs').textContent = data.totalJobs || 0;
            document.getElementById('totalEmployers').textContent = data.totalEmployers || 0;
            document.getElementById('totalEmployees').textContent = data.totalEmployees || 0;
        })
        .catch(error => {
            console.error('Error fetching summary data:', error);
            document.getElementById('totalJobs').textContent = 'N/A';
            document.getElementById('totalEmployers').textContent = 'N/A';
            document.getElementById('totalEmployees').textContent = 'N/A';
        });

    function drawChart() {
        // Lấy dữ liệu từ API với tham số type và range
        fetch(`/FindJob/api/statistics/time-series?type=${type}&range=${range}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                // Tạo DataTable cho Google Charts
                var dataTable = new google.visualization.DataTable();
                if (type === 'day') {
                    dataTable.addColumn('string', 'Ngày');
                } else {
                    dataTable.addColumn('string', 'Tháng');
                }
                dataTable.addColumn('number', 'Employees');
                dataTable.addColumn('number', 'Employers');
                dataTable.addColumn('number', 'Jobs');

                // Đổ dữ liệu từ API vào DataTable
                data.forEach(row => {
                    if (type === 'day') {
                        dataTable.addRow([
                            row.date,
                            parseInt(row.employees || 0),
                            parseInt(row.employers || 0),
                            parseInt(row.jobs || 0)
                        ]);
                    } else {
                        dataTable.addRow([
                            row.month,
                            parseInt(row.employees || 0),
                            parseInt(row.employers || 0),
                            parseInt(row.jobs || 0)
                        ]);
                    }
                });

                // Cấu hình biểu đồ
                var options = {
                    title: type === 'day' ? `Số lượng Job, Employer, Employee theo ${range} ngày` : `Số lượng Job, Employer, Employee theo ${range} tháng`,
                    hAxis: {
                        title: type === 'day' ? 'Ngày' : 'Tháng',
                        format: type === 'day' ? 'yyyy-MM-dd' : 'yyyy-MM'
                    },
                    vAxis: {
                        title: 'Số lượng'
                    },
                    legend: { position: 'top' },
                    height: 500,
                    isStacked: false,
                    bar: { groupWidth: '75%' }
                };

                // Vẽ biểu đồ cột
                var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
                chart.draw(dataTable, options);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
                document.getElementById('chart_div').innerHTML = '<p style="color: red;">Lỗi: Không thể tải dữ liệu biểu đồ. Vui lòng kiểm tra console để biết chi tiết.</p>';
            });
    }

    function updateChart() {
        // Cập nhật URL với tham số type và range
        window.history.pushState({}, '', `?type=${type}&range=${range}`);
        drawChart();
    }

    // Gọi drawChart lần đầu khi trang tải
    drawChart();
});