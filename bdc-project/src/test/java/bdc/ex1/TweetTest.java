package bdc.ex1;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import bdc.jsonexamples.ExampleTweet;

public class TweetTest {

    @Test
    public void testTweet() {
        Tweet tweet = Tweet.fromJson(ExampleTweet.tweet);
        assertEquals("ladie131994", tweet.getUserName());
        assertEquals("RT @daniandreajonas: RT @DisneyStarFacts", tweet.getText().substring(0, 40));
        assertNotNull(tweet.getCreatedAt());
        assertEquals(42011092947312640L, tweet.getId());

        Set<String> mentions = new HashSet<String>(Arrays.asList("daniandreajonas", "DisneyStarFacts"));
        assertEquals(mentions, tweet.getMentions());
    }

    @Test
    public void testRetweetHashtag() throws Exception {
        InputStream stream = TweetTest.class.getResourceAsStream("hashtag.json");
        String tweetString = IOUtils.toString(stream);

        Tweet tweet = Tweet.fromJson(tweetString);
        assertTrue(tweet.isRetweet());

        Tweet original = tweet.getRetweeted();
        Set<String> hashTags = new HashSet<String>(Arrays.asList("RT"));
        assertEquals(hashTags, original.getHashTags());
    }

}
