package com.peng.saishi.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

import com.peng.saishi.R;
import com.peng.saishi.adapter.MainFrag4Adapter;
import com.peng.saishi.entity.MessageInfo;
import com.peng.saishi.fragment.base.LazyFragment;
import com.peng.saishi.interfaces.observer.BadgeListener;
import com.peng.saishi.interfaces.observer.NetworkListener;
import com.peng.saishi.manager.TextTypeManger;
import com.peng.saishi.receiver.NetworkReceiver;
import com.peng.saishi.utils.NetUtils;
import com.peng.saishi.utils.PreferencesTool;

@SuppressLint({ "HandlerLeak", "InflateParams" })
public class Main_frag4 extends LazyFragment implements NetworkListener, OnClickListener {
	public Main_frag4() {
		super(R.layout.main_frag4);
		// TODO Auto-generated constructor stub
	}

	private NetworkReceiver receiver;
	private View header;
	private Map<String, Conversation> convers;
	private TextView nodata_tv;
	MainFrag4Adapter adapter;
	public Map<String, Conversation> getConvers() {
		return convers;
	}

	public void setConvers(Map<String, Conversation> convers) {
		this.convers = convers;
	}

	public List<MessageInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<MessageInfo> datas) {
		this.datas = datas;
	}

	private ProgressBar progressBar;
	private List<MessageInfo> datas;
	private ListView listView;
	private View nodata_layout;

	public BadgeListener getBad_lis() {
		return bad_lis;
	}

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				progressBar.setVisibility(View.GONE);
				int total = msg.arg1;
				if (bad_lis != null) {

					bad_lis.onBadgeUpdate(total);
				}
				adapter = new MainFrag4Adapter(getActivity(), datas);
				listView.setAdapter(adapter);
				if (datas == null || datas.size() == 0) {
					nodata_layout.setVisibility(View.VISIBLE);
				}
				else{
					nodata_layout.setVisibility(View.GONE);
				}
			} else if (msg.what == 2) {
				bad_lis.onBadgeUpdate(msg.arg1);
				if (listView != null) {
					datas = (List<MessageInfo>) msg.obj;
					if (adapter != null) {

						adapter.setList(datas);
						adapter
								.notifyDataSetChanged();
						nodata_layout
								.setVisibility(datas == null
										|| datas.size() == 0 ? View.VISIBLE
										: View.GONE);
					}
				}
			}
		};

	};

	public void setBad_lis(BadgeListener bad_lis) {
		this.bad_lis = bad_lis;
	}

	private BadgeListener bad_lis;

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	public static final int UPDATE = 333;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		UpdateMSg2();
	}

	View layout;

	@Override
	protected View initViews(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(layoudi, null);
		listView = (ListView) layout.findViewById(R.id.frag4_lv);
		progressBar = (ProgressBar) layout.findViewById(R.id.frag4_pb);
		nodata_tv = (TextView) layout.findViewById(R.id.nodata_textView1);
		nodata_layout = layout.findViewById(R.id.nodata_layout);
		convers = new HashMap<String, Conversation>();
		return layout;
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		if (receiver!=null) {
			getActivity().unregisterReceiver(receiver);
		}
		super.onDestroyView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (!(boolean) PreferencesTool.getParam("User", "Login", Boolean.class,false)) {
			
			setFirstLoad(true);
			return ;
		}

		TextTypeManger.setTextViewType(nodata_tv);
		nodata_tv.setText("没有任何消息!!");
		// 使用ImageView显示动画
		// obj.put("teamid", team.getId());
		// obj.put("userid", GlobeScopeUtils.getMeId());
		// obj.put("content", content);
		datas = new ArrayList<>();
		header = LayoutInflater.from(getActivity()).inflate(R.layout.header_frag4, null);
		header.findViewById(R.id.header_frag4).setOnClickListener(this);
		header.findViewById(R.id.header_frag4).setVisibility(NetUtils.isNetworkAvailable(getActivity())?View.GONE:View.VISIBLE);
		listView.addHeaderView(header);
		initReceiver();
		new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				// 请写在线程运行的代码
				final List<Conversation> conversations = JMessageClient
						.getConversationList();
				int total = 0;
				if (conversations != null) {

					for (Conversation conver : conversations) {
						if (conver.getType().equals(ConversationType.single)) {
							continue;
						}
						int count = conver.getAllMessage().size() - 1;
						while (count >= 0
								&& !conver.getAllMessage().get(count)
										.getContentType()
										.equals(ContentType.text)) {
							count--;
						}
						if (count == -1) {
							continue;
						}
						Message msg = conver.getAllMessage().get(count);
						int teamid = 0;
						int userid = 0;
						int type = 0;
						String content = null;
						String teamname = null;
						convers.put(conver.getTargetId(), conver);
						try {

							JSONObject obj = new JSONObject(((TextContent) msg
									.getContent()).getText());
							teamid = obj.getInt("teamid");
							userid = obj.getInt("userid");
							content = obj.getString("content");
							teamname = obj.getString("teamname");
							type = obj.getInt("type");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						MessageInfo info = new MessageInfo(teamid, userid,
								teamname, msg.getCreateTime(), content, conver
										.getUnReadMsgCnt(), type);
						info.setConver(conver);
						datas.add(info);
						Collections.sort(datas, new Comparator<MessageInfo>() {

							@Override
							public int compare(MessageInfo lhs, MessageInfo rhs) {
								// TODO Auto-generated method stub
								if (lhs.getTime() < rhs.getTime()) {
									return 1;
								}
								return -1;
							}
						});
						total += conver.getUnReadMsgCnt();
					}
				}
				android.os.Message msg = new android.os.Message();
				msg.what = 1;
				msg.arg1 = total;
				handler.sendMessage(msg);

			}
		}).start();
	}

	// 打开网络连接设置
	public void openWirelessSet() {
		startActivity(new Intent(
				android.provider.Settings.ACTION_SETTINGS));
	}

	// 初始化广播接受者
	private void initReceiver() {
		// TODO Auto-generated method stub
		receiver = new NetworkReceiver();
		receiver.setListener(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.wifi.STATE_CHANGE");
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		getActivity().registerReceiver(receiver, filter);

	}

	// 第二个更新方法
	public void UpdateMSg2() {
		// if (progressBar == null) {
		// return;
		// }
		final List<MessageInfo> cover_list = new ArrayList<>();
		new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				// 请写在线程运行的代码
				final List<Conversation> conversations = JMessageClient
						.getConversationList();
				int total = 0;
				if (conversations != null) {
					for (Conversation conver : conversations) {
						if (conver.getType().equals(ConversationType.single)) {
							continue;
						}
						int count = conver.getAllMessage().size() - 1;
						while (count >= 0
								&& !conver.getAllMessage().get(count)
										.getContentType()
										.equals(ContentType.text)) {
							count--;
						}
						if (count == -1) {
							continue;
						}
						Message msg = conver.getAllMessage().get(count);
						int teamid = 0;
						int userid = 0;
						int type = 0;
						String content = null;
						String teamname = null;
						convers.put(conver.getTargetId(), conver);
						try {

							JSONObject obj = new JSONObject(((TextContent) msg
									.getContent()).getText());
							teamid = obj.getInt("teamid");
							userid = obj.getInt("userid");
							content = obj.getString("content");
							teamname = obj.getString("teamname");
							type = obj.getInt("type");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						MessageInfo info = new MessageInfo(teamid, userid,
								teamname, msg.getCreateTime(), content, conver
										.getUnReadMsgCnt(), type);
						info.setConver(conver);

						cover_list.add(info);
						Collections.sort(cover_list,
								new Comparator<MessageInfo>() {

									@Override
									public int compare(MessageInfo lhs,
											MessageInfo rhs) {
										// TODO Auto-generated method stub
										if (lhs.getTime() < rhs.getTime()) {
											return 1;
										}
										return -1;
									}
								});
						total += conver.getUnReadMsgCnt();
					}
				}
				android.os.Message msg = new android.os.Message();
				msg.what = 2;
				msg.obj = cover_list;
				msg.arg1 = total;
				handler.sendMessage(msg);
			}
		}).start();

	}

	int totle = 0;


	// 检查listview是不是含有这个对话
	// 假如没有,就直接添加到最前段的节点
	// 假如存在这个数据,就直接更新这个对话
	public MessageInfo haveData(int teamid) {
		if (datas == null) {
			return null;
		}
		for (MessageInfo info : datas) {
			System.out.println(info.getTeamid() + " " + teamid);
			if (info.getTeamid() == teamid) {
				return info;
			}
		}
		return null;
	}

	// 新的信息
	public void UpdateMsg(MessageEvent event) {
		// 请写在线程运行的代码
		// if (progressBar == null || progressBar.isShown()) {
		// return;
		// }

		Message msg = event.getMessage();
		int teamid = 0;
		int userid = 0;
		String content = null;
		String teamname = null;
		int type = 0;
		int groupid = 0;
		try {
			JSONObject obj = new JSONObject(
					((TextContent) msg.getContent()).getText());
			groupid = obj.getInt("groupid");
			teamid = obj.getInt("teamid");
			userid = obj.getInt("userid");
			type = obj.getInt("type");
			content = obj.getString("content");
			teamname = obj.getString("teamname");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MessageInfo bean = haveData(teamid);
		if (bean == null) {
			// 存在这个对话了
			MessageInfo info = new MessageInfo(teamid, userid, teamname,
					msg.getCreateTime(), content, 1, type);
			info.setConver(JMessageClient.getGroupConversation(groupid));
			if (datas != null)
				datas.add(0, info);
		} else {
			// 在原来的基础的基础上修改
			bean.setContent(content);
			bean.setTime(msg.getCreateTime());
			bean.setUserid(userid);
			bean.setType(type);
			bean.setWeidu(bean.getWeidu() + 1);
		}
		if (datas != null)
			Collections.sort(datas, new Comparator<MessageInfo>() {

				@Override
				public int compare(MessageInfo lhs, MessageInfo rhs) {
					// TODO Auto-generated method stub
					if (lhs.getTime() < rhs.getTime()) {
						return 1;
					}
					return -1;
				}
			});
		bad_lis.onBadgesMinus(-1);
		if (listView != null) {

			adapter.notifyDataSetChanged();
			nodata_layout.setVisibility(View.GONE);
		}
	}

	@Override
	protected void setDefaultFragmentTitle(String title) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisConnect() {
		// TODO Auto-generated method stub
		header.findViewById(R.id.header_frag4).setVisibility(View.VISIBLE);
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		header.findViewById(R.id.header_frag4).setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.header_frag4) {
			openWirelessSet();
		}
	}

}
