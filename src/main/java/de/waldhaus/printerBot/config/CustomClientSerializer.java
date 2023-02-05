package de.waldhaus.printerBot.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.waldhaus.printerBot.model.Client;
import java.io.IOException;

public final class CustomClientSerializer extends StdSerializer<Client> {

  public CustomClientSerializer() {
    this(null);
  }

  public CustomClientSerializer(final Class<Client> t) {
    super(t);
  }

  @Override
  public void serialize(final Client client, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("client_id", client.getClientId());
    if (client.getUsername().isEmpty()) {
      gen.writeNullField("username");
    } else {
      gen.writeStringField("username", client.getUsername().get());
    }
    if (client.getPassword().isEmpty()) {
      gen.writeNullField("password");
    } else {
      gen.writeStringField("password", client.getPassword().get());
    }
    gen.writeBooleanField("logged_in", client.isLoggedIn());
    gen.writeStringField("state", client.getState().getStateName().name());
    gen.writeStringField("reply_keyboard_markup", client.getKeyboardMarkup().getKeyboardMarkup().name());
    gen.writeNumberField("timeout", client.getTimeout());
    gen.writeNumberField("last_login_time", client.getLastLoginTime().getEpochSecond());
    gen.writeEndObject();
  }
}
