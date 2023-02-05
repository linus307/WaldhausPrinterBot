package de.waldhaus.printerBot;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.waldhaus.printerApi.PrinterApiImplementation;
import de.waldhaus.printerBot.commands.Command;
import de.waldhaus.printerBot.commands.CommandFactory;
import de.waldhaus.printerBot.config.CustomClientDeserializer;
import de.waldhaus.printerBot.config.CustomClientSerializer;
import de.waldhaus.printerBot.model.Client;
import de.waldhaus.printerBot.model.Model;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PrinterBot extends TelegramLongPollingBot {

  private final Model model;
  private final CommandFactory commandFactory;
  private final PrinterApiImplementation api;
  private final ObjectMapper mapper;

  public PrinterBot() {
    this.model = new Model();
    this.api = new PrinterApiImplementation();
    this.commandFactory = new CommandFactory(api);
    mapper = new ObjectMapper();
    final SimpleModule customClientSerializer = new SimpleModule("CustomClientSerializer",
        new Version(1, 0, 0, null, null, null));
    customClientSerializer.addSerializer(Client.class, new CustomClientSerializer());
    final SimpleModule customClientDeserializer = new SimpleModule("CustomClientDeserializer",
        new Version(1, 0, 0, null, null, null));
    customClientDeserializer.addDeserializer(Client.class, new CustomClientDeserializer(model, api, this));
    mapper.registerModule(customClientSerializer);
    mapper.registerModule(customClientDeserializer);
    final File f = new File("clients.json");
    if (f.exists() && !f.isDirectory()) {
      try {
        final HashMap<Long, Client> clients = new HashMap<>();
        Arrays.stream(mapper.readValue(f, Client[].class)).forEach(client -> clients.put(client.getClientId(), client));
        this.model.setClients(clients);
      } catch (final IOException ignored) {
      }
    }
  }

  @Override
  public String getBotUsername() {
    return "";
  }

  @Override
  public String getBotToken() {
    return "";
  }

  @Override
  public void onUpdateReceived(final Update update) {
    final long chatId = update.getMessage().getChatId();
    final Optional<Client> optional = model.getClient(chatId);
    final Client client = optional.orElseGet(() -> model.createClient(chatId, api, this));
    final Command command = commandFactory.createCommand(update);
    client.setState(client.getState().onUpdateReceived(client, command));
    try {
      mapper.writeValue(new File("clients.json"), model.getClients().values());
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendMessage(final long chatId, final String message) {
    final SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setText(message);
    sendMessage.setReplyMarkup(model.getClient(chatId).get().getKeyboardMarkup());
    try {
      execute(sendMessage);
    } catch (final TelegramApiException ignored) {
    }
  }
}
