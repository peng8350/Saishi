package com.peng.saishi.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.widget.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;

public class PersonDataActivity extends BaseBackActivity implements TextWatcher {
	private RelativeLayout[] layouts;
	private boolean save;
	private Button finish;
	private User user;

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
		if (save) {
			
		}
		finish.setVisibility(save ? View.GONE : View.VISIBLE);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		user = GlobeScopeUtils.Get("ME");
		setContentView(R.layout.activity_persondata);
		finish = (Button) findViewById(R.id.persondata_button1);
		finish.setOnClickListener(this);
		super.init();
		String[] datas = { user.getUsername(), user.getSchool(), user.getMajor(), user.getAge() + "岁", user.getSex(),
				user.getXueli(), user.getPlace(), user.getIntro().substring(0,user.getIntro().length()>7?7:user.getIntro().length()) + "...",
				user.getHobby().substring(0, user.getHobby().length()>7?7:user.getHobby().length()) + "...", user.getPhone() };
		layouts = new RelativeLayout[10];
		for (int i = 1; i <= 10; i++) {
			try {
				int id = Integer.parseInt(R.id.class.getDeclaredField("persondata_layout" + i).get(null).toString());
				layouts[i - 1] = (RelativeLayout) findViewById(id);
				layouts[i - 1].setOnClickListener(this);
				((TextView) layouts[i - 1].getChildAt(1)).setText(datas[i - 1]);
				((TextView) layouts[i - 1].getChildAt(1)).addTextChangedListener(this);
				layouts[i - 1].setClickable(true);
			} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		((TextView) layouts[7].getChildAt(1)).setTag(user.getIntro());
		((TextView) layouts[8].getChildAt(1)).setTag(user.getHobby());
		setSave(true);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 100 && arg1 == RESULT_OK) {
			int type = arg2.getIntExtra("type", 0);
			if (type == 1) {
				if (!arg2.getStringExtra("result").equals(((TextView) layouts[3].getChildAt(1)).getText().toString())) {

					((TextView) layouts[3].getChildAt(1)).setText(arg2.getStringExtra("result"));
				}
			} else if (type == 2) {
				if (!arg2.getStringExtra("result").equals(((TextView) layouts[4].getChildAt(1)).getText().toString())) {
					((TextView) layouts[4].getChildAt(1)).setText(arg2.getStringExtra("result"));
				}
			} else if (type == 3) {
				if (!arg2.getStringExtra("result").equals(((TextView) layouts[5].getChildAt(1)).getText().toString())) {
					((TextView) layouts[5].getChildAt(1)).setText(arg2.getStringExtra("result"));
				}
			} else if (type == 4) {
				if (!arg2.getStringExtra("result").equals(((TextView) layouts[6].getChildAt(1)).getText().toString())) {
					((TextView) layouts[6].getChildAt(1)).setText(arg2.getStringExtra("result"));
				}
			} else if (type == 5) {

				if (!arg2.getStringExtra("result").equals(((TextView) layouts[7].getChildAt(1)).getText().toString())) {
					((TextView) layouts[7].getChildAt(1))
							.setText(arg2.getStringExtra("result").substring(0, 7) + "...");
					((TextView) layouts[7].getChildAt(1)).setTag(arg2.getStringExtra("result"));
				}
			} else {
				if (!arg2.getStringExtra("result").equals(((TextView) layouts[8].getChildAt(1)).getText().toString())) {
					((TextView) layouts[8].getChildAt(1))
							.setText(arg2.getStringExtra("result").substring(0, 7) + "...");
					((TextView) layouts[8].getChildAt(1)).setTag(arg2.getStringExtra("result"));
				}
			}
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	// 对出对话框,要求设置
	public void ShowAlert(final TextView update_tv) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		final EditText eText = new EditText(this);
		eText.setText(update_tv.getText().toString());
		eText.setHint("你要修改的");
		eText.setTextSize(14);
		eText.setFilters(new InputFilter[]{new InputFilter
				.LengthFilter(12)});
		eText.setBackgroundResource(R.drawable.update_et_bg);
		if (update_tv.equals(layouts[9].getChildAt(1))) {
			eText.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		builder.setTitle("修改");
		eText.requestFocus();
		builder.setContentView(eText);
		builder.setPositiveButton("修改", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				update_tv.setText(eText.getText().toString());
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (save) {
			finish();
		} else
			ShowExitSure();
	}

	// 显示确定退出的按钮
	private void ShowExitSure() {
		// TODO Auto-generated method stub
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setTitle("放弃保存");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();

			}
		});
		builder.setMessage("等等,你刚所修改的信息尚未保存~~~你要确定退出不保存吗?");
		builder.create().show();
	}

	/**
	 * 把数据上传到服务器
	 */
	public void PushData() {
		Map<String, String> params = new HashMap<>();
		params.put("name", ((TextView) layouts[0].getChildAt(1)).getText().toString());
		params.put("school", ((TextView) layouts[1].getChildAt(1)).getText().toString());
		params.put("major", ((TextView) layouts[2].getChildAt(1)).getText().toString());
		params.put("phone", ((TextView) layouts[9].getChildAt(1)).getText().toString());
		params.put("age", ((TextView) layouts[3].getChildAt(1)).getText().toString().substring(0,
				((TextView) layouts[3].getChildAt(1)).getText().toString().indexOf("岁")));
		params.put("sex", ((TextView) layouts[4].getChildAt(1)).getText().toString());
		params.put("xueli", ((TextView) layouts[5].getChildAt(1)).getText().toString());
		params.put("place", ((TextView) layouts[6].getChildAt(1)).getText().toString());
		params.put("intro", ((TextView) layouts[7].getChildAt(1)).getTag().toString());
		params.put("hobby", ((TextView) layouts[8].getChildAt(1)).getTag().toString());
		params.put("user",user.getUser());
		OkHttpUtils.post().url(AppConfig.Update).params(params).build().execute(null);
		user.setUsername(((TextView) layouts[0].getChildAt(1)).getText().toString());
		user.setSchool(((TextView) layouts[1].getChildAt(1)).getText().toString());
		user.setMajor(((TextView) layouts[2].getChildAt(1)).getText().toString());
		user.setPhone(((TextView) layouts[9].getChildAt(1)).getText().toString());
		user.setAge(Integer.parseInt(((TextView) layouts[3].getChildAt(1)).getText().toString().substring(0,
				((TextView) layouts[3].getChildAt(1)).getText().toString().indexOf("岁"))));
		user.setSex(((TextView) layouts[4].getChildAt(1)).getText().toString());
		user.setXueli(((TextView) layouts[5].getChildAt(1)).getText().toString());
		user.setPlace(((TextView) layouts[6].getChildAt(1)).getText().toString());
		user.setIntro(((TextView) layouts[7].getChildAt(1)).getTag().toString());
		user.setHobby( ((TextView) layouts[8].getChildAt(1)).getTag().toString());
		user.save();
		setResult(RESULT_OK);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_tv:

			if (save) {
				finish();
			} else
				ShowExitSure();

			break;
		case R.id.persondata_button1:
			// 保存
			PushData();
			finish();
			setSave(true);
			break;
		case R.id.persondata_layout1:
			ShowAlert((TextView) layouts[0].getChildAt(1));
			break;
		case R.id.persondata_layout2:
			ShowAlert((TextView) layouts[1].getChildAt(1));
			break;
		case R.id.persondata_layout3:
			ShowAlert((TextView) layouts[2].getChildAt(1));
			break;
		case R.id.persondata_layout4:
			ActChangeUtils.ChangeActivityforResult(this, UpdatePersonAct1.class,
					new String[] { "data1", "data2", "type" },
					new String[] { ((TextView) layouts[3].getChildAt(0)).getText().toString(),
							((TextView) layouts[3].getChildAt(1)).getText().toString(), "1" },
					100);
			break;
		case R.id.persondata_layout5:
			ActChangeUtils.ChangeActivityforResult(this, UpdatePersonAct1.class,
					new String[] { "data1", "data2", "type" },
					new String[] { ((TextView) layouts[4].getChildAt(0)).getText().toString(),
							((TextView) layouts[4].getChildAt(1)).getText().toString(), "2" },
					100);
			break;
		case R.id.persondata_layout6:
			ActChangeUtils.ChangeActivityforResult(this, UpdatePersonAct1.class,
					new String[] { "data1", "data2", "type" },
					new String[] { ((TextView) layouts[5].getChildAt(0)).getText().toString(),
							((TextView) layouts[5].getChildAt(1)).getText().toString(), "3" },
					100);
			break;
		case R.id.persondata_layout7:
			ActChangeUtils.ChangeActivityforResult(this, UpdatePersonAct2.class, new String[] { "data1", "data2" },
					new String[] { ((TextView) layouts[6].getChildAt(0)).getText().toString(),
							((TextView) layouts[6].getChildAt(1)).getText().toString() },
					100);
			break;
		case R.id.persondata_layout8:
			ActChangeUtils.ChangeActivityforResult(this, UpdatePersonAct3.class,
					new String[] { "data1", "data2", "type" },
					new String[] { ((TextView) layouts[7].getChildAt(0)).getText().toString(),
							((TextView) layouts[7].getChildAt(1)).getTag().toString(), "5" },
					100);
			break;
		case R.id.persondata_layout9:
			ActChangeUtils.ChangeActivityforResult(this, UpdatePersonAct3.class,
					new String[] { "data1", "data2", "type" },
					new String[] { ((TextView) layouts[8].getChildAt(0)).getText().toString(),
							((TextView) layouts[8].getChildAt(1)).getTag().toString(), "6" },
					100);
			break;
		case R.id.persondata_layout10:
			// 联系方式
			ShowAlert((TextView) layouts[9].getChildAt(1));
			break;
		default:
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		setSave(false);
	}
}
