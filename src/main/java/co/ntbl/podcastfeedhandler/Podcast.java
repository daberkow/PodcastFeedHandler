package co.ntbl.podcastfeedhandler;

import co.ntbl.podcastfeedhandler.podcast.Image;
import co.ntbl.podcastfeedhandler.podcast.MediaRestrictions;

import java.util.*;

public class Podcast {
    private String title, link, description, language, copyright, managingEditor, webMaster,
            generator, picsRating, docsString, lastBuildDate ;

    private String itunesNewFeedUrl, itunesAuthor, itunesSubtitle, itunesImage, itunesSummary, itunesBlock, itunesType;
    private List<String> itunesCategory;

    private HashMap<String, String> rssFormats;

    private HashMap<String, String> itunesOwner;

    private Date pubDate;

    private Image image;

    private MediaRestrictions mediaRestrictions;

    private Map<String, List<String>> unknownFields;
    private List<Episode> episodeList;

    public Podcast() {
        itunesCategory = new ArrayList<>();
        unknownFields = new HashMap<>();
        episodeList = new ArrayList<>();
        rssFormats = new HashMap<>();
        itunesOwner = new HashMap<>();
    }

    public HashMap<String, String> getRssFormats() {
        return rssFormats;
    }

    public void setRssFormats(HashMap<String, String> rssFormats) {
        this.rssFormats = rssFormats;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getManagingEditor() {
        return managingEditor;
    }

    public void setManagingEditor(String managingEditor) {
        this.managingEditor = managingEditor;
    }

    public String getWebMaster() {
        return webMaster;
    }

    public void setWebMaster(String webMaster) {
        this.webMaster = webMaster;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getPicsRating() {
        return picsRating;
    }

    public void setPicsRating(String picsRating) {
        this.picsRating = picsRating;
    }

    public String getDocsString() {
        return docsString;
    }

    public void setDocsString(String docsString) {
        this.docsString = docsString;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getItunesNewFeedUrl() {
        return itunesNewFeedUrl;
    }

    public void setItunesNewFeedUrl(String itunesNewFeedUrl) {
        this.itunesNewFeedUrl = itunesNewFeedUrl;
    }

    public String getItunesAuthor() {
        return itunesAuthor;
    }

    public void setItunesAuthor(String itunesAuthor) {
        this.itunesAuthor = itunesAuthor;
    }

    public String getItunesSubtitle() {
        return itunesSubtitle;
    }

    public void setItunesSubtitle(String itunesSubtitle) {
        this.itunesSubtitle = itunesSubtitle;
    }


    public HashMap<String, String> getItunesOwner() {
        return itunesOwner;
    }

    public void setItunesOwner(HashMap<String, String> itunesOwner) {
        this.itunesOwner = itunesOwner;
    }

    public String getItunesImage() {
        return itunesImage;
    }

    public void setItunesImage(String itunesImage) {
        this.itunesImage = itunesImage;
    }

    public List<String> getItunesCategory() {
        return itunesCategory;
    }

    public void setItunesCategory(List<String> itunesCategory) {
        this.itunesCategory = itunesCategory;
    }

    public Map<String, List<String>> getUnknownFields() {
        return unknownFields;
    }

    public void setUnknownFields(Map<String, List<String>> unknownFields) {
        this.unknownFields = unknownFields;
    }

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    public String getItunesSummary() {
        return itunesSummary;
    }

    public void setItunesSummary(String itunesSummary) {
        this.itunesSummary = itunesSummary;
    }

    public String getItunesBlock() {
        return itunesBlock;
    }

    public void setItunesBlock(String itunesBlock) {
        this.itunesBlock = itunesBlock;
    }

    public String getItunesType() {
        return itunesType;
    }

    public void setItunesType(String itunesType) {
        this.itunesType = itunesType;
    }

    public MediaRestrictions getMediaRestrictions() {
        return mediaRestrictions;
    }

    public void setMediaRestrictions(MediaRestrictions mediaRestrictions) {
        this.mediaRestrictions = mediaRestrictions;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
