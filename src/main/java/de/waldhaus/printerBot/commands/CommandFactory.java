package de.waldhaus.printerBot.commands;

import de.waldhaus.printerApi.PrinterAction;
import de.waldhaus.printerApi.PrinterApiImplementation;
import java.util.Arrays;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandFactory {

  private final PrinterApiImplementation api;
  private final CommandLineParser parser = new DefaultParser();

  public CommandFactory(final PrinterApiImplementation api) {
    this.api = api;
  }


  public Command createCommand(final Update update) {
    final long chatId = update.getMessage().getChatId();
    final String messageText = update.getMessage().getText();
    final String[] command = messageText.split(" ");
    return switch (command[0]) {
      case "/login" -> createLogin(chatId, Arrays.copyOfRange(command, 1, command.length));
      case "/start" -> createStart(chatId);
      case "/cancel" -> createCancel(chatId);
      case "/prints_left" -> createPrintsLeft(chatId);
      case "/convert" -> createConversion(chatId, Arrays.copyOfRange(command, 1, command.length));
      case "/logout" -> createLogOut(chatId);
      default -> createInput(chatId, messageText);
    };
  }

  public Command createLogin(final long chatId, final String[] args) {
    final Options options = new Options();
    /*
    final Option usernameOption = Option.builder().longOpt("username").hasArg(true)
        .desc("The username").build();

     */
    final Option passwordOption = Option.builder().longOpt("password").hasArg(true).desc("The password").build();
    options.addOption(passwordOption);
    //options.addOption(usernameOption);
    final CommandLine commandLine;
    try {
      commandLine = parser.parse(options, args);
      /*
      return new LoginCommand(chatId, commandLine.getOptionValue(usernameOption),
          commandLine.getOptionValue(passwordOption));
       */
      return new LoginCommand(chatId, commandLine.getOptionValue(passwordOption));
    } catch (final ParseException e) {
      //return new LoginCommand(chatId, null, null);
      return new LoginCommand(chatId, null);
    }
  }

  public Command createInput(final long chatId, final String input) {
    return new InputCommand(chatId, input);
  }

  public Command createStart(final long chatId) {
    return new StartCommand(chatId);
  }

  public Command createCancel(final long chatId) {
    return new CancelCommand(chatId);
  }

  public Command createPrintsLeft(final long chatId) {
    return new PrintsLeftCommand(chatId, api);
  }

  public Command createConversion(final long chatId, final String[] args) {
    final Options options = new Options();
    final Option printerAction1Option = Option.builder().longOpt("from").hasArg(true).desc("From printerAction").build();
    final Option printerAction2Option = Option.builder().longOpt("to").hasArg(true).desc("To printerAction").build();
    final Option amountOption = Option.builder().longOpt("amount").hasArg(true).desc("Amount").build();
    options.addOption(printerAction1Option).addOption(printerAction2Option).addOption(amountOption);
    final CommandLine commandLine;
    try {
      commandLine = parser.parse(options, args);
      final String name1 = commandLine.getOptionValue(printerAction1Option);
      final String name2 = commandLine.getOptionValue(printerAction2Option);
      try {
        return new ConversionCommand(chatId, name1 == null ? null : PrinterAction.valueOf(name1),
            name2 == null ? null : PrinterAction.valueOf(name2), Integer.valueOf(commandLine.getOptionValue(amountOption)));
      } catch (final NumberFormatException e) {
        return new ConversionCommand(chatId, name1 == null ? null : PrinterAction.valueOf(name1),
            name2 == null ? null : PrinterAction.valueOf(name2), null);
      }
    } catch (final ParseException e) {
      return new ConversionCommand(chatId, null, null, null);
    }
  }

  public Command createLogOut(final long chatId) {
    return new LogOutCommand(chatId);
  }

}
