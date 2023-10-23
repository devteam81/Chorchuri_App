package com.chorchuri.app.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chorchuri.app.R;
import com.chorchuri.app.ui.fragment.LoginFragment;
import com.chorchuri.app.ui.fragment.SearchFragment;
import com.chorchuri.app.ui.fragment.SignupFragment;
import com.chorchuri.app.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chorchuri.app.util.Fragments.HOME_FRAGMENTS;
import static com.chorchuri.app.util.Fragments.LOGIN_FRAGMENTS;

public class NewLoginActivity extends BaseActivity{

    private final String TAG = NewLoginActivity.class.getSimpleName();

    private static String CURRENT_FRAGMENT ;
    private Fragment fragment;
    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.container)
    FrameLayout container;

    private static final int PERMISSION_REQUEST_CODE = 100;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);
        replaceFragmentWithAnimation(new SignupFragment(), LOGIN_FRAGMENTS[0], false);

        back_btn.setOnClickListener(v -> onBackPressed());
    }

    public void replaceFragmentWithAnimation(Fragment fragment, String tag, boolean toBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CURRENT_FRAGMENT = tag;
        this.fragment = fragment;
        if (toBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.replace(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }
}
