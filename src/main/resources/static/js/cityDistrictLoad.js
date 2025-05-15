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
                option.value = province.name; // Sử dụng province.name (ví dụ: "Hà Nội") thay vì province.code
                option.text = province.name;
                citySelect.appendChild(option);
            });

            const urlParams = new URLSearchParams(window.location.search);
            const selectedCity = urlParams.get('city');
            if (selectedCity) {
                citySelect.value = selectedCity;
                // Cần tìm province.code tương ứng với province.name để load quận/huyện
                const selectedProvince = provinces.find(province => province.name === selectedCity);
                if (selectedProvince) {
                    loadDistricts(selectedProvince.code, urlParams.get('district'));
                }
            }
        })
        .catch(error => {
            console.error('Lỗi khi tải danh sách thành phố:', error);
            alert('Không thể tải danh sách thành phố!');
        });
});

document.getElementById('city').addEventListener('change', function () {
    const cityName = this.value;
    const districtSelect = document.getElementById('district');
    districtSelect.innerHTML = '<option value="">Tất cả</option>';

    if (cityName) {
        // Tìm province.code tương ứng với cityName để load quận/huyện
        fetch('https://provinces.open-api.vn/api/p/')
            .then(response => response.json())
            .then(provinces => {
                const selectedProvince = provinces.find(province => province.name === cityName);
                if (selectedProvince) {
                    loadDistricts(selectedProvince.code);
                }
            });
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
                option.value = district.name; // Sử dụng district.name thay vì district.code
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