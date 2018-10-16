package com.wwttr.card;

import com.wwttr.database.DatabaseFacade;

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

  public DestinationCard getDestinationCard(String destinationCardId){

  }

  public List<DestinationCard> peekDestinationCards(){

  }

  public void claimDesinationCards(List<String> destinationCardIds, String playerId){

  }
}
