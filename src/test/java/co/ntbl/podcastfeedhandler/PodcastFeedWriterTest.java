package co.ntbl.podcastfeedhandler;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlunit.assertj.XmlAssert;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

class PodcastFeedWriterTest {
    @Test
    void TestWriteTal() {
        String xmlName = "tal.xml";
        PodcastTest podcastTest = new PodcastTest();
        String originalPodcastFeed = podcastTest.getRawStringFromAssets(xmlName);
        PodcastFeedHandler classUnderTest = new PodcastFeedHandler();
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
        String newParsedDoc = writer.toString();

        System.out.println("fixed theirs feed");
        System.out.println(newParsedDoc);

        System.out.println("Generated feed");
        System.out.println(returnedFeed);

        XmlAssert
            .assertThat(newParsedDoc)
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