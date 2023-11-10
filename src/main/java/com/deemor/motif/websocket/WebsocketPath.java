package com.deemor.motif.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WebsocketPath {
    NEW_ALERT_SPECIFIC_USER("/queue/alert-added");

    private final String path;

}
