package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CvDto;
import com.example.demo.entity.CvEntity;

/*
 * A. The methods : CRUD
 * 		1.Created and save into database : 
 * 		-	storedCvToLocalAndDB(MultipartFile file, Integer userId)
 * 	
 * 		2.Find Cv : 
 * 		-  	CvEntity findById(Integer id); 	
 * 		-	CvEntity findByName(String name);
 * 		-	List<CvDto> findCvByUserId(Integer userId);
 * 
 * 		3.Update cv :
 * 		-	upLoadteCv(CvEntity cvEntity);		
 * 
 *  	4.Delete Cv : user delete cv. 	
 * 		- 	deleteCvEntity(CvEntity cvEntity); delete on database.

 *  BONUTE : 
 *  	1. To save cv(file.pdf) just need save name into database or save type BLOB-Binary Large Object (MultipartFile.getByte())
 *  		a.If used to save file's name the program need space in local to store context of file.
 *  			.The program's memory needs to be large if the user uploads many files.
 *  			.The database's memory capacity is not much.
 *  		b.If used to save BLOB-Binary Large Object into database then database's memory need to large.
 *  			.In reality, large files will be stored on disk and in a database that only stores the path or reference to them.
 *  
 *  	2. In this project i use both to make it. I going to update later for stored on disk only when i upload this project on github.	
 *		
 *		3. Files uploaded by users need to be in email format and the file names need to be different. 
 */

public interface CvService {
	
	CvEntity storedCvToLocalAndDB(MultipartFile file, Integer userId) throws IOException;

	CvEntity findById(Integer id);

	CvEntity findByName(String name);

	void upLoadteCv(CvEntity cvEntity);

	void deleteCvEntity(CvEntity cvEntity);

	List<CvDto> findCvByUserId(Integer userId);

}
