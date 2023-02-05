package de.waldhaus.printerBot.commands;

import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.model.Model;

public class StartCommand extends Command {

  public StartCommand(final long chatId) {
    super(chatId);
  }

  @Override
  protected Result run(final Model model, final PrinterBot bot) {
    bot.sendMessage(chatId, "How to use!");
    return Result.RETRY;
  }
}
