package org.codehaus.jackson.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import org.codehaus.jackson.Version;

public class VersionUtil {
    public static final String VERSION_FILE = "VERSION.txt";
    private static final Pattern VERSION_SEPARATOR;

    static {
        VERSION_SEPARATOR = Pattern.compile("[-_./;:]");
    }

    public static Version parseVersion(String versionStr) {
        int i = 0;
        String str = null;
        if (versionStr == null) {
            return null;
        }
        versionStr = versionStr.trim();
        if (versionStr.length() == 0) {
            return null;
        }
        String[] parts = VERSION_SEPARATOR.split(versionStr);
        if (parts.length < 2) {
            return null;
        }
        int major = parseVersionPart(parts[0]);
        int minor = parseVersionPart(parts[1]);
        if (parts.length > 2) {
            i = parseVersionPart(parts[2]);
        }
        if (parts.length > 3) {
            str = parts[3];
        }
        return new Version(major, minor, i, str);
    }

    protected static int parseVersionPart(String partStr) {
        partStr = partStr.toString();
        int len = partStr.length();
        int number = 0;
        int i = 0;
        while (i < len) {
            char c = partStr.charAt(i);
            if (c > '9' || c < '0') {
                return number;
            }
            number = number * 10 + c - 48;
            i++;
        }
        return number;
    }

    public static Version versionFor(Class<?> cls) {
        Version version = null;
        try {
            InputStream in = cls.getResourceAsStream(VERSION_FILE);
            if (in != null) {
                version = parseVersion(new BufferedReader(new InputStreamReader(in, "UTF-8")).readLine());
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        } catch (IOException e3) {
        } catch (Throwable th) {
            in.close();
        }
        return version == null ? Version.unknownVersion() : version;
    }
}