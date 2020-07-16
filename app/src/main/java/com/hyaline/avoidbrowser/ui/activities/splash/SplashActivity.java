package com.hyaline.avoidbrowser.ui.activities.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.ui.activities.main.MainActivity;
import com.hyaline.avoidbrowser.ui.customviews.RippleView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

public class SplashActivity extends AppCompatActivity {
    private static final String[] BASIC_PERMISSIONS = new String[]{Manifest.permission.INTERNET, Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
    private RippleView rippleView;
    private Runnable jump = () -> {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rippleView = findViewById(R.id.ripple);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions()) {
            rippleView.postDelayed(jump, 2000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        rippleView.removeCallbacks(jump);
    }

    private boolean checkPermissions() {
        for (String permission : BASIC_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                new QMUIDialog.MessageDialogBuilder(this)
                        .setTitle("警告")
                        .setMessage("浏览器需要获取部分手机权限，点击确定请求权限~")
                        .setCanceledOnTouchOutside(false)
                        .addAction("确认", (dialog, index) -> {
                            ActivityCompat.requestPermissions(this, BASIC_PERMISSIONS, 111);
                            dialog.dismiss();
                        })
                        .addAction(0, "取消", QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
                            finish();
                        })
                        .create().show();
                return false;
            }
        }
        return true;
    }
}
