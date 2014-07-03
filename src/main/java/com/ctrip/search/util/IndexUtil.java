package com.ctrip.search.util;

import java.io.File;

/**
 * 文件工具类
 * 
 * @author chenjk
 * @since 2014年6月26日
 *
 */
public class IndexUtil {
	
	/**
	 * 初始化索引目录
	 * 
	 * @param indexPath
	 * 
	 * 		       路径
	 */
	public static void initIndexDir(String indexPath){
		File indexDir = new File(indexPath);
		if(indexDir != null && !indexDir.exists()){
			indexDir.mkdirs();
		}else{
			File[] indexFiles = indexDir.listFiles();
			if(indexFiles != null){
				for(File file : indexFiles){
					file.delete();
				}
			}
		}
	}
	
	/**
	 * 初始化索引目录
	 */
	public static void initIndexDir(){
		initIndexDir(getIndexDir());
	}
	
	/**
	 * 获取索引目录路径
	 * 
	 * @return
	 */
	public static String getIndexDir(){
		StringBuilder sb = new StringBuilder(System.getProperty("user.dir")).append(File.separator).append("index");
		return sb.toString();
	}
}
