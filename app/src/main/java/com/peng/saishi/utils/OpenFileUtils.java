package com.peng.saishi.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OpenFileUtils {

	
    public static void openFile(Context context,String filePath){  
        File file = new File(filePath);  
        if(!file.exists()) return ;  
        /* 取得扩展名 */  
        Intent intent =null;
        String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase();   
        /* 依扩展名的类型决定MimeType */  
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||  
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){  
        	 intent = getAudioFileIntent(filePath);  
        }else if(end.equals("3gp")||end.equals("mp4")){  
        	 intent = getVideoFileIntent(filePath);
        }else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||  
                end.equals("jpeg")||end.equals("bmp")){  
        	 intent = getImageFileIntent(filePath);  
        }else if(end.equals("apk")){  
        	 intent = getApkFileIntent(filePath);  
        }else if(end.equals("ppt")){  
        	 intent = getPptFileIntent(filePath);  
        }else if(end.equals("xls")){  
        	 intent = getExcelFileIntent(filePath);  
        }else if(end.equals("doc")){  
        	 intent = getWordFileIntent(filePath);  
        }else if(end.equals("pdf")){  
        	 intent =getPdfFileIntent(filePath);  
        }else if(end.equals("chm")){  
        	 intent = getChmFileIntent(filePath);  
        }else if(end.equals("txt")){  
        	 intent = getTextFileIntent(filePath,false);  
        }else{  
        	 intent = getAllIntent(filePath);  
        }  
        context.startActivity(intent);
    }  
      
    //Android获取一个用于打开APK文件的intent  
    public static Intent getAllIntent( String param ) {  
  
        Intent intent = new Intent();    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        intent.setAction(android.content.Intent.ACTION_VIEW);    
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri,"*/*");   
        return intent;  
    }  
    //Android获取一个用于打开APK文件的intent  
    public static Intent getApkFileIntent( String param ) {  
  
        Intent intent = new Intent();    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        intent.setAction(android.content.Intent.ACTION_VIEW);    
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri,"application/vnd.android.package-archive");   
        return intent;  
    }  
  
    //Android获取一个用于打开VIDEO文件的intent  
    public static Intent getVideoFileIntent( String param ) {  
  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        intent.putExtra("oneshot", 0);  
        intent.putExtra("configchange", 0);  
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri, "video/*");  
        return intent;  
    }  
  
    //Android获取一个用于打开AUDIO文件的intent  
    public static Intent getAudioFileIntent( String param ){  
  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        intent.putExtra("oneshot", 0);  
        intent.putExtra("configchange", 0);  
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri, "audio/*");  
        return intent;  
    }  
  
    //Android获取一个用于打开Html文件的intent     
    public static Intent getHtmlFileIntent( String param ){  
  
        Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.setDataAndType(uri, "text/html");  
        return intent;  
    }  
  
    //Android获取一个用于打开图片文件的intent  
    public static Intent getImageFileIntent( String param ) {  
  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri, "image/*");  
        return intent;  
    }  
  
    //Android获取一个用于打开PPT文件的intent     
    public static Intent getPptFileIntent( String param ){    
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");     
        return intent;     
    }     
  
    //Android获取一个用于打开Excel文件的intent     
    public static Intent getExcelFileIntent( String param ){    
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/vnd.ms-excel");     
        return intent;     
    }     
  
    //Android获取一个用于打开Word文件的intent     
    public static Intent getWordFileIntent( String param ){    
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/msword");     
        return intent;     
    }     
  
    //Android获取一个用于打开CHM文件的intent     
    public static Intent getChmFileIntent( String param ){     
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/x-chm");     
        return intent;     
    }     
  
    //Android获取一个用于打开文本文件的intent     
    public static Intent getTextFileIntent( String param, boolean paramBoolean){     
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        if (paramBoolean){     
            Uri uri1 = Uri.parse(param );     
            intent.setDataAndType(uri1, "text/plain");     
        }else{     
            Uri uri2 = Uri.fromFile(new File(param ));     
            intent.setDataAndType(uri2, "text/plain");     
        }     
        return intent;     
    }    
    //Android获取一个用于打开PDF文件的intent     
    public static Intent getPdfFileIntent( String param ){     
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/pdf");     
        return intent;     
    }  
	
}
