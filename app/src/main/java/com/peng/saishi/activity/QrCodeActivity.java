package com.peng.saishi.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class QrCodeActivity extends BaseBackActivity {

	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_capture);
		CaptureFragment captureFragment = new CaptureFragment();
		captureFragment.setAnalyzeCallback(analyzeCallback);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_zxing_container, captureFragment).commit();
		super.init();
	}
	
	
	
	private boolean haveTeam(int id) {
		// TODO Auto-generated method stub
		boolean flag = false;
		for (TeamInfo info : GlobeScopeUtils.getUserTeam1()) {
			if (info.getGroupid() == id) {
				flag = true;
				break;
			}
		}
		for (TeamInfo info : GlobeScopeUtils.getUserTeam2()) {
			if (info.getGroupid() == id) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	private String member = "";

	/**
	 * 二维码解析回调函数
	 */
	CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
		@Override
		public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
			if (result.startsWith("{")) {
				final Dialog dialog = DialogUtils.createLoadingDialog(QrCodeActivity.this, "正在处理中...");
				dialog.show();
				try {
					final JSONObject obj = new JSONObject(result);
					if (obj.has("id")) {
						final int id = obj.getInt("id");
						if (haveTeam(id)) {
							ToastUtils.ShowShortToast("你已经有这个队伍不需要添加了");
							dialog.dismiss();
							finish();
							return;
						}
						JMessageClient.getGroupMembers(id, new GetGroupMembersCallback() {

							@Override
							public void gotResult(int arg0, String arg1, final List<UserInfo> arg2) {
								// TODO Auto-generated method stub
								// String uid = request.getParameter("uid");
								// String groupid = request.getParameter("groupid");
								// String members_param =
								// request.getParameter("members");
								List<UserInfo> userInfos =arg2;
								if (arg0 == 0) {
									member = "";
									for (int i = 0; i < userInfos.size(); i++) {
										if (i == userInfos.size() - 1) {
											member += userInfos.get(i).getUserName();
										} else {
											member += userInfos.get(i).getUserName() + ",";
										}
									}

									new Thread(new Runnable() {
										public void run() {
											// 请写在线程运行的代码
											try {
												final JSONObject send_obj = new JSONObject();
												send_obj.put("type", 3);
												send_obj.put("members", member);
												send_obj.put("uid", GlobeScopeUtils.getUser() + "");
												send_obj.put("groupid", id + "");
												cn.jpush.im.android.api.model.Message msg = JMessageClient
														.createSingleTextMessage(obj.getString("owner"), send_obj.toString());
												JMessageClient.sendMessage(msg);
												msg.setOnSendCompleteCallback(new BasicCallback() {

													@Override
													public void gotResult(int arg0, String arg1) {
														// TODO Auto-generated
														// method stub
														dialog.dismiss();
														if (arg0 == 0) {
															ToastUtils.ShowShortToast("成功!等待队长上线即可进群");
															finish();
														}
													}
												});
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}).start();
								} else {
									dialog.dismiss();
									ToastUtils.ShowShortToast("不明原因");
								}
							}

						});

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (result.equals("")) {
				Toast.makeText(QrCodeActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			} else {
				ActChangeUtils.ChangeActivity(QrCodeActivity.this, WebActivity.class,"url",result);
				
			}
		}

		@Override
		public void onAnalyzeFailed() {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
			bundle.putString(CodeUtils.RESULT_STRING, "");
			resultIntent.putExtras(bundle);
			QrCodeActivity.this.setResult(RESULT_OK, resultIntent);
			QrCodeActivity.this.finish();
		}
	};

}
