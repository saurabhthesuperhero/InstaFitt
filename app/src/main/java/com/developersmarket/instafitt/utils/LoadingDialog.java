package com.developersmarket.instafitt.utils;

import android.app.Activity;
import android.app.Dialog;

import com.developersmarket.instafitt.R;


public class LoadingDialog {
    private Activity activity;
    private Dialog dialog ;

    public LoadingDialog(Activity activity){
        this.activity = activity;
        dialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar);
//        dialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    public void showLoading(){
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_loading_screen);
        dialog.setCancelable(true);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public void hideLoading(){
        dialog.dismiss();
    }
}
