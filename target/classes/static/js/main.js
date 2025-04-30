function deleteRecord(e, endpoint, checkBoxClass) {
    e.preventDefault();
    console.log(checkBoxClass);
    const selectedIds = Array.from(document.querySelectorAll(checkBoxClass))
            .map(checkbox => checkbox.value);

    if (selectedIds.length === 0) {
        alert('Vui lòng chọn ít nhất một ứng viên để xóa!');
        return;
    }

    if (confirm(`Bạn có chắc chắn muốn xóa ${selectedIds.length} ứng viên đã chọn?`)) {
        Promise.all(selectedIds.map(id =>
            fetch(`${endpoint}/${id}`, {method: 'DELETE'})
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`Lỗi khi xóa ứng viên ID ${id}`);
                        }
                        return id;
                    })
        ))
                .then(() => {
                    alert('Xóa ứng viên thành công!');
                    location.reload();
                })
                .catch(error => {
                    console.error('Lỗi:', error);
                    alert('Đã xảy ra lỗi khi xóa ứng viên!');
                });
    }
}