package com.imotor.manual;

import android.os.Environment;

import java.io.File;

/**
 * created by shiger on 12/24/18.
 */

public class GreenComConstants {

    //当前为测试模式。
    public final static boolean IS_TEST = true;

    public final static String TEST_PHONE_NO  = "18051150976";

    //文件夹名称
    public final static String folderName = "com.siger.green.folder";
    //图片
    public final static String folderNamePictures = "pics";
    //摩尔多 mp3保存目录
    public final static String folderNameListenEars = "listens";


    //第一个拍照保存的文件名
    public final static String demoImage1 = "head_img_demo1.jpg";
    //第二个拍照保存的文件名
    public final static String demoImage2 = "head_img_demo2.jpg";
    //裁剪保存的文件名
    public final static String demoCutImage = "cut_result.jpg";

    //   sdcard/com.siger.green.folder/pics/
    public static final String FileSavepathPics = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/" + folderName + "/" + folderNamePictures + "/" ;

    //   sdcard/com.siger.green.folder/pics/cut_result.jpg
    public static final String CutFileSavepath = FileSavepathPics + "/" + demoCutImage;
    //   sdcard/com.siger.green.folder/pics/head_img_demo1.jpg
    public static final String FileSavepathHeadImg1 = FileSavepathPics + File.separator + demoImage1;
    //   sdcard/com.siger.green.folder/pics/head_img_demo1.jpg
    public static final String FileSavepathHeadImg2 = FileSavepathPics + File.separator + demoImage2;



    public static String[] sShowRankTypes = {
            "最新作品", "历史最热","本月最热","本周最热"
    };//

    public static final boolean doHideDiaglogClickEmpty = true;

    //platform
    public static final String Platform = "android";

    public static final int School_Id = 1;
    //
    public static final int SMS_Reset_Pwd= 1;
    public static final int SMS_Register= 2;

//    sharedprefence  vaule
    public static   String SHARE_SCH = "user";//用户切换时候不知道会不会有影响

    public static final  String SHARE_TOKEN = "access-token";
    public static final  String SHARE_ID = "id";
    public static final  String SHARE_LOGIN_STATUS= "login_status";
    public static final  String SHARE_USER_NAME= "user name";

    // sharedprefence  object
    public static final  String SHARE_Object_LoginData= "LoginData";
    public static final  String SHARE_Object_LoginUserinfo = "LoginUserinfo";
    public static final  String SHARE_Object_Schoolinfo = "LoginSchoolinfo";

    public static final  String SHARE_Object_MineUserinfo = "MineUserinfo";


}
