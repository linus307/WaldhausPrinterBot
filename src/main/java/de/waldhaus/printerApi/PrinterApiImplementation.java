package de.waldhaus.printerApi;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class PrinterApiImplementation extends PrinterApi {

  private final Map<PrinterAction, String> xpathsAmounts = Map.of(PrinterAction.COLOR_PRINT,
      "/html/body/form[1]/div[4]/table/tbody/tr[2]/td[3]/input", PrinterAction.COLOR_COPY,
      "/html/body/form[1]/div[4]/table/tbody/tr[4]/td[3]/input", PrinterAction.BW_PRINT,
      "/html/body/form[1]/div[5]/table/tbody/tr[2]/td[3]/input", PrinterAction.BW_COPY,
      "/html/body/form[1]/div[5]/table/tbody/tr[4]/td[3]/input", PrinterAction.SCAN,
      "/html/body/form[1]/div[6]/table/tbody/tr[2]/td[3]/input");

  private final Map<PrinterAction, String> xpathsAvailable = Map.of(PrinterAction.COLOR_PRINT,
      "/html/body/form[1]/div[4]/table/tbody/tr[2]/td[5]", PrinterAction.COLOR_COPY,
      "/html/body/form[1]/div[4]/table/tbody/tr[4]/td[5]", PrinterAction.BW_PRINT,
      "/html/body/form[1]/div[5]/table/tbody/tr[2]/td[5]", PrinterAction.BW_COPY,
      "/html/body/form[1]/div[5]/table/tbody/tr[4]/td[5]", PrinterAction.SCAN,
      "/html/body/form[1]/div[6]/table/tbody/tr[2]/td[5]");

  private final WebDriver driver;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  private final String ip = "";
  private String cookie;
  private HttpClient client;

  public PrinterApiImplementation() {
    this.client = HttpClient.newBuilder().build();
    this.driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
    driver.get(String.format("http://%s/properties/authentication/login.php", ip));
    driver.findElement(new ByXPath("/html/body/form/div[1]/div[2]/div[1]/input")).sendKeys("");
    driver.findElement(new ByXPath("/html/body/form/div[1]/div[2]/div[2]/input")).sendKeys("");
    driver.findElement(new ByXPath("/html/body/form/div[2]/button")).click();
    driver.get(String.format("http://%s", ip));
    this.cookie = driver.manage().getCookieNamed("PHPSESSID").getValue();
    reloadPage();
  }

  private void reloadPage() {
    final Runnable reloadPage = () -> driver.get(String.format("http://%s", ip));
    scheduler.scheduleAtFixedRate(reloadPage, 2, 2, MINUTES);
  }

  @Override
  public String getUsername(final String password) throws URISyntaxException, IOException, InterruptedException {
    final HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(String.format("http://%s/properties/accounting/XSA_manage.php", ip)))
        .setHeader("Cookie", String.format("PHPSESSID=%s", cookie)).GET().build();
    final HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    final Document doc = Jsoup.parse(response.body());
    final Elements e = doc.select("td:contains(" + password + ")");
    return e.isEmpty() || !e.get(0).text().equals(password) ? null : e.get(0).parent().child(2).text();
  }

  @Override
  public String getPassword(final String username) throws URISyntaxException, IOException, InterruptedException {
    final HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(String.format("http://%s/properties/accounting/XSA_manage.php", ip)))
        .setHeader("Cookie", String.format("PHPSESSID=%s", cookie)).GET().build();
    final HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    final Document doc = Jsoup.parse(response.body());
    final Elements e = doc.select("td:contains(" + username + ")");
    return e.isEmpty() || !e.get(0).text().equals(username) ? null : e.get(0).parent().child(1).text();
  }

  @Override
  public boolean checkPassword(final String password) throws URISyntaxException, IOException, InterruptedException {
    final HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(String.format("http://%s/properties/accounting/XSA_manage.php", ip)))
        .setHeader("Cookie", String.format("PHPSESSID=%s", cookie)).GET().build();
    final HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    final Document doc = Jsoup.parse(response.body());
    final Elements e = doc.select("td:contains(" + password + ")");
    return !e.isEmpty() && e.get(0).parent().child(1).text().equals(password);
  }

  @Override
  public HashMap<PrinterAction, Integer> getAmounts(final String password)
      throws URISyntaxException, IOException, InterruptedException {
    final String urlString = String.format("http://%s/properties/accounting/XSA_manage_limits.php?acctid=%s", ip, password);
    final HttpRequest request = HttpRequest.newBuilder().uri(new URI(urlString))
        .setHeader("Cookie", String.format("PHPSESSID=%s", cookie)).GET().build();
    final HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    final Document doc = Jsoup.parse(response.body());
    final HashMap<PrinterAction, Integer> amounts = new HashMap<>();
    for (final PrinterAction action : PrinterAction.values()) {
      amounts.put(action, Integer.valueOf(doc.selectXpath(xpathsAmounts.get(action)).val()));
    }
    return amounts;
  }

  @Override
  public Map<PrinterAction, Integer> getAvailable(final String password)
      throws URISyntaxException, IOException, InterruptedException {
    final String urlString = String.format("http://%s/properties/accounting/XSA_manage_limits.php?acctid=%s", ip, password);
    final HttpRequest request = HttpRequest.newBuilder().uri(new URI(urlString))
        .setHeader("Cookie", String.format("PHPSESSID=%s", cookie)).GET().build();
    final HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    final Document doc = Jsoup.parse(response.body());
    final HashMap<PrinterAction, Integer> amounts = new HashMap<>();
    for (final PrinterAction action : PrinterAction.values()) {
      amounts.put(action, Integer.valueOf(doc.selectXpath(xpathsAvailable.get(action)).text()));
    }
    return amounts;
  }

  @Override
  public void setAmounts(final String username, final String password, final Map<PrinterAction, Integer> amounts)
      throws URISyntaxException, IOException, InterruptedException {
    final String urlString = String.format("http://%s/properties/accounting/changeLimits"
            + ".php?userID=%s&userName=%s&colorprint=%d&colorcopy=%d&blackprint=%d&blackcopy"
            + "=%d&scan=%d&faxtrans=16000000&faxrcvd=16000000&loggedInUser=&nextPage=Limits" + "&rowcounter=&nameChanged=false", ip,
        password, username, amounts.get(PrinterAction.COLOR_PRINT), amounts.get(PrinterAction.COLOR_COPY),
        amounts.get(PrinterAction.BW_PRINT), amounts.get(PrinterAction.BW_COPY), amounts.get(PrinterAction.SCAN));
    final HttpRequest request = HttpRequest.newBuilder().uri(new URI(urlString))
        .setHeader("Cookie", String.format("PHPSESSID=%s", cookie)).GET().build();
    client.send(request, BodyHandlers.ofString());
  }

  @Override
  public int getActionAvailable(final String password, final PrinterAction action)
      throws URISyntaxException, IOException, InterruptedException {
    return getAvailable(password).get(action);
  }

  @Override
  public int getActionAmount(final String password, final PrinterAction action)
      throws URISyntaxException, IOException, InterruptedException {
    return getAmounts(password).get(action);
  }

  @Override
  public void setAction(final String username, final String password, final PrinterAction action, final int amount)
      throws URISyntaxException, IOException, InterruptedException {
    final HashMap<PrinterAction, Integer> amounts = getAmounts(password);
    amounts.replace(action, amount);
    setAmounts(username, password, amounts);
  }

  @Override
  public void convertAction(final String username, final String password, final PrinterAction action1,
      final PrinterAction action2, final int amount) throws URISyntaxException, IOException, InterruptedException {
    final HashMap<PrinterAction, Integer> amounts = getAmounts(password);
    amounts.replace(action1, amounts.get(action1) - amount);
    amounts.replace(action2, amounts.get(action2) + (int) (amount * conversionMap.get(action1).get(action2)));
    setAmounts(username, password, amounts);
  }

  public String getCookie() {
    return cookie;
  }

  public void setCookie(final String cookie) {
    this.cookie = cookie;
  }

  public HttpClient getClient() {
    return client;
  }

  public void setClient(final HttpClient client) {
    this.client = client;
  }
}
