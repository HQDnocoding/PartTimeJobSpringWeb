document.addEventListener('DOMContentLoaded', function () {
    const citySelect = document.getElementById('city');
    var selectedCity = '';
    var selectedDistrict = '';
    try {
        
        selectedCity = object && object.city ? object.city : '';
        selectedDistrict = object && object.district ? object.district : '';
        console.log("0", selectedCity);
        
    } catch (e) {
        console.log(e);
    }
    
    console.log("1", selectedCity);
    
    
    // Tải danh sách tỉnh
    fetch('https://provinces.open-api.vn/api/p/')
            .then(response => response.json())
            .then(provinces => {
                if (!Array.isArray(provinces)) {
                    throw new Error('Dữ liệu thành phố không đúng định dạng');
                }
                
                // Xóa các option hiện có (trừ option mặc định)
                citySelect.innerHTML = '<option value="">Chọn thành phố</option>';
                provinces.forEach(province => {
                    const option = document.createElement('option');
                    option.value = province.name;
                    option.text = province.name;
                    citySelect.appendChild(option);
                });
                // Thiết lập tỉnh mặc định
                if (selectedCity && selectedCity !== '') {
                    citySelect.value = selectedCity;
                    const selectedProvince = provinces.find(province => province.name === selectedCity);
                    if (selectedProvince) {
                        loadDistricts(selectedProvince.code, selectedDistrict);
                    }
                }
            })
            .catch(error => {
                console.error('Lỗi khi tải danh sách thành phố:', error);
                alert('Không thể tải danh sách thành phố!');
            });
    // Sự kiện thay đổi tỉnh
    try {
        const districtSelect = document.getElementById('district');
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
    } catch (e) {
        
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
                if (!districtSelect) {
                    console.error('Không tìm thấy phần tử district trong DOM');
                    return;
                }
                
                // Xóa các option hiện có
                districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';
                data.districts.forEach(district => {
                    const option = document.createElement('option');
                    option.value = district.name;
                    option.text = district.name;
                    districtSelect.appendChild(option);
                });
                
                // Thiết lập huyện mặc định
                if (selectedDistrict) {
                    districtSelect.value = selectedDistrict;
                }
            })
            .catch(error => {
                console.error('Lỗi khi tải danh sách quận/huyện:', error);
                alert('Không thể tải danh sách quận/huyện!');
            });
}