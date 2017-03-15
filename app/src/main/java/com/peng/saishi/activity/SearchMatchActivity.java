package com.peng.saishi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseActivity;
import com.peng.saishi.adapter.MatchListAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.NoDataManager;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.TagCloudView;
import com.peng.saishi.widget.TagCloudView.OnTagClickListener;
import com.zhy.http.okhttp.OkHttpUtils;

public class SearchMatchActivity extends BaseActivity implements OnKeyListener, OnTagClickListener,TextWatcher{

	private TagCloudView tagcloud;
	private EditText search_et;
	private List<String> searchdata;
	private List<MatchInfo> match_datas;
	private ListView listview;
	private View nodata_layout;
	private boolean showlist;// 判断列表的显示
	private InputMethodManager mInputMethodManager ;

	public boolean isShowlist() {
		return showlist;
	}

	public void setShowlist(boolean showlist) {
		this.showlist = showlist;
		if (showlist) {
			listview.setVisibility(View.VISIBLE);
			tagcloud.setVisibility(View.GONE);
		} else {
			listview.setVisibility(View.GONE);
			tagcloud.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_searchmatch);
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		nodata_layout = NoDataManager.initNodata("搜索不到任何内容!!!", this);
		search_et = (EditText) findViewById(R.id.search_editText1);
		tagcloud = (TagCloudView) findViewById(R.id.search_tagcloudview1);
		listview = (ListView) findViewById(R.id.search_listView1);
		match_datas = GlobeScopeUtils.Get("search");
		initgetSearchData();
		setShowlist(false);
		findViewById(R.id.search_textView1).setOnClickListener(this);
		search_et.setOnKeyListener(this);
		tagcloud.setOnTagClickListener(this);
		tagcloud.setTags(searchdata);
		search_et.addTextChangedListener(this);
	}

	// 搜索,每搜索一次向服务器请求,要求更改次数,
	public void Search(String search_str) {

		// 修改javabean 数据源
		if (!search_str.equals("")) {
			setShowlist(true);
			Map<String, String> params=new HashMap<>();
			params.put("content",search_str);
			OkHttpUtils.post().url(AppConfig.UserSearch).params(params).build().execute(new MyStringCallBack() {
				
				@Override
				public void onResponse(String response, int id) {
					// TODO Auto-generated method stub
					if (id!=0) {
						ToastUtils.ShowNetError();
						return ;
					}
				}
				
				@Override
				public void onError(Call call, Exception e, int id) {
					// TODO Auto-generated method stub
					ToastUtils.ShowNetError();
				}
			});
			List<MatchInfo> datas = new ArrayList<>();
			// 名字,地方,主办,分类,分类
			for (MatchInfo info : match_datas) {
				if (isConverient(info.getName(), search_str) || isConverient(info.getFenlei(), search_str)
						|| isConverient(info.getPlace(), search_str) || isConverient(info.getZhuban(), search_str)) {
					datas.add(info);
				}
			}
			
			// 修改适配器
			if (listview.getAdapter() == null) {
				listview.setAdapter(new MatchListAdapter(this, datas));
			} else {
				MatchListAdapter adapter = (MatchListAdapter) listview.getAdapter();
				adapter.setList(datas);
				adapter.notifyDataSetChanged();
			}
			NoDataManager.ChangeState(nodata_layout, datas);
		}
		mInputMethodManager.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
	}

	/*
	 * 
	 * 判断这个字符串是否与搜索的字符串符合搜索的判断
	 */
	public boolean isConverient(String str, String target) {
		boolean flag = false;
		if ( str.startsWith(target) || str.contains(target)) {
			flag = true;
		}
		return flag;
	}

	// 首先第一步初始化搜索的数据,捕获到的内容按照次数高低排序
	// 限制条目为30条,最大值
	public void initgetSearchData() {
		// TODO Auto-generated method stub
		OkHttpUtils.post().url(AppConfig.getSearch).build().execute(new MyStringCallBack() {
			
			@Override
			public void onResponse(String response, int id) {
				// TODO Auto-generated method stub
				if (id!=0) {
					ToastUtils.ShowNetError();
					return ;
				}
				// TODO Auto-generated method stub
				try {
					JSONArray array = new JSONArray(response);
					searchdata = JsonParser.getStringsFromJson(array);
					tagcloud.setTags(searchdata);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_textView1:
			// 搜索按钮
			Search(search_et.getText().toString());
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onTagClick(int position) {
		// TODO Auto-generated method stub
		search_et.setText(searchdata.get(position));
		Search(searchdata.get(position));
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			Search(search_et.getText().toString());
			return true;
		}
		return false;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (s.toString().equals("")) {
			setShowlist(false);
		}
	}

}
