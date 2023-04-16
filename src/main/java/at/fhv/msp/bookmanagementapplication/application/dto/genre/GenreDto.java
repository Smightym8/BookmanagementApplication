package at.fhv.msp.bookmanagementapplication.application.dto.genre;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Objects;

public class GenreDto {
    private Long id;
    private String name;

    public static Builder builder() {
        return new Builder();
    }

    @JsonGetter
    public Long id() {
        return id;
    }

    @JsonGetter
    public String name() {
        return name;
    }

    private GenreDto() {
    }

    public static class Builder {
        private final GenreDto instance;

        private Builder() {
            this.instance = new GenreDto();
        }

        public Builder withId(Long id) {
            this.instance.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.instance.name = name;
            return this;
        }

        public GenreDto build() {
            Objects.requireNonNull(this.instance.id, "id must be set in GenreDto");
            Objects.requireNonNull(this.instance.name, "name must be set in GenreDto");

            return instance;
        }
    }
}
