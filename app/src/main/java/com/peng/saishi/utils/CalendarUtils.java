package com.peng.saishi.utils;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
	
    /** 
     * 获取增加多少天的时间 
     *  
     * @return addDay - 增加多少天 
     */  
    public static Date getAddDayDate(int addDay,Date now_date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(now_date);
        calendar.add(Calendar.DAY_OF_MONTH, addDay);  
        
        return calendar.getTime();  
    }  

}
