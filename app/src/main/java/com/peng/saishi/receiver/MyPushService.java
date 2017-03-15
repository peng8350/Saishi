package com.peng.saishi.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.jpush.android.api.JPushInterface;

import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.CaheUtils;
import com.peng.saishi.utils.JsonParser;

public class MyPushService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("接受到了");
		// System.out.println(intent.getStringExtra(JPushInterface.EXTRA_MESSAGE));
		if (!JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			return;
		}
		MatchInfo info = null;
		String msg = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
		try {
			JSONObject msg_json = new JSONObject(msg);
			if (msg_json.getString("msgtype").equals("1")) {

				info = JsonParser.getBeanFromJson(
						new JSONObject(msg).getJSONObject("obj"),
						MatchInfo.class);
				Intent intent2 = new Intent("action");
				intent2.putExtra("info", info);
				context.sendBroadcast(intent2);
			} else if (msg_json.getString("msgtype").equals("2")) {
				// 清空队伍缓存
				CaheUtils.ClearOneCahe(AppConfig.Teanpic_path
						+ msg_json.getString("id"));
			} else if (msg_json.getString("msgtype").equals("3")) {
				// 清空用户缓存
				CaheUtils.ClearOneCahe(AppConfig.Userpic_path
						+ msg_json.getString("id"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
