package bdc.ex1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

}
