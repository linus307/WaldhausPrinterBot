package de.waldhaus.printerBot.state;

import de.waldhaus.printerApi.PrinterApiImplementation;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.commands.Command;
import de.waldhaus.printerBot.model.Client;
import de.waldhaus.printerBot.model.Model;

public abstract class State {

  protected final Model model;
  protected final PrinterBot bot;
  protected final PrinterApiImplementation api;

  public State(final Model model, final PrinterApiImplementation api, final PrinterBot bot) {
    this.model = model;
    this.api = api;
    this.bot = bot;
  }

  public abstract State onUpdateReceived(Client client, Command command);

  public abstract StateName getStateName();

  public enum Phase {
    START,
    //LOGIN_USERNAME,
    LOGIN_PASSWORD,
    LOGGED_IN,
    CONVERSION_ACTION1,
    CONVERSION_ACTION2,
    CONVERSION_AMOUNT
  }

  public enum StateName {
    START,
    LOGIN,
    LOGGED_IN,
    CONVERSION
  }

}
