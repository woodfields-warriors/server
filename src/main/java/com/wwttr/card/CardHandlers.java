package com.wwttr.card;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;


public class CardHandlers extends Api.CardService {

  public void getDestinationCard(RpcController controller, Api.GetDestinationCardRequest request, RpcCallback<Api.DestinationCard> callback) {

  }

  public void peekDestinationCards(RpcController controller, Api.PeekDestinationCardsRequest request, RpcCallback<Api.PeekDestinationCardsResponse> callback){

  }
  public void claimDestinationCards (RpcController controller, Api.ClaimDestinationCardsRequest request, RpcCallback<Api.Empty> callback){

  }
}
