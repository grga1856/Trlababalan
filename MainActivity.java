package com.example.borna2.trlababalan;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {

    private GLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Method1.setCtx(this);
        Method2.setCtx(this);
        getSupportActionBar().hide();


        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Metoda1"),
                        new SecondaryDrawerItem().withIdentifier(2).withName("Metoda2")
                )
                .withOnDrawerItemClickListener(this)
                .build();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        if(position==0){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,new Method1())
                    .commit();

        }
        else if(position==1){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,new Method2())
                    .commit();

        }
        return false;
    }
}
