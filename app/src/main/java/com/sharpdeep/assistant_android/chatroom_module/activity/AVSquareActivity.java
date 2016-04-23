package com.sharpdeep.assistant_android.chatroom_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.chatroom_module.AVImClientManager;
import com.sharpdeep.assistant_android.chatroom_module.Constants;
import com.sharpdeep.assistant_android.chatroom_module.event.LeftChatItemClickEvent;
import com.sharpdeep.assistant_android.chatroom_module.fragment.ChatFragment;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.util.ToastUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by wli on 15/8/13.
 * 广场页面，即群组聊天页面
 *
 * 1、根据 clientId 获得 AVIMClient 实例
 * 2、根据 conversationId 获得 AVIMConversation 实例
 * 3、必须要加入 conversation 后才能拉取消息
 */
public class AVSquareActivity extends AVBaseActivity {

  private AVIMConversation squareConversation;
  private ChatFragment chatFragment;
  private Toolbar toolbar;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_square);

    String conversationId = getIntent().getStringExtra(Constants.CONVERSATION_ID);
    String title = getIntent().getStringExtra(Constants.ACTIVITY_TITLE);

    chatFragment = (ChatFragment)getFragmentManager().findFragmentById(R.id.fragment_chat);
    toolbar = (Toolbar) findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);
    setTitle(title);

    getSquare(conversationId);
    queryInSquare(conversationId);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_square, menu);
    return true;
  }

  /**
   * 根据 conversationId 查取本地缓存中的 conversation，如若没有缓存，则返回一个新建的 conversaiton
   */
  private void getSquare(String conversationId) {
    if (TextUtils.isEmpty(conversationId)) {
      throw new IllegalArgumentException("conversationId can not be null");
    }

    AVIMClient client = AVImClientManager.getInstance().getClient();
    if (null != client) {
      squareConversation = client.getConversation(conversationId);
    } else {
      finish();
      showToast("Please call AVIMClient.open first!");
    }
  }

  /**
   * 加入 conversation
   */
  private void joinSquare() {
    squareConversation.join(new AVIMConversationCallback() {
      @Override
      public void done(AVIMException e) {
        if (filterException(e)) {
          chatFragment.setConversation(squareConversation);
        }else{
          L.d("conversation is -->"+squareConversation.toString());
        }
      }
    });
  }

  /**
   * 先查询自己是否已经在该 conversation，如果存在则直接给 chatFragment 赋值，否则先加入，再赋值
   */
  private void queryInSquare(String conversationId) {
    final AVIMClient client = AVImClientManager.getInstance().getClient();
    AVIMConversationQuery conversationQuery = client.getQuery();
    conversationQuery.whereEqualTo("objectId", conversationId);
    conversationQuery.containsMembers(Arrays.asList(AVImClientManager.getInstance().getClientId()));
    conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
      @Override
      public void done(List<AVIMConversation> list, AVIMException e) {
        if (filterException(e)) {
          if (null != list && list.size() > 0) {
            chatFragment.setConversation(list.get(0));
          } else {
            joinSquare();
          }
        }
      }
    });
  }

  /**
   * 处理聊天 item 点击事件，点击后跳转到相应1对1的对话
   */
  public void onEvent(LeftChatItemClickEvent event) {
    Intent intent = new Intent(this, AVSingleChatActivity.class);
    intent.putExtra(Constants.MEMBER_ID, event.userId);
    startActivity(intent);
  }

  @Override
  protected void onResume() {
    super.onResume();
    AVAnalytics.onResume(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    AVAnalytics.onPause(this);
  }
}
