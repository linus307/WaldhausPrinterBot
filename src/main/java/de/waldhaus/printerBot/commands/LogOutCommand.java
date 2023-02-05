package de.waldhaus.printerBot.commands;

import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.keyboardMarkup.ReplyKeyboardMarkupExtension;
import de.waldhaus.printerBot.model.Model;
import de.waldhaus.printerBot.state.State.Phase;
import de.waldhaus.printerBot.state.State.StateName;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class LogOutCommand extends Command {

  public LogOutCommand(final long chatId) {
    super(chatId);
  }

  @Override
  public Set<Phase> inPhase() {
    return Arrays.stream(Phase.values()).filter(phase -> phase != Phase.START && phase != Phase.LOGIN_PASSWORD)
        .collect(Collectors.toSet());
  }

  @Override
  protected Result run(final Model model, final PrinterBot bot) {
    model.getClient(chatId).get().setKeyboardMarkup(ReplyKeyboardMarkupExtension.createLoginKeyboard());
    bot.sendMessage(chatId, String.format("%s you logged out.", model.getClient(chatId).get().getUsername().get()));
    model.getClient(chatId).get().logOut();
    return Result.END.addState(StateName.START);
  }
}
