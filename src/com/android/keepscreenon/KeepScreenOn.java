/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.keepscreenon;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import java.util.Date;
import android.text.Layout;
import android.content.Context;

import android.app.KeyguardManager;
import android.view.WindowManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class KeepScreenOn extends Activity {
    final String TAG = "KeepScreenOn";
    private String mOutputText = "";

    boolean starting = false;
    String finalText = "";

    static WakeLock wl;

    public void keepScreenOn(boolean on) {
        KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        int flags = WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        if (on) {
            getWindow().addFlags(flags);
        } else {
            getWindow().clearFlags(flags);
        }
    }

    public void keepScreenOn2(boolean on) {
        if (on) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "==KeepScreenOn==");
            wl.acquire();
        }else {  
            wl.release();  
            wl = null;  
        }  
    }  

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Call finish() if you want to start a new activity and exit
        //finish();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onpause");
        super.onPause();
    }

    private void refreshView(boolean started) {
        Button startButton = (Button) findViewById(R.id.button_start);
        Button stopButton = (Button) findViewById(R.id.button_stop);

        if (started) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            return;
        } else {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }            
    }

    public void showMessage(boolean isAppend, String msg) {
        Log.v("Frank", msg);

        TextView outputText = (TextView)findViewById(R.id.output_text_view);
        if (!isAppend)
            outputText.setText("");

        outputText.append(msg + "\n");
        //outputText.setMovementMethod(ScrollingMovementMethod.getInstance()); 

        final Layout layout = outputText.getLayout();
        if (layout != null) {
            final int scrollAmount = layout.getLineTop(outputText.getLineCount()) - outputText.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                outputText.scrollTo(0, scrollAmount);
            else
                outputText.scrollTo(0, 0);
        }
    }

    public void start(View view) {
        showMessage(true, "Start...");
        starting = true;
        //keepScreenOn(true);
        keepScreenOn2(true);
        refreshView(starting);
    }

    public void stop(View view) {
        showMessage(true, "Stop...");
        starting = false;
        //keepScreenOn(false);
        keepScreenOn2(false);
        refreshView(starting);
    }
}

