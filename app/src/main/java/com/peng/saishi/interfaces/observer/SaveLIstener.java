package com.peng.saishi.interfaces.observer;

public interface SaveLIstener {
	
	//保存成功
	public void OnSaveSuccess();
	//保存失败的毁掉
	public void OnSaveFailed();
}
