package com.wwttr.chat;

import com.wwttr.models.Message;
import java.util.Random;
import com.wwttr.database.DatabaseFacade;

//singleton object
public class ChatService{

  private DatabaseFacade database;
  Random rn;

  //have chatListeners

  private static ChatService chatServiceInstance = null;

  public static ChatService getInstance(){
    if(chatServiceInstance == null){
      chatServiceInstance = new ChatService();
    }
    return chatServiceInstance;
  }
  private ChatService(){
    database = DatabaseFacade.getInstance();
    rn = new Random();
  }


  public Message createMessage(String content, String playerId) {
    int unixTime = (int) (System.currentTimeMillis() / 1000L);
    Message message = new Message("msg" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE),
                                  content,playerId,unixTime);
    database.addMessage(message);
    return message;
  }

  public Message getMessage(String messageId){
    Message message = database.getMessagebyId(messageId);
    return message;
  }
}
