package com.peng.saishi.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.peng.saishi.R;
import com.peng.saishi.adapter.MainFrag3Adapter;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.base.LazyFragment;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.pinnedheaderlistview.PinnedHeaderListView;
import com.zhy.http.okhttp.OkHttpUtils;

public class Main_frag3 extends LazyFragment implements OnGroupClickListener{
	static List<TeamInfo> list1,list2;
	private static PinnedHeaderListView listView;
	public Main_frag3() {
		super(R.layout.main_frag3);
		// TODO Auto-generated constructor stub
	}
	public static Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==1) {
				Update();
			}
		};
	};
	View layout;
	protected View initViews(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
		layout = inflater.inflate(layoudi, null);
		listView =(PinnedHeaderListView) layout;
		return layout;
	};
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub

		list1 = GlobeScopeUtils.getUserTeam1();
		list2=GlobeScopeUtils.getUserTeam2();
		listView.setAdapter(new MainFrag3Adapter(list1,getActivity(), list2));
		MainFrag3Adapter adapter = (MainFrag3Adapter) listView.getAdapter();
		adapter.setGroup1(list1);
		adapter.setGroup2(list2);
		adapter.notifyDataSetChanged();
	}
	@Override
	protected void setDefaultFragmentTitle(String title) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Update();
	}
	
	public static void Update(){
		Map<String, String> params = new HashMap<>();
		params.put("id",""+((User)GlobeScopeUtils.Get("ME")).getId());
		if(listView!=null)
		OkHttpUtils.post().url(AppConfig.getUserTeam).params(params).build().execute(new MyStringCallBack() {
			
			@Override
			public void onResponse(String response, int id) {
				// TODO Auto-generated method stub
				//设置适配器
				if (id!=0) {
					ToastUtils.ShowNetError();
					return ;
				}
				JSONObject obj = null;
				try {
					obj = new JSONObject(response);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					list1=JsonParser.getListFromJson(obj.getJSONArray("list1"), TeamInfo.class);
					list2 = JsonParser.getListFromJson(obj.getJSONArray("list2"), TeamInfo.class);
					User me =GlobeScopeUtils.Get("ME");
					me.setTeam1(list1);
					me.setTeam2(list2);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MainFrag3Adapter adapter = (MainFrag3Adapter) listView.getAdapter();
				System.out.println(adapter);
				if (adapter!=null) {
					System.out.println("set");
					
					adapter.setGroup1(list1);
					adapter.setGroup2(list2);
					adapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onError(Call call, Exception e, int id) {
				// TODO Auto-generated method stub
				ToastUtils.ShowNetError();
			}
		});
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		// TODO Auto-generated method stub
		return true;
	}
	

}
