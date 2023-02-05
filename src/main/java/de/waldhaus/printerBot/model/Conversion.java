package de.waldhaus.printerBot.model;


import de.waldhaus.printerApi.PrinterAction;

public class Conversion {

  private PrinterAction printerAction1;
  private PrinterAction printerAction2;
  private Integer amount;

  public Conversion() {
  }

  public PrinterAction getPrinterAction1() {
    return printerAction1;
  }

  public void setPrinterAction1(final PrinterAction printerAction1) {
    this.printerAction1 = printerAction1;
  }

  public PrinterAction getPrinterAction2() {
    return printerAction2;
  }

  public void setPrinterAction2(final PrinterAction printerAction2) {
    this.printerAction2 = printerAction2;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(final Integer amount) {
    this.amount = amount;
  }
}
