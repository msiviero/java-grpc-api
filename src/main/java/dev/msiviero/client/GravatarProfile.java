package dev.msiviero.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GravatarProfile {
    
    public abstract String displayName();

    @JsonCreator
    public static GravatarProfile create(@JsonProperty("displayName") final String displayName) {
        return new AutoValue_GravatarProfile(displayName);
    }
}
