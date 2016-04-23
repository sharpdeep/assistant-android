package com.sharpdeep.assistant_android.chatroom_module;

/**
 * Created by wli on 15/8/23.
 * 用来存放各种 static final 值
 */
public class Constants {

  private static final String LEANMESSAGE_CONSTANTS_PREFIX = "com.sharpdeep.assistant";

  public static final String MEMBER_ID = getPrefixConstant("member_id");
  public static final String CONVERSATION_ID = getPrefixConstant("conversation_id");

  public static final String ACTIVITY_TITLE = getPrefixConstant("activity_title");


  public static final String SQUARE_CONVERSATION_ID = "571b55e371cfe400615c1dea";

  private static String getPrefixConstant(String str) {
    return LEANMESSAGE_CONSTANTS_PREFIX + str;
  }
}
