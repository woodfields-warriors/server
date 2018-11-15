package com.wwttr.database;


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
    private Random rn = new Random();
    static private DatabaseFacade instance;
    private ArrayList<DestinationCard> destinationCards = new ArrayList<>();
    private ArrayList<TrainCard> trainCards = new ArrayList<>();
    private CommandQueue<TrainCard> trainCardQueue = new CommandQueue<TrainCard>();
    private Random rn = new Random();

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


  //  public CreateResponse createGame(String gameName, String hostUserID, Integer numberOfPlayers){
        //arguments checked in GameService
  //      Game game = new Game(hostUserID, new ArrayList(), gameName, numberOfPlayers, Integer.toString(rn.nextInt()));
  //      CreateResponse toReturn = new CreateResponse(game.getDisplayName(),game.getHostPlayerID());
  //      return toReturn;
  //  }


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
      ArrayList<DestinationCard> cards = getCardsByGameId(gameId);
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

  private ArrayList<DestinationCard> getCardsByGameId(String gameId){
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


  //TODO TrainCard Functions
  //***********************************************************************************//
  //-------------------------------Train Card Service Methods------------------------------------

  public TrainCard getTrainCard(String cardDrawnId){
    synchronized (this) {
      for (TrainCard card : trainCards) {
        if (card.getId().equals(cardDrawnId))
          return card;
      }
      throw new NotFoundError("card with id " + cardDrawnId + " not found");
    }
  }

  public void updateTrainCard(TrainCard card){
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
      }
    }
  }

  public void addTrainCardDeck(List<TrainCard> trainCardList){
    synchronized (this) {
      Collections.shuffle(trainCardList);
      trainCards.addAll(trainCardList);
    }
  }

  public void newFaceUpCard(String gameId){
    synchronized (this) {
      ArrayList<TrainCard> cards = getTrainCardsForGame(gameId);
      TrainCard tempCard = cards.at(rn.nextInt(cards.size()));
      while (tempCard.getState() != TrainCard.State.Hidden) {
        tempCard = cards.at(rn.nextInt(cards.size()));
      }
      tempCard.setState(TrainCard.State.Visible);
      updateTrainCard(tempCard);
    }
  }

  public void sendNewHiddenCard(String gameId){
    synchronized (this) {
      ArrayList<TrainCard> cards = getTrainCardsForGame(gameId);
      TrainCard tempCard = cards.at(rn.nextInt(cards.size()));
      while (tempCard.getState() != TrainCard.State.HIDDEN) {
        tempCard = cards.at(rn.nextInt(cards.size()));
      }
      trainCardQueue.publish(tempCard);
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

  void clearCards() {trainCards = new ArrayList<>();}

  ArrayList<TrainCard> getTrainCards() {return trainCards;}


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
}
