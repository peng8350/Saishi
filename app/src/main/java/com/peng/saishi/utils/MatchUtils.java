package com.peng.saishi.utils;

import com.peng.saishi.entity.MatchInfo;

import java.util.ArrayList;
import java.util.List;


/*
 * \
 * 比赛管理的工具类
 * author by 鹏
 */
public class MatchUtils {

	// 从一个大集合获得小集合,限制于类型1
	public static List<MatchInfo> getPart1Match(List<MatchInfo> biglist, int type) {
		List<MatchInfo> params = new ArrayList<>();
		for (MatchInfo info : biglist) {
			System.out.println(info.getType1());
			if (info.getType1() == type) {

				params.add(info);
			}
		}
		return params;
	}

	// 从一个大集合获得小集合,限制于类型2
	public static List<MatchInfo> getPart2Match(List<MatchInfo> biglist, int type) {
		List<MatchInfo> params = new ArrayList<>();
		for (MatchInfo info : biglist) {
			if (info.getType2() == type) {
				params.add(info);
			}
		}
		return params;
	}
	
	// 从一个大集合获得小集合,限制于类型2,并且
	public static List<MatchInfo> getPart2Match15Limit(List<MatchInfo> biglist, int type) {
		List<MatchInfo> params = new ArrayList<>();
		for (MatchInfo info : biglist) {
			if (params.size()>15) {
				return params;
			}
			if (info.getType2() == type) {
				params.add(info);
			}
		}
		return params;
	}

}
