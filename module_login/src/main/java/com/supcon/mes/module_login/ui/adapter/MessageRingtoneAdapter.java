package com.supcon.mes.module_login.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.HeaderRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_login.R;

/**
 * Time:    2019-12-31  10: 13
 * Author： nina
 * Des:
 */
public class MessageRingtoneAdapter extends HeaderRecyclerViewAdapter<String> {
    String checkedName;

    public void setCheckedName(String checkedName) {
        this.checkedName = checkedName;
    }

    public MessageRingtoneAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<String> {
        @BindByTag("title")
        TextView title;
        @BindByTag("image")
        ImageView image;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> {
                checkedName = getItem(getAdapterPosition());

                if (onItemChildViewClickListener != null) {
                    onItemChildViewClick(itemView, 0, getItem(getAdapterPosition()));
                }
            });
        }


        @Override
        protected int layoutId() {
            return R.layout.item_message_ring_tone;
        }

        @Override
        protected void update(String data) {
            title.setText(data);
            if (StringUtil.equalsIgnoreCase(data, checkedName)) {
                image.setVisibility(View.VISIBLE);
            } else {
                image.setVisibility(View.INVISIBLE);
            }
        }
    }

}
