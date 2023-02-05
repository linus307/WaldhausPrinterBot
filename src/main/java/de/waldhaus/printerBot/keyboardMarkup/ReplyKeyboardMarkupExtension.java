package de.waldhaus.printerBot.keyboardMarkup;

import de.waldhaus.printerApi.PrinterAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class ReplyKeyboardMarkupExtension extends ReplyKeyboardMarkup {

  private KEYBOARD_MARKUP keyboardMarkup;

  public ReplyKeyboardMarkupExtension(final List<KeyboardRow> keyboard) {
    super(keyboard);
  }

  public static ReplyKeyboardMarkupExtension createLoginKeyboard() {
    final KeyboardButton login = new KeyboardButton("/login");
    final KeyboardRow loginRow = new KeyboardRow(Collections.singleton(login));
    final ReplyKeyboardMarkupExtension replyKeyboardMarkup = new ReplyKeyboardMarkupExtension(List.of(loginRow));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setKeyboardMarkup(KEYBOARD_MARKUP.LOGIN);
    return replyKeyboardMarkup;
  }

  public static ReplyKeyboardMarkupExtension createCancelKeyboard() {
    final KeyboardButton cancel = new KeyboardButton("/cancel");
    final KeyboardRow cancelRow = new KeyboardRow(Collections.singleton(cancel));
    final ReplyKeyboardMarkupExtension replyKeyboardMarkup = new ReplyKeyboardMarkupExtension(List.of(cancelRow));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setKeyboardMarkup(KEYBOARD_MARKUP.CANCEL);
    return replyKeyboardMarkup;
  }

  public static ReplyKeyboardMarkupExtension createLoggedInKeyboard() {
    final KeyboardButton printsLeft = new KeyboardButton("/prints_left");
    final KeyboardButton convert = new KeyboardButton("/convert");
    final KeyboardRow upperRow = new KeyboardRow(List.of(printsLeft, convert));
    final KeyboardButton logOut = new KeyboardButton("/logout");
    final KeyboardRow lowerRow = new KeyboardRow(Collections.singleton(logOut));
    final ReplyKeyboardMarkupExtension replyKeyboardMarkup = new ReplyKeyboardMarkupExtension(List.of(upperRow, lowerRow));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setKeyboardMarkup(KEYBOARD_MARKUP.LOGGED_IN);
    return replyKeyboardMarkup;
  }

  public static ReplyKeyboardMarkupExtension createConversionActionKeyboard(final Set<PrinterAction> actionSet) {
    final ArrayList<KeyboardRow> keyboardRowList = new ArrayList<>(
        Arrays.stream(PrinterAction.values()).filter(actionSet::contains)
            .map(printerAction -> new KeyboardRow(Collections.singleton(new KeyboardButton(printerAction.name())))).toList());
    final KeyboardRow cancel = new KeyboardRow(Collections.singleton(new KeyboardButton("/cancel")));
    keyboardRowList.add(cancel);
    final ReplyKeyboardMarkupExtension replyKeyboardMarkup = new ReplyKeyboardMarkupExtension(keyboardRowList);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setKeyboardMarkup(KEYBOARD_MARKUP.CONVERSION_ACTION);
    return replyKeyboardMarkup;
  }

  public KEYBOARD_MARKUP getKeyboardMarkup() {
    return keyboardMarkup;
  }

  public void setKeyboardMarkup(final KEYBOARD_MARKUP keyboardMarkup) {
    this.keyboardMarkup = keyboardMarkup;
  }

  public enum KEYBOARD_MARKUP {
    LOGIN,
    CANCEL,
    LOGGED_IN,
    CONVERSION_ACTION
  }

}
