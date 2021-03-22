package com.supcon.mes.module_map.ui.adapter;

import android.content.Context;
import android.widget.BaseExpandableListAdapter;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.util.CloneUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ExpandableListAdapter基类
 * 
 * @author leon
 * 
 * @param <T>
 *            组对象
 * @param <K>
 *            子对象
 */
public abstract class AdapterExBase<T extends List<K>, K> extends BaseExpandableListAdapter {
	protected OnFilterListener<K> onFilterListener;

	protected Context context;
	protected ArrayList<T> dataList;
	protected ArrayList<T> dataListAfterFilter;
	/**
	 * 加完数据之后是否自动全部展开
	 */
	protected boolean isAutoExpandGroup = false;

	public AdapterExBase(Context context, ArrayList<T> objects) throws Exception {
		this.context = context;
		setData(objects);
	}

	public Context getContext() {
		return context;
	}

	/**
	 * 获取总的数据
	 * 
	 * @return
	 */
	public List<T> getData() {
		return this.dataList;
	}

	/**
	 * 获取过滤后的数据
	 * 
	 * @return
	 */
	public List<T> getDataAfterFilter() {
		return this.dataListAfterFilter;
	}

	/**
	 * 添加数据
	 * 
	 * @param objects
	 */
	public void setData(ArrayList<T> objects) throws Exception {
		this.dataList = objects;
		filter();
	}

	/**
	 * 追加数据
	 * 
	 * @param obj
	 */
	public void addData(List<T> obj) throws Exception {
		if (this.dataList == null)
			dataList = new ArrayList<T>();

		this.dataList.addAll(obj);
		filter();
	}

	/**
	 * 过滤数据(本类过滤方法中使用了深复制,过滤之后的数据为全新数据,只适合查询,重写为了源数据能修改)
	 */
	public void filter() throws Exception {
		if (this.onFilterListener != null && dataList != null && dataList.size() > 0) {
			// 序列化深拷贝
			dataListAfterFilter = CloneUtil.clone(dataList);
			Iterator<T> itT = dataListAfterFilter.iterator();

			while (itT.hasNext()) {
				T t = itT.next();
				Iterator<K> itK = t.iterator();
				while (itK.hasNext()) {
					K k = itK.next();
					if (!this.onFilterListener.filter(k)) {
						itK.remove();
					}
				}
				if (t.size() < 1) {
					itT.remove();
				}
			}
		} else {
			dataListAfterFilter = dataList;
		}
		notifyDataSetChanged();
	}

	@Override
	public T getGroup(int position) {
		try {
			if (dataListAfterFilter != null && dataListAfterFilter.size() > position) {
				return dataListAfterFilter.get(position);
			}
		} catch (Exception e) {
			LogUtil.e(e + "");
		}
		return null;
	}

	@Override
	public int getGroupCount() {
		if (dataListAfterFilter != null) {
			return dataListAfterFilter.size();
		}
		return 0;
	}

	@Override
	public long getGroupId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public K getChild(int arg0, int arg1) {
		T t = getGroup(arg0);
		if (t != null) {
			return t.get(arg1);
		}
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return 0;
	}

	@Override
	public int getChildrenCount(int arg0) {
		T t = getGroup(arg0);
		if (t != null) {
			return t.size();
		}
		return 0;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	public interface OnFilterListener<K> {
		boolean filter(K k);
	}

	public void setOnFilterListener(OnFilterListener<K> listener) {
		this.onFilterListener = listener;
	}
}
