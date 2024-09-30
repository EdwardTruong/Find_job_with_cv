package com.example.demo.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Const {
	public final static class DIRECTORY_UPLOAD_CV {
		 public final static String PATH_CREATE = "./src/main/resources/static/assets/cvs/";
	}
	
	public final static class DIRECTORY_IMAGE {
		 public final static String PATH_PERMANENT = "./src/main/resources/static/assets/upload/";
	}
	
	public final static class ALL_IMAGES_PATH{
		 public final static String PATH = "/assets/upload/";
	}
	
	public final static class UNIT{
		public final static Set<Character> SPECIAL_CHACTERLIST = new HashSet<>(Arrays.asList('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'));

	}
	
	
}
