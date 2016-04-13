package com.example.wifi_signal_tracker;

import android.content.Context;
import android.util.Log;

public class JavascriptInterface {
    Context mContext;
    static double c,d;
    int a,b;
    /** Instantiate the interface and set the context */
    public JavascriptInterface(double x,double y) {
        c=x;
        d=y;
        
    }

 

    /** Get the value */
    @android.webkit.JavascriptInterface
    public int getTop() {
        int a=930;
        a=(int) (1280-((1280/51)*d));
        Log.e("X_coord", ""+a+"  "+d);
        //a=(int)(Resources.getSystem().getDisplayMetrics().heightPixels/)*d;
    	return a;
    }
    @android.webkit.JavascriptInterface
    public int getValue() {
    	//MainActivity ma=new MainActivity();
    	//double c=ma.x_temp;
    	int b=300;
    	b=(int) ((1600/65)*c);
    	Log.e("X_coord", ""+b+"  "+c);
    	//b=(int)(Resources.getSystem().getDisplayMetrics().widthPixels/)*d;
    	return b;
    }
}