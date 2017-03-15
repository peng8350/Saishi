package com.peng.saishi.adapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ImageDecodeOptionsBuilder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.peng.saishi.R;
import com.peng.saishi.activity.ChatActivity;
import com.peng.saishi.activity.PhotoActivity;
import com.peng.saishi.activity.SeePersonActivity;
import com.peng.saishi.entity.ChatEntity;
import com.peng.saishi.entity.FileChatEntity;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.observer.ChatPicCallBack;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.QiniuManager;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.FileChatUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.OpenFileUtils;
import com.peng.saishi.utils.PreferencesTool;
import com.peng.saishi.utils.QiniuUtils;
import com.peng.saishi.utils.RecorderControl;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.CircleProgressDrawable;
import com.peng.saishi.widget.numberprogressbar.NumberProgressBar;
import com.zhy.http.okhttp.OkHttpUtils;

@SuppressLint("InflateParams")
public class ChatAdapter extends BaseExpandableListAdapter {
	private Context context = null;
	private RecorderControl control;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap<Integer, Integer> map = new HashMap();
	private Conversation conversation;

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public ChatPicCallBack getListener() {
		return listener;
	}

	public void setListener(ChatPicCallBack listener) {
		this.listener = listener;
	}

	private List<List<ChatEntity>> chatList;
	private LayoutInflater inflater = null;
	public static int COME_MSG = 0;
	public static int TO_MSG = 1;
	public static int COME_PIC_MSG = 2;
	public static int TO_PIC_MSG = 3;
	public static int COME_RADIO_MSG = 4;
	public static int TO_RADIO_MSG = 5;
	public static int COME_FILE_MSG = 6;
	public static int TO_FILE_MSG = 7;
	private HttpUtils httpUtils;
	int myid = 0;
	private ChatPicCallBack listener;

	public List<List<ChatEntity>> getChatList() {
		return chatList;
	}

	public void setChatList(List<List<ChatEntity>> chatList) {
		this.chatList = chatList;
	}

	@SuppressWarnings("deprecation")
	public ChatAdapter(Context context, List<List<ChatEntity>> chatList,
			Conversation conversation) {
		this.context = context;
		this.chatList = chatList;
		this.conversation = conversation;
		myid = GlobeScopeUtils.getMeId();
		control = new RecorderControl();
		httpUtils = new HttpUtils();
		initPoP();
		pool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
		map.put(1, pool.load(context, R.raw.beeg, 1));
		inflater = LayoutInflater.from(this.context);
	}

	private class GroupHolder {
		TextView time;
	}

	private class ChatHolder {
		private TextView name_tv;
		private SimpleDraweeView userImageView;
		private View contentTextView;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return chatList == null ? 0 : chatList.size();
	}

