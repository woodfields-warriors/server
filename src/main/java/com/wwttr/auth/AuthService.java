package com.wwttr.auth;


import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.LoginResponse;
import com.wwttr.models.User;

public class AuthService {
  private DatabaseFacade df;
  private static AuthService instance;

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
          User returnedUser = df.makeUser(username,password);
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
