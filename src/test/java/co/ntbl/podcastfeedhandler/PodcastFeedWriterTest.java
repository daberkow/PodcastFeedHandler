package co.ntbl.podcastfeedhandler;

import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

class PodcastFeedWriterTest {
    @Test
    void TestWriteTal() {
        String xmlName = "tal.xml";
        PodcastTest podcastTest = new PodcastTest();
        String podcastFeedData = podcastTest.getRawStringFromAssets(xmlName);
        PodcastFeedHandler classUnderTest = new PodcastFeedHandler();
        Podcast fromDoc;
        try {
            fromDoc = classUnderTest.getPodcastFromDocument(podcastFeedData);
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

        try {
            File tempFile = File.createTempFile("podcastFeed_test_", ".xml");
            Files.write(tempFile.toPath(), returnedFeed.getBytes(StandardCharsets.UTF_8));
            System.out.println(tempFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Direct feed");
        System.out.println(podcastFeedData);
        System.out.println("Generated feed");
        System.out.println(returnedFeed);

        XmlAssert
            .assertThat(podcastFeedData)
            .and(returnedFeed)
            .ignoreWhitespace()
            .ignoreChildNodesOrder()
            .ignoreElementContentWhitespace()
            .areSimilar();
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
//            .assertThat(podcastTest.getRawStringFromAssets(xmlName))
//            .and(returnedFeed)
//            .ignoreWhitespace().ignoreChildNodesOrder().ignoreElementContentWhitespace().areSimilar();
    }

}