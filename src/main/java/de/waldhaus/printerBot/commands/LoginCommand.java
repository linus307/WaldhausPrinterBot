package de.waldhaus.printerBot.commands;

import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.model.Model;
import de.waldhaus.printerBot.state.State.Phase;
import de.waldhaus.printerBot.state.State.StateName;
import java.util.Set;
import javax.annotation.Nullable;

public class LoginCommand extends Command {

  //private final String username;

  private final String password;

  /*
  public LoginCommand(final long chatId, @Nullable final String username,
      @Nullable final String password) {
    super(chatId);
    this.username = username;
    this.password = password;
  }
   */

  public LoginCommand(final long chatId, @Nullable final String password) {
    super(chatId);
    this.password = password;
  }

  @Override
  public Set<Phase> inPhase() {
    return Set.of(Phase.START);
  }

  @Override
  protected Result run(final Model model, final PrinterBot bot) {
    /*
    if (username != null) {
      model.getClient(chatId).ifPresent(client -> client.setUsername(username));
    }
     */
    if (password != null) {
      model.getClient(chatId).ifPresent(client -> client.setPassword(password));
    }
    return Result.PROCEED.addState(StateName.LOGIN);
  }
}
