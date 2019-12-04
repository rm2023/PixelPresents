package io.chazza.pixelpresents.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ColorUtil {
    private static String translateAlternateColorCodes(String textToTranslate) {
        return textToTranslate;
    }

    public static String translate(String string) {
        return translateAlternateColorCodes(string);
    }

    public static List<String> translate(List<String> string) {
        return string.stream().map(ColorUtil::translate).collect(Collectors.toCollection(ArrayList::new));
    }
}
