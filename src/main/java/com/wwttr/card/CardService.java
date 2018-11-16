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
    generateTrainCardDeckTemplate(TRAIN_COLORS,TRAIN_STATES,COUNT);
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
    for(int i = 0; i < 5; i++){
      TrainCard tempCard = trainCardList.get(rn.nextInt(trainCardList.size()));
      tempCard.setState(TrainCard.State.VISIBLE);
    }
    System.out.println("Train card deck size" + trainCardList.size());
    df.addDestinationCardDeck(cardList);
    df.addTrainCardDeck(trainCardList);
    dealTrainCards(gameId);
  }

  public Stream<DeckStats> streamDeckStats(String gameId) throws NotFoundException{
    if(df.getGame(gameId) == null){
      throw new NotFoundException("game not found");
    }
    return df.streamDeckStats(gameId);
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

  public void claimDestinationCards(List<String> destinationCardIds, String playerId) throws NotFoundException {
    if(df.getPlayer(playerId) == null)
      throw new NotFoundException("player with id" + playerId + " not found");
    for(String id : destinationCardIds){
      DestinationCard card = df.getDestinationCard(id);
      if(card.getPlayerId().equals("sent")){
        card.setPlayerId(playerId);
        df.updateDestinationCard(card);
      }
      else{
        System.out.println(" playerId = '" +card.getPlayerId() + "'");
        throw new ApiError(Code.INVALID_ARGUMENT,"card with id " + id + " has already been claimed");
      }
    }
  }

  public Stream<DestinationCard> streamDestinationCards(String playerId) throws NotFoundException {
    if(df.getPlayer(playerId) == null){
      throw new NotFoundException("player not found");
    }
    return df.streamDestinationCards(playerId);
  }

  //Train Card Functions--------------------------------------------------------------

  private ArrayList<TrainCardTemplate<TrainCard.Color,TrainCard.State>> fullTrainCardDeckTemplate = new ArrayList<>();
  private final TrainCard.Color[] TRAIN_COLORS = {TrainCard.Color.PINK,TrainCard.Color.WHITE,TrainCard.Color.BLUE,TrainCard.Color.YELLOW,TrainCard.Color.ORANGE,
                                                  TrainCard.Color.BLACK,TrainCard.Color.RED,TrainCard.Color.GREEN,TrainCard.Color.RAINBOW};
  private final TrainCard.State[] TRAIN_STATES = {TrainCard.State.HIDDEN,TrainCard.State.HIDDEN,TrainCard.State.HIDDEN,TrainCard.State.HIDDEN,TrainCard.State.HIDDEN,
                                                  TrainCard.State.HIDDEN,TrainCard.State.HIDDEN,TrainCard.State.HIDDEN,TrainCard.State.HIDDEN};
  private final Integer[] COUNT = {12,12,12,12,12,12,12,12,14};

  public void generateTrainCardDeckTemplate(TrainCard.Color[] colors, TrainCard.State[] states, Integer[] count){
    if(colors.length != states.length || states.length != count.length){
      throw new IllegalArgumentException("lengths of Train Deck arrays do not match");
    }
    fullTrainCardDeckTemplate.clear();
    for(int i = 0; i < colors.length; i++){
      for(int j = 0; j < count[i]; j++) {
        TrainCardTemplate<TrainCard.Color, TrainCard.State> tempTrainCardTemplate = new TrainCardTemplate<>(colors[i], states[i]);
        fullTrainCardDeckTemplate.add(tempTrainCardTemplate);
      }
    }
  }

  public void dealTrainCards(String gameId) throws NotFoundException{
    Game game = df.getGame(gameId);
    if(game == null){
      throw new NotFoundException("game not found");
    }
    List<String> playerIDs = game.getPlayerIDs();
    for(String id : playerIDs){
      for(int i = 0; i < 4; i++){
        TrainCard card = df.getRandomTrainCardFromDeck(gameId);
        card.setPlayerId(id);
        card.setState(TrainCard.State.OWNED);
        df.updateTrainCard(card);
      }
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
    if(df.getPlayer(playerId) == null){
      throw new NotFoundException("player with id" + playerId + " not found");
    }
    return df.getTrainCardsForPlayer(playerId);
  }

  public Stream<TrainCard> streamTrainCards(String playerId) throws NotFoundException {
    if(df.getPlayer(playerId) == null){
      throw new NotFoundException("player not found");
    }
    return df.streamTrainCards(playerId);
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
  private final V first;
  private final X second;

  public TrainCardTemplate( V color, X state) {
    this.first = color;
    this.second = state;
  }

  public V getColor() {
    return first;
  }

  public X getState() {
    return second;
  }
}
