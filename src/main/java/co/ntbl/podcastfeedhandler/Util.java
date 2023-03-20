package co.ntbl.podcastfeedhandler;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Properties;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Util {
    private Util() {
    }

    public static Node getChildNodeWithName(Node root, String childName) {
        NodeList childrenNodes = root.getChildNodes();
        for (int i = 0; i < childrenNodes.getLength(); i++) {
            if (childrenNodes.item(i).getNodeName().equals(childName)) {
                return childrenNodes.item(i);
            }
        }
        return null;
    }

    /**
     * These date functions come from the great <a href="https://github.com/MarkusLewis/Podcast-Feed-Library/blob/master/src/main/java/com/icosillion/podengine/utils/DateUtils.java">Podcast Feed Library</a>.
     * @param dt string of date
     * @return Java date Object
     */
    public static ZonedDateTime stringToDate(String dt) {
        DateTimeFormatter [] dateFormats = {
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US),
                DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss Z", Locale.US),
                DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss z", Locale.US),
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm Z", Locale.US),
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm z", Locale.US),
                DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm Z", Locale.US),
                DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm z", Locale.US),
                DateTimeFormatter.RFC_1123_DATE_TIME
        };

        String normalizedDt = normalize(dt);

        ZonedDateTime date = null;

        for (DateTimeFormatter dateFormat : dateFormats) {
            try {
                date = ZonedDateTime.parse(normalizedDt, dateFormat);
                break;
            } catch (DateTimeParseException e) {
                //This format didn't work, keep going
            }
        }

        return date;
    }

    private static String normalize(String dt) {
        return dt.replace("Tues,", "Tue,")
                .replace("Thurs,", "Thu,")
                .replace("Wednes,", "Wed,");
    }

    public static String dateConversion(ZonedDateTime date) {
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        return date.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public static String getVersion() {
        String fullVersionString = "";
        InputStream inputStream = Util.class.getClassLoader().getResourceAsStream("version.properties");

        Properties versionProperties = new Properties();
        try {
            versionProperties.load(inputStream);
        } catch (IOException e) {
            fullVersionString += e.getMessage() + ":";
        }

        fullVersionString += versionProperties.getProperty("version", "");
        return fullVersionString;
    }
}
