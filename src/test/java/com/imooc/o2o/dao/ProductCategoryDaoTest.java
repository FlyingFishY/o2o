package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;

public class ProductCategoryDaoTest extends BaseTest{
	@Autowired
	ProductCategoryDao productCategoryDao;
	@Test
	public void testQueryProductCategory() {
		ProductCategory productCategory = new ProductCategory();
		Shop shop = new Shop();
		shop.setShopId(1L);
		productCategory.setShop(shop);
		int rowIndex = 0;
		int pageSize = 100;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategory(productCategory, rowIndex, pageSize);
		System.out.println("商品种类数:" + productCategoryList.size());
		assertEquals(1, productCategoryList.size());
	}
}

