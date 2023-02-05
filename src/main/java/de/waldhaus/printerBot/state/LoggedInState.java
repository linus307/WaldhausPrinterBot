package de.waldhaus.printerBot.state;

import de.waldhaus.printerApi.PrinterApiImplementation;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.commands.Command;
import de.waldhaus.printerBot.commands.Result;
import de.waldhaus.printerBot.model.Client;
import de.waldhaus.printerBot.model.Model;

public class LoggedInState extends State {

  public LoggedInState(final Model model, final PrinterApiImplementation api, final PrinterBot bot) {
    super(model, api, bot);
  }

  @Override
  public State onUpdateReceived(final Client client, final Command command) {
    final Result result = command.execute(model, Phase.LOGGED_IN, bot);
    if (result == Result.PROCEED && result.getState() == StateName.CONVERSION) {
      return new ConversionState(model, api, bot).checkConversion(client);
    } else if (result == Result.END && result.getState() == StateName.START) {
      return new StartState(model, api, bot);
    }
    return this;
  }

  @Override
  public StateName getStateName() {
    return StateName.LOGGED_IN;
  }
}
