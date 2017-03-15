package com.peng.saishi.utils;

import android.text.TextUtils;

public class CheckUtils {
	
	/**
	 *  string 为要判断的字符串,检查是否包含不法字符串
	 * @param string
	 * @return
	 */
	public static boolean isConSpeCharacters(String string) {
		if (string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0) {
			// 不包含特殊字符
			return false;
		}
		return true;
	}
	
	/** 
	 * 验证手机格式 
	 */  
	public static boolean isMobileNO(String mobiles) {  
	    /* 
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
	    联通：130、131、132、152、155、156、185、186 
	    电信：133、153、180、189、（1349卫通） 
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
	    */  
	    String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
	    if (TextUtils.isEmpty(mobiles)) return false;  
	    else return mobiles.matches(telRegex);  
	   }  

}
