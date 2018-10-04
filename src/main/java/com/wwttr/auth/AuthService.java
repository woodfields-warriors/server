package com.wwttr.auth;


import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.LoginResponse;
import com.wwttr.models.User;


public class AuthService {
  private DatabaseFacade df;
  private static AuthService instance;

  public Api.LoginResponse login(String username, String password) throws Exception{
    try {
      User returnedUser = df.getUser(username);
      if( returnedUser.getPassword().equals(password)){
          Api.LoginResponse.Builder builder = Api.LoginResponse.newBuilder();
          builder.setUserId(returnedUser.getUserID());
          return builder.build();
      }

    }
    catch (Exception e){
        throw e;
    }
    throw new Exception("Login Service Error");
  }

  public Api.LoginResponse register(String username, String password) {
      try{
          User returnedUser = df.makeUser(username,password);
          Api.LoginResponse.Builder builder = Api.LoginResponse.newBuilder();
          builder.setUserId(returnedUser.getUserID());
          return builder.build();
      }
      catch (Exception e){
          throw e;
      }
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
