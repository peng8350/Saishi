package com.peng.saishi.utils;

import android.content.Context;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.config.AppConfig;

public class ShareAppUtils {

	
	//分享App给好友
	public static void showShare(Context context) {  
        ShareSDK.initSDK(context);  
        OnekeyShare oks = new OnekeyShare();  
        //关闭sso授权  
        oks.disableSSOWhenAuthorize();  
  
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法  
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));  
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用  
        oks.setTitle("赛伴");  
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用  
        oks.setTitleUrl("http://sharesdk.cn");  
        // text是分享文本，所有平台都需要这个字段  
        oks.setText("分享分享");  
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数  
        oks.setImageUrl(AppConfig.Img_Path+"lauchicon.png");//确保SDcard下面存在此张图片  
        oks.setImagePath(AppConfig.app_icon);//确保SDcard下面存在此张图片  
        // url仅在微信（包括好友和朋友圈）中使用  
        oks.setUrl("www.baidu.com");  
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用  
        oks.setComment("测试sdk分享app功能的实现");  
        // site是分享此内容的网站名称，仅在QQ空间使用  
        oks.setSite("www.baidu.com");  
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用  
        oks.setSiteUrl("http://sharesdk.cn");  
        // 启动分享GUI  
        oks.show(context);  
    }
	
	//分享应用给好友
	public static void showMatchShare(Context context,MatchInfo info) {  
        ShareSDK.initSDK(context);  
        OnekeyShare oks = new OnekeyShare();  
        //关闭sso授权  
        oks.disableSSOWhenAuthorize();  
  
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法  
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));  
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用  
        oks.setTitle(info.getName());  
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用  
        //传入的形参是web
        oks.setTitleUrl(info.getWebUrl());  
        // text是分享文本，所有平台都需要这个字段  
        oks.setText(info.getName());  
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数  
        oks.setImageUrl(AppConfig.Matchpic_path+info.getId());//确保SDcard下面存在此张图片  
        // url仅在微信（包括好友和朋友圈）中使用  
        oks.setImagePath(AppConfig.app_icon);//确保SDcard下面存在此张图片  
        oks.setUrl(info.getWebUrl());  
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用  
        oks.setComment("这是一个不错的比赛");  
        // site是分享此内容的网站名称，仅在QQ空间使用  
        oks.setSite(info.getWebUrl());  
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用  
        oks.setSiteUrl(info.getWebUrl());  
        // 启动分享GUI  
        oks.show(context);  
    }
}
