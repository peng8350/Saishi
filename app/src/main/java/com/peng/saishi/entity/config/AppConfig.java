package com.peng.saishi.entity.config;


import android.os.Environment;

/**
 * 
 * 配置ＡＰＰ链接服务端的ＩＰ地址等 用来保存一些常量等 配置各种不同的servlet的地址
 * 
 * @author 小鹏
 * 
 */
public class AppConfig {
	// 七牛空间域名
	public static final String qiniu_Url = "http://oe54dehvk.bkt.clouddn.com/";
	// 七牛token
	public static String qiniu_taken;

	// 短信借口api key
	public static final String mob_key = "164a95e4fae8e";
	// 短信接口密码
	public static final String mob_pswd = "10de0490a4ad510aeb92ca4be14de9bd";
	// 指向服务端的ＩＰ地址
	public static final String IP = "127.0.0.1"; // 指向服务端的端口,部署到远程服务器修改的地方1
	public static final int PORT = 8080; // 部署到远程服务器修改的地方2

	public static final String statement = "";// 部署到远程服务器修改的地方3
	// 暂时存储用户图片的位置
	public static final String Userpic_path = "http://" + IP + ":" + PORT
			+ statement + "/images/User/";
	// 暂时存储比赛图片的位置
	public static final String Matchpic_path = "http://" + IP + ":" + PORT
			+ statement + "/images/Match/";
	// 暂时存储队伍信息图片位置
	public static final String Teanpic_path = "http://" + IP + ":" + PORT
			+ statement + "/images/Team/";
	// 向服务端获取二维码图片的位置
	public static final String Code_Path = "http://" + IP + ":" + PORT
			+ statement + "/images/qrcode/";
	// 服务端存储新闻图片的within
	public static final String New_Path = "http://" + IP + ":" + PORT
			+ statement + "/images/News/";

	public static final String Img_Path = "http://" + IP + ":" + PORT
			+ statement + "/images/";
	// 语音存放地址
	public static final String Radio_path = "http://" + IP + ":" + PORT
			+ statement + "/radios/";
	// 语音缓存地址
	public static final String Radiohold_path = Environment
			.getExternalStorageDirectory().getAbsolutePath()

	+ "/saiban/holdrecord/";
	// 服务端下载apk的地址
	public static final String APK_PATH = "http://" + IP + ":" + PORT
			+ "/Version/";
	//文件保存未知
	public static final String File_PATh= Environment.getExternalStorageDirectory()+"/saiban/files/";
	// 存放下载APK的地址
	public static final String APkLocal_path = Environment
			.getExternalStorageDirectory().getAbsolutePath()

	+ "/saiban/apk/";
	// 最终链接的地址
	public static final String CONNECT = "http://" + IP + ":" + PORT
			+ "/Saishi_Sever/";
	// 二维码暂时爆粗路径
	public static final String code_temp = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/saiban/QRCODE/temp.png";
	// 聊天模糊占位图未知
	public static final String blur_temp = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/saiban/blurtemp.jpg";
	// 图片暂时缓存的地方
	public static final String pic_temp = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/saiban/temp.jpg";
	public static final String Chat_Save = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/saiban/Save/";
	// 图标缓存的地方
	public static final String app_icon = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/saiban/lauch.icon";
	/**
	 * 
	 * 操作用户的请求
	 */
	//获取七牛的token
	public static final String getToken=CONNECT + "getToken";
	// 用户登陆请求的地址
	public static final String LOGIN = CONNECT + "Login";
	// 获取用户信息的接口
	public static final String getUser = CONNECT + "getUser";
	// 用户注册请求的地址
	public static final String REG = CONNECT + "Reg";
	// 修改用户信息
	public static final String Update = CONNECT + "UpdateUser";
	// 修改用户密码
	public static final String UpdatePswd = CONNECT + "UpdatePswd";
	// 查询用户的队伍
	public static final String getUserTeam = CONNECT + "getUserTeam";
	// 查询用户的收藏
	public static final String getUserShoucang = CONNECT + "getShoucang";
	// 查询用户的历史
	public static final String getUserHistory = CONNECT + "getAllHistory";
	// 检查版本更新
	public static final String CheckVersion = CONNECT + "CheckVersion";
	// 用户收藏与否的借口
	public static final String SHoucang = CONNECT + "Shoucang";
	// 忘记密码
	public static final String forgetpswd = CONNECT + "ForgetPswd";
	// 判断存在用户
	public static final String UserExit = CONNECT + "UserExist";
	// 更新用户的设备id
	public static final String UpdateChannel = CONNECT + "updatechannel";
	// 得到用户未读通知的数量
	public static final String getUnRead = CONNECT + "getUnread";
	// 清空纬度数量
	public static final String ClearUnRead = CONNECT + "ClearUnread";
	// 清除单一通知
	public static final String ClearOneJoin = CONNECT + "ClearOneJoin";
	// 获取用户的密码
	public static final String getUserPswd = CONNECT + "getUserpswd";
	// 清空某个用户的缓存
	public static final String ClearUserCahe = CONNECT + "ClearUserCahe";

