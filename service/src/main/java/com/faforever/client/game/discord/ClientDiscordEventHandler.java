package com.faforever.client.discord;


import com.faforever.client.notification.NotificationService;
import com.faforever.client.preferences.PreferencesService;
import lombok.extern.slf4j.Slf4j;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRPC.DiscordReply;
import net.arikia.dev.drpc.DiscordUser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.faforever.client.discord.DiscordSpectateSecret;
import com.faforever.client.discord.DiscordSpectateEvent;
import com.faforever.client.discord.DiscordJoinEvent;
import com.faforever.client.discord.DiscordJoinSecret;

import static com.github.nocatch.NoCatch.noCatch;

@Slf4j
@Component
public class ClientDiscordEventHandler extends DiscordEventHandlers {
  private final ApplicationEventPublisher applicationEventPublisher;
  private final NotificationService notificationService;
  private final PreferencesService preferencesService;
  private final ObjectMapper objectMapper;

  public ClientDiscordEventHandler(
    ApplicationEventPublisher applicationEventPublisher,
    NotificationService notificationService,
    PreferencesService preferencesService
  ) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.notificationService = notificationService;
    this.preferencesService = preferencesService;
    objectMapper = new ObjectMapper();

    ready = this::onDiscordReady;
    disconnected = this::onDisconnected;
    errored = this::onError;
    spectateGame = this::onSpectate;
    joinGame = this::onJoinGame;
    joinRequest = this::onJoinRequest;
  }

  private void onJoinRequest(DiscordUser discordUser) {
    DiscordRPC.discordRespond(discordUser.userId, DiscordReply.YES);
  }

  private void onJoinGame(String joinSecret) {
    DiscordJoinSecret discordJoinSecret = noCatch(() -> objectMapper.readValue(joinSecret, DiscordJoinSecret.class));
    try {
      applicationEventPublisher.publishEvent(new DiscordJoinEvent(discordJoinSecret.getGameId()));
    } catch (Exception e) {
      notificationService.addImmediateErrorNotification(e, "game.couldNotJoin", discordJoinSecret.getGameId());
      log.error("Could not join game from discord rich presence", e);
    }
  }

  private void onSpectate(String spectateSecret) {
    DiscordSpectateSecret discordSpectateSecret = noCatch(() -> objectMapper.readValue(spectateSecret, DiscordSpectateSecret.class));
    try {
      applicationEventPublisher.publishEvent(new DiscordSpectateEvent(discordSpectateSecret.getGameId(), discordSpectateSecret.getPlayerId()));
    } catch (Exception e) {
      notificationService.addImmediateErrorNotification(e, "replay.couldNotOpen", discordSpectateSecret.getGameId());
      log.error("Could not join game from discord rich presence", e);
    }
  }

  private void onError(int errorCode, String message) {
    log.error("Discord error with code '{}' and message '{}'", errorCode, message);
  }

  private void onDisconnected(int code, String message) {
    log.info("Discord disconnected with code '{}' and message '{}'", code, message);
  }

  private void onDiscordReady(DiscordUser discordUser) {
    log.info("Discord is ready with user '{}'", discordUser.username);
  }
}