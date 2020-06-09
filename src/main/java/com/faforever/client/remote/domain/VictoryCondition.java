package com.faforever.client.remote.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

public enum VictoryCondition {
  // Order is crucial
  DEMORALIZATION(0),
  DOMINATION(1),
  ERADICATION(2),
  SANDBOX(3),
  UNKNOWN("unknown");

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final Map<Object, VictoryCondition> fromNumber;

  static {
    fromNumber = new HashMap<>();
    for (VictoryCondition victoryCondition : values()) {
      fromNumber.put(victoryCondition.value, victoryCondition);
    }
  }

  private final Object value;

  VictoryCondition(Object value) {
    this.value = value;
  }

  @JsonCreator
  public static VictoryCondition fromNumber(Object number) {
    VictoryCondition victoryCondition = fromNumber.get(number);
    if (victoryCondition == null) {
      logger.warn("Unknown victory condition: {}", number);
      return UNKNOWN;
    }
    return victoryCondition;
  }

  @JsonValue
  public Object getValue() {
    return value;
  }
}
