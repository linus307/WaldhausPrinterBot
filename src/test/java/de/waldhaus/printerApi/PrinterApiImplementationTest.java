package de.waldhaus.printerApi;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PrinterApiImplementationTest {

  static PrinterApiImplementation api;

  @BeforeAll
  static void beforeAll() throws IOException, URISyntaxException, InterruptedException {
    api = new PrinterApiImplementation();
  }

  @Test
  void getPassword() throws URISyntaxException, IOException, InterruptedException {
    org.junit.jupiter.api.Assertions.assertEquals("710904start1337", api.getPassword("71-09-04-LiJa"));
  }

  @Test
  void getAmounts() throws URISyntaxException, IOException, InterruptedException {
    api.getAmounts("71-09-04-LiJa");
    System.out.println();
  }

  @Test
  void setAmounts() {
  }

  @Test
  void getAction() throws URISyntaxException, IOException, InterruptedException {
    System.out.println(api.getActionAvailable("71-09-04-LiJa", PrinterAction.COLOR_PRINT));
  }

  @Test
  void setAction() throws URISyntaxException, IOException, InterruptedException {
    //api.setAction("71-09-04-LiJa", PrinterAction.COLOR_PRINT, 15);
    System.out.println(api.getActionAvailable("71-09-04-LiJa", PrinterAction.COLOR_PRINT));
  }

  @Test
  void convertAction() throws URISyntaxException, IOException, InterruptedException {
    //api.convertAction("71-09-04-LiJa", PrinterAction.COLOR_PRINT, PrinterAction.BW_PRINT, 15);
  }
}