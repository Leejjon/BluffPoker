package net.leejjon.bluffpoker.ios;

import com.badlogic.gdx.Gdx;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceOwnerNameResolver {
    public static String resolveDeviceOwnerName(String deviceName) {
        String defaultName = "Player 1";

        for (Languages language : Languages.values()) {
            Matcher matcher = language.defaultDeviceOwnerNamePattern.matcher(deviceName);
            if (matcher.find()) {
                if (language.nameBeforeDeviceType) {
                    return deviceName.substring(0, matcher.start());
                } else {
                    return deviceName.substring(matcher.end());
                }
            }
        }

        return defaultName;
    }

    private enum Languages {
        ENGLISH(Pattern.compile("'s iPhone"), true),
        ENGLISH_MANUALLY_ENTERED(Pattern.compile("\u2019s iPhone"), true),
        DUTCH(Pattern.compile("iPhone van\\s"), false);

        private final Pattern defaultDeviceOwnerNamePattern;
        private final boolean nameBeforeDeviceType;

        Languages(Pattern defaultDeviceOwnerNamePattern, boolean nameBeforeDeviceType) {
            this.defaultDeviceOwnerNamePattern = defaultDeviceOwnerNamePattern;
            this.nameBeforeDeviceType = nameBeforeDeviceType;
        }
    }
}
