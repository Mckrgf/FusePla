package com.supcon.mes.module_beacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.annotation.apt.Router;
import com.supcon.mes.R;
import com.supcon.mes.middleware.constant.Constant;

@Router(value=Constant.Router.BEACON_WELCOME)
public class Main12Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main12);
    }
}