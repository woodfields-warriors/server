package com.wwttr.database;


import java.lang.reflect.Array;
import java.util.*;

import com.wwttr.api.NotFoundException;
import com.wwttr.models.*;
import java.util.stream.Stream;
import java.util.Random;


public class DatabaseFacade {
    private ArrayList<User> Users = new ArrayList<>();
    private ArrayList<Game> Games = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Message> messages = new ArrayList<>();
    private CommandQueue<Message> messageQueue = new CommandQueue<Message>();
    private ArrayList<GameAction> gameActions = new ArrayList<>();
    private CommandQueue<GameAction> historyQueue = new CommandQueue<GameAction>();
    private Random rn = new Random();
    static private DatabaseFacade instance;
    private ArrayList<DestinationCard> destinationCards = new ArrayList<>();
    private CommandQueue<DestinationCard> destinationCardQueue = new CommandQueue<>();
    private ArrayList<TrainCard> trainCards = new ArrayList<>();
    private CommandQueue<TrainCard> trainCardQueue = new CommandQueue<TrainCard>();
    private CommandQueue<DeckStats> deckStatsCommandQueue = new CommandQueue<>();
  private ArrayList<Route> routes = new ArrayList<>();
  private CommandQueue<Route> routeQueue = new CommandQueue<>();

    private DatabaseFacade(){

    }

    public static DatabaseFacade getInstance(){
        if(instance == null){
            instance = new DatabaseFacade();
        }
        return instance;
    }

    public static void main(String[] args) {
        DatabaseFacade service = new DatabaseFacade();
    }

    public User getUser(String username) {
      synchronized (this) {
        for (User temp : Users) {
            if (temp.getUsername().equals(username)) {
                return temp;
            }
        }
        return null;
      }
    }

    public User makeUser(User user){
      synchronized (this) {
        if(getUser(user.getUsername()) != null){
            throw new IllegalArgumentException("User already exists");
        }
        else{
            Users.add(user);
            return user;
        }
      }
    }

    public User getUserByID(String ID){
      synchronized (this) {
        for(User temp : Users) {
            if(temp.getUserID().equals(ID)){
                return temp;
            }
        }
        return null;
      }
    }

    public void clearUsers(){
      synchronized (this) {
        Users = new ArrayList<>();
      }
    }

    //***********************************************************************************//
    //-------------------------------Player Methods------------------------------------

    public void addPlayer(Player player){
      synchronized (this) {
        players.add(player);
      }
    }

    public void clearPlayers() {players = new ArrayList<>();}

    public Player getPlayer(String playerID) {
      synchronized (this) {
        for (Player p : players) {
          if (p.getPlayerId().equals(playerID)) {
            return p;
          }
        }
        return null;
      }
    }

    //***********************************************************************************//
    //-------------------------------Game Service Methods------------------------------------

    public Game getGame(String gameID){
      synchronized (this) {

        //System.out.println("In database with all games being the following");
        for(int i = 0; i < Games.size(); i++){
          //System.out.println(Games.get(i).getGameID());
            if(Games.get(i).getGameID().equals(gameID)){
                return Games.get(i);
            }
        }
        return null;
      }
    }

    public List<Game> listGames(){
      synchronized (this) {
        return new ArrayList<Game>(Games);
      }
    }


    public void addGame(Game game){
      synchronized (this) {
        Games.add(game);
      }
    }

    public void clearGames(){
      synchronized (this) {
        Games = new ArrayList<>();
      }
    }

    public void updateGame(Game game, String gameID){
      synchronized (this) {
        for(int i = 0; i < Games.size(); i++){
          if(Games.get(i).getGameID().equals(game.getGameID())){
            Games.add(i,game);
          }
        }
      }
    }

    public void deleteGame(String gameID){
      synchronized (this) {
        for (int i = 0; i < Games.size(); i++){
            if(Games.get(i).getGameID().equals(gameID)){
              //  List<Integer> players = Games.get(i).getPlayerUserIDs();
              //  String gameName = Games.get(i).getDisplayName();
              //  DeleteResponse toReturn = new DeleteResponse(gameName,players);
                Games.remove(i);
            }
        }
      }
      //  DeleteResponse toReturn = new DeleteResponse("Couldn't find game with given GameID");
      //  return toReturn;
    }


//***********************************************************************************//
//-----------------------HISTORY -------------------------//
    public void addGameAction(GameAction action){
      synchronized (this) {
        gameActions.add(action);
        historyQueue.publish(action);
      }
    }

    public GameAction getGameActionById(String actionId){
      synchronized (this) {
        for (GameAction action : gameActions){
          if(action.getActionId().equals(actionId)){
            return action;
          }
        }
        return null;
      }
    }

