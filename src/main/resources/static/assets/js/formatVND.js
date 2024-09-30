/**
 * 
 */							    
function formatCurrency(input) {
	var value = input.value.replace(/\D/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
	input.value = value + ' VND';  // Thêm ký hiệu tiền tệ VND vào giá trị
}