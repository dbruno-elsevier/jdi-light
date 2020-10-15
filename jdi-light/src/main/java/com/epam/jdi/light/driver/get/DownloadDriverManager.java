package com.epam.jdi.light.driver.get;

import io.github.bonigarcia.wdm.WebDriverManager;

import static com.epam.jdi.light.common.Exceptions.exception;
import static com.epam.jdi.light.driver.get.DriverInfo.getBelowVersion;
import static com.epam.jdi.light.driver.get.DriverVersion.PENULT;
import static com.epam.jdi.light.settings.WebSettings.logger;
import static io.github.bonigarcia.wdm.WebDriverManager.*;
import static java.lang.String.format;

/**
 * Created by Roman_Iovlev on 11/28/2017.
 */
public class DownloadDriverManager {
    private static boolean hasVersion(String version) {
        char c = version.charAt(0);
        return (c >= '0' && c <= '9');
    }
    static WebDriverManager wdm;
    public static boolean driverDownloaded = false;
    public static String downloadedDriverInfo;
    public static String driverPath;

    static String downloadDriver(DriverTypes driverType, Platform platform, String version) {
        try {
            String driverName = driverType.toString();
            switch (driverType) {
                case CHROME:
                    wdm = chromedriver(); break;
                case FIREFOX:
                    wdm = firefoxdriver(); break;
                case IE:
                    wdm = iedriver(); break;
                case EDGE:
                    wdm = edgedriver(); break;
                case OPERA:
                    wdm = operadriver(); break;
                default:
                    throw exception("%s driver not supported for download");
            }
            switch (platform) {
                case X32:
                    wdm = wdm.arch32();
                    break;
                default: case X64:
                    wdm = wdm.arch64();
                    break;
            }
            driverName += " " + platform;
            if (hasVersion(version)) {
                wdm = wdm.browserVersion(version);
                driverName += " " + version;
            }
            if (version.equalsIgnoreCase(PENULT.value)) {
                wdm.setup();
                wdm.browserVersion(getBelowVersion());
            }
            wdm.setup();
            logger.info("Download driver: '" +  driverName + "' successfully");
            driverDownloaded = true;
            downloadedDriverInfo = format("%s:%s:%s", driverType, platform, version);
            driverPath = wdm.getDownloadedDriverPath();
            return wdm.getDownloadedDriverPath();
        } catch (Exception ex) {
            throw exception(ex, "Can't download latest driver for " + driverType);
        }
    }
}
