package com.wwttr.chat;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;
import com.wwttr.models.Message;
import com.wwttr.api.ApiError;
import com.wwttr.api.Code;

public class ChatHandlers extends Api.ChatService{
  private ChatService service;

  public ChatHandlers(ChatService service){
    this.service = service;
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
    //List<Message> allMessagesInGame = service.getAllMessagesInGame(request.getGameId());
    //TODO streaming

    service.streamMessage().forEach((Message m) -> {
      Api.Message.Builder builder = Api.Message.createBuilder();
      builder.setId(m.Id);
      callback(builder.build());
    });
  }




}
