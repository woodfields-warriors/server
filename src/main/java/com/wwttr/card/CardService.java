package com.wwttr.card;

import com.wwttr.api.ApiError;
import com.wwttr.api.NotFoundException;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.*;
import com.wwttr.api.Code;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardService {
  private DatabaseFacade df;
  private static CardService instance;
  private Random rn = new Random();
  private ArrayList<Triplet<String,String,Integer>> fullDeckTemplate = new ArrayList<>();
  private final String[] FIRST_CITIES = {"Los Angeles", "Duluth", "Sault Ste Marie", "New York", "Portland", "Vancouver", "Duluth", "Toronto", "Portland", "Dallas",
                                          "Calgary", "Calgary", "Los Angeles", "Winnipeg", "San Francisco", "Kansas City", "Los Angeles", "Denver", "Chicago", "Vancouver",
                                          "Boston", "Chicago", "Montreal", "Seattle", "Denver", "Helena", "Winnipeg", "Montreal", "Sault Ste Marie", "Seattle"};
  private final String[] SECOND_CITIES = {"New York City", "Houston", "Nashville", "Atlanta", "Nashville", "Montreal", "El Paso", "Miami", "Phoenix", "New York City",
                                          "Salt Lake City", "Phoenix", "Miami", "Little Rock", "Atlanta", "Houston", "Chicago", "Pittsburgh", "Santa Fe", "Santa Fe",
                                          "Miami", "New Orleans", "Atlanta", "New York", "El Paso", "Los Angeles", "Houston", "New Orleans", "Oklahoma City", "Los Angeles"};
  private final Integer[] POINTS = {21,8,8,6,17,20,10,10,11,11,7,13,20,11,17,5,16,11,9,13,12,7,9,22,4,8,12,13,9,9};
  private CardService(){
    df = DatabaseFacade.getInstance();
    generateDeckTemplate(FIRST_CITIES,SECOND_CITIES,POINTS);
  }

  public static CardService getInstance(){
    if(instance == null)
      instance = new CardService();
    return instance;
  }

  void generateDeckTemplate(String[] firstCities, String[] secondCities, Integer[] points){
    if(firstCities.length != secondCities.length || secondCities.length != points.length)
      throw new IllegalArgumentException("lengths of Deck arrays do not match");
    fullDeckTemplate.clear();
    for(int i = 0; i < firstCities.length; i++){
      Triplet<String, String, Integer> tempTriplet = new Triplet(firstCities[i],secondCities[i],points[i]);
      fullDeckTemplate.add(tempTriplet);
    }
  }

  public void createFullDeckForGame(String gameId) throws NotFoundException{
    if(df.getGame(gameId) == null){
      throw new NotFoundException("game with id " + gameId + "not found");
    }
    List<DestinationCard> cardList = new ArrayList<>();
    for(Triplet<String,String,Integer> tempTriplet : fullDeckTemplate){
      String newId = "destCard" + rn.nextInt();
      while(df.getDestinationCard(newId) != null){
        newId = "destCard" + rn.nextInt();
      }
      DestinationCard tempCard = new DestinationCard(newId, tempTriplet.getFirst(),tempTriplet.getSecond(),tempTriplet.getThird(),"",gameId);
      cardList.add(tempCard);
    }
    df.addDestinationCardDeck(cardList);
  }

  public DestinationCard getDestinationCard(String destinationCardId) throws NotFoundException{
      return df.getDestinationCard(destinationCardId);
  }

  public List<DestinationCard> peekDestinationCards(String gameId)throws NotFoundException{
    if(df.getGame(gameId) == null){
      throw new NotFoundException("game with id " + gameId + " not found");
    }
    return df.listDestinationCards(3,gameId);
  }

  public void claimDesinationCards(List<String> destinationCardIds, String playerId) throws NotFoundException {
    if(df.getPlayer(playerId) == null)
      throw new NotFoundException("player with id" + playerId + " not found");
    for(String id : destinationCardIds){
      DestinationCard card = df.getDestinationCard(id);
      if(card.getPlayerId() != ""){
        card.setPlayerId(playerId);
        df.updateDestinationCard(card);
      }
      else{
        throw new ApiError(Code.INVALID_ARGUMENT,"card with id " + id + " has already been claimed");
      }
    }
  }
}

class Triplet<T,U,V>{
  private final T first;
  private final U second;
  private final V third;

  public Triplet(T first, U second, V third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public T getFirst() { return first; }
  public U getSecond() { return second; }
  public V getThird() { return third; }
}
