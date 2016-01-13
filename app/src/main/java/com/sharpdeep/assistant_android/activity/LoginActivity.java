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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.model.AuthResult;
import com.sharpdeep.assistant_android.model.dbModel.User;
import com.sharpdeep.assistant_android.util.L;

import net.qiujuer.genius.Genius;
import com.pnikosis.*;
import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        Genius.initialize(getApplication());
    }

    private void init(){
        ButterKnife.bind(this); //butterknife init
        setSupportActionBar(mToolBar);
        L.init(); //logger init

        initAutoComplete();

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
                            //进入mainActivity(todo)
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
                                HashMap<String, String> userMap = new HashMap<String, String>();
                                userMap.put("username", username);
                                userMap.put("password", password);
                                Observable.just(userMap)
                                        .observeOn(Schedulers.io())
                                        .subscribe(new Action1<HashMap<String, String>>() {
                                                       @Override
                                                       public void call(HashMap<String, String> map) {
                                                           int len = User.find(User.class, "username = ?", username).size();
                                                           if (len == 0) {//不存在
                                                               User user = new User(map.get("username"), map.get("password"));
                                                               user.setIdentify(authResult.getIdentify());
                                                               user.setToken(authResult.getToken());
                                                               user.save();
                                                           } else if (len > 0) {//已存在，更新
                                                               User user = User.find(User.class, "username = ?", map.get("username")).get(0);
                                                               user.setPassword(map.get("password"));
                                                               user.setIdentify(authResult.getIdentify());
                                                               user.setToken(authResult.getToken());
                                                               user.save();
                                                           }
                                                       }
                                                   });
                            }else{//登陆失败
                                Toast.makeText(LoginActivity.this,authResult.getMsg(),Toast.LENGTH_SHORT).show();
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
}
