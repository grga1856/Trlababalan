package com.example.borna2.trlababalan;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {

    private GLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Method1.setCtx(this);
        Method2.setCtx(this);
        Method3.setCtx(this);
        Method4.setCtx(this);
        Method5.setCtx(this);
        Method6.setCtx(this);
        Method7.setCtx(this);
        getSupportActionBar().hide();


        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Fuzija senzora"),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Akcelerometar"),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Rotacion vector"),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Graf"),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Raw sensor data"),
                        new PrimaryDrawerItem().withIdentifier(6).withName("Roll & pitch"),
                        new PrimaryDrawerItem().withIdentifier(7).withName("About")
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
        else if(position==2){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,new Method3())
                    .commit();

        }

        else if(position==3){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,new Method4())
                    .commit();

        }
        else if(position==4){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,new Method5())
                    .commit();

        }
        else if(position==5){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,new Method6())
                    .commit();

        }
        else if(position==6){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,new Method7())
                    .commit();

        }
        return false;
    }
}
