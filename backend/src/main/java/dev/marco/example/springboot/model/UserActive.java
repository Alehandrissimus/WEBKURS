package dev.marco.example.springboot.model;

public enum UserActive {
  NOT_ACTIVE, ACTIVE;

  public static boolean convertFromIntToBoolean(UserActive active) {
    return active.ordinal() == 1;
  }
}
