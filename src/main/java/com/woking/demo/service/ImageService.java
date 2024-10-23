package com.woking.demo.service;

import org.springframework.web.multipart.MultipartFile;

/*
 * The renameFilesInFolder and getNewFileName methods use to update project later.
 */


public interface ImageService {
	
	boolean isImage(MultipartFile file);
     
	boolean isImageExtension(String fileName);
	
	boolean checkSizeImage(MultipartFile file);
	
	 void renameFilesInFolder(String folderPath);
	 
	 String getNewFileName(String fileName, String folderPath);
}
