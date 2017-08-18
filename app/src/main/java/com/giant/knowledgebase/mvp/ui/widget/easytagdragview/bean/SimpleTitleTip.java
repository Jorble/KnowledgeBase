package com.giant.knowledgebase.mvp.ui.widget.easytagdragview.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SimpleTitleTip implements Tip {
    @Unique
    private int id;
    private String tip;
    private int isAdd;
    @Generated(hash = 1903426733)
    public SimpleTitleTip(int id, String tip, int isAdd) {
        this.id = id;
        this.tip = tip;
        this.isAdd = isAdd;
    }

    @Generated(hash = 726427058)
    public SimpleTitleTip() {
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getIsAdd() {
        return this.isAdd;
    }

    public void setIsAdd(int isAdd) {
        this.isAdd = isAdd;
    }

    @Override
    public String toString() {
        return "SimpleTitleTip{" +
                "id=" + id +
                ", tip='" + tip + '\'' +
                ", isAdd=" + isAdd +
                '}';
    }
}
