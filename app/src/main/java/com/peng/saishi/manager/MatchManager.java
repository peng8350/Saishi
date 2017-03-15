package com.peng.saishi.manager;

import java.util.List;

import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.utils.GlobeScopeUtils;

/**
 * 比赛的管理者
 * @author peng
 *
 */
public class MatchManager {
	
	/**
	 * 通过id捕获比赛的javabean
	 */
	
	public static MatchInfo getMatchById(int id){
		List<MatchInfo> matches = GlobeScopeUtils.getMatches();
		for(MatchInfo info:matches){
			if (info.getId()==id) {
				return info;
			}
		}
		return null;
	}
	

}
