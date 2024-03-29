package co.ntbl.podcastfeedhandler;

import co.ntbl.podcastfeedhandler.podcast.Image;
import co.ntbl.podcastfeedhandler.podcast.MediaRestrictions;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *  Podcast feed reader, this creates a Podcast object from a document.
 */
public class PodcastFeedReader {
    public PodcastFeedReader() {
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
            throw new PodcastFeedException("Failure to find podcast root node");
        }

        // We now know the feed follows the general Podcast / iTunes standard

        return parseFieldsInRootXml(rootNode);
    }

    @SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:MethodLength", "BanSystemErr"})
    private Podcast parseFieldsInRootXml(Node rootNode) throws MalformedURLException, PodcastFeedException {
        Podcast workingPodcast = new Podcast();
        for (int i = 0; i < rootNode.getAttributes().getLength(); i++) {
            if (rootNode.getAttributes().item(i).getNodeName().equals("xmlns:dc")
                    || rootNode.getAttributes().item(i).getNodeName().equals("xmlns:content")
                    || rootNode.getAttributes().item(i).getNodeName().equals("xmlns:itunes")) {
                continue;
            }
            workingPodcast.getRssFormats().put(rootNode.getAttributes().item(i).getNodeName(),
                    rootNode.getAttributes().item(i).getNodeValue());
        }

        Node channel = Util.getChildNodeWithName(rootNode, "channel");
        if (channel == null) {
            throw new PodcastFeedException("Missing channels element.");
        }

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
                    String stringDate = childrenNodes.item(i).getTextContent();
                    ZonedDateTime date = Util.stringToDate(stringDate);
                    workingPodcast.setPubDate(date);
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
                    if (childrenNodes.item(i).hasChildNodes()) {
                        for (int j = 0; j < childrenNodes.item(i).getChildNodes().getLength(); j++) {
                            if (childrenNodes.item(i).getChildNodes().item(j).getNodeName().equals("#text")) {
                                continue;
                            }
                            workingPodcast.getItunesOwner().put(
                                    childrenNodes.item(i).getChildNodes().item(j).getNodeName(),
                                    childrenNodes.item(i).getChildNodes().item(j).getFirstChild().getTextContent());
                        }
                    }
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
                                    + childrenNodes.item(i).getChildNodes()
                                    .item(j).getAttributes().getNamedItem("text").getTextContent());
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
                    workingPodcast.setMediaRestrictions(
                            new MediaRestrictions(attributes, childrenNodes.item(i).getTextContent()));
                    break;
                case "docs":
                    workingPodcast.setDocs(childrenNodes.item(i).getTextContent());
                    break;
                case "ttl":
                    workingPodcast.setTtl(childrenNodes.item(i).getTextContent());
                    break;
                case "skipHours":
                    if (childrenNodes.item(i).hasChildNodes()) {
                        for (int j = 0; j < childrenNodes.item(i).getChildNodes().getLength(); j++) {
                            if (childrenNodes.item(i).getChildNodes().item(j).getNodeName().equals("#text")) {
                                continue;
                            }
                            workingPodcast.getSkipHours().add(
                                    childrenNodes.item(i).getChildNodes().item(j).getFirstChild().getTextContent());
                        }
                    }
                    break;
                case "skipDays":
                    if (childrenNodes.item(i).hasChildNodes()) {
                        for (int j = 0; j < childrenNodes.item(i).getChildNodes().getLength(); j++) {
                            if (childrenNodes.item(i).getChildNodes().item(j).getNodeName().equals("#text")) {
                                continue;
                            }
                            workingPodcast.getSkipDays().add(
                                    childrenNodes.item(i).getChildNodes().item(j).getFirstChild().getTextContent());
                        }
                    }
                    break;
                case "textInput":
                    if (childrenNodes.item(i).hasChildNodes()) {
                        for (int j = 0; j < childrenNodes.item(i).getChildNodes().getLength(); j++) {
                            if (childrenNodes.item(i).getChildNodes().item(j).getNodeName().equals("#text")) {
                                continue;
                            }
                            workingPodcast.getTextInput().put(
                                    childrenNodes.item(i).getChildNodes().item(j).getNodeName(),
                                    childrenNodes.item(i).getChildNodes().item(j).getFirstChild().getTextContent());
                        }
                    }
                    break;
                case "cloud":
                    for (int x = 0; x < childrenNodes.item(i).getAttributes().getLength(); x++) {
                        workingPodcast.getCloud().put(childrenNodes.item(i).getAttributes().item(x).getNodeName(),
                                childrenNodes.item(i).getAttributes().item(x).getNodeValue());
                    }
                    break;
                case "itunes:keywords":
                    workingPodcast.setItunesKeywords(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:explicit":
                    workingPodcast.setItunesExplicit(childrenNodes.item(i).getTextContent());
                    break;
                case "item":
                    Episode parsedEpisode = new Episode(childrenNodes.item(i));
                    workingPodcast.addEpisode(parsedEpisode);
                    break;
                default:
                    if (workingPodcast.getUnknownFields().containsKey(childrenNodes.item(i).getNodeName())) {
                        workingPodcast.getUnknownFields().get(
                                childrenNodes.item(i).getNodeName()).add(childrenNodes.item(i));
                    } else {
                        workingPodcast.getUnknownFields().put(childrenNodes.item(i).getNodeName(),
                                new ArrayList<>(Arrays.asList(childrenNodes.item(i))));
                    }
                    break;
            }
        }
        return workingPodcast;
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    private boolean podcastXmlValidator(Node rootNode) {
        if (!rootNode.getNodeName().equals("rss")) {
            return false;
        }

        boolean standardVersionCheck = rootNode.hasAttributes()
                && ((rootNode.getAttributes().getNamedItem("xmlns:dc") != null
                && rootNode.getAttributes().getNamedItem("xmlns:dc")
                .getTextContent().equals("http://purl.org/dc/elements/1.1/"))
                || (rootNode.getAttributes().getNamedItem("xmlns:content") != null
                && rootNode.getAttributes().getNamedItem("xmlns:content")
                .getTextContent().equals("http://purl.org/dc/elements/1.1/")));

        boolean itunesCheck = rootNode.hasAttributes()
                && (rootNode.getAttributes().getNamedItem("xmlns:itunes") != null
                && rootNode.getAttributes().getNamedItem("xmlns:itunes")
                .getTextContent().equals("http://www.itunes.com/dtds/podcast-1.0.dtd"));

        boolean childCheck = Util.getChildNodeWithName(rootNode, "channel") != null;

        return (standardVersionCheck || itunesCheck) && childCheck;
    }
}
