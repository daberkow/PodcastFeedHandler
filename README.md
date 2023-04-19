# PodcastFeedHandler
[![Run Gradle on Push](https://github.com/daberkow/PodcastFeedHandler/actions/workflows/test.yml/badge.svg)](https://github.com/daberkow/PodcastFeedHandler/actions/workflows/test.yml)

A Java library to read and write podcast feeds. This library took a lot of inspiration from [MarkusLewis - Podcast Feed Library](https://github.com/MarkusLewis/Podcast-Feed-Library). They made a great library, but I wanted to be able to create feed, as well as read them; their library was very good for reading, but would need a lot of work to make it a writing library.

## Key Features
* Reading/Writing Podcast feeds, reading local files and remote
* Java 11+ compatible
* Tested, Built, Signed, and Published with GitHub actions to Maven Central
* No dependencies, 100% self contained and native code

## Progress
Reading and writing seems to be working, going to use in projects I have and document more.

## Future plans
Add support for http://search.yahoo.com/mrss/ and http://www.rawvoice.com/rawvoiceRssModule/ xml standards like sn.xml
contain.

## Getting Started
### Installation

This library is available in [Maven Central!](https://mvnrepository.com/artifact/co.ntbl/podcastfeedhandler)

Gradle
``` groovy
implementation group: 'co.ntbl', name: 'podcastfeedhandler', version: '0.1.1'
```

Maven
``` xml
<dependency>
    <groupId>co.ntbl</groupId>
    <artifactId>podcastfeedhandler</artifactId>
    <version>0.1.1</version>
</dependency>
```

### Reading a feed

``` java
String doc = getRawStringFromAssets("planetmoney.xml");
PodcastFeedReader podcastFeedReader = new PodcastFeedReader();

Podcast fromDoc;
try {
    fromDoc = podcastFeedReader.getPodcastFromDocument(doc);
} catch (MalformedURLException | PodcastFeedException e) {
    throw new RuntimeException(e);
}
```

### Writing a feed

``` java
// The required fields are title, description, iTunes Image (artwork), language, iTunes categories,
// iTunes is explicit.
Podcast myPodcast = new Podcast("My awesome podcast", "Podcast about my breakfast",
        "http://ntbl.co/image.jpg", "en-US", "Society &amp; Culture|Personal Journals",
        "true");

Episode singleEpisode = new Episode("My big Omelet", "https://ntbl.co/media/1.mp3", 9234751L, "audio/mpeg");
myPodcast.addEpisode(singleEpisode);

PodcastFeedWriter podcastFeedWriter = new PodcastFeedWriter();
String xml;
try {
    xml = podcastFeedWriter.getXml(myPodcast);
} catch (ParserConfigurationException | TransformerException e) {
    throw new RuntimeException(e);
}
```

## Support

<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" alt="JetBrains Logo (Main) logo." height="50px" width="50px">

Jetbrains supports this project through their [Open Source Development - Community Support Program](https://jb.gg/OpenSourceSupport).
