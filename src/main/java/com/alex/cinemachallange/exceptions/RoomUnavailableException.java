package com.alex.cinemachallange.exceptions;

public class RoomUnavailableException extends DomainException {
  public RoomUnavailableException(String message) {
    super(message);
  }
}
