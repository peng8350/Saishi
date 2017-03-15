package com.peng.saishi.interfaces.observer;

public interface BadgeListener {
	
	//更新显示
	public void onBadgeUpdate(int count);
	
	public void onBadgesMinus(int count);
	
	public void onBadge2Visiable(boolean flag);

}
