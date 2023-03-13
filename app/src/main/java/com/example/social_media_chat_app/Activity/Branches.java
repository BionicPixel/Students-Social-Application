package com.example.social_media_chat_app.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.social_media_chat_app.R;

public class Branches extends RegistrationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branches);

        TextView  textView=(TextView) findViewById(R.id.txt_bundle);
        Bundle bundle=getIntent().getExtras();
        String data=bundle.get("data").toString();
        textView.setText(data);
    }
}