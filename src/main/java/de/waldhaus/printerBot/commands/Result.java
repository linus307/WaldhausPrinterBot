package de.waldhaus.printerBot.commands;

import de.waldhaus.printerBot.state.State.StateName;

public enum Result {
  PROCEED,
  RETRY,
  END;

  private StateName state;

  public StateName getState() {
    return state;
  }

  public Result addState(final StateName state) {
    this.state = state;
    return this;
  }
}
