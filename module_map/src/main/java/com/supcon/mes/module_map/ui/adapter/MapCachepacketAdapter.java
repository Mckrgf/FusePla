package com.supcon.mes.module_map.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.mes.middleware.ui.view.CustomGroupTitle;
import com.supcon.mes.middleware.ui.view.CustomTitleValueSmall;
import com.supcon.mes.module_map.R;
import com.supcon.mes.module_map.model.bean.MapCacheBaseLayerBeanEntity;
import com.supcon.mes.module_map.model.bean.MapCacheBaseLayerGroupEntity;

import java.util.ArrayList;

public class MapCachepacketAdapter extends AdapterExBase<MapCacheBaseLayerGroupEntity, MapCacheBaseLayerBeanEntity> {

    public MapCachepacketAdapter(Context context, ArrayList<MapCacheBaseLayerGroupEntity> objects) throws Exception {
        super(context, objects);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cache_group, parent, false);
            holder.title = convertView.findViewById(R.id.group);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        final MapCacheBaseLayerGroupEntity item = getGroup(groupPosition);
        if (item != null) {
            holder.title.setTitle(item.getTitle());

            holder.title.setOpen(isExpanded);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {

            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cache_packet, parent, false);
            holder.name = convertView.findViewById(R.id.name);
            holder.size = convertView.findViewById(R.id.size);
            holder.version = convertView.findViewById(R.id.version);
            holder.update = convertView.findViewById(R.id.update);
            holder.icon = convertView.findViewById(R.id.icon);
            holder.needUpdate = convertView.findViewById(R.id.needUpdate);
            holder.lltstate = convertView.findViewById(R.id.lltstate);

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        final MapCacheBaseLayerBeanEntity data = getChild(groupPosition, childPosition);
        if (data != null) {

            holder.name.setValue(data.getName());

            if (data.getVersion() != null) {
                holder.version.setText(context.getString(R.string.map_cache_version) + data.getVersion());
            }

            if (data.getSize() > 0) {
                holder.size.setText(context.getString(R.string.map_cache_size) + data.getSize());
            }

            holder.needUpdate.setVisibility(View.GONE);
            holder.update.setVisibility(View.GONE);


            if (data.getState() == MapCacheBaseLayerBeanEntity.STATE_HAS_DOWNLOAD) {
                holder.lltstate.setVisibility(View.INVISIBLE);
            } else if (data.getState() == MapCacheBaseLayerBeanEntity.STATE_DOWNLOADABLE){
                holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));

                holder.lltstate.setVisibility(View.VISIBLE);
            } else if (data.getState() == MapCacheBaseLayerBeanEntity.STATE_NEED_UPDATE) {
                holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
                holder.needUpdate.setVisibility(View.VISIBLE);
                holder.lltstate.setVisibility(View.VISIBLE);
            } else if (data.getState() == MapCacheBaseLayerBeanEntity.STATE_DOWNLOAD_ING) {
                holder.lltstate.setVisibility(View.VISIBLE);
                holder.update.setVisibility(View.VISIBLE);
                holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_suspend));
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }


    private class ChildViewHolder {

        @BindByTag("name")
        CustomTitleValueSmall name;
        @BindByTag("size")
        TextView size;
        @BindByTag("version")
        TextView version;
        @BindByTag("update")
        TextView update;
        @BindByTag("icon")
        ImageView icon;
        @BindByTag("needUpdate")
        ImageView needUpdate;
        @BindByTag("lltstate")
        LinearLayout lltstate;
    }

    private class GroupViewHolder {
        CustomGroupTitle title;
    }
}


