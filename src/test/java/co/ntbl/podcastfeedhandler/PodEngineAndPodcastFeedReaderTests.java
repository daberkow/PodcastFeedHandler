package co.ntbl.podcastfeedhandler;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.icosillion.podengine.exceptions.DateFormatException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.models.CloudInfo;
import com.icosillion.podengine.models.ITunesChannelInfo;
import com.icosillion.podengine.models.ITunesInfo;
import com.icosillion.podengine.models.ITunesOwner;
import com.icosillion.podengine.models.TextInputInfo;
import com.icosillion.podengine.utils.DateUtils;
import java.net.MalformedURLException;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PodEngineAndPodcastFeedReaderTests {
    private com.icosillion.podengine.models.Podcast podcast;

    /**
     * This test takes the original podengine feed, then parses it, and regenerates it. Then checks against that.
     * @throws MalformedFeedException Xml feed error
     */
    @BeforeEach
    public void before() throws MalformedFeedException {
        PodcastTest podcastTest = new PodcastTest();
        String originalPodcastFeed = podcastTest.getRawStringFromAssets("podengine.xml");
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
        assertEquals("Testing Feed", podcast.getTitle());
        assertEquals("A dummy podcast feed for testing the Podcast Feed Library.", podcast.getDescription());
        assertEquals("https://podcast-feed-library.owl.im/feed", podcast.getLink().toString());
        assertEquals("en-GB", podcast.getLanguage());
        assertEquals("Copyright 2017 Icosillion", podcast.getCopyright());
        assertEquals("Marcus Lewis (marcus@icosillion.com)", podcast.getManagingEditor());
        assertEquals("Marcus Lewis (marcus@icosillion.com)", podcast.getWebMaster());
        assertEquals("Mon, 12 Dec 2016 15:30:00 GMT", podcast.getPubDateString());
        assertEquals(DateUtils.stringToDate("Mon, 12 Dec 2016 15:30:00 GMT"), podcast.getPubDate());
//        assertEquals(DateUtils.stringToDate("Mon, 12 Dec 2016 15:30:00 GMT"), podcast.getLastBuildDate());
//        assertEquals("Mon, 12 Dec 2016 15:30:00 GMT", podcast.getLastBuildDateString());
        assertArrayEquals(new String[] { "Technology" }, podcast.getCategories());
//        assertEquals("Handcrafted", podcast.getGenerator());
        assertEquals("https://podcast-feed-library.owl.im/docs", podcast.getDocs().toString());
        assertEquals(60, (int) podcast.getTTL());
        assertEquals("https://podcast-feed-library.owl.im/images/artwork.png", podcast.getImageURL().toString());
        assertNull(podcast.getPICSRating());

        Set<Integer> skipHours = podcast.getSkipHours();
        assertTrue(skipHours.contains(0));
        assertTrue(skipHours.contains(4));
        assertTrue(skipHours.contains(8));
        assertTrue(skipHours.contains(12));
        assertTrue(skipHours.contains(16));

        Set<String> skipDays = podcast.getSkipDays();
        assertTrue(skipDays.contains("Monday"));
        assertTrue(skipDays.contains("Wednesday"));
        assertTrue(skipDays.contains("Friday"));
        assertArrayEquals(new String[] { "podcast", "java", "xml", "dom4j", "icosillion", "maven" },
                podcast.getKeywords());
        assertEquals(2, podcast.getEpisodes().size());
    }

    @Test
    public void testTextInput() throws MalformedURLException {
        TextInputInfo textInput = podcast.getTextInput();
        assertEquals("Feedback", textInput.getTitle());
        assertEquals("Feedback for the Testing Feed", textInput.getDescription());
        assertEquals("feedback", textInput.getName());
        assertEquals("https://podcast-feed-library.owl.im/feedback/submit", textInput.getLink().toString());
    }

    @Test
    public void testITunesInfo() throws Exception {
        ITunesChannelInfo iTunesInfo = podcast.getITunesInfo();
        assertEquals("Icosillion", iTunesInfo.getAuthor());
        assertEquals("A dummy podcast feed for testing the Podcast Feed Library.", iTunesInfo.getSubtitle());
        assertEquals("This podcast brings testing capabilities to the Podcast Feed Library", iTunesInfo.getSummary());
        assertFalse(iTunesInfo.isBlocked());
        assertEquals(ITunesInfo.ExplicitLevel.CLEAN, iTunesInfo.getExplicit());
        assertEquals("https://podcast-feed-library.owl.im/images/artwork.png", iTunesInfo.getImage().toString());
        assertEquals(ITunesChannelInfo.FeedType.SERIAL, iTunesInfo.getType());
    }

    @Test
    public void testITunesOwnerInfo() {
        ITunesOwner iTunesOwner = podcast.getITunesInfo().getOwner();
        assertEquals("Icosillion", iTunesOwner.getName());
        assertEquals("hello@icosillion.com", iTunesOwner.getEmail());
    }

    @Test
    public void testCloudInfo() {
        CloudInfo cloudInfo = podcast.getCloud();
        assertEquals("rpc.owl.im", cloudInfo.getDomain());
        assertEquals(8080, (int) cloudInfo.getPort());
        assertEquals("/rpc", cloudInfo.getPath());
        assertEquals("owl.register", cloudInfo.getRegisterProcedure());
        assertEquals("xml-rpc", cloudInfo.getProtocol());
    }
}
