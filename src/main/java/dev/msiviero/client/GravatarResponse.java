package dev.msiviero.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue
public abstract class GravatarResponse {

    public abstract List<GravatarProfile> entry();

    @JsonCreator
    public static GravatarResponse create(@JsonProperty("entry") final List<GravatarProfile> gravatarProfile) {
        return new AutoValue_GravatarResponse(gravatarProfile);
    }
}