## Service 知多少

### 目标

需要理清的几个问题：

- Service 启动、绑定的完整流程；
- Service Crashed or Killed，客户端能否收到通知？
- 如何实现 Server, Client 双向通信？

### Android 中的 IPC

常见的几种 IPC 机制。管道、共享内存、Socket等。

#### 现状

![](https://ooo.0o0.ooo/2017/06/13/593f4425e9ff3.png)

#### Binder

Android 特有的 IPC 机制。Binder 通信采用 C/S 架构，从组件视角看：

![](http://gityuan.com/images/binder/prepare/IPC-Binder.jpg)

可以分为 Client、Server、ServiceManager 以及 Binder 驱动。其中 ServiceManager 用来管理系统中的各种服务，四大组件的启动和通信与 ServiceManager 密切相关。

从进程的角度看 IPC 机制：

![](http://gityuan.com/images/binder/prepare/binder_interprocess_communication.png)

每个Android的进程，只能运行在自己进程所拥有的虚拟地址空间。对应一个4GB的虚拟地址空间，其中3GB是用户空间，1GB是内核空间。
对于用户空间，不同进程之间彼此是不能共享的，而内核空间却是可共享的。Client进程向Server进程通信，恰恰是利用进程间可共享的内核内存空间来完成底层通信工作的。
Client端与Server端进程往往采用ioctl等方法跟内核空间的驱动进行交互。

从派生关系上看：

![](http://gityuan.com/images/binder/prepare/Ibinder_classes.jpg)

BpBinder(客户端)和BBinder(服务端)都是Android中Binder通信相关的代表，它们都从IBinder类中派生而来。

- Client端：BpBinder.transact()来发送事务请求；
- Server端：BBinder.onTransact()会接收到相应事务。

### 认识 Binder
；
自己编写一个 AIDL 例子，帮助理解。

#### 创建实体类

```
public class Student {

    private String mName;
    private int mAge;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }
}

```

#### 创建 AIDL 文件

Android 提供 AIDL 创建模板，通过模板可以方便的创建出相关的 AIDL 类。

Student.aidl

```
// Book.aidl
package com.aaron.servicecomponent;

// Declare any non-default types here with import statements

parcelable Student;

```

IStudentManager.aidl

```
// IStudentManager.aidl
package com.aaron.servicecomponent;

// Declare any non-default types here with import statements
import com.aaron.servicecomponent.Student;

interface IStudentManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void addStudent(in Student student);

    List<Student> getStudent();
}

```

> 阅读模板为我们生成的代码注释。

除了基本数据类型参数，其他类型的参数需要额外标上方向信息：in、out 或者 inout。in 标识输入型参数，out 表示输出型参数，inout 表示输入输出型参数。实际应用中，需要根据实际需求指定参数类型。

#### 创建 Server Service

同样使用模板，创建一个承担 Server 角色的 Service 类，命名为 MyService。在 MyService 中，实现 IStudentManager.Stub 抽象类，并作为 Binder 返回给客户端调用。

```
public class MyService extends Service {

    private static final String TAG = "MyService";

    private IBinder mBinder = new LocalBinder();

    public MyService() {
    }

    public static void bind(Context context, ServiceConnection conn) {
        Intent binder = new Intent(context, MyService.class);
        context.bindService(binder, conn, BIND_AUTO_CREATE);
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
```

#### 客户端绑定服务

```
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mStudentManager = IStudentManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
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
        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyService.bind(MainActivity.this, mConn);
            }
        });
    }
}
```

#### 调用

```
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
```

#### 理解 IStudentManager 类

##### 客户端获取管理类接口

```
public static com.aaron.servicecomponent.IStudentManager asInterface(android.os.IBinder obj) {
    if ((obj == null)) {
        return null;
    }
    android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
    if (((iin != null) && (iin instanceof com.aaron.servicecomponent.IStudentManager))) {
        return ((com.aaron.servicecomponent.IStudentManager) iin);
    }
    return new com.aaron.servicecomponent.IStudentManager.Stub.Proxy(obj);
}
```

- 相同进程
- 不同进程

如果客户端与服务端不在同一个进程，管理类接口的实现是交给 IStudentManager.Stub.Proxy(obj) 代理类完成。

##### 调用流程

![](https://ooo.0o0.ooo/2017/06/13/593f5d14a3826.png)

### 四大组件之 Service

### Service 启动流程

```
public static void start(Context context) {
    Intent starter = new Intent(context, MyService.class);
    context.startService(starter);
}
```

![](https://ooo.0o0.ooo/2017/06/16/5943a7f0258f8.png)

### Service 绑定流程



### 结论

### 参考

- Android 开发艺术探索[任玉刚]
- [Gityuan 博客](http://gityuan.com/)