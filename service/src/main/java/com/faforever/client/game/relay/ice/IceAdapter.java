package com.faforever.client.game.relay.ice;

import java.util.concurrent.CompletableFuture;

/**
 * Starts or stops the ICE adapter process.
 */
public interface IceAdapter {
  CompletableFuture<Integer> start();

  void stop();
}