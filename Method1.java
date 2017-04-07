package com.example.borna2.trlababalan;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Method1 extends Fragment {

    MyGLSurfaceView v;

    public static void setCtx(Context ctx) {
        Method1.ctx = ctx;
        Log.e("qwqwq","qqqqqqqqqq");
    }

    public static Context ctx;

    public Method1() {
        v = new MyGLSurfaceView(Method1.ctx);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        return inflater.inflate(R.layout.fragment_method1, container, false);
        return v;
    }

}
