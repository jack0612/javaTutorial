package com.pokemonreview.api.WebClient.Twilio;

import lombok.Data;

@Data
public class TwilioMessageRequest {
    private final String to;
    private final String from;
    private final String body;
}
