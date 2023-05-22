/*
 * Copyright (c) AterAnimAvis
 * SPDX-License-Identifier: MIT
 */
package com.github.ateranimavis.darkmodeserverui.theme;

import java.io.IOException;

import com.formdev.flatlaf.IntelliJTheme;

public class CustomThemeLAF extends IntelliJTheme.ThemeLaf {

    public static IntelliJTheme theme;

    public CustomThemeLAF() throws IOException {
        super(theme);
    }

}
