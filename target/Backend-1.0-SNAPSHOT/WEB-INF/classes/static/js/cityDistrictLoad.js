/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
// Tải danh sách thành phố và quận/huyện
document.addEventListener('DOMContentLoaded', function () {
    fetch('https://provinces.open-api.vn/api/p/')
            .then(response => response.json())
            .then(provinces => {
                if (!Array.isArray(provinces)) {
                    throw new Error('Dữ liệu thành phố không đúng định dạng');
                }
                const citySelect = document.getElementById('city');
                provinces.forEach(province => {
                    const option = document.createElement('option');
                    option.value = province.code;
                    option.text = province.name;
                    citySelect.appendChild(option);
                });

                const urlParams = new URLSearchParams(window.location.search);
                const selectedCity = urlParams.get('city');
                if (selectedCity) {
                    citySelect.value = selectedCity;
                    loadDistricts(selectedCity, urlParams.get('district'));
                }
            })
            .catch(error => {
                console.error('Lỗi khi tải danh sách thành phố:', error);
                alert('Không thể tải danh sách thành phố!');
            });
});

document.getElementById('city').addEventListener('change', function () {
    const cityCode = this.value;
    const districtSelect = document.getElementById('district');
    districtSelect.innerHTML = '<option value="">Tất cả</option>';

    if (cityCode) {
        loadDistricts(cityCode);
    }
});

function loadDistricts(cityCode, selectedDistrict = null) {
    fetch(`https://provinces.open-api.vn/api/p/${cityCode}?depth=2`)
            .then(response => response.json())
            .then(data => {
                if (!data.districts || !Array.isArray(data.districts)) {
                    throw new Error('Dữ liệu quận/huyện không đúng định dạng');
                }
                const districtSelect = document.getElementById('district');
                data.districts.forEach(district => {
                    const option = document.createElement('option');
                    option.value = district.code;
                    option.text = district.name;
                    districtSelect.appendChild(option);
                });

                if (selectedDistrict) {
                    districtSelect.value = selectedDistrict;
                }
            })
            .catch(error => {
                console.error('Lỗi khi tải danh sách quận/huyện:', error);
                alert('Không thể tải danh sách quận/huyện!');
            });
}


