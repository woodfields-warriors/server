package com.wwttr.card;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;
import com.wwttr.api.ApiError;
import com.wwttr.api.Code;
import com.wwttr.api.NotFoundException;
import com.wwttr.models.DeckStats;
import com.wwttr.models.DestinationCard;
import com.wwttr.models.Player;
import com.wwttr.game.Api.Empty;
import com.wwttr.game.GameService;

import com.wwttr.models.TrainCard;

import java.util.List;
import java.util.stream.Stream;

public class CardHandlers extends Api.CardService {

  private CardService service;
  private GameService gameService;

  public CardHandlers() {
    service = CardService.getInstance();
    gameService = GameService.getInstance();

  }
  //--------------Destination Card Functions-----------------//
  public void getDestinationCard(RpcController controller, Api.GetDestinationCardRequest request, RpcCallback<Api.DestinationCard> callback) {
    try{
      if(request.getDestinationCardId().equals(""))
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
      if(request.getPlayerId().equals(""))
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'player_id' is required");

      Player p = gameService.getPlayer(request.getPlayerId());
      if (p == null) {
        throw new ApiError(Code.NOT_FOUND, "");
      }
      if (!p.getPlayerState().canPeek()) {
        throw new ApiError(Code.FAILED_PRECONDITION, "It must be your turn to peek destination cards");
      }
      
      List<DestinationCard> allCards = service.peekDestinationCards(p.getGameId());
      Api.PeekDestinationCardsResponse.Builder builder = Api.PeekDestinationCardsResponse.newBuilder();
      for(DestinationCard card : allCards){
        Api.DestinationCard.Builder cardBuilder = card.createBuilder();
        builder.addDestinationCards(cardBuilder.build());
      }
      callback.run(builder.build());
    }
    catch (NotFoundException e){
      throw new ApiError(Code.NOT_FOUND, e.getMessage());
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL,"");
    }
  }
  public void claimDestinationCards (RpcController controller, Api.ClaimDestinationCardsRequest request, RpcCallback<Api.ClaimDestinationCardsResponse> callback){
    try{
      if(request.getDestinationCardIdsCount() == 0)
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'destination_card_ids' is required");
      if(request.getPlayerId().equals(""))
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'player_id' is required");
      gameService.drawDestinationCards(request.getPlayerId(),request.getDestinationCardIdsList());
    }
    catch (NotFoundException e) {
      throw new ApiError(Code.NOT_FOUND, "");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL,"");
    }

    callback.run(Api.ClaimDestinationCardsResponse.newBuilder().build());
  }

  public void streamDestinationCards(RpcController controller, Api.StreamDestinationCardsRequest request, RpcCallback<Api.DestinationCard> callback) {
    try {
      service.streamDestinationCards(request.getPlayerId()).forEach((DestinationCard card) -> {
        Api.DestinationCard.Builder builder = card.createBuilder();
        /*
        Api.DestinationCard.Builder builder = Api.DestinationCard.newBuilder();
        builder.setId(card.getId());
        builder.setFirstCityId(card.getFirstCityId());
        builder.setSecondCityId(card.getSecondCityId());
        builder.setPointValue(card.getPointValue());
        builder.setPlayerId(card.getPlayerId());*/
        callback.run(builder.build());
      });
    }
    catch (NotFoundException e) {
      throw new ApiError(Code.NOT_FOUND, "player " + request.getPlayerId() + " not found");
    }
  }

  //--------------Train Card Functions----------------//
  public void drawTrainCardFromDeck(RpcController controller, Api.DrawTrainCardFromDeckRequest request, RpcCallback<Empty> callback){
    try{
      //id here is player Id
      if(request.getId().equals("")){
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'id' is required");
      }
      gameService.drawTrainCard(request.getId());
    }
    catch (NotFoundException e){
      throw new ApiError(Code.NOT_FOUND,"");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL,"");
    }
  }
  public void drawFaceUpTrainCard(RpcController controller, Api.DrawFaceUpTrainCardRequest request, RpcCallback<Empty> callback) {
    try{
      if(request.getId().equals("")){
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'id' is required");
      }
      if(request.getCardDrawnId().equals("")){
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'card_drawn_id' is required");
      }
      gameService.drawFaceUpTrainCard(request.getId(),request.getCardDrawnId());
    }
    catch (NotFoundException e){
      throw new ApiError(Code.NOT_FOUND,"");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL,"");
    }
  }
  public void getTrainCardsInHand(RpcController controller, Api.GetTrainCardsInHandRequest request, RpcCallback<Api.GetTrainCardsInHandResponse> callback){
    try{
      if(request.getId().equals("")){
        throw new ApiError(Code.INVALID_ARGUMENT, "argument 'id' is required");
      }
      List<TrainCard> cards = service.getTrainCardsInHand(request.getId());
      Api.GetTrainCardsInHandResponse.Builder builder = Api.GetTrainCardsInHandResponse.newBuilder();
      for(TrainCard card : cards){
        Api.TrainCard.Builder cardBuilder = card.createBuilder();
        builder.addCards(cardBuilder.build());
      }
      callback.run(builder.build());
    }
    catch (NotFoundException e){
      throw new ApiError(Code.NOT_FOUND,"");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL,"");
    }
  }

  public void streamTrainCards(RpcController controller, Api.StreamTrainCardsRequest request, RpcCallback<Api.TrainCard> callback) {
    try {
      Stream<TrainCard> trainCards = service.streamTrainCards(request.getPlayerId());
      trainCards.forEach((TrainCard tc) -> {
        Api.TrainCard.Builder builder = tc.createBuilder();
        callback.run(builder.build());
      });
    }
    catch (NotFoundException e){
      throw new ApiError(Code.NOT_FOUND,"");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL,"");
    }
  }

  public void streamDeckStats(RpcController controller, Api.StreamDeckStatsRequest request, RpcCallback<Api.DeckStats> callback) {
    try {
      service.streamDeckStats(request.getGameId())
          .forEach((DeckStats stats) -> {
            Api.DeckStats.Builder builder = stats.createBuilder();
            callback.run(builder.build());
          });
    }
    catch (NotFoundException e){
      throw new ApiError(Code.NOT_FOUND,"");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL,"");
    }
  }




  //----------------Temporary Functionality For Passoff: Need to Deprecate------------------//
