package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}
	@Override
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if(productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if(effectedNum <= 0) {
					throw new ProductCategoryOperationException("店铺类别创建失败");
				}else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			}catch(Exception e){
				throw new ProductCategoryOperationException("batchAddProductCategory error:" + e.getMessage());
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}
	@Override
	@Transactional
	//因为以后要将此商品类别下的商品的类别Id置为空，然后再删除商品类别，所有是有两步的，需要调用事务@Transactional来管理
	//这样做的好处是：当第一步成功后，并不会急于提交，而是等到第二步也成功之后再提交。如果第二步失败，第一步也不会提交，这样就达到回滚的目的
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// TODO 将此商品类别下的商品的类别Id置为空
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum <= 0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error" + e.getMessage());
		}
	}
}
