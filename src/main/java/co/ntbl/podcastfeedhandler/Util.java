package co.ntbl.podcastfeedhandler;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Util {

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
    public static Date stringToDate(String dt) {
        SimpleDateFormat[] dateFormats = {
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
                new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z", Locale.US),
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm Z", Locale.US),
                new SimpleDateFormat("dd MMM yyyy HH:mm Z", Locale.US)
        };

        String normalizedDt = normalize(dt);

        Date date = null;

        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                date = dateFormat.parse(normalizedDt);
                break;
            } catch (ParseException e) {
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

    public static String dateConversion(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        return dateFormatter.format(date);
    }
}
