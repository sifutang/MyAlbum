package com.example.android.myalbum.model;

/**
 * Created by android on 17-8-16.
 */

public class ImageInfo {

    private String title;
    private String size;
    private String desc;
    private String path;
    private String dispalyName;

//    public ImageInfo(String title, String size, String desc, String path, String dispalyName) {
//        this.title = title;
//        this.size = size;
//        this.desc = desc;
//        this.path = path;
//        this.dispalyName = dispalyName;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDispalyName() {
        return dispalyName;
    }

    public void setDispalyName(String dispalyName) {
        this.dispalyName = dispalyName;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "title='" + title + '\'' +
                ", size='" + size + '\'' +
                ", desc='" + desc + '\'' +
                ", path='" + path + '\'' +
                ", dispalyName='" + dispalyName + '\'' +
                '}';
    }
}
