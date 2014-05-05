package org.elasticsearch.river.twitter;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

public class TwitterRiverBuilder {

    public XContentBuilder buildFromStatus(Status status) throws IOException {

        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
        builder.field("text", status.getText());
        builder.field("created_at", status.getCreatedAt());
        builder.field("source", status.getSource());
        builder.field("truncated", status.isTruncated());
        builder.field("language", status.getIsoLanguageCode());

        if (status.getUserMentionEntities() != null) {
            builder.startArray("mention");
            for (UserMentionEntity user : status.getUserMentionEntities()) {
                builder.startObject();
                builder.field("id", user.getId());
                builder.field("name", user.getName());
                builder.field("screen_name", user.getScreenName());
                builder.field("start", user.getStart());
                builder.field("end", user.getEnd());
                builder.endObject();
            }
            builder.endArray();
        }

        if (status.getRetweetCount() != -1) {
            builder.field("retweet_count", status.getRetweetCount());
        }

        if (status.isRetweet() && status.getRetweetedStatus() != null) {
            builder.startObject("retweet");
            builder.field("id", status.getRetweetedStatus().getId());
            if (status.getRetweetedStatus().getUser() != null) {
                builder.field("user_id", status.getRetweetedStatus().getUser().getId());
                builder.field("user_screen_name", status.getRetweetedStatus().getUser().getScreenName());
                if (status.getRetweetedStatus().getRetweetCount() != -1) {
                    builder.field("retweet_count", status.getRetweetedStatus().getRetweetCount());
                }
            }
            builder.endObject();
        }

        if (status.getInReplyToStatusId() != -1) {
            builder.startObject("in_reply");
            builder.field("status", status.getInReplyToStatusId());
            if (status.getInReplyToUserId() != -1) {
                builder.field("user_id", status.getInReplyToUserId());
                builder.field("user_screen_name", status.getInReplyToScreenName());
            }
            builder.endObject();
        }

        if (status.getHashtagEntities() != null) {
            builder.startArray("hashtag");
            for (HashtagEntity hashtag : status.getHashtagEntities()) {
                builder.startObject();
                builder.field("text", hashtag.getText());
                builder.field("start", hashtag.getStart());
                builder.field("end", hashtag.getEnd());
                builder.endObject();
            }
            builder.endArray();
        }
        if (status.getContributors() != null && status.getContributors().length > 0) {
            builder.array("contributor", status.getContributors());
        }
        if (status.getGeoLocation() != null) {
            builder.startObject("location");
            builder.field("lat", status.getGeoLocation().getLatitude());
            builder.field("lon", status.getGeoLocation().getLongitude());
            builder.endObject();
        }
        if (status.getPlace() != null) {
            builder.startObject("place");
            builder.field("id", status.getPlace().getId());
            builder.field("name", status.getPlace().getName());
            builder.field("type", status.getPlace().getPlaceType());
            builder.field("full_name", status.getPlace().getFullName());
            builder.field("street_address", status.getPlace().getStreetAddress());
            builder.field("country", status.getPlace().getCountry());
            builder.field("country_code", status.getPlace().getCountryCode());
            builder.field("url", status.getPlace().getURL());
            builder.endObject();
        }
        if (status.getURLEntities() != null) {
            builder.startArray("link");
            for (URLEntity url : status.getURLEntities()) {
                if (url != null) {
                    builder.startObject();
                    if (url.getURL() != null) {
                        builder.field("url", url.getURL());
                    }
                    if (url.getDisplayURL() != null) {
                        builder.field("display_url", url.getDisplayURL());
                    }
                    if (url.getExpandedURL() != null) {
                        builder.field("expand_url", url.getExpandedURL());
                    }
                    builder.field("start", url.getStart());
                    builder.field("end", url.getEnd());
                    builder.endObject();
                }
            }
            builder.endArray();
        }

        builder.startObject("user");
        builder.field("id", status.getUser().getId());
        builder.field("name", status.getUser().getName());
        builder.field("screen_name", status.getUser().getScreenName());
        builder.field("location", status.getUser().getLocation());
        builder.field("description", status.getUser().getDescription());
        builder.field("profile_image_url", status.getUser().getProfileImageURL());
        builder.field("profile_image_url_https", status.getUser().getProfileImageURLHttps());

        builder.endObject();

        builder.endObject();
        return builder;
    }
}