    public List<Player> listPlayers(String gameId) {
      ArrayList<Player> ret = new ArrayList<Player>();

      synchronized (this) {
        for (Player player : players) {
          if (gameId == null || player.getGameId().equals(gameId)) {
            ret.add(player);
          }
        }
      }

      return ret;
    }

    //Given a player Id, this method returns the next player in the players list
    //The players list is ordered in the order that people joined the game.
    //This order is also the order of the turn.  So, this method can be used
    // to get the player who is next up in turn order;
    public Player getNextPlayer(String playerId){
      synchronized(this) {
        for (int i = 0; i<players.size();i++){
          if(players.get(i).getPlayerId().equals(playerId)){
            return players.get(i+1);
          }
        }
        return null;
      }
    }
    public Stream<GameAction> streamHistory(String gameId) {
      return historyQueue
        .subscribe()
        .filter((GameAction action) -> action.getGameId().equals(gameId));
    }



  //***********************************************************************************//
  //-------------------------------Destination Card Service Methods------------------------------------
  public DestinationCard getDestinationCard(String destinationCardId) throws NotFoundException{
    synchronized (this) {
      for(DestinationCard card : destinationCards){
        if(card.getId().equals(destinationCardId)){
          return card;
        }
      }
      throw new NotFoundException("card with id " + destinationCardId + " not found");
    }
  }
  public List<DestinationCard> listDestinationCards(int limit, String gameId) throws NotFoundException{
    synchronized (this) {
      List<DestinationCard> listToReturn = new ArrayList<DestinationCard>();
      ArrayList<DestinationCard> cards = getDestinationCardsByGameId(gameId);
      if(cards.size() < limit){
        throw new IllegalArgumentException("only " + cards.size() + " cards in deck, " + limit + " requested");
      }
      if(cards.size() == 0){
        throw new NotFoundException("Cards for id " + gameId + " not found" );
      }
      for(int i = 0 ; i < limit;i++ ){
        int j = 0;
        while(!cards.get(j).getPlayerId().equals("")){
          j++;
          if(j >= cards.size()){
            boolean newCardsToSend = false;
            for(DestinationCard card : cards){
              if(card.getPlayerId().equals("sent")) {
                card.setPlayerId("");
                updateDestinationCard(card);
                newCardsToSend = true;
              }
            }
            if(!newCardsToSend){
              return listToReturn;
            }
          }
        }
        DestinationCard tempCard = cards.get(j);
        listToReturn.add(cards.get(j));
        tempCard.setPlayerId("sent");
        updateDestinationCard(tempCard);
      }
      return listToReturn;
    }
  }
  public void updateDestinationCard(DestinationCard card)throws NotFoundException{
    synchronized (this) {
      DestinationCard retrievedCard = getDestinationCard(card.getId());
      if(retrievedCard == null){
        throw new NotFoundException("id invalid, card not found");
      }
      else{
        retrievedCard.update(card);
        destinationCardQueue.publish(card);
      }
    }
  }

  private ArrayList<DestinationCard> getDestinationCardsByGameId(String gameId){
    synchronized (this) {
      ArrayList<DestinationCard> toReturn = new ArrayList<>();
      for(DestinationCard card : destinationCards){
        if(card.getGameId().equals(gameId))
          toReturn.add(card);
      }
      return toReturn;
    }
  }

  public void addDestinationCardDeck(List<DestinationCard> cards){
    synchronized (this) {
      Collections.shuffle(cards);
      destinationCards.addAll(cards);
    }
  }

  public void clearCards(){
      destinationCards = new ArrayList<>();
  }

  public ArrayList<DestinationCard> getDestinationCards(){
      return  destinationCards;
  }

  public Stream<DestinationCard> streamDestinationCards(String playerId) {
    String gameId = getPlayer(playerId).getGameId();
    return destinationCardQueue
        .subscribe()
        .filter((DestinationCard dc) -> dc.getPlayerId().equals(playerId)|| dc.getPlayerId().equals("") || dc.getPlayerId() == null
                                        && dc.getGameId().equals(gameId));
  }


  //***********************************************************************************//
  //-------------------------------Train Card Service Methods------------------------------------

  public TrainCard getTrainCard(String cardDrawnId) throws NotFoundException{
    synchronized (this) {
      for (TrainCard card : trainCards) {
        if (card.getId().equals(cardDrawnId))
          return card;
      }
      throw new NotFoundException("card with id " + cardDrawnId + " not found");
    }
  }

  public void updateTrainCard(TrainCard card) throws NotFoundException{
    synchronized (this) {
      TrainCard retrievedCard = getTrainCard(card.getId());
      if(retrievedCard == null){
        throw new NotFoundException("id invalid, card not found");
      }
      else{
        if(card.getState() == TrainCard.State.OWNED && retrievedCard.getState() == TrainCard.State.HIDDEN){
          sendNewHiddenCard(card.getGameId());
        }
        retrievedCard.update(card);
        trainCardQueue.publish(retrievedCard);
        updateDeckStats(card.getGameId());
      }
    }
  }

