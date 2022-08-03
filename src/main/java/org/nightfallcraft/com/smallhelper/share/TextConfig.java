package org.nightfallcraft.com.smallhelper.share;

import org.nightfallcraft.com.smallhelper.SmallHelper;

public class TextConfig {
    public static String getText(String path) {
        return replaceColorSymbol(SmallHelper.getInstance().getConfig().getString(path));
    }

    private static String replaceColorSymbol(String text) {
        return text.replace('&', '§');
    }
}
