package com.imooc.o2o.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;

public class ProductCategoryServiceTest {
	@Autowired
	private ProductCategoryService productCategoryService;
	@Test
	public void testGetProductCategoryList() {
		ProductCategory productCategoryCondition = new ProductCategory();
		Shop shop = new Shop();
		shop.setShopId(1L);
		productCategoryCondition.setShop(shop);
		int pageIndex = 1;
		int pageSize = 100;
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList = productCategoryService.getProductCategoryList(productCategoryCondition, pageIndex, pageSize);
		System.out.println("商品类别列表大小" + productCategoryList.size());
	}
}
