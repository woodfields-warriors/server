package com.wwttr.chat;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;
import com.wwttr.models.Message;
import com.wwttr.api.ApiError;
import com.wwttr.api.Code;
import java.util.stream.*;

public class ChatHandlers extends Api.ChatService{
  private ChatService service;

  public ChatHandlers(ChatService service){
    this.service = service;
  }

  // calls addDelta method in ChatService after determining gameId
    public void addDelta(RpcController controller, Message request, RpcCallback<Api.Empty> callback) {
      Controller controllerWrapper = (Controller) controller;
      String id = controller.getId();
      String gameId;
  
      if (request instanceof Api.CreateMessageRequest) {
        Player p = gameService.getPlayer(request.getPlayerId());
        gameId = p.getGameId();
      }
  
      service.addDelta(request, id, gameId);
  
      Api.Empty.Builder toReturn = Api.Empty.newBuilder();
      callback.run(toReturn.build());
    }

  public void createMessage(RpcController controller, Api.CreateMessageRequest request, RpcCallback<Api.Message> callback){
    if (request.getContent().equals("")){
      throw new ApiError(Code.INVALID_ARGUMENT, "Argument 'content' is required");
    }
    if(request.getPlayerId().equals("")){
      throw new ApiError(Code.INVALID_ARGUMENT, "Argument 'playerId' is required");
    }

    Message message = service.createMessage(request.getContent(), request.getPlayerId());
    Api.Message.Builder builder = message.createBuilder();
    callback.run(builder.build());
  }

  public void getMessage(RpcController controller, Api.GetMessageRequest request, RpcCallback<Api.Message> callback){
    if(request.getMessageId().equals("")){
      throw new ApiError(Code.INVALID_ARGUMENT, "Argument 'message_id' is required");
    }
    Message message = service.getMessage(request.getMessageId());
    Api.Message.Builder builder = message.createBuilder();
    callback.run(builder.build());
  }

  public void streamMessages(RpcController controller, Api.StreamMessagesRequest request, RpcCallback<Api.Message> callback){
      Stream<Message> messages = service.streamMessages(request.getGameId());
      messages.forEach((Message m) -> {
      Api.Message.Builder builder = m.createBuilder();
      callback.run(builder.build());
    });
  }




}
