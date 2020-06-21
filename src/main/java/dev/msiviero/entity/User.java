package dev.msiviero.entity;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;
import javax.annotation.Nullable;

@Entity
@Table(name = "users")
public interface User {

    @Key
    @Generated
    int id();

    String username();

    String password();

    @Nullable
    String token();

    void token(String token);
}
