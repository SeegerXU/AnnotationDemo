package com.seeger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.seeger.router.ResourceTest;
import com.seeger.router.api.RouterBinder;

public class MainActivity extends AppCompatActivity {

    @ResourceTest(R.string.app_name)
    String name;

    private TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RouterBinder.bind(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(name);
    }
}
