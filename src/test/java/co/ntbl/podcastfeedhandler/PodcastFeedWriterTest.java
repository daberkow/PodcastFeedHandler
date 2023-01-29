package co.ntbl.podcastfeedhandler;

import org.junit.jupiter.api.Test;
import org.xmlunit.assertj3.XmlAssert;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

class PodcastFeedWriterTest {
    @Test
    void TestWriteTal() {
        String xmlName = "tal.xml";
        PodcastTest podcastTest = new PodcastTest();
        Podcast podcast = podcastTest.stringXmlToPodcastObject(xmlName);
        PodcastFeedWriter writer = new PodcastFeedWriter();
        writer.setAddGeneratedElement(false);
        writer.setAddGeneratedTime(false);

        String returnedFeed;
        try {
            returnedFeed = writer.getXml(podcast);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }

        try {
            File tempFile = File.createTempFile("podcastFeed_test_", ".xml");
            Files.write(tempFile.toPath(), returnedFeed.getBytes(StandardCharsets.UTF_8));
            System.out.println(tempFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XmlAssert
                .assertThat(podcastTest.getRawStringFromAssets(xmlName))
                .and(returnedFeed)
                .ignoreWhitespace().ignoreChildNodesOrder().ignoreElementContentWhitespace().areSimilar();
    }

    @Test
    void TestWritePm() {
        String xmlName = "planetmoney.xml";
        PodcastTest podcastTest = new PodcastTest();
        Podcast podcast = podcastTest.stringXmlToPodcastObject(xmlName);
        PodcastFeedWriter writer = new PodcastFeedWriter();
        writer.setAddGeneratedElement(false);
        writer.setAddGeneratedTime(false);

        String returnedFeed;
        try {
            returnedFeed = writer.getXml(podcast);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }

        try {
            File tempFile = File.createTempFile("podcastFeed_test_", ".xml");
            Files.write(tempFile.toPath(), returnedFeed.getBytes(StandardCharsets.UTF_8));
            System.out.println(tempFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        XmlAssert
//                .assertThat(podcastTest.getRawStringFromAssets(xmlName))
//                .and(returnedFeed)
//                .ignoreWhitespace().ignoreChildNodesOrder().ignoreElementContentWhitespace().areSimilar();
    }

}