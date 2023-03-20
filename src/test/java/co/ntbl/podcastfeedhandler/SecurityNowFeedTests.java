package co.ntbl.podcastfeedhandler;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.icosillion.podengine.exceptions.DateFormatException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.models.ITunesChannelInfo;
import com.icosillion.podengine.models.ITunesInfo;
import com.icosillion.podengine.models.ITunesOwner;
import com.icosillion.podengine.utils.DateUtils;
import java.net.MalformedURLException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SecurityNowFeedTests {
    private com.icosillion.podengine.models.Podcast podcast;

    /**
     * This test takes the original podengine feed, then parses it, and regenerates it. Then checks against that.
     * @throws MalformedFeedException Xml feed error
     */
    @BeforeEach
    public void before() throws MalformedFeedException {
        PodcastTest podcastTest = new PodcastTest();
        String originalPodcastFeed = podcastTest.getRawStringFromAssets("sn.xml");
        PodcastFeedReader classUnderTest = new PodcastFeedReader();
        Podcast fromDoc;
        try {
            fromDoc = classUnderTest.getPodcastFromDocument(originalPodcastFeed);
        } catch (MalformedURLException | PodcastFeedException e) {
            throw new RuntimeException(e);
        }
        PodcastFeedWriter writer = new PodcastFeedWriter();
        writer.setAddGeneratedElement(false);
        writer.setAddGeneratedTime(false);

        String returnedFeed;
        try {
            returnedFeed = writer.getXml(fromDoc);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
        podcast = new com.icosillion.podengine.models.Podcast(returnedFeed);
    }

    @Test
    public void testOverview() throws MalformedFeedException, MalformedURLException, DateFormatException {
        assertEquals("Security Now (Audio)", podcast.getTitle());
        assertEquals("Steve Gibson, the man who coined the term spyware and created the first anti-spyware "
                + "program, creator of SpinRite and ShieldsUP, discusses the hot topics in security today with "
                + "Leo Laporte.\n"
                + "\n"
                + "Records live every Tuesday at 4:30pm Eastern / 1:30pm Pacific / 20:30 UTC.",
                podcast.getDescription());
        assertEquals("https://twit.tv/shows/security-now", podcast.getLink().toString());
        assertEquals("en-US", podcast.getLanguage());
        assertEquals("This work is licensed under a Creative Commons License - Attribution-NonCommercial-NoDerivatives 4.0 International - http://creativecommons.org/licenses/by-nc-nd/4.0/", podcast.getCopyright());
        assertEquals("distro@twit.tv (TWiT Editors)", podcast.getManagingEditor());
        assertEquals("distro@twit.tv (TWiT Engineering)", podcast.getWebMaster());
        assertEquals("Wed, 15 Mar 2023 10:06:23 -0700", podcast.getPubDateString());
        assertEquals(DateUtils.stringToDate("Wed, 15 Mar 2023 10:06:23 PDT"), podcast.getPubDate());
//        assertEquals(DateUtils.stringToDate("Mon, 12 Dec 2016 15:30:00 GMT"), podcast.getLastBuildDate());
//        assertEquals("Mon, 12 Dec 2016 15:30:00 GMT", podcast.getLastBuildDateString());
        assertArrayEquals(new String[] { "Technology" }, podcast.getCategories());
//        assertEquals("Handcrafted", podcast.getGenerator());
        assertEquals("http://blogs.law.harvard.edu/tech/rss", podcast.getDocs().toString());
        assertEquals(720, (int) podcast.getTTL());
        assertEquals("https://elroy.twit.tv/sites/default/files/styles/twit_album_art_2048x2048/public/images/shows/security_now/album_art/audio/sn2022_albumart_standard_2400.jpg?itok=hQwhlisn", podcast.getImageURL().toString());
        assertNotNull(podcast.getPICSRating());

        assertArrayEquals(new String[] { "TWiT", "Technology", "Steve Gibson", "Leo Laporte", "security", "spyware",
                "malware", "hacking", "cyber crime", "emcryption" },
                podcast.getKeywords());
        assertEquals(10, podcast.getEpisodes().size());
        System.out.println(podcast.getXMLData());
    }

    @Test
    public void testITunesInfo() throws Exception {
        ITunesChannelInfo iTunesInfo = podcast.getITunesInfo();
        assertEquals("TWiT", iTunesInfo.getAuthor());
        assertEquals("Steve Gibson discusses the hot topics in security today with Leo Laporte.",
                iTunesInfo.getSubtitle());
        assertEquals("Steve Gibson, the man who coined the term spyware and created the first anti-spyware "
                + "program, creator of SpinRite and ShieldsUP, discusses the hot topics in security today with "
                + "Leo Laporte.\n"
                + "\n"
                + "Records live every Tuesday at 4:30pm Eastern / 1:30pm Pacific / 20:30 UTC.",
                iTunesInfo.getSummary());
        assertFalse(iTunesInfo.isBlocked());
        assertEquals(ITunesInfo.ExplicitLevel.NO, iTunesInfo.getExplicit());
        assertEquals("https://elroy.twit.tv/sites/default/files/styles/twit_album_art_2048x2048/public/images/shows/security_now/album_art/audio/sn2022_albumart_standard_2400.jpg?itok=hQwhlisn", iTunesInfo.getImage().toString());
        assertEquals(ITunesChannelInfo.FeedType.EPISODIC, iTunesInfo.getType());
    }

    @Test
    public void testITunesOwnerInfo() {
        ITunesOwner iTunesOwner = podcast.getITunesInfo().getOwner();
        assertEquals("Leo Laporte", iTunesOwner.getName());
        assertEquals("distro@twit.tv", iTunesOwner.getEmail());
    }
}
