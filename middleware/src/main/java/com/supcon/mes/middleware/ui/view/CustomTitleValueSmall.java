package com.supcon.mes.middleware.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.supcon.common.view.base.view.BaseLinearLayout;
import com.supcon.mes.middleware.R;

/**
 * Time:    2019-11-07  11: 02
 * Author： nina
 * Des:
 *
 * 这个是放在外面使用的，只显示一行，目的是每个cell 一样高
 */
public class CustomTitleValueSmall extends BaseLinearLayout {
    private String title, value;
    private int iconRes;
    ImageView ivIcon;
    TextView tvTitle;
    TextView tvValue;

    public CustomTitleValueSmall(Context context) {
        super(context);
    }

    public CustomTitleValueSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int layoutId() {
        return R.layout.v_title_value_small;
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        ivIcon = findViewById(R.id.iv_icon);
        tvValue = findViewById(R.id.tv_value);
        tvTitle = findViewById(R.id.tv_title);

        if(!TextUtils.isEmpty(value)){
            tvValue.setText(value);
        }

        tvTitle.setText(title);

        if (iconRes != 0) {
            ivIcon.setImageResource(iconRes);
            ivIcon.setVisibility(VISIBLE);
        } else {
            ivIcon.setVisibility(GONE);
        }

    }

    @Override
    protected void initAttributeSet(AttributeSet attrs) {
        super.initAttributeSet(attrs);

        if(attrs!=null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTitleValueSmall);
            iconRes = array.getResourceId(R.styleable.CustomTitleValueSmall_icon, 0);
            title = array.getString(R.styleable.CustomTitleValueSmall_title);
            value = array.getString(R.styleable.CustomTitleValueSmall_value);
            array.recycle();
        }
    }

    public void setTitle(String text){
        tvTitle.setText(text);
    }

    public void setValue(String text){
        tvValue.setText(text);
    }
}
