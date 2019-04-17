/**
 * 
 */
$(function(){
	var initUrl = '/o2o/shopadmin/getshopinitinfo';
	var registerUrl = '/o2o/shopadmin/registershop';
	alert(initUrl);  
	getshopInitInfo();
	function getShopInitInfo(){
		$.getJSON(initUrl,function(data){   //第一个参数是访问的URL，第二个参数是回调的方法
			if(data.success){
				var tempHtml = '';
				var tempAreaHtml = '';
				data.shopCategoryList.map(function(item,index){
					tempHtml += '<option data-id="' + item.shopCategoryId + '">'
					+ item.shopCategoryName + '</option>';
				});
				data.areaList.map(function(item,index){
					tempAreaHtml += '<option data-id="' + item.areaId + '">' 
					+ item.areaName + '</option>';
				});
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
		$('#submit').click(function(){
			var shop = {};
			shop.shopName = $('#shop-name').val();
			shop.shopAddr = $('#shop-addr').val();
			shop.phone = $('shop-phone').val();
			shop.shopDesc = $('shop-desc').val();
			shop.shopCategoryId = {
					shopCategoryId : $('#shop-category').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			shop.area = {
					areaId : $('#area').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			var shopImg = $('#shop-img')[0].file[0];
			var formData = new FormData();
			formData.append('shopImg',shopImg);
			formData.append('shopStr',JSON.stringify(shop));    //JSON.stringify() 方法是将一个JavaScript值(对象或者数组)转换为一个 JSON字符串
			$.ajax({
				url : registerUrl,
				type : 'POST',
				data : formData,
				contentType : false,
				processData : false,
				cache : false,
				success : function(data){
					if(data.success){
						$.toast('提交成功！')    //toast是弹出窗口
					}else{
						$.toast('提交失败！' + data.errMsg)
					}
				}
			});
		});
	}
})
