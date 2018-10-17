package com.wwttr.card;

import com.wwttr.api.ApiError;
import com.wwttr.api.NotFoundException;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.*;

import java.util.List;

public class CardService {
  private DatabaseFacade df;
  private static CardService instance;

  private CardService(){
    df = DatabaseFacade.getInstance();
  }

  public static CardService getInstance(){
    if(instance == null)
      instance = new CardService();
    return instance;
  }

  public DestinationCard getDestinationCard(String destinationCardId) throws NotFoundException{
      return df.getDestinationCard(destinationCardId);
  }

  public List<DestinationCard> peekDestinationCards(String gameId){
    return df.listDestinationCards(3,gameId);
  }

  public void claimDesinationCards(List<String> destinationCardIds, String playerId) throws NotFoundException {
    for(String id : destinationCardIds){
      DestinationCard card = df.getDestinationCard(id);
      if(card.getPlayerId() != ""){
        df.updateDestinationCard(id,playerId);
      }
      else{
        throw ApiError(Code.INVALID_ARGUMENT,"card with id " + id + " has already been claimed");
      }
    }
  }
}
