package com.imooc.o2o.web.shopadmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	/**
	 * 管理session相关的操作
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return  modelMap;			
	}
	
	/**
	 * 根据用户信息，返回用户创建的商铺列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("杨浩");
		request.getSession().setAttribute("user", user);
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			int pageIndex = 0;
			int pageSize = 100;
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}	
		return modelMap;		
	}
	
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList =areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	/**
	 * 获取店铺类别列表和区域列表，通过modelMap返回前台
	 * @return
	 */
	private Map<String,Object> getshopInitInfo(){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	/**
	 * 注册店铺
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> registerShop(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//检查验证码
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接收并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		
		CommonsMultipartFile shopImg = null;  //接收图片用，Spring自带的
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());    //创建一个通用的多部分解析器,是文件上传解析器，解析request里的文件信息,从request本次会话的上下文获取相关文件上传的内容
		if(commonsMultipartResolver.isMultipart(request)) {    //判断request里是否有上传的文件流,即多部分请求 
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;    //转换成多部分request
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","上传图片不能为空");
			return modelMap;
		}

		//2.注册店铺
		if(shop != null && shopImg != null) {
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution se;
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
				se = shopService.addShop(shop,imageHolder);
				if(se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//该用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException | IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","请输入店铺信息");
			return modelMap;
		}
	}
	
	/**
	 * 修改店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/modifyshop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> modifyShop(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//检查验证码
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接收并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		
		CommonsMultipartFile shopImg = null;  //接收图片用，Spring自带的
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());    //创建一个通用的多部分解析器,是文件上传解析器，解析request里的文件信息,从request本次会话的上下文获取相关文件上传的内容
		if(commonsMultipartResolver.isMultipart(request)) {    //判断request里是否有上传的文件流,即多部分请求 
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;    //转换成多部分request
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}

		//2.修改店铺信息
		if(shop != null && shop.getShopId() != null) {
			ShopExecution se;
			try {
				if(shopImg == null) {
					se = shopService.modifyShop(shop,null);
				}else {
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
					se = shopService.modifyShop(shop,imageHolder);
				}
				if(se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException | IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","请输入店铺Id");
			return modelMap;
		}
	}
}
