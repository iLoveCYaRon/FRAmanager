package com.example.zmx.facerecognitionattendancemanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.VersionInfo;
import com.arcsoft.face.enums.RuntimeABI;
import com.example.zmx.facerecognitionattendancemanager.common.Constants;
import com.example.zmx.facerecognitionattendancemanager.faceserver.FaceServer;
import com.example.zmx.facerecognitionattendancemanager.fragment.HistoryFragment;
import com.example.zmx.facerecognitionattendancemanager.fragment.StuListFragment;
import com.example.zmx.facerecognitionattendancemanager.fragment.TimeSettingFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    //在线激活所需的权限
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    // Demo 所需的动态库文件
    boolean libraryExists = true;
    private static final String[] LIBRARIES = new String[]{
            // 人脸相关
            "libarcsoft_face_engine.so",
            "libarcsoft_face.so",
            // 图像库相关
            "libarcsoft_image_util.so",
    };


    //界面控件相关
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    private FragmentManager fragmentManager;


    //界面逻辑相关
    //用于判定主界面浮动按钮的功能是注册还是
    final int TRANSMIT = 0;
    final int REGISTER = 1;
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();
    private int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化fragmentManager
        fragmentManager = getSupportFragmentManager();

        //检测动态库是否存在 该库仅支持ARM架构
        libraryExists = checkSoFile(LIBRARIES);
        if (!libraryExists) {
            showToast("未找到动态库文件");
        }else {
            VersionInfo versionInfo = new VersionInfo();
            int code = FaceEngine.getVersion(versionInfo);
            Log.i(TAG, "onCreate: getVersion, code is: " + code + ", versionInfo is: " + versionInfo);
        }

        //初始化人脸识别引擎
        FaceServer.getInstance().init(this);

        //设置抽屉栏为顶栏
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.toolbar_menu);
        }

        //添加左侧抽屉布局
        drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navView = findViewById(R.id.nav_view);

        //设置抽屉中单项点击事件
        navView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();        //点击每个按钮后都后关闭抽屉
                //根据id判定点击的按钮
                switch (item.getItemId()) {
                    case R.id.nav_history:
                        floatingActionButton.setImageResource(R.mipmap.fab_signature);
                        currentIndex = 0;
                        break;

                    case R.id.nav_stu_list:
                        floatingActionButton.setImageResource(R.mipmap.fab_register);
                        currentIndex = 1;
                        break;

                    case R.id.nav_time_set:
                        currentIndex = 2;
                        break;

                    default:
                        break;
                }

                showFragment();

                return true;
            }
        });


        //FloatingActionButton悬浮按钮
        floatingActionButton = findViewById(R.id.fab_signature);
        floatingActionButton.setOnClickListener(this);

        if (savedInstanceState != null) {       //内存重启时调用

            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);

            //注意，添加顺序要跟下面添加的顺序一样！！！！
            fragments.clear();
            fragments.add(fragmentManager.findFragmentByTag(0 + ""));
            fragments.add(fragmentManager.findFragmentByTag(1 + ""));
            fragments.add(fragmentManager.findFragmentByTag(2 + ""));

            //恢复fragment页面
            restoreFragment();

        } else {      //正常启动时调用

            fragments.add(new HistoryFragment());
            fragments.add(new StuListFragment());
            fragments.add(new TimeSettingFragment());

            showFragment();
        }
    }

    //以下是fragment切换涉及到的逻辑
    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment() {

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (!fragments.get(currentIndex).isAdded()) {      //之前没有被添加过
            transaction
                    .hide(currentFragment)
                    .add(R.id.fragment, fragments.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag
        } else {
            transaction
                    .hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }

        currentFragment = fragments.get(currentIndex);

        transaction.commit();
    }


    /**
     * 恢复fragment
     */
    private void restoreFragment() {

        FragmentTransaction mBeginTransaction = fragmentManager.beginTransaction();


        for (int i = 0; i < fragments.size(); i++) {

            if (i == currentIndex) {
                mBeginTransaction.show(fragments.get(i));
            } else {
                mBeginTransaction.hide(fragments.get(i));
            }

        }

        mBeginTransaction.commit();

        //把当前显示的fragment记录下来
        currentFragment = fragments.get(currentIndex);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //“内存重启”时保存当前的fragment名字
        outState.putInt(CURRENT_FRAGMENT, currentIndex);
        super.onSaveInstanceState(outState);
    }


    //悬浮按钮响应
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        //考虑到可能有其他按钮 暂时保留switch结构
        switch (v.getId()) {
            //点击扫脸按钮的功能
            case R.id.fab_signature:
                Intent takePictureIntent = new Intent(
                        MainActivity.this, TakePhotoActivity.class);

                switch (currentIndex) {
                    case 0:
                        takePictureIntent.putExtra("request_flag", TRANSMIT);
                        break;
                    case 1:
                        //当目前处于学生列表Fragment时，启动拍照Activity传入一个参数，表示此次拍照为注册人脸
                        takePictureIntent.putExtra("request_flag", REGISTER);
                    default:
                        break;
                }

                startActivity(takePictureIntent);

                break;
            default:
                break;
        }
    }


    //设置toolbar样式，为menu中的toolbar.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //给toolbar中按钮添加功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                //设置的代码
                break;
            case android.R.id.home:
                //注意深坑：这里的R前面还有个android
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }



    //重写返回键的方法，按返回键后检查抽屉栏是否打开，若打开，关闭它；若未打开，正常的返回即可。
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //激活人脸识别引擎
    public void activeEngine(final View view) {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
        if (view != null) {
            view.setClickable(false);
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                RuntimeABI runtimeABI = FaceEngine.getRuntimeABI();
                Log.i(TAG, "subscribe: getRuntimeABI() " + runtimeABI);

                long start = System.currentTimeMillis();
                int activeCode = FaceEngine.activeOnline(MainActivity.this, Constants.APP_ID, Constants.SDK_KEY);
                Log.i(TAG, "subscribe cost: " + (System.currentTimeMillis() - start));
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            showToast("激活成功");
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            showToast("已经激活");
                        } else {
                            showToast("激活失败"+activeCode);
                        }

                        if (view != null) {
                            view.setClickable(true);
                        }
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = FaceEngine.getActiveFileInfo(MainActivity.this, activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        if (view != null) {
                            view.setClickable(true);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //检查库文件方法
    private boolean checkSoFile(String[] libraries) {
        File dir = new File(getApplicationInfo().nativeLibraryDir);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return false;
        }
        List<String> libraryNameList = new ArrayList<>();
        for (File file : files) {
            libraryNameList.add(file.getName());
        }
        boolean exists = true;
        for (String library : libraries) {
            exists &= libraryNameList.contains(library);
        }
        return exists;
    }

    @Override
    void afterRequestPermission(int requestCode, boolean isAllGranted) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
