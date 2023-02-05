package de.waldhaus.printerBot.commands;

import de.waldhaus.printerApi.PrinterAction;
import de.waldhaus.printerApi.PrinterApiImplementation;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.model.Model;
import de.waldhaus.printerBot.state.State.Phase;
import de.waldhaus.printerBot.state.State.StateName;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PrintsLeftCommand extends Command {

  private final PrinterApiImplementation api;

  public PrintsLeftCommand(final long chatId, final PrinterApiImplementation api) {
    super(chatId);
    this.api = api;
  }

  @Override
  public Set<Phase> inPhase() {
    return Set.of(Phase.LOGGED_IN);
  }

  @Override
  protected Result run(final Model model, final PrinterBot bot) {
    try {
      final Map<PrinterAction, Integer> map = api.getAmounts(model.getClient(chatId).get().getPassword().get());
      bot.sendMessage(chatId, Arrays.stream(PrinterAction.values()).filter(printerAction -> !"SCAN".equals(printerAction.name()))
          .map(printerAction -> String.format("%s: %d", printerAction.label, map.get(printerAction)))
          .collect(Collectors.joining("\n")));
    } catch (final URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
    return Result.PROCEED.addState(StateName.LOGGED_IN);
  }
}
