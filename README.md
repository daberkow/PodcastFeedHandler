# PodcastFeedHandler
[![Run Gradle on Push](https://github.com/daberkow/PodcastFeedHandler/actions/workflows/test.yml/badge.svg)](https://github.com/daberkow/PodcastFeedHandler/actions/workflows/test.yml)

A Java library to read and write podcast feeds. This library took a lot of inspiration from [MarkusLewis - Podcast Feed Library](https://github.com/MarkusLewis/Podcast-Feed-Library). They made a great library, but I wanted to be able to create feed, as well as read them; their library was very good for reading, but would need a lot of work to make it a writing library.

## Key Features
* Reading/Writing Podcast feeds, reading local files and remote
* Java 11+ compatible
* No dependencies, 100% self contained and native code

## Progress
Reading in seems to work well, need to test more. Then work on writing feeds. That should work but has not been tested.


## Getting Started
### Installation

### Reading a feed

```
String doc = getRawStringFromAssets("planetmoney.xml");
PodcastFeedHandler podcastFeedHandler = new PodcastFeedHandler();

Podcast fromDoc;
try {
    fromDoc = podcastFeedHandler.getPodcastFromDocument(doc);
} catch (MalformedURLException | PodcastFeedException e) {
    throw new RuntimeException(e);
}
```

### Writing a feed