	/**
	 * 关于操作比赛表的请求
	 */
	// 查询比赛的评价
	public static final String getMatchPingjia = CONNECT + "getAllPingjias";
	// 查询所有比赛
	public static final String getAllMatch = CONNECT + "GetAllMatter";
	// 判断比赛是不是杯添加了
	public static final String isAdd = CONNECT + "IsShoucang";
	// 被人阅读过
	public static final String Visit = CONNECT + "Visit";
	//获得阅读量
	public static final String getMatchVisit = CONNECT  +"getMatchVisit";

	/**
	 * 关于操作队伍表的请求
	 */
	// 获取所有队伍
	public static final String getAllTeam = CONNECT + "getAllTeam";
	// 新组队,添加队伍
	public static final String AddTeam = CONNECT + "AddTeam";
	// 发送消息
	public static final String SendMsg = CONNECT + "SendMsg";
	// 同意假如
	public static final String AgreeJoin = CONNECT + "AgreeJoin";
	// 拒绝加入
	public static final String RefuseJoin = CONNECT + "RefuseJoin";
	// 申请成为成员
	public static final String ApplyMember = CONNECT + "ApplyMember";
	// 获取队伍成员
	public static final String getTeamMember = CONNECT + "getTeamMember";
	// 得到所有加入队伍的申请
	public static final String getApplys = CONNECT + "getApplys";
	// 清空队伍通知
	public static final String ClearJoin = CONNECT + "ClearJoin";
	// 踢人
	public static final String KickPerson = CONNECT + "KickMember";
	// 退出群组
	public static final String ExitTeam = CONNECT + "ExitTeam";
	// 修改群组
	public static final String UpdateTeam = CONNECT + "UpdateTeam";
	// 得到群组的最新bean
	public static final String getTeam = CONNECT + "getTeam";
	// 通过二维码加入
	public static final String QRcodeTeam = CONNECT + "QRCodeInTeam";
	// 获取比赛的新闻
	public static final String getNews = CONNECT + "getNews";
	// 清空队伍的缓存
	public static final String ClearTeamCahe = CONNECT + "ClearTeamCahe";

	/**
	 * 
	 * 图片，语音操作
	 */
	public static final String Pic_Upload = CONNECT + "ImageUpload";
	// 聊天图片
	public static final String Chat_picUpload = CONNECT + "ChatUpload";
	// 语音
	public static final String RadioUpload = CONNECT + "RadioUpload";

	/*
	 * 
	 * 对评价的操作
	 */
	// 点击好评
	public static final String ClickGood = CONNECT + "ClickGood";
	// 点击差评
	public static final String ClickBad = CONNECT + "ClickBad";
	// 添加一条评论
	public static final String AddPingjia = CONNECT + "Addpingjia";

	/**
	 * 对搜索表的操作
	 * 
	 */
	public static final String getSearch = CONNECT + "getAllSearch";
	public static final String UserSearch = CONNECT + "UserSearch";

	/*
	 * 
	 * 对历史的操作
	 */
	public static final String ClearHistory = CONNECT + "ClearHistory";

	/**
	 * 
	 * 问题与回复的rest api
	 */
	// 得到所有问题
	public static final String GetQuestions = CONNECT + "GetQuestions";
	// 得到所有回复
	public static final String GetAnswer = CONNECT + "GetAnswer";
	// 创建一个问题
	public static final String CreateQuestion = CONNECT + "CreateQuestion";
	// 回复消息给一个问题
	public static final String AnswerQuestion = CONNECT + "AnswerQuestion";
	// 获得用户问题
	public static final String getQuestions2 = CONNECT + "getQuestion2";
	// 阅读过
	public static final String VisitQuestion = CONNECT + "VisitQuestion";
	// 删除问题
	public static final String DelQuestion = CONNECT + "DelQuestion";

}
