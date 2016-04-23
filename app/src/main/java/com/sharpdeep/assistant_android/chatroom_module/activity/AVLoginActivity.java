package com.sharpdeep.assistant_android.chatroom_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.chatroom_module.AVImClientManager;
import com.sharpdeep.assistant_android.chatroom_module.Constants;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wli on 15/8/13.
 * 登陆页面，暂时未做自动登陆，每次重新进入都要再登陆一次
 */
public class AVLoginActivity extends AVBaseActivity {

  /**
   * 此处 xml 里限制了长度为 30，汉字算一个
   */
  @Bind(R.id.activity_login_et_username)
  protected EditText userNameView;

  @Bind(R.id.activity_login_btn_login)
  protected Button loginButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_login);
  }

  @OnClick(R.id.activity_login_btn_login)
  public void onLoginClick(View view) {
    openClient(userNameView.getText().toString().trim());
  }

  private void openClient(String selfId) {
    if (TextUtils.isEmpty(selfId)) {
      showToast(R.string.login_null_name_tip);
      return;
    }

    loginButton.setEnabled(false);
    userNameView.setEnabled(false);
    AVImClientManager.getInstance().open(selfId, new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
        loginButton.setEnabled(true);
        userNameView.setEnabled(true);
        if (filterException(e)) {
          Intent intent = new Intent(AVLoginActivity.this, AVSquareActivity.class);
          intent.putExtra(Constants.CONVERSATION_ID, Constants.SQUARE_CONVERSATION_ID);
          intent.putExtra(Constants.ACTIVITY_TITLE, getString(R.string.square_name));
          startActivity(intent);
          finish();
        }
      }
    });
  }
}
