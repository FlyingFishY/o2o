package com.imooc.o2o.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;

public class ProductCategoryServiceTest extends BaseTest{
	@Autowired
	private ProductCategoryService productCategoryService;
	@Test
	public void testGetProductCategoryList() {
		Shop shop = new Shop();
		shop.setShopId(1L);
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList = productCategoryService.getProductCategoryList(shop.getShopId());
		System.out.println("商品类别列表大小:" + productCategoryList.size());
	}
}
