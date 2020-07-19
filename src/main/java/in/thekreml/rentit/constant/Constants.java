package in.thekreml.rentit.constant;

public class Constants {
  public static final String PERMISSION_USAGE = "rentit.usage";
  public static final String PERMISSION_REGISTER = "rentit.register";
  public static final String PERMISSION_UNREGISTER = "rentit.deregister";

  public static final String ERROR_PERMISSION = "You lack the required permissions!";
  public static final String ERROR_NO_TARGET = "You are not targeting a block!";
  public static final String ERROR_INVALID_TARGET = "You are not targeting an anvil!";
  public static final String ERROR_ALREADY_REGISTERED = "This anvil is already registered!";
  public static final String ERROR_NOT_REGISTERED = "This anvil is not registered!";
  public static final String ERROR_ALREADY_RENTED = "You have already rented this anvil!";
  public static final String ERROR_INSUFFICIENT_BALANCE = "Insufficient balance!";
  public static final String ERROR_WITHDRAWAL = "Error trying to withdraw rental fee!";
  public static final String ERROR_USAGE = "Usage: /rent [register|unregister]";

  public static final String PATH_DATABASE = "./plugins/RentIt/data.json";

  public static final String MESSAGE_USE = "Use /rent on this anvil to rent it!";
  public static final String MESSAGE_REGISTER = "Registered this anvil!";
  public static final String MESSAGE_UNREGISTER = "Unregistered this anvil!";
}
