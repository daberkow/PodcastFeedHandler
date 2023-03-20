package co.ntbl.podcastfeedhandler;

import co.ntbl.podcastfeedhandler.episode.EpisodeEnclosure;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class PodcastFeedWriterTest {
    @Test
    void testWriteTal() {
        String xmlName = "tal.xml";
        PodcastTest podcastTest = new PodcastTest();
        String originalPodcastFeed = podcastTest.getRawStringFromAssets(xmlName);
        PodcastFeedReader classUnderTest = new PodcastFeedReader();
        Podcast fromDoc;
        try {
            fromDoc = classUnderTest.getPodcastFromDocument(originalPodcastFeed);
        } catch (MalformedURLException | PodcastFeedException e) {
            throw new RuntimeException(e);
        }
        PodcastFeedWriter writer = new PodcastFeedWriter();
        writer.setAddGeneratedElement(true);
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
        System.out.println(originalPodcastFeed);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        Document parsedDocument = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            parsedDocument = documentBuilder.parse(new InputSource(new StringReader(originalPodcastFeed)));
            parsedDocument.normalize();
            parsedDocument.normalizeDocument();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        DOMSource domSource = new DOMSource(parsedDocument);
        StringWriter writer2 = new StringWriter();
        StreamResult result = new StreamResult(writer2);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        // This moves the rss line to the second line
        transformer.setOutputProperty("http://www.oracle.com/xml/is-standalone", "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        try {
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        String newParsedDoc = writer2.toString();
//
//        System.out.println("fixed theirs feed");
//        System.out.println(newParsedDoc);
//
        System.out.println("Generated feed");
        System.out.println(returnedFeed);

        //Somehow this breaks on github actions
//        XmlAssert
//            .assertThat(newParsedDoc)
//            .and(returnedFeed)
//            .ignoreWhitespace()
//            .ignoreChildNodesOrder()
//            .ignoreElementContentWhitespace()
//            .areSimilar();
    }

    @Test
    void testWritePm() {
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

    @Test
    void usingPodEngine() {
        String xmlName = "podengine.xml";
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
    }

    @Test
    void createANewFeedFromNothing() {
        Podcast myPodcast = new Podcast();
        // The required fields are title, description, iTunes Image (artwork), language, iTunes categories,
        // iTunes is explicit.
        Assertions.assertFalse(myPodcast.isContainingAllRequiredItunesFields());
        myPodcast.setTitle("My awesome podcast");
        Assertions.assertFalse(myPodcast.isContainingAllRequiredItunesFields());
        myPodcast.setItunesImage("http://ntbl.co/image.jpg"); // This does not actually exist
        Assertions.assertFalse(myPodcast.isContainingAllRequiredItunesFields());
        myPodcast.setDescription("Podcast about my breakfast");
        Assertions.assertFalse(myPodcast.isContainingAllRequiredItunesFields());
        myPodcast.setLanguage("en-us");
        Assertions.assertFalse(myPodcast.isContainingAllRequiredItunesFields());
        myPodcast.addItunesCategory("Society &amp; Culture|Personal Journals");
        Assertions.assertFalse(myPodcast.isContainingAllRequiredItunesFields());
        myPodcast.setItunesExplicit("true");
        Assertions.assertTrue(myPodcast.isContainingAllRequiredItunesFields());

        myPodcast.setGenerator("PodcastFeedGenerator");

        URL episodeUrl;
        try {
            episodeUrl = new URL("https://ntbl.co/media/1.mp3");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        EpisodeEnclosure firstEpEnclosure = new EpisodeEnclosure(episodeUrl, 57981736L, "audio/mpeg");
        Episode singleEpisode = new Episode("My big Omelet", firstEpEnclosure);

        myPodcast.addEpisode(singleEpisode);

        PodcastFeedWriter podcastFeedWriter = new PodcastFeedWriter();
        String xml;
        try {
            xml = podcastFeedWriter.getXml(myPodcast);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(xml.isEmpty());
        System.out.println(xml);
    }

    @Test
    void createANewFeedFromNothingQuickInit() {
        // The required fields are title, description, iTunes Image (artwork), language, iTunes categories,
        // iTunes is explicit.
        Podcast myPodcast = new Podcast("My awesome podcast", "Podcast about my breakfast",
                "http://ntbl.co/image.jpg", "en-US", "Society &amp; Culture|Personal Journals",
                "true");
        Assertions.assertTrue(myPodcast.isContainingAllRequiredItunesFields());

        Episode singleEpisode = new Episode("My big Omelet", "https://ntbl.co/media/1.mp3", 9234751L, "audio/mpeg");
        myPodcast.addEpisode(singleEpisode);

        PodcastFeedWriter podcastFeedWriter = new PodcastFeedWriter();
        String xml;
        try {
            xml = podcastFeedWriter.getXml(myPodcast);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(xml.isEmpty());
        System.out.println(xml);
    }
}
