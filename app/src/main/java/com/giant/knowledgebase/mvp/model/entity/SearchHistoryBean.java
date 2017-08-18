package com.giant.knowledgebase.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Jorble on 2017/6/22.
 */

@Entity
public class SearchHistoryBean {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String history;
    @Generated(hash = 1724186286)
    public SearchHistoryBean(Long id, @NotNull String history) {
        this.id = id;
        this.history = history;
    }
    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getHistory() {
        return this.history;
    }
    public void setHistory(String history) {
        this.history = history;
    }
}
