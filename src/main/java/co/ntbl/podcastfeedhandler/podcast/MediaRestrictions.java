package co.ntbl.podcastfeedhandler.podcast;

import java.util.Map;

public class MediaRestrictions {
    private Map<String, String> attributes;
    private String field;

    public MediaRestrictions(Map<String, String> attributes, String field) {
        this.attributes = attributes;
        this.field = field;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
