(function () {
    'use strict';

    // Hàm khởi tạo sau khi DOM tải xong
    function init() {
        // Bootstrap form validation
        const forms = document.querySelectorAll('.needs-validation');
        Array.from(forms).forEach(form => {
            form.addEventListener('submit', event => {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });

        // Cập nhật thông tin công ty
        function updateCompanyInfo() {
            const employerSelect = document.getElementById('employerId');
            const companyName = document.getElementById('companyName');
            const companyAddress = document.getElementById('companyAddress');
            const companyTaxCode = document.getElementById('companyTaxCode');
            const viewCompanyImagesBtn = document.getElementById('viewCompanyImagesBtn');

            // Kiểm tra tồn tại các phần tử
            if (!employerSelect || !companyName || !companyAddress || !companyTaxCode || !viewCompanyImagesBtn) {
                console.error('Một hoặc nhiều phần tử không tồn tại:', {
                    employerSelect,
                    companyName,
                    companyAddress,
                    companyTaxCode,
                    viewCompanyImagesBtn
                });
                return;
            }

            const selectedOption = employerSelect.options[employerSelect.selectedIndex];
            console.log('Selected Option:', selectedOption);
            console.log('Data Company ID:', selectedOption ? selectedOption.getAttribute('data-company-id') : null);
            console.log('Data Company Name:', selectedOption ? selectedOption.getAttribute('data-company-name') : null);
            console.log('Data Company Address:', selectedOption ? selectedOption.getAttribute('data-company-address') : null);
            console.log('Data Company TaxCode:', selectedOption ? selectedOption.getAttribute('data-company-taxcode') : null);

            if (selectedOption && selectedOption.value) {
                // Cập nhật thông tin công ty
                companyName.value = selectedOption.getAttribute('data-company-name') || 'Không có thông tin tên công ty.';
                companyAddress.value = selectedOption.getAttribute('data-company-address') || 'Không có thông tin địa chỉ.';
                companyTaxCode.value = selectedOption.getAttribute('data-company-taxcode') || 'Không có thông tin mã số thuế.';

                // Kích hoạt nút xem hình ảnh
                viewCompanyImagesBtn.disabled = false;
            } else {
                // Đặt lại thông tin công ty
                companyName.value = 'Vui lòng chọn nhà tuyển dụng để xem thông tin công ty.';
                companyAddress.value = '';
                companyTaxCode.value = '';

                // Vô hiệu hóa nút xem hình ảnh
                viewCompanyImagesBtn.disabled = true;
            }
        }

        // Hiển thị hình ảnh công ty
        async function showCompanyImages() {
            const employerSelect = document.getElementById('employerId');
            const companyImagesList = document.getElementById('companyImagesList');

            if (!employerSelect || !companyImagesList) {
                console.error('Phần tử employerId hoặc companyImagesList không tồn tại');
                return;
            }

            const selectedOption = employerSelect.options[employerSelect.selectedIndex];
            const companyId = selectedOption ? selectedOption.getAttribute('data-company-id') : null;

            // Xóa nội dung cũ
            companyImagesList.innerHTML = '<p>Đang tải hình ảnh...</p>';

            if (!companyId) {
                companyImagesList.innerHTML = '<p>Vui lòng chọn nhà tuyển dụng.</p>';
                return;
            }

            try {
                // Sử dụng đường dẫn tuyệt đối dựa trên origin
                const apiUrl = `${window.location.origin}/FindJob/api/company_images/${companyId}`;
                console.log('Fetching images from:', apiUrl);
                const response = await fetch(apiUrl);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const images = await response.json();
                console.log('Company Images:', images);

                // Xóa nội dung loading
                companyImagesList.innerHTML = '';

                if (images.length > 0) {
                    images.forEach(img => {
                        const card = document.createElement('div');
                        card.className = 'image-card';
                        card.innerHTML = `
                            <div class="card">
                                <img src="${img.imagePath}" class="card-img-top" alt="${img.caption || 'Hình ảnh công ty'}">
                                <div class="card-body">
                                    <p class="card-text">${img.caption || 'Không có mô tả'}</p>
                                </div>
                            </div>
                        `;
                        companyImagesList.appendChild(card);
                    });
                } else {
                    companyImagesList.innerHTML = '<p>Không có hình ảnh công ty.</p>';
                }
            } catch (error) {
                console.error('Lỗi khi lấy hình ảnh:', error);
                companyImagesList.innerHTML = '<p>Lỗi khi tải hình ảnh công ty.</p>';
            }
        }

        // Gán sự kiện
        const employerSelect = document.getElementById('employerId');
        const viewCompanyImagesBtn = document.getElementById('viewCompanyImagesBtn');

        if (employerSelect) {
            employerSelect.addEventListener('change', updateCompanyInfo);
            updateCompanyInfo(); // Khởi tạo
        } else {
            console.error('Phần tử employerId không tồn tại');
        }

        if (viewCompanyImagesBtn) {
            viewCompanyImagesBtn.addEventListener('click', showCompanyImages);
        } else {
            console.error('Phần tử viewCompanyImagesBtn không tồn tại');
        }
    }

    // Chờ DOM tải xong
    document.addEventListener('DOMContentLoaded', init);
})();