package com.aaron.servicecomponent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

public class MyService extends Service {

    private static final String TAG = "MyService";

    private IBinder mBinder = new LocalBinder();

    public MyService() {
    }

    public static void bind(Context context, ServiceConnection conn) {
        Intent binder = new Intent(context, MyService.class);
        context.bindService(binder, conn, BIND_AUTO_CREATE);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MyService.class);
        context.startService(starter);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    private class LocalBinder extends IStudentManager.Stub {

        @Override
        public void addStudent(Student student) throws RemoteException {
            Log.d(TAG, "addStudent called, student is " + student);
        }

        @Override
        public List<Student> getStudent() throws RemoteException {
            return null;
        }

    }

}
