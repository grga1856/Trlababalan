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

public class Method3 extends Fragment {

    MyGLSurfaceView3 v;

    public static void setCtx(Context ctx) {
        Method3.ctx = ctx;
    }

    public static Context ctx;

    public Method3() {
        v = new MyGLSurfaceView3(Method3.ctx);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return v;
    }

}
