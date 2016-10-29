package com.qican.ygj.bean;

import com.videogo.openapi.bean.EZCameraInfo;

public class Camera {
    private String id;
    private String preImgUrl; //镜头预览图
    private String name; //镜头命名

    private String pondId; //属于哪个池塘
    private EZCameraInfo cameraInfo; //萤石提供的摄像头信息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreImgUrl() {
        return preImgUrl;
    }

    public void setPreImgUrl(String preImgUrl) {
        this.preImgUrl = preImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPondId() {
        return pondId;
    }

    public void setPondId(String pondId) {
        this.pondId = pondId;
    }

    public EZCameraInfo getCameraInfo() {
        return cameraInfo;
    }

    public void setCameraInfo(EZCameraInfo cameraInfo) {
        this.cameraInfo = cameraInfo;
    }
}
