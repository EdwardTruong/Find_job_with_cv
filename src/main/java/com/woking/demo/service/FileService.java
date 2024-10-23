package com.woking.demo.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/*
 * A. To stored file to disk i make 4 steps and read on github:
 *  
 *  1. Create local on project to storage files. 
 *  	The methods to do it :
 *  	- getUserFolder(int userId) - Each user will have their own folder.
 *  	- initCreateLocalDisk(Integer userId)
 *  		
 *  2. Create exceptions to throw when have something wrong.
 *  	Two class on com.woking.demo.exception :
 *  	- StorageException.java 
 *  	- StorageFileNotFoundException.java
 *  
 *  3. Create methods to storage and load all paths.
 *  	a.The methods use to load all paths of files in layer controller : 
 *  		- Stream<Path> loadAll(Integer userId);	
 *  	b.To storage : 
 *  		- String storedFileToLocal(MultipartFile file, Integer userId);
 *  			If user uploaded a file same image's name have already it going to bug. 
 *  			Fix later. I make  renameFilesInFolder(String folderPath) and  
 *  			getNewFileName(String fileName, String folderPath) in ImageService but 
 *  			it's not woking yet ! 
 *  		
 *  
 *  4. Using @ResponseBody to download it.
 *  	The method use to download : 
 *  	-  Path load(String filename, Integer userDto);
 *   	-  Resource loadAsResource(String filename,Integer userId);
 *  
 *  5. Reading https://spring.io/guides/gs/uploading-files.
 *  
 * B. Check file before save:
 *  	- isPdfFile(MultipartFile file);
 *  	- isExist(MultipartFile file, Integer id)
 */

public interface FileService {
	
	String getUserFolder(int userId);
	
	boolean initCreateLocalDisk(Integer userId);
	
	Stream<Path> loadAll(Integer userId);

	String storedFileToLocal(MultipartFile file, Integer userId) ;
	
	Path load(String filename, Integer userId);
	
	void deleteFile(String filename,Integer userId);
	
	Resource loadAsResource(String filename, Integer userId);
	
	boolean isPdfFile(MultipartFile file) throws IOException;

	boolean isExist(MultipartFile file, Integer id) throws IOException;

	
}
