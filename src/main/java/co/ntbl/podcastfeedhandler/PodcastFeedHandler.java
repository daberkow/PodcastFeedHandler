package co.ntbl.podcastfeedhandler;

import co.ntbl.podcastfeedhandler.podcast.Image;
import co.ntbl.podcastfeedhandler.podcast.MediaRestrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.*;

public class PodcastFeedHandler {
    private String feed;

    public PodcastFeedHandler() {
    }

    public PodcastFeedHandler initFromFeed(String feed) {
        this.feed = feed;
        return this;
    }

    private Episode[] getPodcastFromFeed() {
        return null;
    }

    public Podcast getPodcastFromDocument(String document) throws MalformedURLException, PodcastFeedException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        Document parsedDocument = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            parsedDocument = documentBuilder.parse(new InputSource(new StringReader(document)));
            parsedDocument.normalize();
            parsedDocument.normalizeDocument();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        Node rootNode = parsedDocument.getFirstChild();
        if (!podcastXmlValidator(rootNode)) {
            // Invalid podcast feed
            return null;
        }

        // We now know the feed follows the general Podcast / iTunes standard
        Podcast podcast = parseFieldsInRootXml(rootNode);

        System.out.println("Test read");
        return podcast;
    }

    private Podcast parseFieldsInRootXml(Node rootNode) throws MalformedURLException, PodcastFeedException {
        Podcast workingPodcast = new Podcast();
        Node channel = Util.getChildNodeWithName(rootNode, "channel");
        if (channel == null) {
            //TODO throw an exception
            throw new PodcastFeedException("Missing channels element.");
        }

        List<Episode> episodes = new ArrayList<>();
        NodeList childrenNodes = channel.getChildNodes();
        for (int i = 0; i < childrenNodes.getLength(); i++) {
            switch (childrenNodes.item(i).getNodeName()) {
                case "#text":
                    // The default library trees pretty printed strings as a node
                    break;
                case "image":
                    workingPodcast.setImage(new Image(childrenNodes.item(i)));
                    break;
                case "title":
                    workingPodcast.setTitle(childrenNodes.item(i).getTextContent());
                    break;
                case "link":
                    workingPodcast.setLink(childrenNodes.item(i).getTextContent());
                    break;
                case "description":
                    workingPodcast.setDescription(childrenNodes.item(i).getTextContent());
                    break;
                case "language":
                    workingPodcast.setLanguage(childrenNodes.item(i).getTextContent());
                    break;
                case "copyright":
                    workingPodcast.setCopyright(childrenNodes.item(i).getTextContent());
                    break;
                case "managingEditor":
                    workingPodcast.setManagingEditor(childrenNodes.item(i).getTextContent());
                    break;
                case "webMaster":
                    workingPodcast.setWebMaster(childrenNodes.item(i).getTextContent());
                    break;
                case "pubDate":
                    workingPodcast.setPubDate(Util.stringToDate(childrenNodes.item(i).getTextContent()));
                    break;
                case "lastBuildDate":
                    workingPodcast.setLastBuildDate(childrenNodes.item(i).getTextContent());
                    break;
                case "generator":
                    workingPodcast.setGenerator(childrenNodes.item(i).getTextContent());
                    break;
                case "picsRating":
                    workingPodcast.setPicsRating(childrenNodes.item(i).getTextContent());
                    break;
                case "docsString":
                    workingPodcast.setDocsString(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:new-feed-url":
                    workingPodcast.setItunesNewFeedUrl(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:author":
                    workingPodcast.setItunesAuthor(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:subtitle":
                    workingPodcast.setItunesSubtitle(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:owner":
                    workingPodcast.setItunesOwner(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:image":
                    workingPodcast.setItunesImage(childrenNodes.item(i).getAttributes().getNamedItem("href").getTextContent());
                    break;
                case "itunes:category":
                    String tempString = childrenNodes.item(i).getAttributes().getNamedItem("text").getTextContent();
                    if (childrenNodes.item(i).hasChildNodes()) {
                        for (int j = 0; j < childrenNodes.item(i).getChildNodes().getLength(); j++) {
                            if (childrenNodes.item(i).getChildNodes().item(j).getNodeName().equals("#text")) {
                                continue;
                            }
                            workingPodcast.getItunesCategory().add(tempString + "|"
                                    + childrenNodes.item(i).getChildNodes().item(j).getAttributes().getNamedItem("text").getTextContent());
                        }
                    } else {
                        workingPodcast.getItunesCategory().add(tempString);
                    }
                    break;
                case "itunes:summary":
                    workingPodcast.setItunesSummary(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:block":
                    workingPodcast.setItunesBlock(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:type":
                    workingPodcast.setItunesType(childrenNodes.item(i).getTextContent());
                    break;
                case "media:restriction":
                    Map<String, String> attributes = new HashMap<>();
                    for (int j = 0; j < childrenNodes.item(i).getAttributes().getLength(); j++) {
                        attributes.put(childrenNodes.item(i).getAttributes().item(j).getNodeName(),
                                childrenNodes.item(i).getAttributes().item(j).getNodeValue());
                    }
                    workingPodcast.setMediaRestrictions(new MediaRestrictions(attributes, childrenNodes.item(i).getTextContent()));
                    break;
                    // Stopped here, we need to implement a lot of the fields in NPR feed.
                case"item":
                    Episode parsedEpisode = new Episode(childrenNodes.item(i));
                    episodes.add(parsedEpisode);
                    break;
                default:
                    if (workingPodcast.getUnknownFields().containsKey(childrenNodes.item(i).getNodeName())) {
                        workingPodcast.getUnknownFields().get(childrenNodes.item(i).getNodeName()).add(childrenNodes.item(i).toString());
                    } else {
                        workingPodcast.getUnknownFields().put(childrenNodes.item(i).getNodeName(), Collections.singletonList(childrenNodes.item(i).toString()));
                    }
                    System.err.println(childrenNodes.item(i).getNodeName() + " not implemented in podcast feed.");
                    //Throw here when ready for production
//                    throw new PodcastFeedException(childrenNodes.item(i).getNodeName() + " not implemented");
                    break;
            }
        }
        workingPodcast.setEpisodeList(episodes);
        return workingPodcast;
    }

    private boolean podcastXmlValidator(Node rootNode) {
        if (!rootNode.getNodeName().equals("rss")) {
            return false;
        }

        boolean standardVersionCheck = false;
        if (rootNode.hasAttributes()
                && ((rootNode.getAttributes().getNamedItem("xmlns:dc") != null
                    && rootNode.getAttributes().getNamedItem("xmlns:dc")
                        .getTextContent().equals("http://purl.org/dc/elements/1.1/"))
                || (rootNode.getAttributes().getNamedItem("xmlns:content") != null
                    && rootNode.getAttributes().getNamedItem("xmlns:content")
                        .getTextContent().equals("http://purl.org/dc/elements/1.1/")))) {
            standardVersionCheck = true;
        }

        boolean itunesCheck = false;
        if (rootNode.hasAttributes()
                && (rootNode.getAttributes().getNamedItem("xmlns:itunes") != null
                && rootNode.getAttributes().getNamedItem("xmlns:itunes")
                .getTextContent().equals("http://www.itunes.com/dtds/podcast-1.0.dtd"))) {
            itunesCheck = true;
        }

        boolean childCheck = false;
        if (Util.getChildNodeWithName(rootNode, "channel") != null) {
            childCheck = true;
        }

        return standardVersionCheck && itunesCheck && childCheck;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }
}
