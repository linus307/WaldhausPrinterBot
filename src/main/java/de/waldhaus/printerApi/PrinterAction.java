package de.waldhaus.printerApi;

public enum PrinterAction {
  COLOR_PRINT("colored prints"),
  COLOR_COPY("colored copies"),
  BW_PRINT("black prints"),
  BW_COPY("black copies"),
  SCAN("scans");

  public final String label;

  PrinterAction(final String label) {
    this.label = label;
  }
}
