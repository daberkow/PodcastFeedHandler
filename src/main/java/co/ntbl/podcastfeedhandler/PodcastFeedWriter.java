package co.ntbl.podcastfeedhandler;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PodcastFeedWriter {
    private boolean addGeneratedElement = true;
    private boolean addGeneratedTime = true;

    /**
     * Convert a Podcast object into an XML feed.
     *
     * @param podcast Podcast Object
     * @return String of XML, this is a representative podcast feed
     * @throws ParserConfigurationException if the parser config fails
     * @throws TransformerException failure transforming the data into feed
     */
    @SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:MethodLength"})
    public String getXml(Podcast podcast) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        doc.setXmlStandalone(true);
        Element rootRssContainer = doc.createElement("rss");
        rootRssContainer.setAttribute("version", "2.0");
        rootRssContainer.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        rootRssContainer.setAttribute("xmlns:content", "http://purl.org/rss/1.0/modules/content/");
        rootRssContainer.setAttribute("xmlns:itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd");
        podcast.getRssFormats().forEach(rootRssContainer::setAttribute);
        // rootRssContainer.setAttribute("xml:base", "https://www.thisamericanlife.org");
        Element channelRssElement = doc.createElement("channel");

        channelRssElement.appendChild(createSingleElement(doc, "title", podcast.getTitle()));
        channelRssElement.appendChild(createSingleElement(doc, "link", podcast.getLink()));
        channelRssElement.appendChild(createSingleElement(doc, "description", podcast.getDescription()));
        if (addGeneratedElement) {
            channelRssElement.appendChild(createSingleElement(doc, "generator",
                    "co.ntbl.podcastfeedhandler.PodcastFeedWriter v" + PodcastFeedHandler.VERSION));
        }

        if (podcast.getLanguage() != null && !podcast.getLanguage().isEmpty()) {
            channelRssElement.appendChild(createSingleElement(doc, "language", podcast.getLanguage()));
        }

        if (podcast.getCopyright() != null && !podcast.getCopyright().isEmpty()) {
            channelRssElement.appendChild(createSingleElement(doc, "copyright", podcast.getCopyright()));
        }

        if (podcast.getManagingEditor() != null) {
            channelRssElement.appendChild(createSingleElement(doc, "managingEditor", podcast.getManagingEditor()));
        }

        if (podcast.getWebMaster() != null) {
            channelRssElement.appendChild(createSingleElement(doc, "webMaster", podcast.getWebMaster()));
        }

        if (podcast.getItunesNewFeedUrl() != null) {
            channelRssElement.appendChild(createSingleElement(doc, "itunes:new-feed-url",
                    podcast.getItunesNewFeedUrl()));
        }

        if (podcast.getItunesSummary() != null && !podcast.getItunesSummary().isEmpty()) {
            channelRssElement.appendChild(createSingleElement(doc, "itunes:summary", podcast.getItunesSummary()));
        }

        if (podcast.getItunesSubtitle() != null && !podcast.getItunesSubtitle().isEmpty()) {
            channelRssElement.appendChild(createSingleElement(doc, "itunes:subtitle", podcast.getItunesSubtitle()));
        }

        if (podcast.getItunesAuthor() != null && !podcast.getItunesAuthor().isEmpty()) {
            channelRssElement.appendChild(createSingleElement(doc, "itunes:author", podcast.getItunesAuthor()));
        }

        if (podcast.getItunesBlock() != null && !podcast.getItunesBlock().isEmpty()) {
            channelRssElement.appendChild(createSingleElement(doc, "itunes:block", podcast.getItunesBlock()));
        }

        if (podcast.getItunesOwner().size() > 0) {
            Element itunesOwner = doc.createElement("itunes:owner");
            podcast.getItunesOwner().forEach((k, v) -> {
                Element temp = doc.createElement(k);
                temp.setTextContent(v);
                itunesOwner.appendChild(temp);
            });
            channelRssElement.appendChild(itunesOwner);
        }

        if (podcast.getItunesCategory().size() > 0) {
            podcast.getItunesCategory().forEach((k) -> {
                if (k.contains("|")) {
                    // This is doing sub categories
                    String[] splitCats = k.split("\\|");
                    if (splitCats.length != 0) {
                        List<Element> elements = new ArrayList<>();
                        Element itunesRootCat = doc.createElement("itunes:category");
                        itunesRootCat.setAttribute("text", splitCats[0]);
                        elements.add(itunesRootCat);
                        for (int i = 1; i < splitCats.length; i++) {
                            Element itunesTempCat = doc.createElement("itunes:category");
                            itunesTempCat.setAttribute("text", splitCats[1]);
                            elements.get(i - 1).appendChild(itunesTempCat);
                        }
                        channelRssElement.appendChild(itunesRootCat);
                    }
                } else {
                    // Single Line
                    Element itunesTempCat = doc.createElement("itunes:category");
                    itunesTempCat.setAttribute("text", k);
                    channelRssElement.appendChild(itunesTempCat);
                }
            });
        }

        if (podcast.getItunesImage() != null && !podcast.getItunesImage().isEmpty()) {
            Element itunesImage = doc.createElement("itunes:image");
            itunesImage.setAttribute("href", podcast.getItunesImage());
            channelRssElement.appendChild(itunesImage);
        }

        if (podcast.getItunesType() != null) {
            channelRssElement.appendChild(createSingleElement(doc, "itunes:type", podcast.getItunesType()));
        }

        if (podcast.getMediaRestrictions() != null) {
            Element mediaRestrictions = doc.createElement("media:restriction");
            podcast.getMediaRestrictions().getAttributes().forEach(mediaRestrictions::setAttribute);
            mediaRestrictions.setTextContent(podcast.getMediaRestrictions().getField());
            channelRssElement.appendChild(mediaRestrictions);
        }

        if (podcast.getImage() != null) {
            Element mediaRestrictions = doc.createElement("image");
            if (podcast.getImage().getUrl() != null) {
                Element temp = doc.createElement("url");
                temp.setTextContent(podcast.getImage().getUrl().toString());
                mediaRestrictions.appendChild(temp);
            }
            if (podcast.getImage().getTitle() != null) {
                Element temp = doc.createElement("title");
                temp.setTextContent(podcast.getImage().getTitle());
                mediaRestrictions.appendChild(temp);
            }
            if (podcast.getImage().getLink() != null) {
                Element temp = doc.createElement("link");
                temp.setTextContent(podcast.getImage().getLink().toString());
                mediaRestrictions.appendChild(temp);
            }
            channelRssElement.appendChild(mediaRestrictions);
        }

        if (podcast.getDocs() != null) {
            channelRssElement.appendChild(createSingleElement(doc, "docs", podcast.getDocs()));
        }

        if (podcast.getTtl() != null) {
            channelRssElement.appendChild(createSingleElement(doc, "ttl", podcast.getTtl()));
        }

        if (podcast.getSkipHours().size() > 0) {
            Element skipHours = doc.createElement("skipHours");
            podcast.getSkipHours().forEach((k) -> {
                Element itunesTempHour = doc.createElement("hour");
                itunesTempHour.setTextContent(k);
                skipHours.appendChild(itunesTempHour);
            });
            channelRssElement.appendChild(skipHours);
        }

        if (podcast.getSkipDays().size() > 0) {
            Element skipHours = doc.createElement("skipDays");
            podcast.getSkipDays().forEach((k) -> {
                Element itunesTempHour = doc.createElement("day");
                itunesTempHour.setTextContent(k);
                skipHours.appendChild(itunesTempHour);
            });
            channelRssElement.appendChild(skipHours);
        }

        if (podcast.getTextInput().size() > 0) {
            Element textInput = doc.createElement("textInput");
            podcast.getTextInput().forEach((k, v) -> {
                Element itunesTemp = doc.createElement(k);
                itunesTemp.setTextContent(v);
                textInput.appendChild(itunesTemp);
            });
            channelRssElement.appendChild(textInput);
        }
        if (podcast.getCloud().size() > 0) {
            Element textInput = doc.createElement("cloud");
            podcast.getCloud().forEach(textInput::setAttribute);
            channelRssElement.appendChild(textInput);
        }

        if (podcast.getItunesKeywords() != null) {
            channelRssElement.appendChild(createSingleElement(doc, "itunes:keywords", podcast.getItunesKeywords()));
        }

        if (podcast.getItunesExplicit() != null) {
            channelRssElement.appendChild(createSingleElement(doc, "itunes:explicit", podcast.getItunesExplicit()));
        }

        if (podcast.getPubDate() != null) {
            channelRssElement.appendChild(createSingleElement(doc, "pubDate",
                    Util.dateConversion(podcast.getPubDate())));
        }

        LocalDateTime time = LocalDateTime.now();
        /*
            The example is <lastBuildDate>Fri, 06 Jan 2023 17:24:17 -0500</lastBuildDate>, but I think this is just for
             debugging and format is not important.
         */
        if (addGeneratedTime) {
            channelRssElement.appendChild(createSingleElement(doc, "lastBuildDate",
                    time.format(DateTimeFormatter.ISO_DATE_TIME)));
        }

        // Start Episode Data
        addEpisodes(doc, channelRssElement, podcast);

        rootRssContainer.appendChild(channelRssElement);
        doc.appendChild(rootRssContainer);

        return documentConverter(doc);
    }

    private String documentConverter(Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        // This moves the rss line to the second line
        transformer.setOutputProperty("http://www.oracle.com/xml/is-standalone", "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(domSource, result);
        return writer.toString();
    }

    private Element createSingleElement(Document doc, String elementName, String elementValue) {
        Element titleElement = doc.createElement(elementName);
        titleElement.setTextContent(elementValue);
        return titleElement;
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    private void addEpisodes(Document doc, Element channelRssElement, Podcast podcast) {
        podcast.getEpisodeList().forEach((episode -> {
            Element itemElement = doc.createElement("item");
            itemElement.appendChild(createSingleElement(doc, "title", episode.getTitle()));
            itemElement.appendChild(createSingleElement(doc, "link", episode.getLink().toString()));
            itemElement.appendChild(createSingleElement(doc, "description", episode.getDescription()));
            itemElement.appendChild(createSingleElement(doc, "pubDate", Util.dateConversion(episode.getPubDate())));

            Element guidElement = doc.createElement("guid");
            guidElement.setAttribute("isPermaLink", "false");
            if (episode.getGuid() != null) {
                guidElement.setTextContent(episode.getGuid());
            } else {
                // If there is no guid
                guidElement.setTextContent(UUID.fromString(episode.getTitle()).toString());
            }
            itemElement.appendChild(guidElement);

            if (episode.getItunesTitle() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:title", episode.getItunesTitle()));
            }

            if (episode.getItunesEpisode() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:episode", episode.getItunesEpisode()));
            }

            if (episode.getItunesAuthor() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:author", episode.getItunesAuthor()));
            }

            if (episode.getItunesSummary() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:summary", episode.getItunesSummary()));
            }

            if (episode.getItunesImage() != null) {
                Element itunesImage = doc.createElement("itunes:image");
                itunesImage.setAttribute("href", episode.getItunesImage());
                itemElement.appendChild(itunesImage);
            }

            if (episode.getItunesDuration() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:duration", episode.getItunesDuration()));
            }

            if (episode.getItunesExplicit() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:explicit", episode.getItunesExplicit()));
            }

            if (episode.getItunesEpisodeType() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:episodeType", episode.getItunesEpisodeType()));
            }

            if (episode.getContentEncoded() != null) {
                itemElement.appendChild(createSingleElement(doc, "content:encoded", episode.getContentEncoded()));
            }

            // This is required
            Element fileToDownload = doc.createElement("enclosure");
            fileToDownload.setAttribute("type", episode.getEnclosure().getMimeType());
            fileToDownload.setAttribute("url", episode.getEnclosure().getUrl().toString());
            if (episode.getEnclosure().getLength() != -1) {
                // Some example feeds do not put the length in
                fileToDownload.setAttribute("length", episode.getEnclosure().getLength().toString());
            }
            itemElement.appendChild(fileToDownload);

            if (episode.getSourceName() != null) {
                itemElement.appendChild(createSingleElement(doc, "sourceName", episode.getSourceName()));
            }

            if (episode.getSourceLink() != null) {
                itemElement.appendChild(createSingleElement(doc, "sourceLink", episode.getSourceLink().toString()));
            }

            if (episode.getCopyright() != null) {
                itemElement.appendChild(createSingleElement(doc, "copyright", episode.getCopyright()));
            }

            if (episode.getItunesSubtitle() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:subtitle", episode.getItunesSubtitle()));
            }

            if (episode.getComments() != null) {
                itemElement.appendChild(createSingleElement(doc, "comments", episode.getComments().toString()));
            }

            if (episode.getContentEncoded() != null) {
                itemElement.appendChild(createSingleElement(doc, "content:encoded", episode.getContentEncoded()));
            }

            if (episode.getItunesBlock() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:block", episode.getItunesBlock()));
            }

            if (!episode.getCategory().isEmpty()) {
                episode.getCategory().forEach((k) -> {
                    itemElement.appendChild(createSingleElement(doc, "category", k));
                });
            }

            if (episode.getSourceName() != null && episode.getSourceLink() != null) {
                Element titleElement = doc.createElement("source");
                titleElement.setTextContent(episode.getSourceName());
                titleElement.setAttribute("url", episode.getSourceLink().toString());
                itemElement.appendChild(titleElement);
            }

            if (episode.getItunesIsClosedCaptioned() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:isClosedCaptioned",
                        episode.getItunesIsClosedCaptioned()));
            }

            if (episode.getItunesOrder() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:order", episode.getItunesOrder()));
            }

            if (episode.getItunesSeason() != null) {
                itemElement.appendChild(createSingleElement(doc, "itunes:season", episode.getItunesSeason()));
            }

            channelRssElement.appendChild(itemElement);
        }));
    }

    public boolean isAddGeneratedElement() {
        return addGeneratedElement;
    }

    /**
     * This allows enabling and disabling of Generated element in RSS. Useful to disable when trying to do 1:1 testing.
     *
     * @param addGeneratedElement Enable or disable generated element
     */
    public void setAddGeneratedElement(boolean addGeneratedElement) {
        this.addGeneratedElement = addGeneratedElement;
    }

    public boolean isAddGeneratedTime() {
        return addGeneratedTime;
    }

    /**
     * Allows enabling and disabling of the "lastBuildDate" element.
     *
     * @param addGeneratedTime enable/disable "lastBuildDate"
     */
    public void setAddGeneratedTime(boolean addGeneratedTime) {
        this.addGeneratedTime = addGeneratedTime;
    }
}
