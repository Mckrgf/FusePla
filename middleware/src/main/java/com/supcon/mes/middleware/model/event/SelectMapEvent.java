package com.supcon.mes.middleware.model.event;

/**
 * Time:    2020-05-11  14: 04
 * Author： nina
 * Des:
 */
public class SelectMapEvent<T> extends BaseEvent {
    private T data;
    private String selectTag;

    public SelectMapEvent(T data, String selectTag){
        this.data = data;
        this.selectTag = selectTag;
    }


    public T getEntity() {
        return data;
    }

    public String getSelectTag() {
        return selectTag;
    }
}
