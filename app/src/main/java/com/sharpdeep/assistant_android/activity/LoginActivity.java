package com.sharpdeep.assistant_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.model.resultModel.AuthResult;
import com.sharpdeep.assistant_android.model.dbModel.AppInfo;
import com.sharpdeep.assistant_android.model.dbModel.User;
import com.sharpdeep.assistant_android.util.AndroidUtil;
import com.sharpdeep.assistant_android.util.L;

import net.qiujuer.genius.Genius;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity {


    @Bind(R.id.account)
    AutoCompleteTextView mAccountView;
    @Bind(R.id.txt_password)
    EditText mPasswdView;
    @Bind(R.id.sign_in_button)
    Button mSignInBtn;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.progress_login)
    View mProgress;
    Boolean isAuth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this); //butterknife init
        setSupportActionBar(mToolBar);
        Genius.initialize(getApplication());

        getAuthTimeObservable().subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                long now = new Date().getTime()/1000;
                if (now - aLong <= Constant.EXPRIED){//有效
                    startActivityAndFinsh(MainActivity.class);
                }else{
                    init();
                }
            }
        });
        //判断网络连接情况(todo)
    }

    private void init(){
        L.init(); //logger init
        initAutoComplete();
    }

    private Observable<Long> getAuthTimeObservable(){
        long now = new Date().getTime()/1000;
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                List<AppInfo> infoList = AppInfo.listAll(AppInfo.class);
                if(infoList.size() > 0){
                    subscriber.onNext(infoList.get(0).getAuthTime());
                }else if(infoList.size() == 0){
                    subscriber.onNext(0l);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @OnClick(R.id.sign_in_button)
    void signIn(){
        final String username = mAccountView.getText().toString();
        final String password = mPasswdView.getText().toString();
        if (username.isEmpty()) {
            mAccountView.setError("请输入校园网账号！");
        } else if (password.isEmpty()) {
            mPasswdView.setError("请输入密码！");
        } else {
            final Retrofit retrofit = RetrofitHelper.getRetrofit(LoginActivity.this);
            retrofit.create(AssistantService.class)
                    .authUser(username,password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() { //开始网络操作前
                        @Override
                        public void call() {
                            mSignInBtn.setEnabled(false);
                            mProgress.setVisibility(View.VISIBLE);
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AuthResult>() {
                        @Override
                        public void onCompleted() {
                            L.d("completed!");
                            mSignInBtn.setEnabled(true);
                            mProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            L.d("internet error!%s", e.toString());
                        }

                        @Override
                        public void onNext(final AuthResult authResult) {
                            L.d(authResult.getStatus() + "-->" + authResult.getMsg());
                            L.d(authResult.getIdentify() + " token is:" + authResult.getToken());
                            if (authResult.isSuccess()) { //成功了更新或者保存密码
                                L.d("成功登陆");
                                L.d("开始缓存数据...");
                                //两次数据库操作可以转移到MainActivity后台执行(todo)
                                //save user'data
                                int len = User.find(User.class, "username = ?", username).size();
                                User user = null;
                                if (len == 0) {//不存在
                                    L.d("数据库中没有该用户");
                                    user = new User(username, password);
                                    user.setIdentify(authResult.getIdentify());
                                    user.setToken(authResult.getToken());
                                    user.save();
                                } else if (len > 0) {//已存在，更新
                                    L.d("更新用户");
                                    user = User.find(User.class, "username = ?", username).get(0);
                                    user.setPassword(password);
                                    user.setIdentify(authResult.getIdentify());
                                    user.setToken(authResult.getToken());
                                    user.save();
                                }
                                //set current user
                                List<AppInfo> infoList = AppInfo.listAll(AppInfo.class);
                                AppInfo info;
                                if(infoList.size() == 0){//之前没有
                                    info = new AppInfo();
                                    info.setCurrentUser(user);
                                    info.setAuthTime(new Date().getTime()/1000);
                                    info.save();
                                }else if(infoList.size() > 0){
                                    info = infoList.get(0);
                                    info.setCurrentUser(user);
                                    info.setAuthTime(new Date().getTime()/1000);
                                    info.save();
                                }
                                startActivityAndFinsh(MainActivity.class);
                            }else{//登陆失败
                                Toast.makeText(LoginActivity.this, authResult.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void initAutoComplete() {
        Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                List<User> users = User.listAll(User.class);
                subscriber.onNext(users);
            }
        })
                .subscribeOn(Schedulers.io()) //查询数据库在io线程中
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<User>>() {
                    @Override
                    public void call(final List<User> users) {
                        if (users.size() > 0) { //数据库存在缓存用户
                            List<String> strUserList = new ArrayList<String>();
                            String[] strUsers;
                            for (User u : users) {
                                strUserList.add(u.getUsername());
                            }
                            strUsers = strUserList.toArray(new String[users.size()]);
                            ArrayAdapter adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line, strUsers);
                            mAccountView.setAdapter(adapter);
                            mAccountView.setThreshold(1);
                            mAccountView.setOnFocusChangeListener(new View.OnFocusChangeListener() { //当获取焦点时,自动提示
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    AutoCompleteTextView view = (AutoCompleteTextView) v;
                                    if (hasFocus) {
                                        view.showDropDown();
                                    }
                                }
                            });
                            mAccountView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != -1){
                                        mPasswdView.setText(users.get(position).getPassword());
                                        L.d(users.get(position).getPassword());
                                    }
                                }
                            });
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Genius.dispose();
    }

    private void startActivityAndFinsh(Class to){
        AndroidUtil.startActivity(this, to);
        this.finish();
    }
}
