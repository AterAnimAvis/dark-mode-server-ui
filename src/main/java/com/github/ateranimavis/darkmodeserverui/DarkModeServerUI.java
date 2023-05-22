/*
 * Copyright (c) AterAnimAvis
 * SPDX-License-Identifier: MIT
 */
package com.github.ateranimavis.darkmodeserverui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(DarkModeServerUI.MOD_ID)
public class DarkModeServerUI {

    public static final String MOD_ID = "dark_mode_server_ui";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public DarkModeServerUI() {
        //TODO:
        // ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DarkModeConfig.get());

        try {
            DarkMode.install();
        } catch (Exception e) {
            LOGGER.warn("Could not initialize DarkModeServerUI", e);
        }
    }

}
