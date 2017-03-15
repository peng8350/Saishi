package com.peng.saishi.manager;

import java.util.List;

import com.peng.saishi.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class NoDataManager {

	// 找到id,设置对应的数据
	public static View initNodata(String text, Context context) {
		Activity activity = (Activity) context;
		View layout = activity.findViewById(R.id.nodata_layout);
		TextView tView = (TextView) activity
				.findViewById(R.id.nodata_textView1);
		TextTypeManger.setTextViewType(tView);
		;
		tView.setText(text);
		return layout;
	}

	// 判断没有数据view的显示与隐藏
	public static <T> void ChangeState(View layout, List<T> list) {
		if (list == null || list.size() == 0) {
			layout.setVisibility(View.VISIBLE);
		} else {
			layout.setVisibility(View.GONE);
		}
	}

}
