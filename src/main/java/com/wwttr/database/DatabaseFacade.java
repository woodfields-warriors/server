package com.wwttr.database;

import java.util.ArrayList;
import java.util.Random;

import com.wwttr.models.User;


public class DatabaseFacade {
    ArrayList<User> Users;
    Random rn = new Random();
    static private DatabaseFacade instance;

    public User getUser(String username) {
        for (User temp : Users){
            if( temp.getUsername().equals(username)){
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
            while(getUserByID(tempID) != null)
                tempID = rn.nextInt();
            User temp = new User(username, password, tempID);
            Users.add(temp);
            return temp;
        }
    }

    public User getUserByID(Integer ID){
        for(User temp : Users) {
            if(temp.getUserID().equals(ID)){
                return temp;
            }
        }
        return null;
    }

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
}
