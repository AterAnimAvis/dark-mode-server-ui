/*
 * Copyright (c) AterAnimAvis
 * SPDX-License-Identifier: MIT
 */
package com.github.ateranimavis.darkmodeserverui.mixins;

import java.awt.Color;

import com.github.ateranimavis.darkmodeserverui.DarkMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.server.gui.StatsComponent;

@Mixin(StatsComponent.class)
public class MixinStatsComponent {

    @ModifyArg(method = "paint", at = @At(value = "INVOKE", target = "Ljava/awt/Graphics;setColor(Ljava/awt/Color;)V", ordinal = 0))
    Color dark_mode_server_ui$modifyBackground(Color backgroundColor) {
        return DarkMode.Context.BACKGROUND.modify(backgroundColor);
    }

    /* TODO: Bar Colors
    @ModifyArg(method = "paint", at = @At(value = "INVOKE", target = "Ljava/awt/Graphics;setColor(Ljava/awt/Color;)V", ordinal = 1))
    Color dark_mode_server_ui$modifyBarColor(Color barColor) {
        return DarkMode.Context.BAR.modify(barColor);
    }
    */

    @ModifyArg(method = "paint", at = @At(value = "INVOKE", target = "Ljava/awt/Graphics;setColor(Ljava/awt/Color;)V", ordinal = 2))
    Color dark_mode_server_ui$modifyFontColor(Color fontColor) {
        return DarkMode.Context.FONT.modify(fontColor);
    }

}
