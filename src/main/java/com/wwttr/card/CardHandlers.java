package com.wwttr.card;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;
import com.wwttr.api.ApiError;
import com.wwttr.api.Code;
import com.wwttr.api.NotFoundException;
import com.wwttr.models.DestinationCard;

import java.util.List;

import sun.net.httpserver.Code;


public class CardHandlers extends Api.CardService {

  private CardService service;

  public CardHandlers() {
    service = CardService.getInstance();
  }

  public void getDestinationCard(RpcController controller, Api.GetDestinationCardRequest request, RpcCallback<Api.DestinationCard> callback) {
    try{
      if(request.getDestinationCardId() == "")
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'destination_card_id' is required");
      DestinationCard result = service.getDestinationCard(request.getDestinationCardId());
      Api.DestinationCard.Builder builder = result.createBuilder();
      callback.run(builder.build());
    }
    catch (NotFoundException e) {
      throw new ApiError(Code.NOT_FOUND, "card not found");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL_ERROR,"");
    }
  }

  public void peekDestinationCards(RpcController controller, Api.PeekDestinationCardsRequest request, RpcCallback<Api.PeekDestinationCardsResponse> callback){
    try{
      if(request.getGameId() == "")
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'game_id' is required");
      List<DestinationCard> allCards = service.peekDestinationCards(request.getGameId());
      Api.PeekDestinationCardsResponse.Builder builder = Api.PeekDestinationCardsResponse.newBuilder();
      for(DestinationCard card : allCards){
        Api.DestinationCard.Builder cardBuilder = card.createBuilder();
        builder.addDestinationCards(cardBuilder.build());
      }
      callback.run(builder.build());
    }
    catch (NotFoundException e){
      throw new ApiError(Code.NOT_FOUND, "game_id not found");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL_ERROR,"");
    }
  }
  public void claimDestinationCards (RpcController controller, Api.ClaimDestinationCardsRequest request, RpcCallback<Api.Empty> callback){
    try{
      if(request.getDestinationCardIds() = "")
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'destination_card_ids' is required");
      if(request.getPlayerId() = "")
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'player_id' is required");
      service.claimDesinationCards(request.getDestinationCardIds(),request.getPlayerId());
    }
    catch (NotFoundException e) {
      throw new ApiError(Code.NOT_FOUND, e.getMessage());
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL_ERROR,"");
    }
  }
}
