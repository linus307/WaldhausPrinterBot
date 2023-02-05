package de.waldhaus.printerApi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

public abstract class PrinterApi {

  protected final Map<PrinterAction, Map<PrinterAction, Double>> conversionMap = Map.of(PrinterAction.COLOR_PRINT,
      Map.of(PrinterAction.COLOR_PRINT, 1., PrinterAction.COLOR_COPY, 1., PrinterAction.BW_PRINT, 4., PrinterAction.BW_COPY, 4.),
      PrinterAction.COLOR_COPY,
      Map.of(PrinterAction.COLOR_PRINT, 1., PrinterAction.COLOR_COPY, 1., PrinterAction.BW_PRINT, 4., PrinterAction.BW_COPY, 4.),
      PrinterAction.BW_PRINT,
      Map.of(PrinterAction.COLOR_PRINT, 0.25, PrinterAction.COLOR_COPY, 0.25, PrinterAction.BW_PRINT, 1., PrinterAction.BW_COPY,
          1.), PrinterAction.BW_COPY,
      Map.of(PrinterAction.COLOR_PRINT, 0.25, PrinterAction.COLOR_COPY, 0.25, PrinterAction.BW_PRINT, 1., PrinterAction.BW_COPY,
          1.));

  protected final Map<PrinterAction, Set<PrinterAction>> allowedConversions = Map.of(PrinterAction.COLOR_PRINT,
      Set.of(PrinterAction.COLOR_COPY), PrinterAction.COLOR_COPY, Set.of(PrinterAction.COLOR_PRINT), PrinterAction.BW_PRINT,
      Set.of(PrinterAction.BW_COPY), PrinterAction.BW_COPY, Set.of(PrinterAction.BW_PRINT), PrinterAction.SCAN, Set.of());

  public abstract String getUsername(String password) throws URISyntaxException, IOException, InterruptedException;

  public abstract String getPassword(String username) throws URISyntaxException, IOException, InterruptedException;

  public abstract boolean checkPassword(String password) throws URISyntaxException, IOException, InterruptedException;

  public abstract Map<PrinterAction, Integer> getAmounts(String password)
      throws URISyntaxException, IOException, InterruptedException;

  public abstract Map<PrinterAction, Integer> getAvailable(String password)
      throws URISyntaxException, IOException, InterruptedException;

  public abstract void setAmounts(String username, String password, Map<PrinterAction, Integer> amounts)
      throws URISyntaxException, IOException, InterruptedException;

  public abstract int getActionAvailable(String password, PrinterAction action)
      throws URISyntaxException, IOException, InterruptedException;

  public abstract int getActionAmount(String password, PrinterAction action)
      throws URISyntaxException, IOException, InterruptedException;

  public abstract void setAction(String username, String password, PrinterAction action, int amount)
      throws URISyntaxException, IOException, InterruptedException;

  public abstract void convertAction(String username, String password, PrinterAction action1, PrinterAction action2, int amount)
      throws URISyntaxException, IOException, InterruptedException;

  public Map<PrinterAction, Map<PrinterAction, Double>> getConversionMap() {
    return conversionMap;
  }

  public Set<PrinterAction> getAllowedConversions(final PrinterAction printerAction) {
    return allowedConversions.get(printerAction);
  }

  public boolean checkConversion(final PrinterAction printerAction1, final PrinterAction printerAction2) {
    return allowedConversions.get(printerAction1).contains(printerAction2);
  }
}
