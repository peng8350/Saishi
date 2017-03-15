package com.peng.saishi.entity;

import com.qiniu.android.storage.UpProgressHandler;

public abstract class MyUploadProgressHandler implements UpProgressHandler {
	private FileChatEntity entity;

	public FileChatEntity getEntity() {
		return entity;
	}

	public void setEntity(FileChatEntity entity) {
		this.entity = entity;
	}


	
	

}
