package de.waldhaus;

import de.waldhaus.printerBot.PrinterBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

  public static void main(final String[] args) {
    try {
      final TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
      botsApi.registerBot(new PrinterBot());
    } catch (final TelegramApiException e) {
      e.printStackTrace();
    }
  }
}
