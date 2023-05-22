/*
 * Copyright (c) AterAnimAvis
 * SPDX-License-Identifier: MIT
 */
package com.github.ateranimavis.darkmodeserverui;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.formdev.flatlaf.IntelliJTheme;
import com.github.ateranimavis.darkmodeserverui.theme.CustomThemeLAF;
import net.minecraft.Util;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.loading.FMLPaths;

public class DarkMode {

    private static Color find(String keyOrValue, Color defaultValue) {
        var value = UIManager.getLookAndFeelDefaults().getColor(keyOrValue);
        if (value != null) return value;

        try {
            return Color.decode(keyOrValue);
        } catch (NumberFormatException formatException) {
            return defaultValue;
        }
    }

    private static final Lazy<Color> BACKGROUND = Lazy.of(() -> find(DarkModeConfig.BACKGROUND_BASED_ON.get(), Color.WHITE));
    private static final Lazy<Color> FOREGROUND = Lazy.of(() -> find(DarkModeConfig.FOREGROUND_BASED_ON.get(), Color.BLACK));

    private static boolean fastExit = false;

    public static void install() {
        DarkModeConfig.forceLoad();

        var type = DarkModeConfig.TYPE.get();
        if (type == DarkModeConfig.LookAndFeelType.NONE) {
            fastExit = true;
            return;
        }

        if (type == DarkModeConfig.LookAndFeelType.CUSTOM) {
            var path = FMLPaths.CONFIGDIR.get().resolve(DarkModeConfig.CUSTOM_THEME.get()).toAbsolutePath();
            if (!Files.exists(path)) {
                DarkModeServerUI.LOGGER.warn("Could not find '%s' to load.".formatted(path));
                fastExit = true;
                return;
            }

            //TODO: If Jar try grab the theme from inside it?
            // JAR
            // -> META-INF/plugin.xml
            //    -> themeProvider => path

            try {
                CustomThemeLAF.theme = new IntelliJTheme(Files.newInputStream(path));
            } catch (IOException e) {
                DarkModeServerUI.LOGGER.warn("Could not load custom theme '%s'.".formatted(path), e);
                fastExit = true;
                return;
            }

            System.setProperty("swing.systemlaf", CustomThemeLAF.class.getName());
            initSystemSpecific();
            return;
        }

        var clazz = type.getLAF();
        if (clazz == null) return;

        System.setProperty("swing.systemlaf", clazz.getName());
        initSystemSpecific();
    }

    private static void initSystemSpecific() {
        /* Mac-OS has built in support for dark title bars but needs the properties */
        if (Util.getPlatform() == Util.OS.OSX) {
            // Valid values: (system, NSAppearanceNameAqua, NSAppearanceNameDarkAqua)
            System.setProperty( "apple.awt.application.appearance", "system");
            return;
        }

        /* Linux needs these so we can use FlatLAF's title bar */
        // TODO: Config Property to skip?
        if (Util.getPlatform() == Util.OS.LINUX) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }
    }

    public static Color modifyColor(Color color, Context context) {
        if (fastExit) return color;

        return switch (context) {
            case BACKGROUND -> BACKGROUND.get();
            case FONT       -> FOREGROUND.get();
            case BAR        -> color; //TODO: Custom Colors / Some sort of custom Interpolation here?
        };
    }

    public enum Context {
        BACKGROUND,
        FONT,
        BAR;

        public Color modify(Color color) {
            return modifyColor(color, this);
        }

    }

}
