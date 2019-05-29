package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 通过shopId查询店铺商品类别
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);
}
