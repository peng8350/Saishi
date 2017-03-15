package com.peng.saishi.fragment;

import com.peng.saishi.R;
import com.peng.saishi.adapter.PicPickAdapter;
import com.peng.saishi.fragment.base.BaseFragment;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.CustomDialog;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;

public class AdviceFrag extends BaseFragment implements OnClickListener {

	private EditText input_et;
	private GridView gridView;
	private PicPickAdapter adapter;

	public PicPickAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(PicPickAdapter adapter) {
		this.adapter = adapter;
	}

	public AdviceFrag() {
		super(R.layout.frag_advice);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		input_et = (EditText) layout.findViewById(R.id.frag_advice_editText1);
		gridView = (GridView) layout.findViewById(R.id.frag_advice_gridView1);
		adapter = new PicPickAdapter(getActivity());
		layout.findViewById(R.id.frag_advice_btn).setOnClickListener(this);
		
		gridView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.frag_advice_btn:
			// 提交建议
			if (input_et.getText().toString().equals("")) {
				ToastUtils.ShowShortToast("你输入的内容为空!不能为空!");
				return;
			}
			if (input_et.getText().toString().length() < 10) {
				ToastUtils.ShowShortToast("输入的内容少于10个字");
				return;
			}
			CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
			builder.setTitle("提示");
			builder.setMessage("你已经成功的把信息上传到服务端");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					getActivity().finish();
				}
			});
			builder.create().show();
			break;

		default:
			break;
		}

	}

}
