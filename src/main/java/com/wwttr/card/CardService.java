package com.wwttr.card;

import com.wwttr.api.ApiError;
import com.wwttr.api.NotFoundException;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.*;
import com.wwttr.api.Code;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class CardService {
  private DatabaseFacade df;
  private static CardService instance;
  private Random rn = new Random();



  private CardService(){
    df = DatabaseFacade.getInstance();
    setDefaultTemplates();
  }

  public static CardService getInstance(){
    if(instance == null)
      instance = new CardService();
    return instance;
  }

  // General Functions --------------------------------------------------

  void setDefaultTemplates(){
    generateDestinationDeckTemplate(FIRST_CITIES,SECOND_CITIES,POINTS);
    generateTrainCardDeckTemplate(TRAIN_COLORS,TRAIN_STATES);
  }

  public void createFullDecksForGame(String gameId) throws NotFoundException{
    if(df.getGame(gameId) == null){
      throw new NotFoundException("game with id " + gameId + " not found");
    }
    List<DestinationCard> cardList = new ArrayList<>();
    for(DestinationTemplate<String,String,Integer> tempDestinationTemplate : fullDestinationDeckTemplate){
      String newId = "destCard" + rn.nextInt();
      try {
        while (df.getDestinationCard(newId) != null) {
          newId = "destCard" + rn.nextInt();
        }
      }
      catch (NotFoundException e){

      }
      DestinationCard tempCard = new DestinationCard(newId, tempDestinationTemplate.getFirst(), tempDestinationTemplate.getSecond(), tempDestinationTemplate.getThird(),"",gameId);
      cardList.add(tempCard);
    }
    List<TrainCard> trainCardList = new ArrayList<>();
    for(TrainCardTemplate<TrainCard.Color,TrainCard.State> tempTrainCardTemplate : fullTrainCardDeckTemplate){
      String newId = "trainCard" + rn.nextInt();
      try{
        while(df.getTrainCard(newId) != null){
          newId = "trainCard" + rn.nextInt();
        }
      }
      catch (NotFoundException e){

      }
      TrainCard tempCard = new TrainCard(newId,gameId,"",tempTrainCardTemplate.getColor(),tempTrainCardTemplate.getState());
      trainCardList.add(tempCard);
    }
    for(int i = 0; i < 4; i++){
      TrainCard tempCard = trainCardList.at(rn.nextInt(trainCardList.size()));
      tempCard.setState(TrainCard.State.VISIBLE);
    }
    df.addDestinationCardDeck(cardList);
    df.addTrainCardDeck(trainCardList);
  }

  // Destination Card Functions -------------------------------------------

  private ArrayList<DestinationTemplate<String,String,Integer>> fullDestinationDeckTemplate = new ArrayList<>();
  private final String[] FIRST_CITIES = {"Los Angeles", "Duluth", "Sault Ste Marie", "New York", "Portland", "Vancouver", "Duluth", "Toronto", "Portland", "Dallas",
      "Calgary", "Calgary", "Los Angeles", "Winnipeg", "San Francisco", "Kansas City", "Los Angeles", "Denver", "Chicago", "Vancouver",
      "Boston", "Chicago", "Montreal", "Seattle", "Denver", "Helena", "Winnipeg", "Montreal", "Sault Ste Marie", "Seattle"};
  private final String[] SECOND_CITIES = {"New York City", "Houston", "Nashville", "Atlanta", "Nashville", "Montreal", "El Paso", "Miami", "Phoenix", "New York City",
      "Salt Lake City", "Phoenix", "Miami", "Little Rock", "Atlanta", "Houston", "Chicago", "Pittsburgh", "Santa Fe", "Santa Fe",
      "Miami", "New Orleans", "Atlanta", "New York", "El Paso", "Los Angeles", "Houston", "New Orleans", "Oklahoma City", "Los Angeles"};
  private final Integer[] POINTS = {21,8,8,6,17,20,10,10,11,11,7,13,20,11,17,5,16,11,9,13,12,7,9,22,4,8,12,13,9,9};


  public void generateDestinationDeckTemplate(String[] firstCities, String[] secondCities, Integer[] points){
    if(firstCities.length != secondCities.length || secondCities.length != points.length)
      throw new IllegalArgumentException("lengths of Destination Deck arrays do not match");
    fullDestinationDeckTemplate.clear();
    for(int i = 0; i < firstCities.length; i++){
      DestinationTemplate<String, String, Integer> tempDestinationTemplate = new DestinationTemplate(firstCities[i],secondCities[i],points[i]);
      fullDestinationDeckTemplate.add(tempDestinationTemplate);
    }
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

  public Stream<DestinationCard> streamDestinationCards(String playerId) throws NotFoundException {
    if (df.getPlayer(playerId) == null) {
      throw new NotFoundException("player with id " + playerId + " not found");
    }
    return df.streamDestinationCards().filter((DestinationCard card) -> card.getPlayerId().equals(playerId) || card.getPlayerId().equals("") || card.getPlayerId() == null);
  }

  public void claimDesinationCards(List<String> destinationCardIds, String playerId) throws NotFoundException {
    if(df.getPlayer(playerId) == null)
      throw new NotFoundException("player with id" + playerId + " not found");
    for(String id : destinationCardIds){
      DestinationCard card = df.getDestinationCard(id);
      if(card.getPlayerId().equals("")){
        card.setPlayerId(playerId);
        df.updateDestinationCard(card);
      }
      else{
        System.out.println(" playerId = '" +card.getPlayerId() + "'");
        throw new ApiError(Code.INVALID_ARGUMENT,"card with id " + id + " has already been claimed");
      }
    }
  }

  //Train Card Functions--------------------------------------------------------------

  private ArrayList<TrainCardTemplate<TrainCard.Color,TrainCard.State>> fullTrainCardDeckTemplate = new ArrayList<>();
//TODO Set Default Values
  private final TrainCard.Color[] TRAIN_COLORS = {};
  private final TrainCard.State[] TRAIN_STATES = {};

  public void generateTrainCardDeckTemplate(TrainCard.Color[] colors, TrainCard.State[] states){
    if(colors.length != states.length){
      throw new IllegalArgumentException("lengths of Train Deck arrays do not match");
    }
    fullTrainCardDeckTemplate.clear();
    for(int i = 0; i < colors.length; i++){
      TrainCardTemplate<TrainCard.Color,TrainCard.State> tempTrainCardTemplate = new TrainCardTemplate<TrainCard.Color, TrainCard.State>(colors[i],states[i]);
      fullTrainCardDeckTemplate.add(tempTrainCardTemplate);
    }
  }

  public void claimTrainCardFromDeck(String playerId, String cardDrawnId) throws NotFoundException {
    if(df.getPlayer(playerId) == null){
      throw new NotFoundException("player with id" + playerId + " not found");
    }
    TrainCard returned = df.getTrainCard(cardDrawnId);
    if(returned.getPlayerId().equals("")){
      if(!returned.getState().equals(TrainCard.State.HIDDEN)){
        throw new ApiError(Code.INVALID_ARGUMENT, "card with id " + cardDrawnId + " is not in deck ");
      }
      else {
        returned.setPlayerId(playerId);
        returned.setState(TrainCard.State.OWNED);
        df.updateTrainCard(returned);
      }
    }
    else{
      System.out.println(" playerId = '" +returned.getPlayerId() + "'");
      throw new ApiError(Code.INVALID_ARGUMENT,"card with id " + cardDrawnId + " has already been claimed");
    }
  }

  public void claimFaceUpTrainCard(String playerId, String cardDrawnId) throws NotFoundException{
    if(df.getPlayer(playerId) == null){
      throw new NotFoundException("player with id" + playerId + " not found");
    }
    TrainCard returned = df.getTrainCard(cardDrawnId);
    if(returned.getPlayerId().equals("")){
      if(!returned.getState().equals(TrainCard.State.VISIBLE)){
        throw new ApiError(Code.INVALID_ARGUMENT, "card with id " + cardDrawnId + " is not visible ");
      }
      else {
        returned.setPlayerId(playerId);
        returned.setState(TrainCard.State.OWNED);
        df.updateTrainCard(returned);
        df.newFaceUpCard(df.getPlayer(playerId).getGameId());
      }
    }
    else{
      System.out.println(" playerId = '" +returned.getPlayerId() + "'");
      throw new ApiError(Code.INVALID_ARGUMENT,"card with id " + cardDrawnId + " has already been claimed");
    }
  }

  public List<TrainCard> getTrainCardsInHand(String playerId) throws NotFoundException {

  }

  //Testing Functions-------------------------------------------------------------------

  ArrayList<DestinationTemplate<String, String, Integer>> getFullDestinationDeckTemplate() {
    return fullDestinationDeckTemplate;
  }

  void setFullDestinationDeckTemplate(ArrayList<DestinationTemplate<String, String, Integer>> fullDestinationDeckTemplate) {
    this.fullDestinationDeckTemplate = fullDestinationDeckTemplate;
  }

}

class DestinationTemplate<T,U,V>{
  private final T first;
  private final U second;
  private final V third;

  public DestinationTemplate(T first, U second, V third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public T getFirst() { return first; }
  public U getSecond() { return second; }
  public V getThird() { return third; }
}

class TrainCardTemplate<V,X>{
  private final V third;
  private final X fourth;

  public TrainCardTemplate( V color, X state) {
    this.third = color;
    this.fourth = state;
  }

  public V getColor() {
    return third;
  }

  public X getState() {
    return fourth;
  }
}
