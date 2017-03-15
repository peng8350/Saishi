package com.peng.saishi.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.User;

public class GlobeScopeUtils {
	
	public static Map<String, Object> scopes;
	
	public static void Put(String key, Object value){
		if (scopes==null) {
			scopes= new HashMap<>();
		}
		scopes.put(key, value);
	}
	
	//获取当前user的对象
	public static  User getMeEntity(){
		return GlobeScopeUtils.Get("ME");
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T Get(String key){
		return (T) scopes.get(key);
	}
	
	//获取自己的id
	public static int getMeId(){
		return Integer.parseInt(((User)GlobeScopeUtils.Get("ME")).getId()+"");
	}
	
	//获取自己的用胡
	public static String getUser(){
		return  ((User)GlobeScopeUtils.Get("ME")).getUser();
	}
	
	//获取自己的用胡明
	public static String getUserName(){
		return  ((User)GlobeScopeUtils.Get("ME")).getUsername();
	}
	
	//获取用户的比赛
	public static List<MatchInfo> getMatches(){
		return  ((User)GlobeScopeUtils.Get("ME")).getMatchs();
	}
	//获取用户的收藏
	public static List<MatchInfo> getShoucangs(){
		return  ((User)GlobeScopeUtils.Get("ME")).getShoucangs();
	}

	//获取用户的队伍1
	public static List<TeamInfo> getUserTeam1(){
		return  ((User)GlobeScopeUtils.Get("ME")).getTeam1();
	}

	//获取用户的队伍1
	public static List<TeamInfo> getUserTeam2(){
		return  ((User)GlobeScopeUtils.Get("ME")).getTeam2();
	}
	
	
	//获取当前对话
	public static int getConver(){
		return GlobeScopeUtils.Get("conversation");
	}



}
