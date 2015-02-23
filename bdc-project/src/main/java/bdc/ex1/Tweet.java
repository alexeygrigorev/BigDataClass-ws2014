package bdc.ex1;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/**
 * Class which takes in Tweet extracts some information
 */
public class Tweet {

    private long id;
    private String name;
    private String text;
    private Date createdAt;
    private Set<String> mentions = new HashSet<>();

    public static Tweet fromJson(String jsonString) {
        JSONObject json = (JSONObject) JSONValue.parse(jsonString);

        JSONObject user = (JSONObject) json.get("user");
        String name = user.getAsString("screen_name");
        String text = json.getAsString("text");
        Date createdAt = DateHelpers.parse(json.getAsString("created_at"));
        Number id = json.getAsNumber("id");

        Tweet tweet = new Tweet(name, text, createdAt);
        tweet.id = id.longValue();

        JSONObject entities = (JSONObject) json.get("entities");
        @SuppressWarnings("unchecked")
        List<JSONObject> mentions = (List<JSONObject>) entities.get("user_mentions");

        for (JSONObject mention : mentions) {
            tweet.mentions.add(mention.getAsString("screen_name"));
        }

        return tweet;
    }

    public Tweet(String name, String text, Date createdAt) {
        this.name = name;
        this.text = text;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Set<String> getMentions() {
        return mentions;
    }

    public boolean containsMentionOf(Collection<String> others) {
        for (String other : others) {
            if (mentions.contains(other)) {
                return true;
            }
        }
        return false;
    }
}
