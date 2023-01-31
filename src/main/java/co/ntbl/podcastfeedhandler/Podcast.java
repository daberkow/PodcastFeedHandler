package co.ntbl.podcastfeedhandler;

import co.ntbl.podcastfeedhandler.podcast.Image;
import co.ntbl.podcastfeedhandler.podcast.MediaRestrictions;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Podcast {
    private String title, link, description, language, copyright, managingEditor, webMaster,
            generator, picsRating, docsString, lastBuildDate, docs, ttl;

    private String itunesNewFeedUrl, itunesAuthor, itunesSubtitle, itunesImage, itunesSummary, itunesBlock, itunesType,
            itunesKeywords, itunesExplicit;
    private List<String> itunesCategory, skipHours, skipDays;

    private Map<String, String> rssFormats, textInput, itunesOwner, cloud;

    private ZonedDateTime pubDate;

    private Image image;

    private MediaRestrictions mediaRestrictions;

    private Map<String, List<String>> unknownFields;
    private List<Episode> episodeList;

    /**
     * A blank podcast with internal arrays initialized.
     */
    public Podcast() {
        itunesCategory = new ArrayList<>();
        skipDays = new ArrayList<>();
        skipHours = new ArrayList<>();
        episodeList = new ArrayList<>();
        unknownFields = new HashMap<>();
        rssFormats = new HashMap<>();
        itunesOwner = new HashMap<>();
        textInput = new HashMap<>();
        cloud = new HashMap<>();
    }

    /**
     * These are the RSS type attributes in a field, some podcast feeds include extra types. Note: The default iTunes
     * types will not show up in here, but will be added to a feed.
     *
     * @return Hashmap of extra attributes to add on RSS
     */
    public Map<String, String> getRssFormats() {
        return rssFormats;
    }

    public void setRssFormats(Map<String, String> rssFormats) {
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

    public ZonedDateTime getPubDate() {
        return pubDate;
    }

    public void setPubDate(ZonedDateTime pubDate) {
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


    public Map<String, String> getItunesOwner() {
        return itunesOwner;
    }

    public void setItunesOwner(Map<String, String> itunesOwner) {
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

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getItunesKeywords() {
        return itunesKeywords;
    }

    public void setItunesKeywords(String itunesKeywords) {
        this.itunesKeywords = itunesKeywords;
    }

    public String getItunesExplicit() {
        return itunesExplicit;
    }

    public void setItunesExplicit(String itunesExplicit) {
        this.itunesExplicit = itunesExplicit;
    }

    public List<String> getSkipHours() {
        return skipHours;
    }

    public void setSkipHours(List<String> skipHours) {
        this.skipHours = skipHours;
    }

    public List<String> getSkipDays() {
        return skipDays;
    }

    public void setSkipDays(List<String> skipDays) {
        this.skipDays = skipDays;
    }

    public Map<String, String> getTextInput() {
        return textInput;
    }

    public void setTextInput(Map<String, String> textInput) {
        this.textInput = textInput;
    }

    public Map<String, String> getCloud() {
        return cloud;
    }

    public void setCloud(Map<String, String> cloud) {
        this.cloud = cloud;
    }
}
