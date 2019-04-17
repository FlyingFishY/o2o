package com.imooc.o2o.util;

public class PathUtil {
	// 获取文件的分隔符
	private static String seperator = System.getProperty("file.separator");

	// 创建静态类，使用时不用创建实例，直接调用类下的方法
	public static String getImgBasePath() {
		// 获取系统的名称
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().startsWith("win")) {
			basePath = "G:/自学资料/项目图目录/"; // window系统
		} else {
			basePath = "/home/yanghao/image"; // Linux等其他系统
		}
		basePath = basePath.replace("/", seperator);
		return basePath;
	}
	public static String getShopImagePath(long shopId) {
		String imagePath = "upload/item/shop" + shopId + "/";
		return imagePath.replace("/", seperator);
	}
}
