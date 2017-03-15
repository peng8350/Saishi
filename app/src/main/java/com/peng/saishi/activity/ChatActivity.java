package com.peng.saishi.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.ExpandableListView.OnGroupClickListener;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import com.iflytek.cloud.*;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.ChatAdapter;
import com.peng.saishi.entity.ChatEntity;
import com.peng.saishi.entity.FileChatEntity;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.observer.ChatPicCallBack;
import com.peng.saishi.interfaces.observer.SaveLIstener;
import com.peng.saishi.manager.PickManager;
import com.peng.saishi.manager.QiniuManager;
import com.peng.saishi.utils.*;
import com.peng.saishi.utils.AudioRecoderUtils.OnAudioStatusUpdateListener;
import com.peng.saishi.widget.emojicon.EmojiconGridFragment.OnEmojiconClickedListener;
import com.peng.saishi.widget.emojicon.EmojiconsFragment;
import com.peng.saishi.widget.emojicon.emoji.Emojicon;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase.OnRefreshListener2;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshExpandableListView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import net.qiujuer.genius.blur.StackBlur;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends BaseBackActivity implements
		OnAudioStatusUpdateListener, ChatPicCallBack, TextWatcher,
		OnGroupClickListener, OnTouchListener,
		EmojiconsFragment.OnEmojiconBackspaceClickedListener,
		OnEmojiconClickedListener, OnKeyListener,
		OnRefreshListener2<ExpandableListView>, OnHanlderResultCallback {
	private View mViewVoice;// 语音界面
	private View mViewInput;
	private PullToRefreshExpandableListView listview;
	private List<List<ChatEntity>> chats;
	private ProgressBar upload_bar;

	private HorizontalScrollView mLlAffix;
	private EditText mEtMsg;
	private FrameLayout biaoqing_ll;
	private TeamInfo team;
	private InputMethodManager mInputMethodManager;
	private Button btn_send, btn_add;
	private TextView title;
	ChatAdapter adapter;
	private long t_time = 0;
	private SpeechRecognizer mSpeech;
	public static final int IFFINISH = 201;
	private static final int SEND_FILE = 454;
	private ExpandableListView refresh_listview;
	private StringBuffer ret1, ret2;
	private Conversation conversation;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_chat);
		super.init();
		if (new File(AppConfig.File_PATh).exists()) {
			new File(AppConfig.File_PATh).mkdirs();
		}
		findViewById(R.id.tv_chatmain_affix_micophone).setOnClickListener(this);
		findViewById(R.id.Chat_imageView1).setOnClickListener(this);
		findViewById(R.id.tv_chatmain_affix_location).setOnClickListener(this);
		findViewById(R.id.tv_chatmain_affix_folder).setOnClickListener(this);
		upload_bar = (ProgressBar) findViewById(R.id.Chat_bar);

		mLlAffix = (HorizontalScrollView) findViewById(R.id.ll_chatmain_affix);
		listview = (PullToRefreshExpandableListView) findViewById(R.id.Chat_listView);
		listview.setLoadingDrawable(new BitmapDrawable(BitmapFactory
				.decodeResource(getResources(), R.drawable.loading)));
		refresh_listview = listview.getRefreshableView();
		team = getIntent().getParcelableExtra("team");
		listview.setPullLabel("努力加载中....");// 刚上拉时显示的提示
		listview.setRefreshingLabel("努力加载中....");// 加载时的提示
		listview.setReleaseLabel("努力加载中....");// 上拉达到一定距离时显示的提示
		conversation = GlobeScopeUtils.Get("conversation");
		if (conversation == null) {
			conversation = JMessageClient.getGroupConversation(team
					.getGroupid());
		}
		listview.setOnRefreshListener(this);
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mEtMsg = (EditText) findViewById(R.id.msg_et);
		title = (TextView) findViewById(R.id.Chat_title);
		biaoqing_ll = (FrameLayout) findViewById(R.id.emojicons);
		initRecorderView();
		// listview.setAdapter(new ChatAdapter(this, chats));
		// 设置监听器
		findViewById(R.id.face_btn).setOnClickListener(this);
		findViewById(R.id.ib_chatmain_msg).setOnClickListener(this);
		findViewById(R.id.send_btn).setOnClickListener(this);
		btn_send = (Button) findViewById(R.id.send_btn);
		btn_add = (Button) findViewById(R.id.btn_chat_affix);
		btn_add.setOnClickListener(this);
		findViewById(R.id.tv_chatmain_affix_take_picture).setOnClickListener(
				this);
		findViewById(R.id.tv_chatmain_affix_album).setOnClickListener(this);
		JMessageClient.registerEventReceiver(this);
		mEtMsg.setOnKeyListener(this);
		mEtMsg.setOnTouchListener(this);
		mEtMsg.addTextChangedListener(this);
		refresh_listview.setOnGroupClickListener(this);
		title.setText(team.getName());

		chats = getChatRecord();
		adapter = new ChatAdapter(this, chats, conversation);

		adapter.setListener(this);
		refresh_listview.setAdapter(adapter);
		if (adapter.getTotal() != 0) {
			adapter.ContinueUpload();
		}
		int connt = refresh_listview.getExpandableListAdapter().getGroupCount();
		for (int i = 0; i < connt; i++) {
			refresh_listview.expandGroup(i);
		}
		refresh_listview.setSelection(refresh_listview.getCount() - 1);

		SpeechUtility.createUtility(ChatActivity.this, "appid=57d013f5");

		mSpeech = SpeechRecognizer.createRecognizer(this, mInitListener);
	}

	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d("mytest", "SpeechRecognizer init() code = " + code);
		}
	};

	public RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onEndOfSpeech() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(SpeechError arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onResult(RecognizerResult results, boolean arg1) {
			// 正确的结果
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onVolumeChanged(int arg0, byte[] arg1) {
			// TODO Auto-generated method stub

		}

	};

	// 拼音转文字
	public void ChangeText(String audio_path) {
		byte[] data = SpeechUtils.readFileFromSDcard(this, audio_path);
		ArrayList<byte[]> buffers = SpeechUtils.splitBuffer(data, data.length,
				1280);
		SpeechUtils.writeaudio(buffers, mSpeech, mRecognizerListener);
	}

	private String parseIatResult(String json) {
		ret1 = new StringBuffer();
		ret2 = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);
			Log.i("joResult", "joResult:" + joResult.toString());
			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 转写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");

				JSONObject obj = items.getJSONObject(0);
				ret1.append(obj.getString("w"));
				Log.i("joResult", "items.length():" + items.length());

				switch (items.length()) {

					case 1:
						JSONObject obj1 = items.getJSONObject(0);
						ret2.append(obj1.getString("w"));
						break;

					case 2:
						JSONObject obj2 = items.getJSONObject(1);
						ret2.append(obj2.getString("w"));
						break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret1.toString();
	}

	private List<List<ChatEntity>> getChatRecord() {
		// TODO Auto-generated method stub

		List<List<ChatEntity>> params = new ArrayList<List<ChatEntity>>();

		List<ChatEntity> point_list = null;
		if (conversation == null) {
			return new ArrayList<List<ChatEntity>>();
		}
		List<Message> msgs = conversation.getMessagesFromNewest(0, 15);
		for (Message msg : msgs) {
			if (!msg.getContentType().equals(ContentType.text)) {
				continue;
			}
			String username = "";
			String content = "";
			int userid = 0;
			int type = 0;
			try {
				JSONObject obj = new JSONObject(
						((TextContent) msg.getContent()).getText());
				username = obj.getString("username");
				content = obj.getString("content");
				userid = obj.getInt("userid");
				type = obj.getInt("type");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long cur_time = msg.getCreateTime();
			String time = TimeUtils.dateToStrLong(new Date(cur_time));
			ChatEntity entity = null;
			if (type != 4) {

				entity = new ChatEntity(0, userid, content, time, type,
						username);
			} else {
				entity = new FileChatEntity(0, userid, content, time, type,
						username, 0, msg);
			}
			if ((long) Math.abs(cur_time - t_time) >= 60000) {
				// 假如不在梁分钟之内
				List<ChatEntity> list = new ArrayList<ChatEntity>();
				point_list = list;
				params.add(list);
				point_list.add(entity);
				t_time = cur_time;
			} else {

				point_list.add(entity);
			}
		}
		return params;
	}

	private View mChatPopWindow;
	private AudioRecoderUtils recoderUtils;
	private ImageButton mIbVoiceBtn;
	private ImageButton mIbMsgBtn;// 文字按钮
	private TextView mTvVoiceBtn;// 语音按钮
	private ImageView record_icon;
	private TextView tv_time;

	/**
	 * 初始化语音布局
	 */
	private void initRecorderView() {
		recoderUtils = new AudioRecoderUtils();
		recoderUtils.setOnAudioStatusUpdateListener(this);
		mIbMsgBtn = (ImageButton) findViewById(R.id.ib_chatmain_msg);
		mViewVoice = findViewById(R.id.ll_chatmain_voice);
		mIbVoiceBtn = (ImageButton) findViewById(R.id.ib_chatmain_voice);
		mViewInput = findViewById(R.id.ll_chatmain_input);
		mTvVoiceBtn = (TextView) findViewById(R.id.tv_chatmain_press_voice);
		mIbMsgBtn.setOnClickListener(this);
		mTvVoiceBtn.setOnClickListener(this);
		mIbVoiceBtn.setOnClickListener(this);
		mTvVoiceBtn.setOnTouchListener(this);

		// include包含的布局语音模块
		mChatPopWindow = this.findViewById(R.id.rcChat_popup);
		record_icon = (ImageView) mChatPopWindow
				.findViewById(R.id.iv_recording_icon);
		tv_time = (TextView) mChatPopWindow
				.findViewById(R.id.tv_recording_time);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final RecognizerDialog recognizerDialog = new RecognizerDialog(this,
				mInitListener);
		// 这里的appid应该写从科大讯飞申请到的appid，为保密，用123467代替了，不能保证用123467能高运行
		recognizerDialog.show();
		recognizerDialog.setListener(new RecognizerDialogListener() {

			@Override
			public void onError(SpeechError arg0) {
				// TODO Auto-generated method stub
				recognizerDialog.dismiss();
			}

			@Override
			public void onResult(RecognizerResult arg0, boolean arg1) {
				// TODO Auto-generated method stub
				radio_builder.append(parseIatResult(arg0.getResultString()));
				if (arg1) {

					mEtMsg.setText(radio_builder.toString());
					radio_builder = new StringBuilder();
					recognizerDialog.dismiss();
				}
			}
		});
		return recognizerDialog;
	}

	// 发送信息
	/*
	 * * name用户的名称 ,content,//发送内容 userid//发送人的id ,teamid//队伍的id
	 * channels//要推送的设备
	 */
	public void SendMsg(String content) {
		if (content.equals("")) {
			return;
		}
		upload_bar.setVisibility(View.VISIBLE);
		mEtMsg.setText("");
		final JSONObject obj = new JSONObject();
		try {
			obj.put("groupid", team.getGroupid());
			obj.put("username", GlobeScopeUtils.getUserName());
			obj.put("teamid", team.getId());
			obj.put("userid", GlobeScopeUtils.getMeId());
			obj.put("content", content);
			obj.put("type", 1);
			obj.put("teamname", team.getName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			upload_bar.setVisibility(View.GONE);
		}
		final Message msg = JMessageClient.createGroupTextMessage(
				team.getGroupid(), obj.toString());
		if (msg == null) {
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				// 请写在线程运行的代码
				JMessageClient.sendMessage(msg);
			}
		}).start();
		msg.setOnSendCompleteCallback(new BasicCallback() {

			@Override
			public void gotResult(int arg0, String arg1) {
				// TODO Auto-generated method stub
				upload_bar.setVisibility(View.GONE);
				if (arg0 == 0) {
					try {
						ChatEntity entity = new ChatEntity(team.getId(), obj
								.getInt("userid"), obj.getString("content"),
								TimeUtils.getCurrentTime(), 1, GlobeScopeUtils
								.getUserName());

						AddtoList(entity);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					ToastUtils.ShowShortToast("发送失败");
				}
			}
		});
	}

	private void setEmojiconFragment(boolean useSystemDefault) {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.emojicons,
						EmojiconsFragment.newInstance(useSystemDefault))
				.commit();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (conversation != null && chats != null && chats.size() != 0)
			conversation.resetUnreadCount();

		super.onPause();
		mInputMethodManager.hideSoftInputFromWindow(mEtMsg.getWindowToken(), 0);
		biaoqing_ll.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		JMessageClient.unRegisterEventReceiver(this);
		adapter.CancelUpload();
		System.out.println("取消所有上传");
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == IFFINISH && arg1 == RESULT_OK) {
			finish();
		}
		if (arg0 == SEND_FILE && arg1 == RESULT_OK) {
			// 接受到文件
			final Uri uri = arg2.getData();

			// Get the File path from the Uri
			String path = FileUtils.getPath(this, uri);

			// Alternatively, use FileUtils.getFile(Context, Uri)
			if (path != null && FileUtils.isLocal(path)) {
				SendFile(path);
			}
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	// 用户发送文件
	private void SendFile(String path) {
		// 发送文件

		// 预订义消息
		final JSONObject obj = new JSONObject();
		final String filename = path.substring(path.lastIndexOf("/") + 1,
				path.length());
		File file = new File(path);
		try {
			obj.put("username", GlobeScopeUtils.getUserName());
			obj.put("teamid", team.getId());
			obj.put("groupid", team.getGroupid());
			obj.put("userid", GlobeScopeUtils.getMeId());
			obj.put("content", filename);
			obj.put("type", 4);
			obj.put("teamname", team.getName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 数据库也会创建
		final Message msg = JMessageClient.createGroupTextMessage(
				team.getGroupid(), obj.toString());
		if (conversation == null) {
			conversation = JMessageClient.getGroupConversation(team
					.getGroupid());
			adapter.setConversation(conversation);
		}
		msg.getContent().setStringExtra("size", "" + file.length());
		msg.getContent().setStringExtra("status",  "0");
		msg.getContent().setStringExtra("path", path);
		conversation.updateMessageExtra(msg, "size", "" + file.length());
		conversation.updateMessageExtra(msg, "status", "0");
		// 第一步添加一个list进去,作为等待
		final FileChatEntity entity = new FileChatEntity(team.getId(),
				GlobeScopeUtils.getMeId(), filename,
				TimeUtils.getCurrentTime(), 4, GlobeScopeUtils.getUserName(),
				0, msg);
		AddtoList(entity);
		QiniuManager.PrepartUpload(path, entity, adapter);

	}

	private void SendPic(final String path) {
		// TODO Auto-generated method stub科大
		final JSONObject obj = new JSONObject();
		final String id = System.currentTimeMillis() + ""
				+ GlobeScopeUtils.getMeId();
		try {
			obj.put("username", GlobeScopeUtils.getUserName());
			obj.put("teamid", team.getId());
			obj.put("groupid", team.getGroupid());
			obj.put("userid", GlobeScopeUtils.getMeId());
			obj.put("content", id);
			obj.put("type", 2);
			obj.put("teamname", team.getName());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		upload_bar.setVisibility(View.VISIBLE);
		final Message msg = JMessageClient.createGroupTextMessage(
				team.getGroupid(), obj.toString());
		if (msg == null) {
			return;
		}
		FIleUtils.saveBitmap(BitmapFactory.decodeFile(path),
				AppConfig.pic_temp, new SaveLIstener() {

					@Override
					public void OnSaveSuccess() { // TODO Auto-generated method
						// stub
						// TODO Auto-generated method stub
						Bitmap b = StackBlur.blurNatively(
								BitmapFactory.decodeFile(AppConfig.pic_temp),
								60, true);
						FIleUtils.saveBitmap(b, AppConfig.blur_temp,
								new SaveLIstener() {

									@Override
									public void OnSaveSuccess() {
										// TODO Auto-generated method stub
										msg.setOnSendCompleteCallback(new BasicCallback() {

											@Override
											public void gotResult(int arg0,
																  String arg1) {
												// TODO Auto-generated method
												// stub
												upload_bar
														.setVisibility(View.GONE);
												if (arg0 == 0) {

													ChatEntity entity = null;
													try {
														entity = new ChatEntity(
																team.getId(),
																GlobeScopeUtils
																		.getMeId(),
																obj.getString("content"),
																TimeUtils
																		.getCurrentTime(),
																2,
																GlobeScopeUtils
																		.getUserName());
													} catch (JSONException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}
													AddtoList(entity);
												}
											}
										});
										// TODO Auto-generated method stub
										FIleUtils.UploadChatPic(
												path.endsWith("gif")
														|| path.endsWith("swf") ? path
														: AppConfig.pic_temp,
												id + "",
												new UpCompletionHandler() {

													@Override
													public void complete(
															String arg0,
															ResponseInfo arg1,
															JSONObject arg2) {
														// TODO Auto-generated
														// method stub

														// TODO
														// Auto-generated
														// method stub
														if (arg1.isOK()) {
															new Thread(
																	new Runnable() {
																		public void run() {
																			// 请写在线程运行的代码
																			JMessageClient
																					.sendMessage(msg);
																		}
																	}).start();
														} else {
															System.out
																	.println("上传失败:"
																			+ arg1.error);
															upload_bar
																	.setVisibility(View.GONE);
														}
														if (new File(
																AppConfig.pic_temp)
																.exists())
															new File(
																	AppConfig.pic_temp)
																	.delete();
													}
												});
									}

									@Override
									public void OnSaveFailed() {
										// TODO Auto-generated method stub
										upload_bar.setVisibility(View.GONE);
									}

								});

					}

					@Override
					public void OnSaveFailed() {
						// TODO Auto-generated method stvub
						upload_bar.setVisibility(View.GONE);
					}

				});

	}

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.back_tv:
				setResult(RESULT_OK);
				finish();

				break;
			case R.id.tv_chatmain_affix_micophone:
				// 语音直接录入输入框
				onCreateDialog(1);
				break;
			case R.id.send_btn:

				if (!mEtMsg.getText().toString().trim().equals("")) {
					SendMsg(mEtMsg.getText().toString());
				}
				break;
			case R.id.tv_chatmain_affix_take_picture: {
				mLlAffix.setVisibility(View.GONE);
				PickManager.getCamera(this, this, false);
				break;
			}
			case R.id.tv_chatmain_affix_album: {
				mLlAffix.setVisibility(View.GONE);
				PickManager.getAlmera(this, this, false);
				break;
			}
			case R.id.tv_chatmain_affix_folder:
				// 发送文件按
				ActChangeUtils.OpenFileChoose(this, SEND_FILE);
				break;

			case R.id.tv_chatmain_affix_location:
				// 用户发送当前位置
				SendMsg("我在这里[" + GlobeScopeUtils.getMeEntity().getReal_address()
						+ "]");
				break;
			case R.id.btn_chat_affix: {
				// 图片附件
				if (mLlAffix.isShown()) {
					mLlAffix.setVisibility(View.GONE);
				} else {
					// 这里存在一个问题
					mInputMethodManager.hideSoftInputFromWindow(
							mEtMsg.getWindowToken(), 0);
					mLlAffix.setVisibility(View.VISIBLE);
					biaoqing_ll.setVisibility(View.GONE);
				}
				break;
			}
			case R.id.ib_chatmain_msg: {
				// 切换文字shu按钮
				if (!mViewVoice.isShown()) {
					mInputMethodManager.hideSoftInputFromWindow(
							mEtMsg.getWindowToken(), 0);
					mViewVoice.setVisibility(View.VISIBLE);
					mViewInput.setVisibility(View.GONE);
					biaoqing_ll.setVisibility(View.GONE);
					mLlAffix.setVisibility(View.GONE);
				} else {
					mViewVoice.setVisibility(View.GONE);
					mViewInput.setVisibility(View.VISIBLE);
				}
				break;
			}
			case R.id.ib_chatmain_voice: {
				// 切换语音按钮

				if (!mViewVoice.isShown()) {

					mViewVoice.setVisibility(View.VISIBLE);
					mViewInput.setVisibility(View.GONE);

				} else {
					mViewVoice.setVisibility(View.GONE);
					mViewInput.setVisibility(View.VISIBLE);
				}
				break;
			}

			case R.id.Chat_imageView1:
				// 点击介绍队成员
				Intent intent = new Intent(this, TeamInfoActivity.class);
				intent.putExtra("team", team);
				startActivityForResult(intent, IFFINISH);
				break;
			case R.id.face_btn: {
				// mian b
				if (!emojshow) {
					setEmojiconFragment(false);
					emojshow = true;
				}
				if (!biaoqing_ll.isShown()) {
					mInputMethodManager.hideSoftInputFromWindow(
							mEtMsg.getWindowToken(), 0);
					biaoqing_ll.setVisibility(View.VISIBLE);
					mLlAffix.setVisibility(View.GONE);
				} else {
					biaoqing_ll.setVisibility(View.GONE);
				}
				break;
			}
			default:
				break;
		}
	}

	private boolean emojshow = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (mLlAffix.isShown() || biaoqing_ll.isShown()) {
			mLlAffix.setVisibility(View.GONE);
			biaoqing_ll.setVisibility(View.GONE);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
								int groupPosition, long id) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onEmojiconBackspaceClicked(View v) {
		// TODO Auto-generated method stub
		EmojiconsFragment.backspace(mEtMsg);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		super.onBackPressed();

	}

	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		// TODO Auto-generated method stub
		EmojiconsFragment.input(mEtMsg, emojicon);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_ENTER) {
			if (!mEtMsg.getText().toString().trim().equals(""))
				SendMsg(mEtMsg.getText().toString());
			return true;
		}
		return false;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				if (v.getId() == R.id.msg_et) {
					mLlAffix.setVisibility(View.GONE);
					biaoqing_ll.setVisibility(View.GONE);
				} else {
					mChatPopWindow.setVisibility(View.VISIBLE);

					recoderUtils.startRecord();
					mTvVoiceBtn.setText("松开结束");
				}
				break;

			case MotionEvent.ACTION_UP:
				if (v.getId() != R.id.msg_et) {
					// m
					if (mViewVoice.isShown()) {
						mViewVoice.setVisibility(View.GONE);
						mViewInput.setVisibility(View.VISIBLE);
						recoderUtils.stopRecord();
						mChatPopWindow.setVisibility(View.GONE);
						mTvVoiceBtn.setText("按住说话");
					}
				}
				break;
		}
		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		String content = s.toString();
		if (!content.trim().equals("")) {
			// 显示
			btn_send.setVisibility(View.VISIBLE);
			btn_add.setVisibility(View.GONE);
		} else {

			btn_add.setVisibility(View.VISIBLE);
			btn_send.setVisibility(View.GONE);

		}
	}

	public void onEventMainThread(MessageEvent event) {
		if ((!event.getMessage().getContentType().equals(ContentType.text) || !!event
				.getMessage().getContentType().equals(ContentType.file))
				|| event.getMessage().getTargetType()
				.equals(ConversationType.single)) {
			return;
		}
		String username = "";
		String content = "";
		int userid = 0;
		int type = 0;
		Message msg = event.getMessage();
		Date param_date = new Date(msg.getCreateTime());
		String time = TimeUtils.dateToStrLong(param_date);

		try {
			JSONObject obj = new JSONObject(
					((TextContent) msg.getContent()).getText());
			username = obj.getString("username");
			content = obj.getString("content");
			userid = obj.getInt("userid");
			type = obj.getInt("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ChatEntity entity = null;
		if (type == 4) {
			entity = new FileChatEntity(0, userid, content, time, type,
					username, 0, msg);
		} else
			entity = new ChatEntity(0, userid, content, time, type, username);
		ChatAdapter adapter = (ChatAdapter) refresh_listview
				.getExpandableListAdapter();
		adapter.notifyDataSetChanged();
		AddtoList(entity);

	}

	// 添加到队列
	public void AddtoList(ChatEntity entity) {
		if (chats.size() == 0) {
			List<ChatEntity> param = new ArrayList<ChatEntity>();
			param.add(entity);
			chats.add(param);
			ChatAdapter adapter = (ChatAdapter) refresh_listview
					.getExpandableListAdapter();
			for (int i = 0; i < 1; i++) {
				refresh_listview.expandGroup(i);
			}
			adapter.notifyDataSetChanged();

			return;
		}
		Date last_date = TimeUtils.getDateFromChatTime(chats.get(0).get(0)
				.getTime());
		if ((long) Math.abs(last_date.getTime()
				- TimeUtils.getDateFromChatTime(entity.getTime()).getTime()) >= 60000) {
			List<ChatEntity> param = new ArrayList<ChatEntity>();
			param.add(entity);
			chats.add(0, param);
		} else {

			chats.get(0).add(0, entity);
		}
		ChatAdapter adapter = (ChatAdapter) refresh_listview
				.getExpandableListAdapter();
		adapter.notifyDataSetChanged();

		int count = refresh_listview.getExpandableListAdapter().getGroupCount();
		for (int i = 0; i < count; i++) {
			refresh_listview.expandGroup(i);
		}
		refresh_listview.setSelection(refresh_listview.getCount() - 1);

	}

	// 录音中....db为声音分贝，time为录音时长
	@Override
	public void onUpdate(double db, long time) {
		// 根据分贝值来设置录音时话筒图标的上下波动，下面有讲解
		if (time > 30000) {
			mViewVoice.setVisibility(View.GONE);
			mViewInput.setVisibility(View.VISIBLE);
			recoderUtils.stopRecord();
			mChatPopWindow.setVisibility(View.GONE);
			mTvVoiceBtn.setText("按住说话");
			return;
		}
		record_icon.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
		tv_time.setText(TimeUtils.long2String(time));
	}

	private StringBuilder radio_builder = new StringBuilder();

	// 录音结束，filePath为保存路径
	@Override
	public void onStop(String filePath, int time) {

		if (time < 1) {
			ToastUtils.ShowShortToast("时间太短了");
			return;
		}
		tv_time.setText(TimeUtils.long2String(0));
		String id = System.currentTimeMillis() + "" + GlobeScopeUtils.getMeId();
		FIleUtils.UploadRadio(filePath, id + "");

		SendRadio(time, id);
	}

	@Override
	public void onCompleted() {
		// TODO Auto-generated method stub

	}

	public void SendRadio(int time, String id) {
		upload_bar.setVisibility(View.VISIBLE);
		final JSONObject obj = new JSONObject();
		try {
			obj.put("username", GlobeScopeUtils.getUserName());
			obj.put("groupid", team.getGroupid());
			obj.put("teamid", team.getId());
			obj.put("userid", GlobeScopeUtils.getMeId());
			obj.put("content", time + "''" + " " + id);
			obj.put("type", 3);
			obj.put("teamname", team.getName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Message msg = JMessageClient.createGroupTextMessage(
				team.getGroupid(), obj.toString());
		if (msg == null) {
			upload_bar.setVisibility(View.GONE);
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				// 请写在线程运行的代码
				JMessageClient.sendMessage(msg);
			}
		}).start();
		msg.setOnSendCompleteCallback(new BasicCallback() {

			@Override
			public void gotResult(int arg0, String arg1) {
				// TODO Auto-generated method stub
				upload_bar.setVisibility(View.GONE);
				if (arg0 == 0) {
					ChatEntity entity = null;
					try {
						entity = new ChatEntity(team.getId(), GlobeScopeUtils
								.getMeId(), obj.getString("content"), TimeUtils
								.getCurrentTime(), 3, GlobeScopeUtils
								.getUserName());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AddtoList(entity);
				} else {
					ToastUtils.ShowShortToast("发送失败");
				}
			}
		});
	}

	// 回复某人,追加et
	public void SubReply(String username) {
		mEtMsg.setText("@" + username + " ");
		mEtMsg.requestFocus();
		mEtMsg.setSelection(mEtMsg.length());
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				listview.onRefreshComplete();
				if (msg.arg1 == 1) {
					adapter.notifyDataSetChanged();
					ExpandAll();
					refresh_listview.setSelection(msg.arg2);
				}
			}
		};
	};

	@Override
	public void onPullDownToRefresh(
			PullToRefreshBase<ExpandableListView> refreshView) {
		// TODO Auto-generated method stub
		if (conversation == null) {
			listview.onRefreshComplete();
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				// 请写在线程运行的代码
				try {
					Thread.sleep(400);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int total = 0;
				List<ChatEntity> point_list = null;
				List<Message> msgs = conversation.getMessagesFromNewest(
						adapter.getTotal(), 15);
				System.out.println(msgs.size() + "size");
				for (Message msg : msgs) {
					if (!msg.getContentType().equals(ContentType.text)) {
						continue;
					}
					String username = "";
					String content = "";
					int userid = 0;
					int type = 0;
					try {
						JSONObject obj = new JSONObject(
								((TextContent) msg.getContent()).getText());
						username = obj.getString("username");
						content = obj.getString("content");
						userid = obj.getInt("userid");
						type = obj.getInt("type");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long cur_time = msg.getCreateTime();
					String time = TimeUtils.dateToStrLong(new Date(cur_time));

					ChatEntity entity = null;
					if (type == 4) {
						entity = new FileChatEntity(0, userid, content, time,
								type, username, 0, msg);
					} else
						entity = new ChatEntity(0, userid, content, time, type,
								username);
					if ((long) Math.abs(cur_time - t_time) >= 60000) {
						// 假如不在梁分钟之内
						List<ChatEntity> list = new ArrayList<ChatEntity>();
						point_list = list;
						chats.add(list);
						total += 2;
						point_list.add(entity);
						t_time = cur_time;
					} else {
						if (point_list == null) {
							chats.get(chats.size() - 1).add(entity);
							total += 1;
						} else {
							point_list.add(entity);
							total += 1;
						}
					}
				}
				android.os.Message msg = new android.os.Message();
				msg.what = 1;
				msg.arg2 = total;
				msg.arg1 = msgs.size() == 0 ? 0 : 1;
				handler.sendMessage(msg);
			}
		}).start();

	}

	public void ExpandAll() {
		int groupcount = refresh_listview.getExpandableListAdapter()
				.getGroupCount();
		for (int i = 0; i < groupcount; i++) {
			refresh_listview.expandGroup(i);
		}
	}

	@Override
	public void onPullUpToRefresh(
			PullToRefreshBase<ExpandableListView> refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
		// TODO Auto-generated method stub
		String path = resultList.get(0).getPhotoPath();
		SendPic(path);
	}

	@Override
	public void onHanlderFailure(int requestCode, String errorMsg) {
		// TODO Auto-generated method stub

	}

}