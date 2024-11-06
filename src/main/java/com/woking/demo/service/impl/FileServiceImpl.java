package com.woking.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.woking.demo.exception.StorageException;
import com.woking.demo.exception.StorageFileNotFoundException;
import com.woking.demo.service.FileService;
import com.woking.demo.service.ImageService;
import com.woking.demo.utils.Const;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	ImageService iService;

	@Override
	public String getUserFolder(int userId) {
		return Const.DIRECTORY_UPLOAD_CV.PATH_CREATE + userId;
	}

	@Override
	public boolean initCreateLocalDisk(Integer userId) {
		String folder = getUserFolder(userId);
		try {
			Path userFolderPath = Paths.get(folder);
			if (!userFolderPath.toFile().exists()) {
				Files.createDirectories(userFolderPath);
			}
			return true;
		} catch (Exception e) {
			throw new StorageException("Can't create directory storage.");
		}
	}

	// Check ở đây
	@Override
	public String storedFileToLocal(MultipartFile file, Integer userId) {

		String folder = "";
		
		try {
			if (file.isEmpty()) { throw new StorageException("Failed to store empty files."); }
				
		
			if (userId == null) {
				folder = Const.DIRECTORY_IMAGE.PATH_PERMANENT;
			} else {
				initCreateLocalDisk(userId);
				folder = getUserFolder(userId);
			}

			String filename = StringUtils.cleanPath(file.getOriginalFilename());

			if (filename.contains("..")) {
				throw new StorageException("Sorry! Filename contains invalid path sequence " + filename);
			}

			Path userPath = Paths.get(folder);
			Path destinationFile = userPath.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
				return filename;
			}

		} catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
																	
		}
	}

	@Override
	public Path load(String filename, Integer userId) {

		String folder = "";

		if (userId != null) {
			folder = getUserFolder(userId);
		} else {
			folder = Const.DIRECTORY_IMAGE.PATH_PERMANENT;
		}

		Path userPath = Paths.get(folder);
		Path result = userPath.resolve(filename);

		System.out.println("Check in form FileServiceImpl : load : result : " + result);

		return result;
	}

	@Override
	public void deleteFile(String filename, Integer userId) {
		try {
			Path file = load(filename, userId);
			Resource resource = new UrlResource(file.toUri());

			System.out.println("Check in form FileServiceImpl : deleteFile : resource : " + resource);

			if (resource.exists() || resource.isReadable()) {
				File fileDelete = new File(resource.getFile().toString());
				fileDelete.delete();
			} else {
				throw new StorageFileNotFoundException("Could not detele file: " + filename);
			}
		} catch (IOException e) {
			throw new StorageFileNotFoundException("Could not delete file: " + filename, e);
		}
	}

	@Override
	public Stream<Path> loadAll(Integer userId) {

		/*
		 * 
		 * Có thể đi qua luôn cả folder bằng cách truyền vào đường dẫn 1.Bước đầu tiên
		 * là đổi tên folder của mỗi user chỉ có cái id example :user01 : name of folder
		 * is (1) 2. String folder = "./src/resources/upload/"+id; 3 .Path userPath =
		 * Paths.get(folder)
		 * 
		 */

		String folder = getUserFolder(userId);
		Path userPath = Paths.get(folder);
		if (userPath != null) {
			try {
				return Files.walk(userPath, 1).filter(path -> !path.equals(userPath)).map(userPath::relativize);
			} catch (IOException e) {
				throw new StorageException("Failed to read stored files", e);
			}
		}
		throw new StorageException("Failed to read stored files");
	}

	@Override
	public Resource loadAsResource(String filename, Integer userId) {
		try {
			Path file = load(filename, userId);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public boolean isExist(MultipartFile file, Integer userId) throws IOException {
		String folder = getUserFolder(userId);
		Path userPath = Paths.get(folder);
		Path destinationFile = userPath.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
		File checkFile = new File(destinationFile.toString());
		return checkFile.exists();
	}

	@Override
	public boolean isPdfFile(MultipartFile file) throws IOException {
		String fileType = "";
		try (InputStream inputStream = file.getInputStream()) {
			Tika tika = new Tika();
			fileType = tika.detect(inputStream);
		}
		if (fileType.equals("application/pdf")) {
			return true;
		}
		return false;
	}

}
