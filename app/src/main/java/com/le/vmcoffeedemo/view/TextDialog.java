package com.le.vmcoffeedemo.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.le.vmcoffeedemo.R;


public class TextDialog extends Dialog {
    private TextView tv;
    
    public TextDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_text);
        tv = findViewById(R.id.dialog_text_tv);
    }
    
    public void setText(String text,int color) {
        tv.setText(text);
        tv.setTextColor(color);
        if (!isShowing()) show();
    }
}
