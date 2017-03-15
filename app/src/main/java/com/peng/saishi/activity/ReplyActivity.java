package com.peng.saishi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.ReplyListAdapter;
import com.peng.saishi.entity.Pingjia;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase.OnRefreshListener2;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;

public class ReplyActivity extends BaseBackActivity implements OnRefreshListener2<ListView> {

	private PullToRefreshListView listview;
	private List<Pingjia> list;
	int match_id;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_reply);
		super.init();
		match_id = getIntent().getIntExtra("match_id", 0);
		list = getIntent().getParcelableArrayListExtra("replys");
		listview = (PullToRefreshListView) findViewById(R.id.Reply_listView1);
		listview.setAdapter(new ReplyListAdapter(this, list));
		findViewById(R.id.Reply_Btn).setOnClickListener(this);
		listview.getLoadingLayoutProxy().setRefreshingLabel("正在刷新评论...");
		listview.getLoadingLayoutProxy().setReleaseLabel("松开手指刷新...");
		listview.getLoadingLayoutProxy().setPullLabel("下拉刷新..");
		listview.setOnRefreshListener(this);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0==101&&arg1==RESULT_OK) {
			listview.setRefreshing(true);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putParcelableArrayListExtra("replys", (ArrayList<? extends Parcelable>) list);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Reply_Btn:
			// 打开发表页面
			System.out.println(match_id);
			ActChangeUtils.ChangeActivityForResult(this, PublishReplyActivity.class, "match_id", match_id,101);
			break;
		case R.id.back_tv:
			Intent intent = new Intent();
			intent.putParcelableArrayListExtra("replys", (ArrayList<? extends Parcelable>) list);
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<>();
		params.put("id", match_id + "");
		OkHttpUtils.post().url(AppConfig.getMatchPingjia).params(params).build().execute(new MyStringCallBack() {
			
			@Override
			public void onResponse(String response, int id) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				if (id!=0) {
					ToastUtils.ShowNetError();
					return ;
				}
				try {
					JSONArray array = new JSONArray(response);
					list = JsonParser.getListFromJson(array, Pingjia.class);
					ReplyListAdapter adapter = ((ReplyListAdapter) ((HeaderViewListAdapter) listview.getRefreshableView().getAdapter())
							.getWrappedAdapter());
					adapter.setList(list);
					adapter.notifyDataSetChanged();
					listview.onRefreshComplete();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
			@Override
			public void onError(Call call, Exception e, int id) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				ToastUtils.ShowNetError();
			
			}
		});
		
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

}
