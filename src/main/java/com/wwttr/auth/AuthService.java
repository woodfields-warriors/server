package com.wwttr.auth;


import com.wwttr.api.NotFoundException;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.LoginResponse;
import com.wwttr.models.User;

import java.util.Random;

public class AuthService {
  private DatabaseFacade df;
  private static AuthService instance;
  private Random rn = new Random();

  public LoginResponse login(String username, String password) throws Exception{
    User returnedUser = df.getUser(username);
    if (returnedUser == null) {
      throw new NotFoundException("");
    }
    if( returnedUser.getPassword().equals(password)){
        return new LoginResponse(returnedUser.getUserID());
    }
    throw new AccessDeniedException("Password incorrect");
  }

  public LoginResponse register(String username, String password) {
      try{
          String newId = "usr" + rn.nextInt();
          while (df.getUserByID(newId) != null)
            newId = "usr" + rn.nextInt();
          User newUser = new User(username, password, newId);
          User returnedUser = df.makeUser(newUser);
          return new LoginResponse(returnedUser.getUserID());
      }
      catch (Exception e){
          throw e;
      }
  }

  public String getUsername(String userID){
    User user = df.getUserByID(userID);
    return user.getUsername();
  }


    private AuthService() {
      df = DatabaseFacade.getInstance();
    }

    public static AuthService getInstance(){
      if(instance == null)
          instance = new AuthService();
      return instance;
    }

    public static void main(String[] args) {
    AuthService service = new AuthService();
  }
}
