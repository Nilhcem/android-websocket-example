package com.nilhcem.websockets;

import android.view.View;

public class SecondActivity extends BaseSheepActivity {

    @Override
    int getLayoutResID() {
        return R.layout.second;
    }

    public void goBack(View view) {
        finish();
    }
}