/*
  CommandQueue<Api.Route> routeQueue = new CommandQueue<Api.Route>();
  CommandQueue<Api.TrainCard> trainCardQueue = new CommandQueue<Api.TrainCard>();
  CommandQueue<Api.DeckStats> deckStatsQueue = new CommandQueue<Api.DeckStats>();



  public void streamDeckStats(RpcController controller, Api.StreamDeckStatsRequest request, RpcCallback<Api.DeckStats> callback) {
    deckStatsQueue.subscribe()
      .forEach((Api.DeckStats stats) -> {
        callback.run(stats);
      });
  }

  boolean deckStatsState;
  void toggleDeckStats() {
    deckStatsState = !deckStatsState;
    Api.DeckStats.Builder builder = Api.DeckStats.newBuilder();

    if (deckStatsState) {
      builder.setHiddenTrainCardCount(100);
      builder.setHiddenDestinationCardCount(100);
      deckStatsQueue.publish(builder.build());
    } else {
      builder.setHiddenTrainCardCount(20);
      builder.setHiddenDestinationCardCount(30);
      deckStatsQueue.publish(builder.build());
    }
  }



  public void claimTrainCard(RpcController controller, Api.ClaimTrainCardRequest request, RpcCallback<Api.ClaimTrainCardResponse> callback) {
    toggleTrainCards();
    callback.run(Api.ClaimTrainCardResponse.newBuilder().build());
  }

  boolean trainCardState;

  void toggleTrainCards() {
    toggleDeckStats();
    trainCardState = !trainCardState;
    Api.TrainCard.Builder builder = Api.TrainCard.newBuilder();

    if (trainCardState) {
      builder.setId("trainCard1");
      builder.setColor(Api.TrainColor.RED);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard2");
      builder.setColor(Api.TrainColor.BLUE);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard3");
      builder.setColor(Api.TrainColor.RED);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard4");
      builder.setColor(Api.TrainColor.BLUE);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard5");
      builder.setColor(Api.TrainColor.WHITE);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard6");
      builder.setColor(Api.TrainColor.GREEN);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard7");
      builder.setColor(Api.TrainColor.GREEN);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard8");
      builder.setColor(Api.TrainColor.YELLOW);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard9");
      builder.setColor(Api.TrainColor.YELLOW);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard10");
      builder.setColor(Api.TrainColor.ORANGE);
      builder.setState(Api.TrainCard.State.HIDDEN);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard11");
      builder.setColor(Api.TrainColor.PINK);
      builder.setState(Api.TrainCard.State.HIDDEN);
      trainCardQueue.publish(builder.build());
    } else {
      builder.setId("trainCard1");
      builder.setColor(Api.TrainColor.RED);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard2");
      builder.setColor(Api.TrainColor.BLUE);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard3");
      builder.setColor(Api.TrainColor.RED);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard4");
      builder.setColor(Api.TrainColor.BLUE);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard5");
      builder.setColor(Api.TrainColor.WHITE);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard6");
      builder.setColor(Api.TrainColor.GREEN);
      builder.setState(Api.TrainCard.State.OWNED);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard7");
      builder.setColor(Api.TrainColor.GREEN);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard8");
      builder.setColor(Api.TrainColor.YELLOW);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard9");
      builder.setColor(Api.TrainColor.YELLOW);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard10");
      builder.setColor(Api.TrainColor.ORANGE);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
      builder.setId("trainCard11");
      builder.setColor(Api.TrainColor.PINK);
      builder.setState(Api.TrainCard.State.VISIBLE);
      trainCardQueue.publish(builder.build());
    }


  }

  public void streamRoutes(RpcController controller, Api.StreamRoutesRequest request, RpcCallback<Api.Route> callback) {
    routeQueue.subscribe()
      .forEach((Api.Route route) -> {
        callback.run(route);
      });
  }

  public void claimRoute(RpcController controller, Api.ClaimRouteRequest request, RpcCallback<Api.ClaimRouteResponse> callback) {
    toggleRoutes();
    callback.run(Api.ClaimRouteResponse.newBuilder().build());
  }

  boolean routeState;

  void toggleRoutes() {
    routeState = !routeState;
    Api.Route.Builder builder = Api.Route.newBuilder();

    if (routeState) {
      builder.setId("route1");
      builder.setFirstCityId("city1");
      builder.setSecondCityId("city2");
      builder.setColor(Api.TrainColor.RED);
      builder.setPlayerId("");
      routeQueue.publish(builder.build());
      builder.setId("route2");
      builder.setFirstCityId("city1");
      builder.setSecondCityId("city3");
      builder.setColor(Api.TrainColor.BLUE);
      builder.setPlayerId("");
      routeQueue.publish(builder.build());
      builder.setId("route3");
      builder.setFirstCityId("city2");
      builder.setSecondCityId("city3");
      builder.setColor(Api.TrainColor.GREEN);
      builder.setPlayerId("");
      routeQueue.publish(builder.build());
    } else {
      builder.setId("route1");
      builder.setFirstCityId("city1");
      builder.setSecondCityId("city2");
      builder.setColor(Api.TrainColor.RED);
      builder.setPlayerId("player1");
      routeQueue.publish(builder.build());
      builder.setId("route2");
      builder.setFirstCityId("city1");
      builder.setSecondCityId("city3");
      builder.setColor(Api.TrainColor.BLUE);
      builder.setPlayerId("player1");
      routeQueue.publish(builder.build());
      builder.setId("route3");
      builder.setFirstCityId("city2");
      builder.setSecondCityId("city3");
      builder.setColor(Api.TrainColor.GREEN);
      builder.setPlayerId("player1");
      routeQueue.publish(builder.build());
    }
  }*/
}
