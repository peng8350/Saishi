package com.peng.saishi.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.TextView;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.peng.saishi.R;
import com.peng.saishi.adapter.MainFrag5Adapter;
import com.peng.saishi.entity.StringIconInfo;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.base.LazyFragment;
import com.peng.saishi.interfaces.observer.BadgeListener;
import com.peng.saishi.interfaces.observer.SaveLIstener;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.PickManager;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.FIleUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.PreferencesTool;
import com.peng.saishi.utils.SystemUtils;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.actionsheet.ActionSheet.MenuItemClickListener;
import com.zhy.http.okhttp.OkHttpUtils;

@SuppressLint("InflateParams")
public class Main_frag5 extends LazyFragment implements OnClickListener,
		MenuItemClickListener, OnHanlderResultCallback {
	private ListView listView;
	private View header_view;
	private SimpleDraweeView head;
	private TextView name, intro;
	private SimpleDraweeView top_layout;
	private User user;
	private ViewStub stub;

	public BadgeListener getListener() {
		return listener;
	}

	public void setListener(BadgeListener listener) {
		this.listener = listener;
	}

	private Timer timer;
	private MainFrag5Adapter adapter;
	public static final int UPDATEICON = 908;
	private BadgeListener listener;

	public Main_frag5() {
		super(R.layout.main_frag5);
		// TODO Auto-generated constructor stub
		timer = new Timer();
	}

	View layout;

	@Override
	protected View initViews(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-gene'rated method stub
		layout = inflater.inflate(layoudi, null);

		user = GlobeScopeUtils.Get("ME");
		stub = (ViewStub) layout.findViewById(R.id.frag5_Vs);

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				OkHttpUtils.post().url(AppConfig.getUnRead)
						.addParams("id", GlobeScopeUtils.getMeId() + "")
						.build().execute(new MyStringCallBack() {

							@Override
							public void onResponse(String response, int id) {
								// TODO Auto-generated method stub
								try {
									if (id != 0) {
										ToastUtils.ShowNetError();
										return;
									}
									JSONObject obj = new JSONObject(response);
									int count = obj.getInt("unread");
									if (adapter != null) {
										adapter.setUnread(count);
										adapter.notifyDataSetChanged();
									}

									if (listener != null) {
										listener.onBadge2Visiable(count != 0);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								// TODO Auto-generated method stub

							}
						});
			}
		}, 10, 1000);
		return layout;
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

		listView = (ListView) stub.inflate();
		header_view = LayoutInflater.from(getActivity()).inflate(
				R.layout.header_frag5, null);
		head = (SimpleDraweeView) header_view.findViewById(R.id.frag5_image);
		getActivity().setTheme(R.style.ActionSheetStyleIOS7);
		name = (TextView) header_view.findViewById(R.id.frag5_tv);
		intro = (TextView) header_view.findViewById(R.id.frag5_intro);
		top_layout = (SimpleDraweeView) header_view.findViewById(R.id.frag5_bg);
		head.setOnClickListener(this);
		adapter = new MainFrag5Adapter(getActivity(), getfrag4List(), 0);
		listView.addHeaderView(header_view);
		listView.setAdapter(adapter);
		listView.setSelection(listView.getCount() - 1);
		name.setText(user.getUsername());
		intro.setText(user.getIntro());
		FrescoUtils.Display(head,
				AppConfig.Userpic_path + GlobeScopeUtils.getMeId(), 150, 150,
				true);
		resetTopBg(AppConfig.Userpic_path + GlobeScopeUtils.getMeId());
	}

	private List<StringIconInfo> getfrag4List() {
		// TODO Auto-generated method stub
		List<StringIconInfo> list = new ArrayList<StringIconInfo>();
		list.add(new StringIconInfo("个人资料", R.drawable.frag4_1));
		list.add(new StringIconInfo("修改密码", R.drawable.frag4_2));
		list.add(new StringIconInfo("我的收藏", R.drawable.frag4_3));
		list.add(new StringIconInfo("我的提问", R.drawable.frag4_6));
		list.add(new StringIconInfo("我的文件", R.drawable.frag4_7));
		list.add(new StringIconInfo("队伍通知", R.drawable.frag4_4));
		list.add(new StringIconInfo("设置", R.drawable.frag4_5));

		return list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.frag5_image:
			PickManager.ShowSheetPick(getActivity(), this);
			break;

		default:
			break;
		}

	}

	public void resetTopBg(String path) {
		if ((boolean) PreferencesTool.getParam("setting", "change",
				Boolean.class, false)) {
			ResizeOptions options = new ResizeOptions(
					SystemUtils.getAppWidth(), 250);
			ImageRequest request = ImageRequestBuilder
					.newBuilderWithSource(Uri.parse(path))
					.setPostprocessor(
							new com.peng.saishi.utils.BlurPostprocessor(
									getActivity(), 60))
					.setResizeOptions(options).build();

			PipelineDraweeController controller = (PipelineDraweeController) Fresco
					.newDraweeControllerBuilder().setImageRequest(request)
					.setOldController(top_layout.getController()).build();
			top_layout.setController(controller);
		} else {

			FrescoUtils.Display(top_layout, "res://com.peng.saishi/"
					+ R.drawable.blur_bg, SystemUtils.getAppWidth(), 250, true);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == UPDATEICON && resultCode == Activity.RESULT_OK) {
			if ((boolean) PreferencesTool.getParam("setting", "change",
					Boolean.class, false)) {

				resetTopBg(AppConfig.Userpic_path + GlobeScopeUtils.getMeId());
			} else {
				resetTopBg("res://com.peng.saishi/");
			}
		}

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
			timer = null;

		}
		super.onDestroy();
	}

	@Override
	public void onItemClick(int itemPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setDefaultFragmentTitle(String title) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
		// TODO Auto-geerated method stub

		final String path = resultList.get(0).getPhotoPath();

		FrescoUtils.Display(head, "file://" + path, 150, 150, true);
		resetTopBg("file://" + path);
		final Dialog wait_dia = DialogUtils.createLoadingDialog(getActivity(),
				"上传中...");
		wait_dia.show();
		FIleUtils.saveBitmap(BitmapFactory.decodeFile(path),AppConfig.pic_temp,new SaveLIstener() {
			
			@Override
			public void OnSaveSuccess() {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				FIleUtils.UploadPic(AppConfig.pic_temp,
						GlobeScopeUtils.getMeId(), new SaveLIstener() {

							@Override
							public void OnSaveSuccess() {
								// TODO Auto-generated method stub
								wait_dia.dismiss();
								OkHttpUtils
										.post()
										.url(AppConfig.ClearUserCahe)
										.addParams(
												"id",
												GlobeScopeUtils
														.getMeId() + "")
										.build().execute(null);

							}
							

							@Override
							public void OnSaveFailed() {
								// TODO Auto-generated method stub
								wait_dia.dismiss();
							}

			});
			
			}
			
			@Override
			public void OnSaveFailed() {
				// TODO Auto-generated method stub
				
			}
			
		
			
		});

	}

	@Override
	public void onHanlderFailure(int requestCode, String errorMsg) {
		// TODO Auto-generated method stub

	}

}
