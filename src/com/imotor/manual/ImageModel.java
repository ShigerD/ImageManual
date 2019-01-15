package com.imotor.manual;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ImageModel {

    private static final String TAG = "ImageModel";
    private Context mContext;

    public ImageModel(Context context) {
        mContext = context;
    }

    public ImageEntries getImagePath(String filePath) {
        Log.d(TAG, "init: FileSavepath >> " + filePath);

        ImageEntries imageEntries = new ImageEntries();
        List<String> imagePathList = new ArrayList<String>();
        File fileAll = new File(filePath);
        Log.d(TAG, "fileAll.exists()---" + fileAll.exists());
        if (!fileAll.exists()) {
            return null;
        }
        File[] files = fileAll.listFiles();
        Log.d(TAG, "getImagePath: files >>> " + files);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
                imageEntries.addImage(file.getPath());//
            }
        }
        try {
            Collections.sort(imageEntries.mUriList, new Comparator<Uri>() {
                @Override
                public int compare(Uri o1, Uri o2) {
                    File file1 = new File(o1.getPath());
                    File file2 = new File(o2.getPath());
                    Integer integer1 = Integer.valueOf(getFileNameNoEx(file1.getName()));
                    Integer integer2 = Integer.valueOf(getFileNameNoEx(file2.getName()));
                    return integer1.compareTo(integer2);
                }
            });
        } catch (NumberFormatException ex) {
            Collections.sort(imageEntries.mUriList);
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imageEntries;
    }

    public boolean checkIsImageFile(String fileName) {
        boolean isImageFile = false;
        String FileEnd = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") ||
                FileEnd.equals("gif") || FileEnd.equals("jpeg") ||
                FileEnd.equals("bmp") || FileEnd.equals("wbmp") ||
                FileEnd.equals("ico") || FileEnd.equals("jpe")) {
            isImageFile = true;
        }
        return isImageFile;
    }

    private String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
}
