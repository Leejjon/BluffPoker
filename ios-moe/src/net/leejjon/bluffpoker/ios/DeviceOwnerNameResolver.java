package net.leejjon.bluffpoker.ios;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceOwnerNameResolver {
    public static String resolveDeviceOwnerName(String deviceName, String systemLanguageCode) {
        String defaultName = "Player 1";
        Pattern nameResolvePattern = null;
        boolean nameBeforeDeviceType = true;
        if (systemLanguageCode.equals("en")) {
            nameResolvePattern = Pattern.compile("'s iPhone");
        } else if (systemLanguageCode.equals("nl")) {
            nameResolvePattern = Pattern.compile("iPhone van\\s");
            nameBeforeDeviceType = false;
        }

        if (nameResolvePattern != null) {
            Matcher matcher = nameResolvePattern.matcher(deviceName);
            boolean found = matcher.find();

            if (found) {
                if (nameBeforeDeviceType) {
                    defaultName = deviceName.substring(0, matcher.start());
                } else {
                    defaultName = deviceName.substring(matcher.end());
                }
            }
        }
        return defaultName;
    }
}
