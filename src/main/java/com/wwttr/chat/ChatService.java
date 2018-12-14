package com.wwttr.chat;

import com.wwttr.models.Message;
import com.wwttr.models.Player;
import java.util.Random;
import com.wwttr.database.DatabaseFacade;
import java.util.stream.*;


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

  public void addDelta(com.google.protobuf.Message request, String id, String gameId) {
    database.addDelta(request, id, gameId);
  }


  public Message createMessage(String content, String playerId) {
    int unixTime = (int) (System.currentTimeMillis() / 1000L);
    Player player = database.getPlayer(playerId);
    Message message = new Message("msg" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE),
                                  content,playerId,player.getGameId(),unixTime);
    database.addMessage(message);
    return message;
  }

  public Message getMessage(String messageId){
    Message message = database.getMessagebyId(messageId);
    return message;
  }

  public Stream<Message> streamMessages(String gameId) {
    return database.streamMessages(gameId);
  }
}
