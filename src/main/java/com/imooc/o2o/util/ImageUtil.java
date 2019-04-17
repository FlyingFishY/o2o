package com.imooc.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;


public class ImageUtil {
	//获取绝对值路径
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
	private static final Random r = new Random();
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	
	
	/**
	 * 将CommonsMultipartFile转换成File类
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newfile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newfile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newfile;
	}
	
	
	/**
	 * 处理缩略图，并返回新生成图片的相对值路径
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
//	public static String generateThumbnail(CommonsMultipartFile thumbnail,String targetAddr) {
//		String realFileName = getRandomFileName();
//		String extension = getFileExtension(thumbnail);
//		makeDirPath(targetAddr);
//		String relativeAddr = targetAddr + realFileName + extension;
//		logger.debug("current relativeAddr is:" + relativeAddr);
//		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
//		logger.debug("current complete addr is:" + PathUtil.getImgBasePath() + relativeAddr);
//		try {
//			Thumbnails.of(thumbnail.getInputStream()).size(200,200)
//					.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "/watermark.jpg")),0.25f)
//					.outputQuality(0.8f).toFile(dest);;
//		}catch(IOException e){
//			logger.error(e.toString());
//			e.printStackTrace();
//		}
//		return relativeAddr;
//	}
	
	public static String generateThumbnail(InputStream thumbnailInputStream,String fileName, String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(fileName);
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:" + relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is:" + PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnailInputStream).size(200,200)
					.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "/watermark.png")),0.25f)
					.outputQuality(0.8f).toFile(dest);;
		}catch(IOException e){
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;
	}
	
	
	
	/**
	 * 创建目标路径所涉及到的目录，即:若home/work/yangaho/xxx.jpg
	 * 那么home work yanghao 这三个文件夹都得自动创建
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() +targetAddr;
		File dirPath = new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * 获取输入文件流的扩展名
	 * @param cFile
	 * @return
	 */
//	private static String getFileExtension(CommonsMultipartFile cFile) {
//		String originalFileName = cFile.getOriginalFilename();
//		return originalFileName.substring(originalFileName.lastIndexOf("."));
//	}
	
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数（以确保在统一批次和不同批次上传的文件名都是不一样的）
	 * @return
	 */
	public static String getRandomFileName() {
		//获取随机的五位数
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}

	public static void main(String[] args) {
		//获取绝对值路径
		String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		try {
			Thumbnails.of(new File("C:/Users/yanghao/Pictures/BnS/41.png"))
			.size(200,200).watermark(Positions.BOTTOM_RIGHT, 
					ImageIO.read(new File(basePath + "/watermark.png")), 0.25f).outputQuality(0.8f)
			.toFile("C:/Users/yanghao/Pictures/BnS/41new.png");
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
}
 