package com.imotor.manual;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

public class ImageEntries {

    public File mImageFile;
    public ArrayList<Uri> mUriList = new ArrayList<Uri>();

    public ImageEntries() {
    }

    public void addImage(String file) {
        mImageFile = new File(file);
        Uri uri = Uri.fromFile(mImageFile);
        mUriList.add(uri);
    }


    public void clear() {
        mUriList.clear();
    }

    public void updateImageList(ArrayList<Uri> lists) {
        mUriList = null;
        mUriList = lists;
    }

}
