package com.wwttr.models;

import java.io.Serializable;
import java.util.*;
import com.google.protobuf.Message;

public class Delta implements Serializable {

    private static final long serialVersionUID = 12345L;

    private Message request;
    private String id;
    private String gameId;

    public Delta(Message request, String id, String gameId) {
        this.request = request;
        this.id = id;
        this.gameId = gameId;
    }

    public Message getRequest() {
        return this.request;
    }

    public String getId() {
        return this.id;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setRequest(Message request) {
        
    }


}