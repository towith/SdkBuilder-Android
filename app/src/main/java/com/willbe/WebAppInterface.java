package com.willbe;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.willbe.pipeline.obj.App_DTO;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    
    @JavascriptInterface
    public void testObj(App_DTO app_dto) {
        Toast.makeText(mContext, app_dto.toString(), Toast.LENGTH_SHORT).show();
    }
}