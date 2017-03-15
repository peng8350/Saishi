package com.peng.saishi.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.widget.Adapter;
import android.widget.BaseAdapter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.MyFileAdapter;
import com.peng.saishi.entity.MyFIleInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.OpenFileUtils;
import com.peng.saishi.utils.SystemUtils;
import com.peng.saishi.utils.TimeUtils;
import com.peng.saishi.widget.swipelistview.BaseSwipeListViewListener;
import com.peng.saishi.widget.swipelistview.SwipeListView;

public class MyFIleActivity extends BaseBackActivity {
	private SwipeListView listview;
	private List<MyFIleInfo> files;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_myfile);
		super.init();
		files = getFiles();
		listview = (SwipeListView) findViewById(R.id.Myfile_listview);
		Collections.sort(files, new Comparator<MyFIleInfo>() {

			@Override
			public int compare(MyFIleInfo lhs, MyFIleInfo rhs) {
				// TODO Auto-generated method stub
				if (TimeUtils.getDateFromChatTime(lhs.getCreate_time()).before(
						TimeUtils.getDateFromChatTime(rhs.getCreate_time()))) {
					return 1;
				}

				return -1;
			}
		});
		listview.setOffsetLeft(SystemUtils.getAppWidth() - 280);
		listview.setOffsetRight(280);
		listview.setSwipeListViewListener(new BaseSwipeListViewListener() {

			@Override
			public void onClosed(int position, boolean fromRight) {
				// TODO Auto-generated method stub
				super.onClosed(position, fromRight);
				if (((MyFileAdapter)listview.getAdapter()).isNeedupdate()) {
					
					((BaseAdapter)listview.getAdapter()).notifyDataSetChanged();
					((MyFileAdapter)listview.getAdapter()).setNeedupdate(false);
				}
			}
			@Override
			public void onClickFrontView(int position) {
				// TODO Auto-generated method stub
				// 查看
				MyFIleInfo info = files.get(position);
				OpenFileUtils.openFile(MyFIleActivity.this, info.getParent()+"/"+info.getName());
			}
		});
		listview.setAdapter(new MyFileAdapter(this, files));

	}


	// 得到所有文件
	private List<MyFIleInfo> getFiles() {
		// TODO Auto-generated method stub
		// 打开指定目录，显示项目说明书列表，供用户选择
		List<MyFIleInfo> localList = new ArrayList<MyFIleInfo>();
		File specItemDir = new File(AppConfig.File_PATh);
		if (!specItemDir.exists()) {
			specItemDir.mkdir();
		}

		final File[] files = specItemDir.listFiles();
		for (File f : files) {
			MyFIleInfo info = new MyFIleInfo();

			// 设置名字
			info.setName(f.getName());
			// 设置大小
			info.setSize(f.length() < 1000 ? f.length() + "B"
					: f.length() < 1000000 ? ((double) (f.length() / 100) + "KB")
							: (double) (f.length() / 1000000) + "M");
			// 设置后缀
			info.setEnd(f.getName().substring(f.getName().lastIndexOf(".") + 1,
					f.getName().length()));
			// 设置最后更新的时间
			info.setCreate_time(TimeUtils.dateToStrLong(new Date(f
					.lastModified())));
			// 设置父目录
			info.setParent(f.getParent());
			// 设置mimetype
			info.setMimetype(getMimetype(f.getName()));
			String end = info.getEnd();
			info.setType(end.equals("avi") || end.equals("mp4")
					|| end.equals("3gp") || end.equals("rmvb") ? "视频" : end
					.equals("doc") ? "Doc" : end.equals("m4a")
					|| end.equals("mp3") || end.equals("mid")
					|| end.equals("xmf") || end.equals("ogg")
					|| end.equals("wav") ? "音乐" : end.equals("apk") ? "APK安装文件"
					: end.equals("jpg") || end.equals("gif")
							|| end.equals("png") || end.equals("jpeg")
							|| end.equals("bmp") ? "图片"
							: end.equals("txt") ? "文本" : "未知");
			localList.add(info);
		}
		return localList;
	}

	// 获取后最名
	public String getMimetype(String name) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String type = fileNameMap.getContentTypeFor(name);// name:"aa.txt"
		return type;
	}

	/**
	 * 读取文件创建时间
	 */
	public static String getCreateTime(String filePath) {
		String strTime = null;
		try {
			Process p = Runtime.getRuntime().exec(
					"cmd /C dir " + filePath + "/tc");
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.endsWith(".txt")) {
					strTime = line.substring(0, 17);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出：创建时间 2009-08-17 10:21
		return strTime;
	}

}
