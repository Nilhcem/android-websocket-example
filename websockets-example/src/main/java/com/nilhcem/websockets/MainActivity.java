package com.nilhcem.websockets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseSheepActivity {

    @Override
    int getLayoutResID() {
        return R.layout.main;
    }

    public void goToNextActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
