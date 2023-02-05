package de.waldhaus.printerBot.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.waldhaus.printerApi.PrinterApiImplementation;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.keyboardMarkup.ReplyKeyboardMarkupExtension;
import de.waldhaus.printerBot.model.Client;
import de.waldhaus.printerBot.model.Model;
import de.waldhaus.printerBot.state.LoggedInState;
import de.waldhaus.printerBot.state.LoginState;
import de.waldhaus.printerBot.state.StartState;
import de.waldhaus.printerBot.state.State;
import java.io.IOException;
import java.time.Instant;

public final class CustomClientDeserializer extends StdDeserializer<Client> {

  private Model model;
  private PrinterApiImplementation api;
  private PrinterBot bot;

  public CustomClientDeserializer(final Model model, final PrinterApiImplementation api, final PrinterBot bot) {
    this(null);
    this.model = model;
    this.api = api;
    this.bot = bot;
  }

  public CustomClientDeserializer(final Class<?> vc) {
    super(vc);
  }

  @Override
  public Client deserialize(final JsonParser parser, final DeserializationContext deserializer) {
    final ObjectCodec codec = parser.getCodec();
    final JsonNode node;
    try {
      node = codec.readTree(parser);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }

    final long clientId = node.get("client_id").asLong();
    final String username = node.get("username").asText();
    final String password = node.get("password").asText();
    final State state = switch (node.get("state").asText()) {
      case "START" -> new StartState(model, api, bot);
      case "LOGIN" -> new LoginState(model, api, bot);
      case "LOGGED_IN" -> new LoggedInState(model, api, bot);
      default -> null;
    };
    final boolean loggedIn = node.get("logged_in").asBoolean();
    final ReplyKeyboardMarkupExtension keyboardMarkup = switch (node.get("reply_keyboard_markup").asText()) {
      case "LOGIN" -> ReplyKeyboardMarkupExtension.createLoginKeyboard();
      case "CANCEL" -> ReplyKeyboardMarkupExtension.createCancelKeyboard();
      case "LOGGED_IN" -> ReplyKeyboardMarkupExtension.createLoggedInKeyboard();
      default -> null;
    };
    final long timeout = node.get("timeout").asLong();
    final Instant lastLoginTime = Instant.ofEpochSecond(node.get("last_login_time").asLong());
    return new Client(clientId, username, password, state, loggedIn, keyboardMarkup, timeout, lastLoginTime);
  }
}
