package de.waldhaus.printerBot.commands;

import de.waldhaus.printerApi.PrinterAction;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.model.Model;
import de.waldhaus.printerBot.state.State;

public class InputCommand extends Command {

  private final String input;

  public InputCommand(final long chatId, final String input) {
    super(chatId);
    this.input = input;
  }

  @Override
  public Result execute(final Model model, final State.Phase phase, final PrinterBot bot) {
    switch (phase) {
      //case LOGIN_USERNAME -> model.getClient(chatId).ifPresent(client -> client.setUsername(input));
      case LOGIN_PASSWORD -> model.getClient(chatId).ifPresent(client -> client.setPassword(input));
      case CONVERSION_ACTION1 ->
          model.getClient(chatId).ifPresent(client -> client.getConversion().setPrinterAction1(PrinterAction.valueOf(input)));
      case CONVERSION_ACTION2 ->
          model.getClient(chatId).ifPresent(client -> client.getConversion().setPrinterAction2(PrinterAction.valueOf(input)));
      case CONVERSION_AMOUNT ->
          model.getClient(chatId).ifPresent(client -> client.getConversion().setAmount(Integer.valueOf(input)));
    }
    return Result.RETRY;
  }

  @Override
  protected Result run(final Model model, final PrinterBot bot) {
    return null;
  }
}
