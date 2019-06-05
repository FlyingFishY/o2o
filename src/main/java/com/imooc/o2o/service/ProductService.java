package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * 添加商品信息以及图片处理
	 * @param product
	 * @param thumbnail  属于缩略图的
	 * @param productImgList   属于详情图的
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail, 
			List<ImageHolder> productImgList) throws ProductOperationException;
}