	@Override
	public int getChildTypeCount() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public int getChildType(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ChatEntity entity = chatList.get(chatList.size() - 1 - groupPosition)
				.get(chatList.get(chatList.size() - 1 - groupPosition).size()
						- 1 - childPosition);
		;
		if (entity.getUserid() != myid && entity.getType() == 1) {
			return COME_MSG;
		} else if (entity.getUserid() == myid && entity.getType() == 1) {
			return TO_MSG;
		} else if (entity.getUserid() != myid && entity.getType() == 2) {
			return COME_PIC_MSG;
		} else if (entity.getUserid() == myid && entity.getType() == 2) {
			return TO_PIC_MSG;
		} else if (entity.getUserid() != myid && entity.getType() == 3) {
			return COME_RADIO_MSG;
		} else if (entity.getUserid() == myid && entity.getType() == 3) {
			return TO_RADIO_MSG;
		} else if (entity.getUserid() != myid && entity.getType() == 4) {
			return COME_FILE_MSG;
		} else {
			return TO_FILE_MSG;
		}
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return chatList.get(chatList.size() - 1 - groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	private PopupWindow more_pop;

	private void initPoP() {
		// TODO Auto-generated method stub
		more_pop = new PopupWindow(250, 75);
		more_pop.setBackgroundDrawable(new ColorDrawable(
				android.R.color.transparent));
		more_pop.setContentView(LayoutInflater.from(context).inflate(
				R.layout.pop_msg, null));
		more_pop.setFocusable(true);
		more_pop.setOutsideTouchable(true);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View view,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupHolder vh;
		if (view == null) {
			vh = new GroupHolder();
			view = inflater.inflate(R.layout.group_chat, null);
			vh.time = (TextView) view.findViewById(R.id.tv_time);

			view.setTag(vh);

		} else {
			vh = (GroupHolder) view.getTag();
		}
		if (chatList.get(chatList.size() - 1 - groupPosition).get(0) != null) {

			vh.time.setText(chatList.get(chatList.size() - 1 - groupPosition)
					.get(0).getTime());
		}

		return view;

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ChatEntity entity = chatList.get(
				chatList.size() - 1 - groupPosition).get(
				chatList.get(chatList.size() - 1 - groupPosition).size() - 1
						- childPosition);
		final ChatHolder chatHolder;
		if (view == null) {
			chatHolder = new ChatHolder();
			if (entity.getUserid() != myid && entity.getType() == 1) {
				view = inflater.inflate(R.layout.item_chatfrom, null);
			} else if (entity.getUserid() == myid && entity.getType() == 1) {
				view = inflater.inflate(R.layout.item_chatto, null);
			} else if (entity.getUserid() != myid && entity.getType() == 2) {
				view = inflater.inflate(R.layout.item_chatfrom2, null);
			} else if (entity.getUserid() == myid && entity.getType() == 2) {
				view = inflater.inflate(R.layout.item_chatto2, null);
			} else if (entity.getUserid() != myid && entity.getType() == 3) {
				view = inflater.inflate(R.layout.item_chatfrom3, null);
			} else if (entity.getUserid() == myid && entity.getType() == 3) {
				view = inflater.inflate(R.layout.item_chatto3, null);
			} else if (entity.getUserid() != myid && entity.getType() == 4) {
				view = inflater.inflate(R.layout.item_chatfrom4, null);
			} else {
				view = inflater.inflate(R.layout.item_chatto4, null);
			}

			chatHolder.name_tv = (TextView) view.findViewById(R.id.tv_name);
			chatHolder.contentTextView = view.findViewById(R.id.tv_content);
			chatHolder.userImageView = (SimpleDraweeView) view
					.findViewById(R.id.iv_user_image);
			view.setTag(chatHolder);
		} else {
			chatHolder = (ChatHolder) view.getTag();
		}
		if (entity.getType() == 1) {
			((TextView) chatHolder.contentTextView)
					.setText(entity.getContent());
		} else if (entity.getType() == 2) {
			ImageDecodeOptions deocdeoptions = new ImageDecodeOptionsBuilder()
					.setDecodePreviewFrame(true).build();
			GenericDraweeHierarchy hierarchy = ((SimpleDraweeView) chatHolder.contentTextView)
					.getHierarchy();
			hierarchy.setProgressBarImage(new CircleProgressDrawable());
			((SimpleDraweeView) chatHolder.contentTextView)
					.setHierarchy(hierarchy);
			ResizeOptions options = new ResizeOptions(250, 250);
			ImageRequest request = ImageRequestBuilder
					.newBuilderWithSource(
							Uri.parse(AppConfig.qiniu_Url + entity.getContent()))
					.setImageDecodeOptions(deocdeoptions)
					.setResizeOptions(options).build();
			DraweeController controller = Fresco
					.newDraweeControllerBuilder()
					.setOldController(
							((SimpleDraweeView) chatHolder.contentTextView)
									.getController())
					.setLowResImageRequest(
							ImageRequest.fromUri(AppConfig.qiniu_Url + "blur"
									+ entity.getContent()))
					.setImageRequest(request).setAutoPlayAnimations(true)
					.build();
			((SimpleDraweeView) chatHolder.contentTextView)
					.setController(controller);

			((SimpleDraweeView) chatHolder.contentTextView)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ActChangeUtils.ChangeActivity((Activity) context,
									PhotoActivity.class, new String[] { "type",
											"id" }, entity.getContent(), 4);

						}
					});
		} else if (entity.getType() == 3) {
			TextView durtion = (TextView) view.findViewById(R.id.t);
			final String[] param = entity.getContent().split(" ");
			final File folder = new File(AppConfig.Radiohold_path);
			final boolean ishold = new File(folder + "/" + param[1] + ".amr")
					.exists();
			((TextView) chatHolder.contentTextView)
					.setTextColor(ishold ? Color.WHITE : Color.RED);
			((TextView) chatHolder.contentTextView).setText(param[0]);
			durtion.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					boolean playing = v.isSelected();
					if (control.isPlayState()) {
						control.stopPlaying();
					}

					if (playing) {
						return;
					}
					// 播放录音
					if (!ishold) {
						// 没有缓存

						if (!folder.exists()) {
							folder.mkdirs();
						}
						httpUtils.download(AppConfig.Radio_path + param[1],
								folder + "/" + param[1] + ".amr",
								new RequestCallBack<File>() {

									@Override
									public void onSuccess(
											ResponseInfo<File> arg0) {
										// TODO Auto-generated method stub
										File file = arg0.result;
										String path = file.getPath();
										((TextView) chatHolder.contentTextView)
												.setTextColor(Color.WHITE);

										PlayAudio(path, v);
									}

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										// TODO Auto-generated method stub
										System.out.println(arg1);
										ToastUtils.ShowShortToast("网络链接异常");
									}
								});
					} else {
						String path = folder + "/" + param[1] + ".amr";
						PlayAudio(path, v);
					}

				}
			});
		} else {
			// 文件信息
			final FileChatEntity fileeEntity = (FileChatEntity) entity;
			final Message file_msg = fileeEntity.getMsg();
			final TextContent file_content = (TextContent) file_msg
					.getContent();
			final boolean isme = GlobeScopeUtils.getMeId() == fileeEntity
					.getUserid();
			TextView tv_file_name = (TextView) view
					.findViewById(R.id.item_chat_filename);
			TextView tv_file_size = (TextView) view
					.findViewById(R.id.item_chat_filesize);
			final TextView tv_file_status = (TextView) view
					.findViewById(R.id.item_chat_filestate);
			final NumberProgressBar npb = (NumberProgressBar) view
					.findViewById(R.id.item_chat_progress);
			final TextView tv_cancel = (TextView) view
					.findViewById(R.id.item_chat_cancel);
			tv_file_name.setText(fileeEntity.getContent());
			int size = Integer.parseInt(file_content.getStringExtra("size"));
			tv_file_size.setText(size < 1000 ? size + "B"
					: size < 1000000 ? ((double) (size / 100) + "KB")
							: (double) (size / 1000000) + "M");
			npb.setMax(100);
			npb.setProgress((int) (100 * fileeEntity.getCurrent()));
			/**
			 * 他们不同的地方有tv_canncel,tv_status,一个上传一个下载
			 */
			view.findViewById(R.id.t).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					OpenFileUtils.openFile(
							context,
							isme ? file_content.getStringExtra("path")
									: AppConfig.File_PATh
											+ fileeEntity.getContent());
				}
			});
			if (isme) {
				// 假如是我
				// 设置可显示行
				npb.setVisibility(file_content.getStringExtra("status").equals(
						"0") ? View.VISIBLE : View.GONE);
				tv_cancel.setVisibility(file_content.getStringExtra("status")
						.equals("0") ? View.VISIBLE : View.GONE);
				// 设置状态
				tv_file_status.setText(file_content.getStringExtra("status")
						.equals("0") ? "发送中" : file_content.getStringExtra(
						"status").equals("1") ? "发送完毕" : "已取消");
				if (file_content.getStringExtra("status").equals("0")) {
					tv_cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							FileChatUtils.updateStatus(conversation,
									fileeEntity, "2", ChatAdapter.this);
							fileeEntity.setCanceled(true);
						}
					});
				}

			} else {
				// 不是我
				tv_file_status.setOnClickListener(null);
				tv_file_status.setText(file_content.getStringExtra("status")
						.equals("1") ? "接收" : file_content.getStringExtra(
						"status").equals("2") ? "下载中" : file_content
						.getStringExtra("status").equals("3") ? "已下载" : "下载失败");
				tv_cancel.setVisibility(file_content.getStringExtra("status")
						.equals("2") ? View.VISIBLE : View.GONE);
				npb.setVisibility(file_content.getStringExtra("status").equals(
						"2") ? View.VISIBLE : View.GONE);

				if (file_content.getStringExtra("status").equals("1")
						) {
					tv_file_status.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							FileChatUtils.updateStatus(conversation,
									fileeEntity, "2", ChatAdapter.this);

						}
					});

				} else if (file_content.getStringExtra("status").equals("2")) {
					if (fileeEntity.getHandler() == null) {
						npb.setMax(100);
						HttpHandler<File> handler = httpUtils.download(
								AppConfig.qiniu_Url + entity.getContent(),
								AppConfig.File_PATh + entity.getContent(),
								true, new RequestCallBack<File>() {

									@Override
									public void onSuccess(
											ResponseInfo<File> arg0) {
										// TODO Auto-generated
										// method stub
										FileChatUtils.updateStatus(
												conversation, fileeEntity, "3",
												ChatAdapter.this);
									}

									@Override
									public void onLoading(long total,
											long current, boolean isUploading) {
										// TODO Auto-generated
										// method stub
										System.out.println(current+"现在");
										System.out.println(total+"总的");
											System.out.println((double)((double)current/(double)total));
										npb.setProgress((int) (100*(double)((double)current/(double)total)));
									}

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										// TODO Auto-generated
										// method stub
										ToastUtils
												.ShowShortToast("下载失败!可能文件已经存在!");
										FileChatUtils.updateStatus(
												conversation, fileeEntity, "4",
												ChatAdapter.this);
									}
								});
						fileeEntity.setHandler(handler);
					}
					tv_cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							FileChatUtils.updateStatus(conversation,
									fileeEntity, "4", ChatAdapter.this);
							// 给handler取消下载事件
							fileeEntity.getHandler().cancel();
						}
					});
				}
			}
		}
		chatHolder.name_tv.setText(entity.getName());
		FrescoUtils.Display(chatHolder.userImageView, AppConfig.Userpic_path
				+ entity.getUserid(), 80, 80, false);
		if (entity.getType() == 3)
			view.findViewById(R.id.t).setOnLongClickListener(
					new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub

							more_pop.showAsDropDown(v,
									(v.getWidth() - 250) / 2,
									-v.getHeight() - 40);
							View pop_view = more_pop.getContentView();
							final String username = entity.getName();
							((TextView) pop_view.findViewById(R.id.pop_msg_tv1))
									.setText("转文字");
							pop_view.findViewById(R.id.pop_msg_tv1)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											DialogUtils
													.ShowTips(
															"该功能难度过高,音频格式无法匹配,尚未完成,请体谅",
															context);
											more_pop.dismiss();
										}
									});
							pop_view.findViewById(R.id.pop_msg_tv2)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											((ChatActivity) context)
													.SubReply(username);
											more_pop.dismiss();
										}
									});

							return false;
						}
					});
		chatHolder.userImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map<String, String> params = new HashMap<>();
				params.put("id", entity.getUserid() + "");
				OkHttpUtils.post().url(AppConfig.getUser).params(params)
						.build().execute(new MyStringCallBack() {

							@Override
							public void onResponse(String response, int id) {
								// TODO Auto-generated method stub
								if (id != 0) {
									ToastUtils.ShowNetError();
									return;
								}
								try {
									User user = JsonParser.getBeanFromJson(
											new JSONObject(response),
											User.class);

									ActChangeUtils.ChangeActivity(
											(Activity) context,
											SeePersonActivity.class, "user",
											user);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								// TODO Auto-generated method stub
								ToastUtils.ShowNetError();
							}
						});
			}
		});
		if (entity.getType() != 4)
			chatHolder.contentTextView
					.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							if (entity.getType() == 1 || entity.getType() == 2) {
								more_pop.showAsDropDown(v,
										(v.getWidth() - 250) / 2,
										-v.getHeight() - 40);
								View pop_view = more_pop.getContentView();
								if (entity.getType() == 2) {
									((TextView) pop_view
											.findViewById(R.id.pop_msg_tv1))
											.setText("保存");
								} else {
									((TextView) pop_view
											.findViewById(R.id.pop_msg_tv1))
											.setText("复制");
								}
								final String username = entity.getName();
								pop_view.findViewById(R.id.pop_msg_tv1)
										.setOnClickListener(
												new OnClickListener() {

													@SuppressWarnings("deprecation")
													@Override
													public void onClick(View v) {
														// TODO Auto-generated
														// method stub
														if (entity.getType() == 1) {

															ClipboardManager cm = (ClipboardManager) context
																	.getSystemService(Context.CLIPBOARD_SERVICE);
															// 将文本内容放到系统剪贴板里。
															cm.setText(entity
																	.getContent());
															more_pop.dismiss();
														} else {
															// 下载图片到本地
															more_pop.dismiss();
															System.out.println(AppConfig.qiniu_Url
																	+ entity.getContent());
															httpUtils
																	.download(
																			AppConfig.qiniu_Url
																					+ entity.getContent(),
																			AppConfig.Chat_Save
																					+System.currentTimeMillis()+".jpg",
																			true,
																			new RequestCallBack<File>() {
																				public void onFailure(
																						HttpException arg0,
																						String arg1) {
																					ToastUtils
																							.ShowNetError();
																				};

																				public void onSuccess(
																						com.lidroid.xutils.http.ResponseInfo<File> arg0) {
																					ToastUtils
																							.ShowShortToast("保存成功!存放在:"
																									+ AppConfig.Chat_Save);
																				};
																			});
														}
													}
												});
								pop_view.findViewById(R.id.pop_msg_tv2)
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(View v) {
														// TODO Auto-generated
														// method stub
														((ChatActivity) context)
																.SubReply(username);

														more_pop.dismiss();
													}
												});
							}
							return false;
						}
					});
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	public void PlayAudio(String path, final View v) {
		control.startPlaying(path, new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				control.playingFinish();
				v.setSelected(false);
				if ((Boolean) PreferencesTool.getParam("setting", "sound",
						Boolean.class, true))
					pool.play((int) map.get(1), 1, 1, 0, 0, 1);
			}
		}, v);

	}

	SoundPool pool;

	public int getTotal() {
		// TODO Auto-generated method stub
		if (chatList == null) {
			return 0;
		}
		int total = 0;
		for (List<ChatEntity> list : chatList) {
			for (ChatEntity entity : list) {
				total++;
			}
		}
		return total;
	}

	// 控制上传的文件是否需要下载
	public void CancelUpload() {
		for (List<ChatEntity> list : chatList) {
			for (ChatEntity entity : list) {
				if (entity.getType() == 4) {
					FileChatEntity fentity = ((FileChatEntity) entity);
					if (fentity.getMsg().getContent().getStringExtra("status")
							.equals("0")
							&& GlobeScopeUtils.getMeId() == entity.getUserid()) {
						// 取消所有上传
						((FileChatEntity) entity).setCanceled(true);
					} else if (fentity.getMsg().getContent()
							.getStringExtra("status").equals("2")
							&& GlobeScopeUtils.getMeId() != entity.getUserid()) {
						// 取消所有下载
						((FileChatEntity) entity).getHandler().cancel();
					}
				}
			}
		}
	}

	// 让所有没有上传完毕的文件继续上传
	public void ContinueUpload() {
		for (List<ChatEntity> list : chatList) {
			for (ChatEntity entity : list) {
				if (entity.getType() == 4) {
					FileChatEntity fentity = ((FileChatEntity) entity);
					
					if (fentity.getMsg().getContent().getStringExtra("status")
							.equals("0")
							&& GlobeScopeUtils.getMeId() == fentity.getUserid()) {
						QiniuManager.PrepartUpload(fentity.getMsg()
								.getContent().getStringExtra("path"), fentity,
								this);
//						FileChatUtils.updateStatus(conversation, fentity, "1",
//								ChatAdapter.this);
					}
				}
			}
		}
	}

}
