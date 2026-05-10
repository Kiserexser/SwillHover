package com.swill.client.modules.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class AirHoverToggle {
    private static boolean active = false;
    private static double lockedY = -1;
    
    public static boolean isActive() { return active; }
    
    public static void enable() {
        if (active) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null || player.isOnGround()) {
            if (player != null) player.sendMessage(net.minecraft.text.Text.literal("§cНужно прыгнуть сначала"), true);
            return;
        }
        lockedY = player.getY();
        active = true;
        player.sendMessage(net.minecraft.text.Text.literal("§aВИСИМ"), true);
    }
    
    public static void disable() {
        if (!active) return;
        active = false;
        lockedY = -1;
        MinecraftClient.getInstance().player.getAbilities().flying = false;
        MinecraftClient.getInstance().player.sendMessage(net.minecraft.text.Text.literal("§cПАДАЕМ"), true);
    }
    
    public static void toggle() {
        if (active) disable();
        else enable();
    }
    
    public static void onGameTick() {
        if (!active) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (player.isOnGround()) { active = false; return; }
        
        player.setVelocity(player.getVelocity().x, 0, player.getVelocity().z);
        player.updatePosition(player.getX(), lockedY, player.getZ());
        
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.PositionAndOnGround(
                player.getX(), lockedY + 0.0001, player.getZ(), false
            );
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
        }
    }
}
