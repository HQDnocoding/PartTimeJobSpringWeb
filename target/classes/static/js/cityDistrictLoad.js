document.addEventListener('DOMContentLoaded', function () {
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    let selectedCity = window.selectedCity || '';
    let selectedDistrict = window.selectedDistrict || '';

    // Hàm ánh xạ giá trị từ database sang định dạng API
    function mapToApiFormat(location, isCity = true) {
        if (!location) return '';
        // Các thành phố cần thêm tiền tố "Thành phố"
        const cityPrefixes = ['Hà Nội', 'Hồ Chí Minh', 'Đà Nẵng', 'Hải Phòng', 'Cần Thơ'];
        if (isCity && cityPrefixes.includes(location)) {
            return `Thành phố ${location}`;
        }
        // Không thêm tiền tố cho quận/huyện, vì API có thể trả về "Quận" hoặc "Huyện"
        return location;
    }

    // Ánh xạ giá trị từ database
    selectedCity = mapToApiFormat(selectedCity, true);
    selectedDistrict = mapToApiFormat(selectedDistrict, false);

    console.log("Selected City:", selectedCity);
    console.log("Selected District:", selectedDistrict);

    // Tải danh sách tỉnh/thành phố
    fetch('https://provinces.open-api.vn/api/p/')
        .then(response => response.json())
        .then(provinces => {
            if (!Array.isArray(provinces)) {
                throw new Error('Dữ liệu thành phố không đúng định dạng');
            }

            citySelect.innerHTML = '<option value="">Chọn Tỉnh/thành phố</option>';
            provinces.forEach(province => {
                const option = document.createElement('option');
                option.value = province.name;
                option.text = province.name;
                citySelect.appendChild(option);
            });

            // Thiết lập tỉnh/thành phố mặc định
            if (selectedCity && selectedCity !== '') {
                citySelect.value = selectedCity;
                const selectedProvince = provinces.find(province => province.name === selectedCity);
                if (selectedProvince) {
                    loadDistricts(selectedProvince.code, selectedDistrict);
                } else {
                    console.warn(`Không tìm thấy tỉnh/thành phố: ${selectedCity}`);
                }
            }
        })
        .catch(error => {
            console.error('Lỗi khi tải danh sách thành phố:', error);
            alert('Không thể tải danh sách thành phố!');
        });

    // Sự kiện thay đổi tỉnh/thành phố
    if (districtSelect) {
        citySelect.addEventListener('change', function () {
            const cityName = this.value;
            districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';
            if (cityName) {
                fetch('https://provinces.open-api.vn/api/p/')
                    .then(response => response.json())
                    .then(provinces => {
                        const selectedProvince = provinces.find(province => province.name === cityName);
                        if (selectedProvince) {
                            loadDistricts(selectedProvince.code);
                        }
                    })
                    .catch(error => {
                        console.error('Lỗi khi tải danh sách tỉnh/thành phố:', error);
                        alert('Không thể tải danh sách tỉnh/thành phố!');
                    });
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
            districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';
            data.districts.forEach(district => {
                const option = document.createElement('option');
                option.value = district.name; // Lưu giá trị gốc của API
                option.text = district.name;
                districtSelect.appendChild(option);
            });

            // Thiết lập quận/huyện mặc định
            if (selectedDistrict) {
                // Thử tìm quận/huyện với giá trị gốc, hoặc thêm tiền tố "Quận"/"Huyện"
                const matchedDistrict = data.districts.find(d => 
                    d.name === selectedDistrict || 
                    d.name === `Quận ${selectedDistrict}` || 
                    d.name === `Huyện ${selectedDistrict}`
                );
                if (matchedDistrict) {
                    districtSelect.value = matchedDistrict.name;
                } else {
                    console.warn(`Không tìm thấy quận/huyện: ${selectedDistrict}`);
                }
            }
        })
        .catch(error => {
            console.error('Lỗi khi tải danh sách quận/huyện:', error);
            alert('Không thể tải danh sách quận/huyện!');
        });
}