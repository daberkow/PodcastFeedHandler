package co.ntbl.podcastfeedhandler.podcast;

import org.w3c.dom.Node;

import java.net.MalformedURLException;
import java.net.URL;

public class Image {
    private URL url, link;
    private String title;

    public Image(Node enclosureNode) throws MalformedURLException {
        for (int i = 0; i < enclosureNode.getChildNodes().getLength(); i++) {
            switch (enclosureNode.getChildNodes().item(i).getNodeName()){
                case "url":
                    this.url = new URL(enclosureNode.getChildNodes().item(i).getTextContent());
                    break;
                case "title":
                    this.title =enclosureNode.getChildNodes().item(i).getTextContent();
                    break;
                case "link":
                    this.link = new URL(enclosureNode.getChildNodes().item(i).getTextContent());
                    break;
            }
        }
    }

    public Image(URL url, URL link, String title) {
        this.url = url;
        this.link = link;
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
