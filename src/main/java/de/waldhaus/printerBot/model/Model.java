package de.waldhaus.printerBot.model;

import de.waldhaus.printerApi.PrinterApiImplementation;
import de.waldhaus.printerBot.PrinterBot;
import de.waldhaus.printerBot.state.StartState;
import java.util.HashMap;
import java.util.Optional;

public class Model {

  private HashMap<Long, Client> clients;

  public Model() {
    this.clients = new HashMap<>();
  }

  public Client createClient(final long clientId, final PrinterApiImplementation api, final PrinterBot bot) {
    clients.put(clientId, new Client(clientId, new StartState(this, api, bot)));
    return clients.get(clientId);
  }

  public Optional<Client> getClient(final long clientId) {
    return Optional.ofNullable(clients.get(clientId));
  }

  public HashMap<Long, Client> getClients() {
    return clients;
  }

  public void setClients(final HashMap<Long, Client> clients) {
    this.clients = clients;
  }
}
