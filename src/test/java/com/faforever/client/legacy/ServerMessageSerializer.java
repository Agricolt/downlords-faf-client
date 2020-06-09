package com.faforever.client.legacy;

import com.faforever.client.remote.JsonMessageSerializer;
import com.faforever.client.remote.domain.FafServerMessage;
import com.faforever.client.remote.domain.FafServerMessageType;
import com.faforever.client.remote.domain.MessageTarget;
import com.faforever.client.remote.domain.RatingRange;
import com.google.gson.GsonBuilder;

public class ServerMessageSerializer extends JsonMessageSerializer<FafServerMessage> {

  @Override
  protected void addTypeAdapters(GsonBuilder gsonBuilder) {
    super.addTypeAdapters(gsonBuilder);

    gsonBuilder.registerTypeAdapter(MessageTarget.class, MessageTargetTypeAdapter.INSTANCE);
    gsonBuilder.registerTypeAdapter(FafServerMessageType.class, ServerMessageTypeTypeAdapter.INSTANCE);
    gsonBuilder.registerTypeAdapter(RatingRange.class, RatingRangeTypeAdapter.INSTANCE);
  }
}
