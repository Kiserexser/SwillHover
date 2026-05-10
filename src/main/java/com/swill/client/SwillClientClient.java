package com.swill.client;

import com.swill.client.core.AirHoverKeyBind;
import net.fabricmc.api.ClientModInitializer;

public class SwillClientClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AirHoverKeyBind.register();
    }
}
