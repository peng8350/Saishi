package com.peng.saishi.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.TextView;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;

public class AboutActivity extends BaseBackActivity {

    @Override
    public void init() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_about);
        super.init();

        TextView content = (TextView) findViewById(R.id.textView1);
        try {
            content.setText("版本号:\t\t\t" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "\nauthor by:     尐鹏\n制作时间:\t\t\t接近两个月\n服务端:\t\tJsp+Servlet\n数据库:\t\t\tMySQL");
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
