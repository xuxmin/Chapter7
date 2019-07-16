package com.xxm.chapter7;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class GlideImageActivity extends AppCompatActivity {

    private String[] mPermissionsArrays = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET};

    private final static int REQUEST_PERMISSION = 123;


    ViewPager pager = null;
    ViewAdapter adapter;
    LayoutInflater layoutInflater = null;
    List<View> pages = new ArrayList<View>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initPermission();


        layoutInflater = getLayoutInflater();
        pager = (ViewPager) findViewById(R.id.view_pager);
        addImage(R.drawable.drawableimage);
        addImage(R.drawable.ic_markunread);
        if (checkPermission(Manifest.permission.INTERNET)
                && checkPermission(Manifest.permission.ACCESS_NETWORK_STATE)) {
            addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963756&di=9c0c6c839381c8314a3ce8e7db61deb2&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110316%2F5961966_124313527122_2.jpg");
        }
        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            addImage("/sdcard/fileimage.jpg");
        }
        addImage("file:///android_asset/assetsimage.jpg");
        addImage(R.raw.rawimage);
        this.adapter = new ViewAdapter();
        adapter.setDatas(pages);
        pager.setAdapter(adapter);
    }

    private void addImage(int resId) {
        ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.activity_image_item, null);
        Glide.with(this)
                .load(resId)
                .error(R.drawable.error)
                .into(imageView);
        pages.add(imageView);
    }

    private void addImage(String path) {
        ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.activity_image_item, null);
        Glide.with(this)
                .load(path)
                .apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                .error(R.drawable.error)
                .into(imageView);
        pages.add(imageView);
    }

    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void initPermission() {
        if (!checkPermissionAllGranted(mPermissionsArrays)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissionsArrays, REQUEST_PERMISSION);
            }
        } else {
            Toast.makeText(this, "已经获取所有所需权限", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermissionAllGranted(String[] permissions) {
        // 6.0以下不需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults.length > i && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已经授权" + permissions[i], Toast.LENGTH_LONG).show();
                }
            }

            if (checkPermission(Manifest.permission.INTERNET)
                    && checkPermission(Manifest.permission.ACCESS_NETWORK_STATE)) {
                addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963756&di=9c0c6c839381c8314a3ce8e7db61deb2&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110316%2F5961966_124313527122_2.jpg");
            }
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                addImage("/sdcard/fileimage.jpg");
            }
            adapter.setDatas(pages);
        }
    }
}
