package de.waldhaus.printerBot.commands;

import de.waldhaus.printerApi.PrinterAction;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.model.Model;
import de.waldhaus.printerBot.state.State.Phase;
import de.waldhaus.printerBot.state.State.StateName;
import java.util.Set;
import javax.annotation.Nullable;

public class ConversionCommand extends Command {

  private final PrinterAction printerAction1;
  private final PrinterAction printerAction2;
  private final Integer amount;

  public ConversionCommand(final long chatId, @Nullable final PrinterAction printerAction1,
      @Nullable final PrinterAction printerAction2, @Nullable final Integer amount) {
    super(chatId);
    this.printerAction1 = printerAction1;
    this.printerAction2 = printerAction2;
    this.amount = amount;
  }

  @Override
  public Set<Phase> inPhase() {
    return Set.of(Phase.LOGGED_IN);
  }

  @Override
  protected Result run(final Model model, final PrinterBot bot) {
    if (printerAction1 != null) {
      model.getClient(chatId).get().getConversion().setPrinterAction1(printerAction1);
    }
    if (printerAction2 != null) {
      model.getClient(chatId).get().getConversion().setPrinterAction2(printerAction2);
    }
    if (amount != null) {
      model.getClient(chatId).get().getConversion().setAmount(amount);
    }
    return Result.PROCEED.addState(StateName.CONVERSION);
  }
}
