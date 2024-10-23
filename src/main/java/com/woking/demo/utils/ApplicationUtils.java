package com.woking.demo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.woking.demo.entity.RecruitmentEntity;


/*
 * 1. The getCurrentDate() method use to save current time when created new Recruitment entity and Apply_job entity.
 * 2. The checkInputDate() method use to check start day is earlier end day.
 * 3. The getNumberPagation() method use to convert list size number of page when using pagination 
 */

@Component
public class ApplicationUtils {
    

    public java.sql.Date getCurrentDateSQL(){
    Date currentDate = new Date();
    java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
    return sqlDate;
    } 
    
    public String getCurrentDateJavaUtils() {
    	 Date currentDate = new Date();
    	 SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    	 String currentDateString = formatter.format(currentDate);
    	 return currentDateString;
    }
    
    public String getStringDate(Date inputDate) {
    	SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    	return formatter.format(inputDate);
    }
    
  
	public boolean checkInputDate(String inputStartDate, String inputEndDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss zzz yyyy");
		LocalDate dateStart = LocalDate.parse(inputStartDate, formatter);
		LocalDate dateEnd = LocalDate.parse(inputEndDate, formatter);
		return dateStart.isAfter(dateEnd);
	}

	
	public Date getDateByString(String inputDate) throws ParseException {
		 DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
		 Date date = df.parse(inputDate);
        return date;
	}
	
	public String changeDateFormatDMY(String dateTypeString) {
		 SimpleDateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
	        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
	        String formattedDate = "";
	        try {
	            Date date = inputFormat.parse(dateTypeString);
	            formattedDate = outputFormat.format(date);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return formattedDate;
	}
	
    public int[] getNumberPagation(int number){
        int[] arr = new int[number];
		for(int i : arr){
            arr[i] = i;
        }
        return arr;
    }





    // Chưa đụng tới
    // public boolean existRecruitmentIOfUserDto(UserDto user, int id){
    //    List<RecruitmentEntity> listRecruitment = user.getSaveRecruitment();
    //    quickSort(listRecruitment, 0, listRecruitment.size()-1);
    //    if(binarySearching(listRecruitment,id)){
    //     return true;
    //    }
    //    return false;
    // }


    public void quickSort(List<RecruitmentEntity> listRecruitment, int low, int high) {
        if (low >= high) {
            return;
        }

        // Chọn phần tử chốt
        int middle = low + (high - low) / 2;
        int pivot = listRecruitment.get(middle).getId();

        // Phân chia mảng thành 2 phần trước và sau phần tử chốt
        int i = low, j = high;
        while (i <= j) {
            while (listRecruitment.get(i).getId() < pivot) {
                i++;
            }

            while (listRecruitment.get(j).getId() > pivot) {
                j--;
            }

            if (i <= j) {
                RecruitmentEntity temp = listRecruitment.get(i);
                listRecruitment.set(i, listRecruitment.get(j));
                listRecruitment.set(j, temp);
                i++;
                j--;
            }
        }

        // Gọi đệ quy để sắp xếp 2 phần đã phân chia
        if (low < j) {
            quickSort(listRecruitment, low, j);
        }

        if (high > i) {
            quickSort(listRecruitment, i, high);
        }
    }


    public boolean binarySearching(List<RecruitmentEntity> list , int x){
        
        int i = 0 , length = list.size() -1;

        while (i<=length){
            int mid = i+(length-i)/2;
           
            if(mid == x){
                return true;
            }
            if (list.get(mid).getId() <= x){
                i = mid + 1;
            }else{
                length = mid -1;
            }
        }
        return false;
    }

}
