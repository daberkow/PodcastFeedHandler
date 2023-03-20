package co.ntbl.podcastfeedhandler;

import co.ntbl.podcastfeedhandler.podcast.Image;
import co.ntbl.podcastfeedhandler.podcast.MediaRestrictions;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Node;

/**
 * Podcast Object, this is the root of a podcast feed.
 *
 * @author daberkow@github
 */
public class Podcast {
    private String title, link, description, language, copyright, managingEditor, webMaster,
            generator, picsRating, docsString, lastBuildDate, docs, ttl;

    private String itunesNewFeedUrl, itunesAuthor, itunesSubtitle, itunesImage, itunesSummary, itunesBlock, itunesType,
            itunesKeywords, itunesExplicit;
    private final List<String> itunesCategory;
    private List<String> skipHours;
    private List<String> skipDays;

    private Map<String, String> rssFormats, textInput, itunesOwner, cloud;

    private ZonedDateTime pubDate;

    private Image image;

    private MediaRestrictions mediaRestrictions;

    private Map<String, List<Node>> unknownFields;
    private final List<Episode> episodeList;

    /**
     * A blank podcast with internal arrays initialized.
     */
    public Podcast() {
        this.itunesCategory = new ArrayList<>();
        this.skipDays = new ArrayList<>();
        this.skipHours = new ArrayList<>();
        this.episodeList = new ArrayList<>();
        this.unknownFields = new HashMap<>();
        this.rssFormats = new HashMap<>();
        this.itunesOwner = new HashMap<>();
        this.textInput = new HashMap<>();
        this.cloud = new HashMap<>();
    }

    /**
     * Constructor for Podcast object, with all the fields which are required for a iTunes feed.
     *
     * @param title podcast title
     * @param description podcast description
     * @param itunesImage artwork for the podcast
     * @param language language in the 2 letter ISO639 code (or with modifiers such as "en-us")
     * @param itunesCategory category info, pipe separated
     * @param itunesExplicit a "true" or "false" for is the podcast explicit, sometimes this seems to be set to "clean"
     */
    public Podcast(String title, String description, String itunesImage, String language, String itunesCategory,
                   String itunesExplicit) {
        this.itunesCategory = new ArrayList<>();
        this.skipDays = new ArrayList<>();
        this.skipHours = new ArrayList<>();
        this.episodeList = new ArrayList<>();
        this.unknownFields = new HashMap<>();
        this.rssFormats = new HashMap<>();
        this.itunesOwner = new HashMap<>();
        this.textInput = new HashMap<>();
        this.cloud = new HashMap<>();

        this.title = title;
        this.description = description;
        this.itunesImage = itunesImage;
        this.language = language;
        this.itunesCategory.add(itunesCategory);
        this.itunesExplicit = itunesExplicit;
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

    /**
     * Return the current set ISO639 language code.
     *
     * @return two letter (with possible - modifier) language code
     */
    public String getLanguage() {
        return language;
    }

    /**
     * A 2 letter language code in compliance with <a href="http://www.loc.gov/standards/iso639-2/php/code_list.php">codes</a>.
     * Some modifiers are allowed like "en-us".
     *
     * @param language string of language
     */
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

    /**
     * Add a category to the iTunes categories, these can be multiple levels, we use | to separate them. When creating
     * a feed, those will be separated into appropriate levels. <a href="https://podcasters.apple.com/support/1691-apple-podcasts-categories">List of categories</a>.
     *
     * @param passedItunesCategory pipe separated categories (ex "Society &amp; Culture|Personal Journals")
     */
    public void addItunesCategory(String passedItunesCategory) {
        getItunesCategory().add(passedItunesCategory);
    }

    public Map<String, List<Node>> getUnknownFields() {
        return unknownFields;
    }

    public void setUnknownFields(Map<String, List<Node>> unknownFields) {
        this.unknownFields = unknownFields;
    }

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public void addEpisode(Episode singleEpisode) {
        getEpisodeList().add(singleEpisode);
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

    /**
     * While this is labeled a required field, there are large podcasts that don't have it. iTunes doc says it should be
     * true or false, yet I also have found "clean" or "yes"/"no" used.
     *
     * @param itunesExplicit in theory true or false for if the podcast is explicit
     */
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

    /**
     * This checks for the minimal required fields for a feed to work in iTunes. The required fields are title,
     * description, iTunes Image (artwork), language, iTunes categories, iTunes is explicit.
     *
     * @return true if podcast has all the fields it needs
     */
    @SuppressWarnings("CyclomaticComplexity")
    public boolean isContainingAllRequiredItunesFields() {
        return (getTitle() != null && !getTitle().isEmpty())
                && (getDescription() != null && !getDescription().isEmpty())
                && (getItunesImage() != null && !getItunesImage().isEmpty())
                && (getLanguage() != null && !getLanguage().isEmpty())
                && !getItunesCategory().isEmpty()
                && (getItunesExplicit() != null && !getItunesExplicit().isEmpty());
    }
}
