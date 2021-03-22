package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2017/12/29.
 * Email:wangshizhan@supcon.com
 */

public class RefreshEvent extends BaseEntity {
    private static final String DEFAULT_TAG = "DEFAULT_TAG";
    /**
     * 设置一个标记,如果这个标记为DEFAULT_TAG,意味着这个Refresh的接受对象是所有能接受RefreshEvent的类
     * 但是很多情况,并不希望对于一个事件,去特别刷新所有列表,那么这个时候就通过指定tag来确定对应类的信息
     */
    public String tag = DEFAULT_TAG;
    public String action;
    public Integer pos;
    //删除表体某项id
    public Long delId;

    public Boolean isDefault() {
        return tag.equals(DEFAULT_TAG);
    }

    public static RefreshEvent createNew() {
        return new RefreshEvent();
    }

    public RefreshEvent tag(String tag) {
        this.tag = tag;
        return  this;
    }
    public RefreshEvent tag(Class<?> tagClass) {
        this.tag = tagClass.getCanonicalName();
        return this;
    }

    public RefreshEvent() {
    }

    public RefreshEvent(String action, Integer pos) {
        this.action = action;
        this.pos = pos;
    }

    public RefreshEvent(Long delId) {
        this.delId = delId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
