package com.mvcoder.loaddexdemo;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mvcoder.loaddexdemo.dex.IToast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dalvik.system.DexClassLoader;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private Button btLoad;
    private Button btUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btLoad = findViewById(R.id.bt_load);
        btUpload = findViewById(R.id.bt_upload);
        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getDir("dex1", MODE_PRIVATE);
                if(file == null || !file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(this, permissions)){
            loadDexFile();
        }else{
            EasyPermissions.requestPermissions(this, "加载外部dex需要sd卡权限", 1, permissions);
        }
    }


    private void loadDexFile(){
        String dexPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/libdex.jar";
        String outputPath = getDir("dex1", MODE_PRIVATE).getAbsolutePath();

        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, outputPath, null, getClassLoader());
        try {
            Class clazz = dexClassLoader.loadClass("com.mvcoder.loaddexdemo.OtherDexToastImpl");
            IToast iToast = (IToast) clazz.newInstance();
            Toast.makeText(this,iToast.getToast(), Toast.LENGTH_LONG).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(requestCode == 1 && perms != null && perms.size() == 2) {
            checkPermission();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
