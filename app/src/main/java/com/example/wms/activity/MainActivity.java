package com.example.wms.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.wms.fragments.ProductFragment;
import com.example.wms.R;
import com.example.wms.fragments.SupplierFragment;
import com.example.wms.adapters.ViewPagerManager;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    ViewPagerManager viewPagerManager;
    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetching view IDs from resource
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);

        // Registering TabLayout with FragmentManager
        viewPagerManager = new ViewPagerManager(getSupportFragmentManager());

        // Adding Fragment with help of TabLayout adapter
        viewPagerManager.addFragment(new SupplierFragment(),"Supplier");
        viewPagerManager.addFragment(new ProductFragment(),"Products");

        //Setting the Fragments with ViewPager
        viewPager.setAdapter(viewPagerManager);

        //Setting up TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);



    }


    public static byte[] mImageViewToByte(ImageView image) {
        Bitmap bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray =stream.toByteArray();
        return  byteArray;
    }
}