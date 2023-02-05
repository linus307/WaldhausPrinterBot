package de.waldhaus.printerBot.commands;

import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.model.Model;
import de.waldhaus.printerBot.state.State;
import java.util.Set;

public abstract class Command {

  protected final long chatId;

  public Command(final long chatId) {
    this.chatId = chatId;
  }

  public Set<State.Phase> inPhase() {
    return Set.of(State.Phase.values());
  }

  public Result execute(final Model model, final State.Phase phase, final PrinterBot bot) {
    if (inPhase().contains(phase)) {
      return this.run(model, bot);
    }
    bot.sendMessage(chatId, "This action is not applicable now.");
    return Result.RETRY;
  }

  protected abstract Result run(Model model, PrinterBot bot);

}
