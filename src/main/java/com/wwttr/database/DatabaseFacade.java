package com.wwttr.database;


import java.util.*;
import com.wwttr.models.*;



public class DatabaseFacade {
    private ArrayList<User> Users = new ArrayList<>();
    private ArrayList<Game> Games = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private Random rn = new Random();
    static private DatabaseFacade instance;

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
        for (User temp : Users) {
            if (temp.getUsername().equals(username)) {
                return temp;
            }
        }
        return null;
    }

    public User makeUser(String username, String password){
        if(getUser(username) != null){
            throw new IllegalArgumentException("User already exists");
        }
        else{
            Integer tempID = rn.nextInt();
            while(getUserByID("usr" + tempID.toString()) != null)
                tempID = rn.nextInt();
            User temp = new User(username, password, "usr" + tempID.toString());
            Users.add(temp);
            return temp;
        }
    }

    public User getUserByID(String ID){
        for(User temp : Users) {
            if(temp.getUserID().equals(ID)){
                return temp;
            }
        }
        return null;
    }

    public void clearUsers(){
        Users = new ArrayList<>();
    }

    //***********************************************************************************//
    //-------------------------------Player Methods------------------------------------

    public void addPlayer(Player player){
      players.add(player);
    }

    public void clearPlayers() {players = new ArrayList<>();}

    //***********************************************************************************//
    //-------------------------------Game Service Methods------------------------------------

    public Game getGame(String gameID){
        //System.out.println("In database with all games being the following");
        for(int i = 0; i < Games.size(); i++){
          //System.out.println(Games.get(i).getGameID());
            if(Games.get(i).getGameID().equals(gameID)){
                return Games.get(i);
            }
        }
        return null;
    }

    public List<Game> listGames(){
        return Games;
    }


    public void addGame(Game game){
        Games.add(game);
    }

    public void clearGames(){Games = new ArrayList<>();}


  //  public CreateResponse createGame(String gameName, String hostUserID, Integer numberOfPlayers){
        //arguments checked in GameService
  //      Game game = new Game(hostUserID, new ArrayList(), gameName, numberOfPlayers, Integer.toString(rn.nextInt()));
  //      CreateResponse toReturn = new CreateResponse(game.getDisplayName(),game.getHostPlayerID());
  //      return toReturn;
  //  }


    public void updateGame(Game game, String gameID){
      for(int i = 0; i < Games.size(); i++){
        if(Games.get(i).getGameID() == game.getGameID()){
          Games.add(i,game);
        }
      }
    }

    public void deleteGame(String gameID){
        for (int i = 0; i < Games.size(); i++){
            if(Games.get(i).getGameID() == gameID){
              //  List<Integer> players = Games.get(i).getPlayerUserIDs();
              //  String gameName = Games.get(i).getDisplayName();
              //  DeleteResponse toReturn = new DeleteResponse(gameName,players);
                Games.remove(i);
            }
        }
      //  DeleteResponse toReturn = new DeleteResponse("Couldn't find game with given GameID");
      //  return toReturn;
    }

    public Player getPlayer(String playerID) {
      for (Player p : players) {
        if (p.getPlayerId().equals(playerID)) {
          return p;
        }
      }
      return null;
    }

  //***********************************************************************************//
  //-------------------------------Card Service Methods------------------------------------
  public DestinationCard getDestinationCard(String destinationCardId){}
  public List<DestinationCard> listDestinationCards(int limit, String gameId){}
  public void updateDestinationCard(String destinationCardId, String playerId){}

}