  private void updateDeckStats(String gameId) {
    synchronized (this){
      ArrayList<DestinationCard> dcards = getDestinationCardsByGameId(gameId);
      ArrayList<DestinationCard> filteredDCards = new ArrayList<>();
      for(DestinationCard card : dcards){
        if(!card.getPlayerId().equals("") || card.getPlayerId().equals("sent")){
          filteredDCards.add(card);
        }
      }
      ArrayList<TrainCard> tcards = getTrainCardsForGame(gameId);
      ArrayList<TrainCard> filteredtcards = new ArrayList<>();
      for(TrainCard card : tcards){
        if(card.getState() == TrainCard.State.HIDDEN ){
          filteredtcards.add(card);
        }
      }
      DeckStats newstats = new DeckStats(filteredtcards.size(),filteredDCards.size(),gameId);
      deckStatsCommandQueue.publish(newstats);
    }
  }

  public void addTrainCardDeck(List<TrainCard> trainCardList){
    synchronized (this) {
      Collections.shuffle(trainCardList);
      trainCards.addAll(trainCardList);
    }
  }

  public void newFaceUpCard(String gameId) throws NotFoundException{
    synchronized (this) {
      ArrayList<TrainCard> cards = getTrainCardsForGame(gameId);
      TrainCard tempCard = getRandomTrainCardFromDeck(gameId);
      tempCard.setState(TrainCard.State.VISIBLE);
      try {
        updateTrainCard(tempCard);
      }
      catch (NotFoundException e){
        e.printStackTrace();
        newFaceUpCard(gameId);
      }
    }
  }

  public void sendNewHiddenCard(String gameId) throws NotFoundException{
    synchronized (this) {
      trainCardQueue.publish(getRandomTrainCardFromDeck(gameId));
    }
  }

  public ArrayList<TrainCard> getTrainCardsForGame(String gameId){
    synchronized (this) {
      ArrayList<TrainCard> toReturn = new ArrayList<>();
      for (TrainCard temp : trainCards) {
        if (temp.getGameId().equals(gameId))
          toReturn.add(temp);
      }
      return toReturn;
    }
  }

  public List<TrainCard> getTrainCardsForPlayer(String playerId){
      synchronized (this){
        List<TrainCard> toReturn = new ArrayList<>();
        for(TrainCard card : trainCards){
          if(card.getPlayerId().equals(playerId))
            toReturn.add(card);
        }
        return toReturn;
      }
  }

  public TrainCard getRandomTrainCardFromDeck(String gameId) throws NotFoundException{
      ArrayList<TrainCard> cards = getTrainCardsForGame(gameId);
      TrainCard temp = cards.get(rn.nextInt(cards.size()));
      for(int i = 0; temp.getState() != TrainCard.State.HIDDEN; i++){
        if(i != 100) {
          temp = cards.get(rn.nextInt(cards.size()));
        }
        else{
          throw new NotFoundException("random card cannot be generated");
        }
      }
      return temp;
  }


  public Stream<TrainCard> streamTrainCards(String playerId) {
    String gameId = getPlayer(playerId).getGameId();
    return trainCardQueue
        .subscribe()
        .filter((TrainCard tc) -> tc.getGameId().equals(gameId) && ( tc.getPlayerId().equals(playerId)
                                  || tc.getState().equals(TrainCard.State.VISIBLE) ));
  }

  public Stream<DeckStats> streamDeckStats(String gameId){
      return deckStatsCommandQueue.subscribe()
          .filter((DeckStats ds) -> ds.getGameId().equals(gameId));
  }

  public void clearTrainCards() {trainCards = new ArrayList<>();}

  public ArrayList<TrainCard> getTrainCards() {return trainCards;}


  //***********************************************************************************//
  //-------------------------------Chat Service Methods------------------------------------

  public void addMessage(Message message){
    synchronized (this) {
      messages.add(message);
      messageQueue.publish(message);
    }
  }

  public Message getMessagebyId(String messageId){
    synchronized (this) {
      for (Message message : messages){
        if(message.getMessageId().equals(messageId)){
          return message;
        }
      }
      return null;
    }
  }

  public Stream<Message> streamMessages(String gameId) {
    return messageQueue
      .subscribe()
      .filter((Message m) -> m.getGameId().equals(gameId));
  }


  public void addRoute(Route r) {
    synchronized (this) {
      routes.add(r);
      routeQueue.publish(r);
    }
  }

  public Route getRoutebyId(String routeId) {
    synchronized (this) {
      for (Route route : routes) {
        if (route.getRouteId().equals(routeId)) {
          return route;
        }
      }
      return null;
    }
  }

  public Stream<Route> streamRoutes() {
    return routeQueue.subscribe();
  }
}
