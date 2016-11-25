package com.qican.ygj.bean;

import java.io.Serializable;

public class Pond implements Serializable {
    private String id;
    private String name;
    private String imgUrl;
    private String desc;

    private String userId;
    private String location;

    public Pond() {

    }

    /**
     * 把天柱的Pond转换为我的Pond
     *
     * @param pond
     */
    public Pond(com.qican.ygj.beanfromzhu.Pond pond) {
        this.setId(pond.getPondId());
        this.setName(pond.getPondName());
        this.setImgUrl(pond.getPondImageUrl());
        this.setDesc(pond.getPondDescrible());

        this.setUserId(pond.getUserId());
        this.setLocation(pond.getPondLocation());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
