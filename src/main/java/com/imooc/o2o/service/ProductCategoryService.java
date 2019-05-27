package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.entity.ProductCategory;

public interface ProductCategoryService {
	 List<ProductCategory> getProductCategoryList(ProductCategory productCategoryCondition, int pageIndex, int pageSize);
}
