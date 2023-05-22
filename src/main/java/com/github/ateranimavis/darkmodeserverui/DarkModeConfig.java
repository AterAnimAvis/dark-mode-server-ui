/*
 * Copyright (c) AterAnimAvis
 * SPDX-License-Identifier: MIT
 */
package com.github.ateranimavis.darkmodeserverui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

public class DarkModeConfig {

    private static final String IJ_THEME_SPEC = "https://www.jetbrains.org/intellij/sdk/docs/reference_guide/ui_themes/themes_customize.html";

    private static final String FILE_NAME                  = DarkModeServerUI.MOD_ID + ".toml";
    private static final String NORD_THEME_FILE = "nord.theme.json";
    private static final String NORD_LICENSE_FILE = "nord.license.txt";
    private static final String NORD_THEME = DarkModeServerUI.MOD_ID + "/nord/" + NORD_THEME_FILE;
    private static final String NORD_LICENSE = DarkModeServerUI.MOD_ID + "/nord/" + NORD_LICENSE_FILE;

    private static final ForgeConfigSpec CONFIG;

    public static final ForgeConfigSpec.EnumValue<LookAndFeelType> TYPE;
    public static final ForgeConfigSpec.ConfigValue<String> CUSTOM_THEME;

    public static final ForgeConfigSpec.ConfigValue<String> FOREGROUND_BASED_ON;
    public static final ForgeConfigSpec.ConfigValue<String> BACKGROUND_BASED_ON;

    static {
        var builder = new ForgeConfigSpec.Builder();
        TYPE = builder
            .comment("The FlatLAF to use, set to NONE to disable the mod, set to CUSTOM to load a custom theme.json")
            .defineEnum("look-and-feel", LookAndFeelType.DARK);

        CUSTOM_THEME = builder
            .comment("The path to a custom theme.json (IntelliJ Format), used when `look-and-feel` is set to CUSTOM",
                     "The IntelliJ .theme.json file are documented here: " + IJ_THEME_SPEC)
            .define("custom-theme-location", NORD_THEME);

        FOREGROUND_BASED_ON = builder
            .comment("What property (or hex color) the foreground should be based on")
            .define("foreground-based-on", "TextField.foreground");

        BACKGROUND_BASED_ON = builder
            .comment("What property (or hex color) the background should be based on")
            .define("background-based-on", "TextField.background");

        CONFIG = builder.build();
    }

    public static ForgeConfigSpec get() {
        return CONFIG;
    }

    public static void forceLoad() {
        CommentedFileConfig config = CommentedFileConfig
            .builder(FMLPaths.CONFIGDIR.get().resolve(FILE_NAME))
            .sync()
            .preserveInsertionOrder()
            .writingMode(WritingMode.REPLACE)
            .build();

        config.load();
        config.save();

        CONFIG.setConfig(config);

        setupExampleTheme();
    }

    private static void setupExampleTheme() {
        extract("Nord Theme",   NORD_THEME,   NORD_THEME_FILE);
        extract("Nord License", NORD_LICENSE, NORD_LICENSE_FILE);
    }

    private static void extract(String description, String path, String source) {
        var destination = FMLPaths.CONFIGDIR.get().resolve(path);
        if (!Files.exists(destination)) {
            try {
                Files.createDirectories(destination.getParent());
                Files.copy(findResource(source), destination, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                DarkModeServerUI.LOGGER.warn("Could not extract " + description + " to config folder.", e);
            }
        }
    }

    private static Path findResource(String path) {
        return ModList.get().getModFileById(DarkModeServerUI.MOD_ID).getFile().findResource(path);
    }

    public enum LookAndFeelType {
        NONE(null),
        LIGHT(FlatLightLaf.class),
        LIGHT_MAC(FlatMacLightLaf.class),
        DARK(FlatDarkLaf.class),
        DARK_MAC(FlatMacDarkLaf.class),
        DRACULA(FlatDarculaLaf.class),
        INTELLIJ(FlatIntelliJLaf.class),
        CUSTOM(null);

        private final @Nullable Class<? extends FlatLaf> laf;

        LookAndFeelType(@Nullable Class<? extends FlatLaf> laf) {
            this.laf = laf;
        }

        public @Nullable Class<? extends FlatLaf> getLAF() {
            return laf;
        }
    }

}
