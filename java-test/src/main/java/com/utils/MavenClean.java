package com.utils;

import java.io.File;

/**
 * maven文件删除
 */
public class MavenClean {

	
	public MavenClean(){
		
	}
	
	public void clean(String filePath){
		File f = new File(filePath);
		if(!f.exists()){
			System.out.printf("此文件路径不存在:%s",filePath);
		}
		clean(f);
	}
	
	public boolean clean(File f){
		if (f.isDirectory()) {
			File[] listFiles = f.listFiles();
			for (File file : listFiles) {
				if (clean(file)) {
					deleteFile(file.getParentFile());
					break;
				}
			}
		} else if (f.isFile()) {
			if (f.getName().endsWith("lastUpdated")) {
				return true;
			}
		}
		return false;
	}
	
	public void deleteFile(File file){
		if(!file.exists()){
        } else if(file.isFile()){
            System.out.println("删除文件:" + file.getAbsolutePath());
            file.delete();
        } else if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f : files){
                deleteFile(f);
            }
            System.out.println("删除文件夹:" + file.getAbsolutePath());
            System.out.println("====================================");
            file.delete();
        }
	}
	
	public static void main(String[] args) {
		String filePath = "/Users/gbs/software/apache-maven-3.3.1/conf/repo";
		new MavenClean().clean(filePath);
	}
}