package com.wwttr.card;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;
import com.wwttr.api.ApiError;
import com.wwttr.api.Code;
import com.wwttr.api.NotFoundException;
import com.wwttr.models.DestinationCard;


import com.wwttr.database.CommandQueue;
import java.util.List;
import java.util.stream.Stream;

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
      throw new ApiError(Code.INTERNAL,"");
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
      throw new ApiError(Code.INTERNAL,"");
    }
  }
  public void claimDestinationCards (RpcController controller, Api.ClaimDestinationCardsRequest request, RpcCallback<Api.Empty> callback){
    try{
      if(request.getDestinationCardIdsCount() == 0)
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'destination_card_ids' is required");
      if(request.getPlayerId().equals(""))
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'player_id' is required");
      service.claimDesinationCards(request.getDestinationCardIdsList(),request.getPlayerId());
    }
    catch (NotFoundException e) {
      throw new ApiError(Code.NOT_FOUND, "");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL,"");
    }
  }

  CommandQueue<Api.Route> routeQueue = new CommandQueue<Api.Route>();
  CommandQueue<Api.TrainCard> trainCardQueue = new CommandQueue<Api.TrainCard>();
  CommandQueue<Api.DestinationCard> destinationCardQueue = new CommandQueue<Api.DestinationCard>();
  CommandQueue<Api.DeckStats> deckStatsQueue = new CommandQueue<Api.DeckStats>();

  public void streamDestinationCards(RpcController controller, Api.StreamDestinationCardsRequest request, RpcCallback<Api.DestinationCard> callback) {
    destinationCardQueue.subscribe()
      .forEach((Api.DestinationCard card) -> {
        callback.run(card);
      });
  }

  public void streamDeckStats(RpcController controller, Api.StreamDeckStatsRequest request, RpcCallback<Api.DeckStats> callback) {
    deckStatsQueue.subscribe()
      .forEach((Api.DeckStats stats) -> {
        callback.run(stats);
      });
  }

  public void streamTrainCards(RpcController controller, Api.StreamTrainCardsRequest request, RpcCallback<Api.TrainCard> callback) {
    trainCardQueue.subscribe()
      .forEach((Api.TrainCard card) -> {
        callback.run(card);
      });
  }

  public void streamRoutes(RpcController controller, Api.StreamRoutesRequest request, RpcCallback<Api.Route> callback) {
    routeQueue.subscribe()
      .forEach((Api.Route route) -> {
        callback.run(route);
      });
  }
}
