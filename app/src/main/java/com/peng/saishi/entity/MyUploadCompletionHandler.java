package com.peng.saishi.entity;

import com.qiniu.android.storage.UpCompletionHandler;

public abstract class MyUploadCompletionHandler implements UpCompletionHandler {
	private FileChatEntity entity;

	public FileChatEntity getEntity() {
		return entity;
	}

	public void setEntity(FileChatEntity entity) {
		this.entity = entity;
	}
	
}
