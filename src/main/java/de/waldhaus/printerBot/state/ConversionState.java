package de.waldhaus.printerBot.state;

import de.waldhaus.printerApi.PrinterAction;
import de.waldhaus.printerApi.PrinterApiImplementation;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.commands.Command;
import de.waldhaus.printerBot.commands.Result;
import de.waldhaus.printerBot.keyboardMarkup.ReplyKeyboardMarkupExtension;
import de.waldhaus.printerBot.model.Client;
import de.waldhaus.printerBot.model.Model;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;

public class ConversionState extends State {

  public ConversionState(final Model model, final PrinterApiImplementation api, final PrinterBot bot) {
    super(model, api, bot);
  }

  @Override
  public State onUpdateReceived(final Client client, final Command command) {
    final Result result =
        client.getConversion().getPrinterAction1() == null ? command.execute(model, Phase.CONVERSION_ACTION1, bot)
            : client.getConversion().getPrinterAction2() == null ? command.execute(model, Phase.CONVERSION_ACTION2, bot)
                : command.execute(model, Phase.CONVERSION_AMOUNT, bot);
    if (result == Result.RETRY) {
      return checkConversion(client);
    } else if (result == Result.END && result.getState() == StateName.START) {
      return new StartState(model, api, bot);
    } else if (result == Result.END) {
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createLoginKeyboard());
      bot.sendMessage(client.getClientId(), "Conversion was cancelled.");
      client.resetConversion();
      return new LoggedInState(model, api, bot);
    }
    return this;
  }

  @Override
  public StateName getStateName() {
    return StateName.CONVERSION;
  }

  public State checkConversion(final Client client) {
    if (client.getConversion().getPrinterAction1() == null) {
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createConversionActionKeyboard(Set.copyOf(
          Arrays.stream(PrinterAction.values()).filter(printerAction -> printerAction != PrinterAction.SCAN).toList())));
      bot.sendMessage(client.getClientId(), "Type in from which type you want to convert.");
      return this;
    } else if (client.getConversion().getPrinterAction2() == null) {
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createConversionActionKeyboard(
          api.getAllowedConversions(client.getConversion().getPrinterAction1())));
      bot.sendMessage(client.getClientId(), "Type in to which type you want to convert.");
      return this;
    } else if (client.getConversion().getAmount() == null) {
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createCancelKeyboard());
      bot.sendMessage(client.getClientId(), "Type in the amount you want to convert.");
      return this;
    }
    try {
      final PrinterAction printerAction1 = client.getConversion().getPrinterAction1();
      final PrinterAction printerAction2 = client.getConversion().getPrinterAction2();
      final int amount = client.getConversion().getAmount();
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createLoggedInKeyboard());
      if (api.checkConversion(printerAction1, printerAction2) && amount <= api.getActionAvailable(client.getPassword().get(),
          printerAction1)) {
        api.convertAction(client.getUsername().get(), client.getPassword().get(), printerAction1, printerAction2, amount);
        bot.sendMessage(client.getClientId(),
            String.format("Successfully converted %d %s in %d %s.", amount, printerAction1.label,
                (int) (amount * api.getConversionMap().get(printerAction1).get(printerAction2)), printerAction2.label));
      } else {
        bot.sendMessage(client.getClientId(), "Conversion was unsuccessful.");
      }
      client.resetConversion();
      return new LoggedInState(model, api, bot);
    } catch (final URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
