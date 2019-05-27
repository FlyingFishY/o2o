package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	List<ProductCategory> queryProductCategory(
			@Param("productCategoryCondition") ProductCategory productCatrgoryCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
}
