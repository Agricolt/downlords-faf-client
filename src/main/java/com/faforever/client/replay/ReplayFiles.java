package com.faforever.client.replay;

import com.faforever.client.remote.domain.GameAccess;
import com.faforever.client.remote.domain.VictoryCondition;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class ReplayFiles {

  public static final GsonBuilder GSON_BUILDER = new GsonBuilder()
      .disableHtmlEscaping()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .registerTypeAdapter(GameAccess.class, GameAccessTypeAdapter.INSTANCE)
      .registerTypeAdapter(VictoryCondition.class, VictoryConditionTypeAdapter.INSTANCE);

  private ReplayFiles() {
    // Utility class
  }

  public static Gson gson() {
    return GSON_BUILDER.create();
  }
}
