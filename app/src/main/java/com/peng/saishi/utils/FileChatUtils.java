package com.peng.saishi.utils;

import android.widget.BaseAdapter;

import com.peng.saishi.adapter.ChatAdapter;
import com.peng.saishi.entity.FileChatEntity;

import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

public class FileChatUtils {

	// 修改状态
	public static void updateStatus(Conversation conver,
			FileChatEntity fEntity, String status, ChatAdapter adapter) {
		fEntity.getMsg().getContent().setStringExtra("status",
				status);
		conver.updateMessageExtra(fEntity.getMsg(), "status", status);
		adapter.notifyDataSetChanged();
	}

	// 修改状态
	public static void updateStatus(Conversation conver,
			FileChatEntity fEntity, String status) {
		((FileContent) fEntity.getMsg().getContent()).setStringExtra("status",
				status);
		conver.updateMessageExtra(fEntity.getMsg(), "status", status);
	}

}
