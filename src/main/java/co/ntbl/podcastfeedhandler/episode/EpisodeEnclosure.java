package co.ntbl.podcastfeedhandler.episode;

import java.net.MalformedURLException;
import java.net.URL;
import org.w3c.dom.Node;

/**
 * This class is for the per episode "enclosure", this is the actual download reference for a podcast file.
 */
@SuppressWarnings("DesignForExtension")
public class EpisodeEnclosure {
    private URL url;
    private Long length = -1L;
    private String mimeType;

    /**
     * From example feeds I have, length is an optional field.
     *
     * @param enclosureNode The XML node of the enclosure element.
     * @throws MalformedURLException if failure to parse the correct attribute
     */
    public EpisodeEnclosure(Node enclosureNode) throws MalformedURLException {
        for (int i = 0; i < enclosureNode.getAttributes().getLength(); i++) {
            switch (enclosureNode.getAttributes().item(i).getNodeName()) {
                case "url":
                    this.url = new URL(enclosureNode.getAttributes().item(i).getTextContent());
                    break;
                case "length":
                    this.length = Long.decode(enclosureNode.getAttributes().item(i).getTextContent());
                    break;
                case "type":
                    this.mimeType = enclosureNode.getAttributes().item(i).getTextContent();
                    break;
            }
        }
    }

    public EpisodeEnclosure(URL url, Long length, String mimeType) {
        this.url = url;
        this.length = length;
        this.mimeType = mimeType;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
