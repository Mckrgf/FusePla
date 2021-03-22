package com.supcon.mes.middleware.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supcon.common.view.base.view.BaseLinearLayout;
import com.supcon.mes.middleware.R;

/**
 * Time:    2019-11-07  11: 02
 * Author： nina
 * Des: 前面带蓝色的组的样式
 */
public class CustomGroupTitle extends BaseLinearLayout {
    private String title;
    private int iconRes;
    ImageView ivIcon;
    TextView tvTitle;
    LinearLayout llt;
    boolean isOpen = true;
    boolean hasIcon = true;

    public void setOpen(boolean open) {
        isOpen = open;

        if (isOpen) {
            ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drop_down));
        } else {
            ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drop_up));
        }
    }

    public CustomGroupTitle(Context context) {
        super(context);
    }

    public CustomGroupTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int layoutId() {
        return R.layout.v_group_title;
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        ivIcon = findViewById(R.id.iv_icon);
        tvTitle = findViewById(R.id.tv_title);
        llt = findViewById(R.id.llt);

        tvTitle.setText(title);

        if (hasIcon) {
            ivIcon.setVisibility(VISIBLE);
        } else {
            ivIcon.setVisibility(GONE);
        }

        if (isOpen) {
            ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drop_down));
        } else {
            ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drop_up));
        }

        if (iconRes != 0) {
            ivIcon.setImageResource(iconRes);
        }
    }

    @Override
    protected void initAttributeSet(AttributeSet attrs) {
        super.initAttributeSet(attrs);

        if(attrs!=null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CustomGroupTitle);
            iconRes = array.getResourceId(R.styleable.CustomGroupTitle_icon, 0);
            title = array.getString(R.styleable.CustomGroupTitle_title);
            isOpen = array.getBoolean(R.styleable.CustomGroupTitle_isopen, true);
            hasIcon = array.getBoolean(R.styleable.CustomGroupTitle_hasicon, true);

            array.recycle();
        }
    }
    public void setTitle(String title) {
        this.title = title;

        tvTitle.setText(title);
    }

    public void setHasIcon(boolean hasIcon) {
        this.hasIcon = hasIcon;

        if (hasIcon) {
            ivIcon.setVisibility(VISIBLE);
        } else {
            ivIcon.setVisibility(GONE);
        }
    }
}
