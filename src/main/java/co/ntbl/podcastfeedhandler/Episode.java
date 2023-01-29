package co.ntbl.podcastfeedhandler;

import co.ntbl.podcastfeedhandler.episode.EpisodeEnclosure;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

public class Episode {
    private String title, description, guid, sourceName, copyright, contentEncoded;
    private URL link, comments, sourceLink;

    private String itunesDuration, itunesSubtitle, itunesSummary, itunesExplicit, itunesTitle, itunesEpisode,
            itunesImage, itunesAuthor, itunesEpisodeType, itunesBlock, itunesIsClosedCaptioned, itunesOrder,
            itunesSeason;

    private List<String> category = new ArrayList<>(); //Apparently there can be multiple
    private EpisodeEnclosure enclosure;
    private ZonedDateTime pubDate;

    private Map<String, List<String>> unknownFields = new HashMap<>();

    public Episode() {
    }

    public Episode(Node enclosureNode) throws MalformedURLException {
        NodeList childrenNodes = enclosureNode.getChildNodes();
        for (int i = 0; i < childrenNodes.getLength(); i++) {
            switch (childrenNodes.item(i).getNodeName()) {
                case "#text":
                    // The default library trees pretty printed strings as a node
                    break;
                case "title":
                    this.title = childrenNodes.item(i).getTextContent();
                    break;
                case "description":
                    this.description = childrenNodes.item(i).getTextContent();
                    break;
                case "author":
                case "itunes:author":
                    this.itunesAuthor = childrenNodes.item(i).getTextContent();
                    break;
                case "guid":
                    this.guid = childrenNodes.item(i).getTextContent();
                    break;
                case "copyright":
                    this.copyright = childrenNodes.item(i).getTextContent();
                    break;
                case "link":
                    this.link = new URL(childrenNodes.item(i).getTextContent());
                    break;
                case "comments":
                    this.comments = new URL(childrenNodes.item(i).getTextContent());
                    break;
                case "source":
                    this.sourceLink = new URL(childrenNodes.item(i).getAttributes().getNamedItem("url").getTextContent());
                    this.sourceName = childrenNodes.item(i).getTextContent();
                    break;
                case "enclosure":
                    this.enclosure = new EpisodeEnclosure(childrenNodes.item(i));
                    break;
                case "itunes:image":
                    this.itunesImage = childrenNodes.item(i).getAttributes().getNamedItem("href").getTextContent();
                    break;
                case "itunes:title":
                    this.itunesTitle = childrenNodes.item(i).getTextContent();
                    break;
                case "itunes:episode":
                    this.itunesEpisode = childrenNodes.item(i).getTextContent();
                    break;
                case "itunes:duration":
                    this.itunesDuration = childrenNodes.item(i).getTextContent();
                    break;
                case "itunes:subtitle":
                    this.itunesSubtitle = childrenNodes.item(i).getTextContent();
                    break;
                case "itunes:summary":
                    this.itunesSummary = childrenNodes.item(i).getTextContent();
                    break;
                case "itunes:explicit":
                    this.itunesExplicit = childrenNodes.item(i).getTextContent();
                    break;
                case "itunes:block":
                    this.itunesBlock = childrenNodes.item(i).getTextContent();
                    break;
                case "pubDate":
                    this.pubDate = Util.stringToDate(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:episodeType":
                    this.itunesEpisodeType = childrenNodes.item(i).getTextContent();
                    break;
                case "content:encoded":
                    this.contentEncoded = childrenNodes.item(i).getTextContent();
                    break;
                case "category":
                    this.category.add(childrenNodes.item(i).getTextContent());
                    break;
                case "itunes:isClosedCaptioned":
                    this.itunesIsClosedCaptioned = childrenNodes.item(i).getTextContent();
                    break;
                case "itunes:order":
                    this.itunesOrder = childrenNodes.item(i).getTextContent();
                    break;
                case "itunes:season":
                    this.itunesSeason = childrenNodes.item(i).getTextContent();
                    break;
                default:
                    System.err.println(childrenNodes.item(i).getNodeName() + " not implemented in episode.");
                    if (unknownFields.containsKey(childrenNodes.item(i).getNodeName())) {
                        unknownFields.get(childrenNodes.item(i).getNodeName()).add(childrenNodes.item(i).toString());
                    } else {
                        unknownFields.put(childrenNodes.item(i).getNodeName(), new ArrayList<>());
                        unknownFields.get(childrenNodes.item(i).getNodeName()).add(childrenNodes.item(i).toString());
                    }
                    break;
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItunesImage() {
        return itunesImage;
    }

    public void setItunesImage(String itunesImage) {
        this.itunesImage = itunesImage;
    }

    public String getItunesAuthor() {
        return itunesAuthor;
    }

    public void setItunesAuthor(String itunesAuthor) {
        this.itunesAuthor = itunesAuthor;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public URL getComments() {
        return comments;
    }

    public void setComments(URL comments) {
        this.comments = comments;
    }

    public URL getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(URL sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getItunesDuration() {
        return itunesDuration;
    }

    public void setItunesDuration(String itunesDuration) {
        this.itunesDuration = itunesDuration;
    }

    public String getItunesSubtitle() {
        return itunesSubtitle;
    }

    public void setItunesSubtitle(String itunesSubtitle) {
        this.itunesSubtitle = itunesSubtitle;
    }

    public String getItunesSummary() {
        return itunesSummary;
    }

    public void setItunesSummary(String itunesSummary) {
        this.itunesSummary = itunesSummary;
    }

    public EpisodeEnclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(EpisodeEnclosure enclosure) {
        this.enclosure = enclosure;
    }

    public ZonedDateTime getPubDate() {
        return pubDate;
    }

    public void setPubDate(ZonedDateTime pubDate) {
        this.pubDate = pubDate;
    }

    public String getContentEncoded() {
        return contentEncoded;
    }

    public void setContentEncoded(String contentEncoded) {
        this.contentEncoded = contentEncoded;
    }

    public Map<String, List<String>> getUnknownFields() {
        return unknownFields;
    }

    public void setUnknownFields(Map<String, List<String>> unknownFields) {
        this.unknownFields = unknownFields;
    }

    public String getItunesExplicit() {
        return itunesExplicit;
    }

    public void setItunesExplicit(String itunesExplicit) {
        this.itunesExplicit = itunesExplicit;
    }

    public String getItunesTitle() {
        return itunesTitle;
    }

    public void setItunesTitle(String itunesTitle) {
        this.itunesTitle = itunesTitle;
    }

    public String getItunesEpisode() {
        return itunesEpisode;
    }

    public void setItunesEpisode(String itunesEpisode) {
        this.itunesEpisode = itunesEpisode;
    }

    public String getItunesEpisodeType() {
        return itunesEpisodeType;
    }

    public void setItunesEpisodeType(String itunesEpisodeType) {
        this.itunesEpisodeType = itunesEpisodeType;
    }

    public String getItunesBlock() {
        return itunesBlock;
    }

    public void setItunesBlock(String itunesBlock) {
        this.itunesBlock = itunesBlock;
    }

    public String getItunesIsClosedCaptioned() {
        return itunesIsClosedCaptioned;
    }

    public void setItunesIsClosedCaptioned(String itunesIsClosedCaptioned) {
        this.itunesIsClosedCaptioned = itunesIsClosedCaptioned;
    }

    public String getItunesOrder() {
        return itunesOrder;
    }

    public void setItunesOrder(String itunesOrder) {
        this.itunesOrder = itunesOrder;
    }

    public String getItunesSeason() {
        return itunesSeason;
    }

    public void setItunesSeason(String itunesSeason) {
        this.itunesSeason = itunesSeason;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
