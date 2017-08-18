package com.giant.knowledgebase.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Jorble on 2017/6/16.
 */
@Entity
public class UserBean {
    /**
     * uid : 899**
     * email : ****@gmail.com
     * name : 彭博
     * gender : 男
     * portrait : http://www.oschina.net/uploads/user/****
     * location : 广东 广州
     * phone : 10899132493
     * state : 1
     */

    @Id
    private Long dbid;
    @Unique
    private String uid;
    private String email;
    private String name;
    private String gender;
    private String portrait;
    private String location;
    private String phone;
    private int state;
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    @Generated(hash = 996058848)
    public UserBean(Long dbid, String uid, String email, String name, String gender,
            String portrait, String location, String phone, int state,
            String accessToken, String refreshToken, int expiresIn) {
        this.dbid = dbid;
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.portrait = portrait;
        this.location = location;
        this.phone = phone;
        this.state = state;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
    @Generated(hash = 1203313951)
    public UserBean() {
    }
    public Long getDbid() {
        return this.dbid;
    }
    public void setDbid(Long dbid) {
        this.dbid = dbid;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getPortrait() {
        return this.portrait;
    }
    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getAccessToken() {
        return this.accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getRefreshToken() {
        return this.refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public int getExpiresIn() {
        return this.expiresIn;
    }
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
