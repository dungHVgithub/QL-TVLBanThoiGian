document.addEventListener('DOMContentLoaded', function() {
    google.charts.load('current', {packages: ['corechart', 'bar']});
    google.charts.setOnLoadCallback(drawChart);

    // Lấy giá trị interval từ URL hoặc mặc định là "month"
    const urlParams = new URLSearchParams(window.location.search);
    let interval = urlParams.get('interval') || 'month';

    // Cập nhật interval và vẽ lại biểu đồ khi thay đổi dropdown
    const intervalSelect = document.getElementById('intervalSelect');
    if (intervalSelect) {
        intervalSelect.addEventListener('change', function() {
            interval = this.value;
            updateChart();
        });

        // Đặt giá trị mặc định cho dropdown
        intervalSelect.value = interval;
    } else {
        console.error('Element with id "intervalSelect" not found');
    }

    function drawChart() {
        // Lấy dữ liệu từ API với tham số interval
        fetch(`/FindJob/api/statistics/time-series?interval=${interval}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                // Tạo DataTable cho Google Charts
                var dataTable = new google.visualization.DataTable();
                if (interval === 'day') {
                    dataTable.addColumn('string', 'Ngày');
                } else {
                    dataTable.addColumn('string', 'Tháng');
                }
                dataTable.addColumn('number', 'Employees');
                dataTable.addColumn('number', 'Employers');
                dataTable.addColumn('number', 'Jobs');

                // Đổ dữ liệu từ API vào DataTable
                data.forEach(row => {
                    if (interval === 'day') {
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
                    title: interval === 'day' ? 'Số lượng Job, Employer, Employee theo ngày' : 'Số lượng Job, Employer, Employee theo tháng',
                    hAxis: {
                        title: interval === 'day' ? 'Ngày' : 'Tháng',
                        format: interval === 'day' ? 'yyyy-MM-dd' : 'yyyy-MM'
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
        // Cập nhật URL với tham số interval và vẽ lại biểu đồ
        window.history.pushState({}, '', `?interval=${interval}`);
        drawChart();
    }

    // Gọi drawChart lần đầu khi trang tải
    drawChart();
});