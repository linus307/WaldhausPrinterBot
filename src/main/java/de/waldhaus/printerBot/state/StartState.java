package de.waldhaus.printerBot.state;

import de.waldhaus.printerApi.PrinterApiImplementation;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.commands.Command;
import de.waldhaus.printerBot.commands.Result;
import de.waldhaus.printerBot.model.Client;
import de.waldhaus.printerBot.model.Model;

public class StartState extends State {

  public StartState(final Model model, final PrinterApiImplementation api, final PrinterBot bot) {
    super(model, api, bot);
  }

  @Override
  public State onUpdateReceived(final Client client, final Command command) {
    final Result result = command.execute(model, Phase.START, bot);
    if (result == Result.PROCEED && result.getState() == StateName.LOGIN) {
      return new LoginState(model, api, bot).checkLogin(client);
    }
    return this;
  }

  @Override
  public StateName getStateName() {
    return StateName.START;
  }
}
