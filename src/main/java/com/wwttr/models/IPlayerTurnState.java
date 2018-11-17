package com.wwttr.models;

import com.wwttr.api.NotFoundException;
import java.util.List;

public interface IPlayerTurnState {
  public void drawTrainCard(String playerId) throws NotFoundException;

  public void claimRoute(String playerId) throws NotFoundException;

  public void drawDestinationCards(String playerId, List<String> destinationCardIds) throws NotFoundException;

  public void drawFaceUpTrainCard(String playerId, String cardId) throws NotFoundException;
}