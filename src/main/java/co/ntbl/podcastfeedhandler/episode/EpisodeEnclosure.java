package co.ntbl.podcastfeedhandler.episode;

import org.w3c.dom.Node;

import java.net.MalformedURLException;
import java.net.URL;

public class EpisodeEnclosure {
    private URL url;
    private Long length;
    private String mimeType;

    public EpisodeEnclosure(Node enclosureNode) throws MalformedURLException {
        // <enclosure url="https://dts.podtrac.com/redirect.mp3/chtbl.com/track/8DB4DB/pdst.fm/e/nyt.simplecastaudio.com/bbbcc290-ed3b-44a2-8e5d-5513e38cfe20/episodes/7ac9d0b2-7669-4abd-9ffb-624f4ff7b0d8/audio/128/default.mp3?awCollectionId=bbbcc290-ed3b-44a2-8e5d-5513e38cfe20&amp;awEpisodeId=7ac9d0b2-7669-4abd-9ffb-624f4ff7b0d8"
        //                       type="audio/mpeg" />
        for (int i = 0; i < enclosureNode.getAttributes().getLength(); i++) {
            switch (enclosureNode.getAttributes().item(i).getNodeName()){
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
