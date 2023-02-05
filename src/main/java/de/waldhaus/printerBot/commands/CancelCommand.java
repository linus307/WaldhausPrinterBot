package de.waldhaus.printerBot.commands;

import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.model.Model;
import de.waldhaus.printerBot.state.State.Phase;
import java.util.Set;

public class CancelCommand extends Command {

  public CancelCommand(final long chatId) {
    super(chatId);
  }

  @Override
  public Set<Phase> inPhase() {
    return Set.of(Phase.LOGIN_PASSWORD, Phase.CONVERSION_AMOUNT, Phase.CONVERSION_ACTION1, Phase.CONVERSION_ACTION2);
  }

  @Override
  protected Result run(final Model model, final PrinterBot bot) {
    return Result.END;
  }
}
