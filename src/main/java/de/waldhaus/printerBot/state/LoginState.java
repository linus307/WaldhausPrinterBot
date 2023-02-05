package de.waldhaus.printerBot.state;

import de.waldhaus.printerApi.PrinterApiImplementation;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.commands.Command;
import de.waldhaus.printerBot.commands.Result;
import de.waldhaus.printerBot.keyboardMarkup.ReplyKeyboardMarkupExtension;
import de.waldhaus.printerBot.model.Client;
import de.waldhaus.printerBot.model.Model;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class LoginState extends State {

  public LoginState(final Model model, final PrinterApiImplementation api, final PrinterBot bot) {
    super(model, api, bot);
  }

  @Override
  public State onUpdateReceived(final Client client, final Command command) {
    /*
    final Result result =
        client.getUsername().isEmpty() ? command.execute(model, Phase.LOGIN_USERNAME, bot)
            : command.execute(model, Phase.LOGIN_PASSWORD, bot);
     */
    final Result result = command.execute(model, Phase.LOGIN_PASSWORD, bot);
    if (result == Result.RETRY) {
      return checkLogin(client);
    } else if (result == Result.END) {
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createLoginKeyboard());
      bot.sendMessage(client.getClientId(), "Login was cancelled.");
      client.logOut();
      return new StartState(model, api, bot);
    }
    return this;
  }

  @Override
  public StateName getStateName() {
    return StateName.LOGIN;
  }

  public State checkLogin(final Client client) {
    if (!client.testLoginTime()) {
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createLoginKeyboard());
      bot.sendMessage(client.getClientId(), String.format("You're currently not allowed to login. Please wait %d seconds.",
          client.getTimeout() - ChronoUnit.SECONDS.between(client.getLastLoginTime(), Instant.now())));
      client.logOut();
      return new StartState(model, api, bot);
    }
    /*
    if (client.getUsername().isEmpty()) {
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createCancelKeyboard());
      bot.sendMessage(client.getClientId(), "Type in your username now.");
      return this;
    } else
    */
    if (client.getPassword().isEmpty()) {
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createCancelKeyboard());
      bot.sendMessage(client.getClientId(), "Type in your password now.");
      return this;
    }
    try {
      client.setLastLoginTime(Instant.now());
      /*
      final String username = client.getUsername().get();
      if (Objects.equals(api.getPassword(username), client.getPassword().get())) {
       */
      final String password = client.getPassword().get();
      if (api.checkPassword(password)) {
        final String username = api.getUsername(password);
        client.setUsername(username);
        client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createLoggedInKeyboard());
        bot.sendMessage(client.getClientId(), String.format("%s you are now logged in.", username));
        client.setLoggedIn(true);
        client.resetTimeout();
        return new LoggedInState(model, api, bot);
      }
      client.setKeyboardMarkup(ReplyKeyboardMarkupExtension.createLoginKeyboard());
      bot.sendMessage(client.getClientId(), "Login was unsuccessful.");
      client.logOut();
      client.increaseTimeout();
      return new StartState(model, api, bot);
    } catch (final URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
