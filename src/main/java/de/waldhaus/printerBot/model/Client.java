package de.waldhaus.printerBot.model;

import de.waldhaus.printerBot.keyboardMarkup.ReplyKeyboardMarkupExtension;
import de.waldhaus.printerBot.state.State;
import java.time.Instant;
import java.util.Optional;

public class Client {

  private final long clientId;
  private String username;
  private String password;
  private State state;
  private boolean loggedIn;
  private ReplyKeyboardMarkupExtension keyboardMarkup;
  private long timeout;
  private Instant lastLoginTime;
  private Conversion conversion = new Conversion();

  public Client(final long clientId, final State state) {
    this.clientId = clientId;
    this.state = state;
    this.username = null;
    this.password = null;
    this.loggedIn = false;
    this.keyboardMarkup = ReplyKeyboardMarkupExtension.createLoginKeyboard();
    this.timeout = 5;
    this.lastLoginTime = Instant.now().minusSeconds(10);
  }

  public Client(final long clientId, final String username, final String password, final State state, final boolean loggedIn,
      final ReplyKeyboardMarkupExtension keyboardMarkup, final long timeout, final Instant lastLoginTime) {
    this.clientId = clientId;
    this.username = username;
    this.password = password;
    this.state = state;
    this.loggedIn = loggedIn;
    this.keyboardMarkup = keyboardMarkup;
    this.timeout = timeout;
    this.lastLoginTime = lastLoginTime; // TODO: add conversion in backup
  }

  public void logOut() {
    this.username = null;
    this.password = null;
    this.loggedIn = false;
    resetConversion();
    resetTimeout();
  }

  public Optional<String> getUsername() {
    return Optional.ofNullable(username);
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public Optional<String> getPassword() {
    return Optional.ofNullable(password);
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public State getState() {
    return state;
  }

  public void setState(final State state) {
    this.state = state;
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(final boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  public long getClientId() {
    return clientId;
  }

  public ReplyKeyboardMarkupExtension getKeyboardMarkup() {
    return keyboardMarkup;
  }

  public void setKeyboardMarkup(final ReplyKeyboardMarkupExtension keyboardMarkup) {
    this.keyboardMarkup = keyboardMarkup;
  }

  public long getTimeout() {
    return timeout;
  }

  public void resetTimeout() {
    this.timeout = 5;
  }

  public void increaseTimeout() {
    this.timeout *= 2;
  }

  public Instant getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(final Instant lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public boolean testLoginTime() {
    return Instant.now().isAfter(lastLoginTime.plusSeconds(timeout));
  }

  public Conversion getConversion() {
    return conversion;
  }

  public void resetConversion() {
    this.conversion = new Conversion();
  }
}
