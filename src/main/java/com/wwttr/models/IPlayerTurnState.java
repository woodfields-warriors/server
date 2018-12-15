package com.wwttr.models;

import com.wwttr.api.NotFoundException;
import com.wwttr.api.FailedPreconditionException;

import java.io.Serializable;
import java.util.List;

public interface IPlayerTurnState extends java.io.Serializable {
  
  public void drawTrainCard(String playerId) throws NotFoundException;

  public void claimRoute(String playerId, String routeId,List<String> cardIds) throws NotFoundException, IllegalArgumentException, FailedPreconditionException;

  public void drawDestinationCards(String playerId, List<String> destinationCardIds) throws NotFoundException;

  public void drawFaceUpTrainCard(String playerId, String cardId) throws NotFoundException;

  public PlayerStats.PlayerTurnState getTurnState();

  public boolean canPeek();
}
