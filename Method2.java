package com.example.borna2.trlababalan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Borna2 on 07-Apr-17.
 */

public class Method2 extends Fragment {

    MyGLSurfaceView2 v;

    public static void setCtx(Context ctx) {
        Method2.ctx = ctx;
        Log.e("qwqwq","qqqqqqqqqq");
    }

    public static Context ctx;

    public Method2() {
        v = new MyGLSurfaceView2(Method2.ctx);
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
