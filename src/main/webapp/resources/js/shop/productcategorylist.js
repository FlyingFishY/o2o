$(function() {
	getlist();
	function getlist() {
		$.ajax({
			url : "/o2o/shopadmin/getproductcategorylist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					handleList(data.productCategoryList);
				}
			}
		});
	}
	function handleList(data) {
		var html = '';
		data.map(function(item, index) {
			html += '<div class="row row-shop"><div class="col-40">'
					+ item.productCategoryName + '</div><div class="col-40">'
					+ item.priority + '</div><div class="col-20">'
					+ deleteProductCategory(item.productCategoryId)
					+ '</div></div>';
		});
		$('.shop-wrap').html(html);
	}
	function deleteProductCategory(id) {
		return '<a href="/o2o/shopadmin/deleteproductcategory?productCategoryId=' + id + '">删除</a>';
	}
})
