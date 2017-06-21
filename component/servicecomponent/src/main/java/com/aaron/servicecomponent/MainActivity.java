package com.aaron.servicecomponent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private  ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mStudentManager = IStudentManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            bindService(MainActivity.this, mConn);
        }
    };
    private IStudentManager mStudentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyService.start(MainActivity.this);
            }
        });
        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService(MainActivity.this, mConn);
            }
        });
        findViewById(R.id.btn_add_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mStudentManager.addStudent(new Student("John", 20));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void bindService(Context context, ServiceConnection conn) {
        Intent binder = new Intent(context, MyService.class);
        context.bindService(binder, conn, BIND_AUTO_CREATE);
    }
}
