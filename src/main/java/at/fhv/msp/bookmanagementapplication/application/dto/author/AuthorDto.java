package at.fhv.msp.bookmanagementapplication.application.dto.author;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Objects;

public class AuthorDto {
    private Long id;
    private String firstName;
    private String lastName;

    @JsonGetter
    public Long id() {
        return id;
    }

    @JsonGetter
    public String firstName() {
        return firstName;
    }

    @JsonGetter
    public String lastName() {
        return lastName;
    }

    public static Builder builder() {
        return new Builder();
    }

    private AuthorDto() {}

    public static class Builder {
        private final AuthorDto instance;

        private Builder() {
            this.instance = new AuthorDto();
        }

        public Builder withId(Long id) {
            this.instance.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.instance.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.instance.lastName = lastName;
            return this;
        }

        public AuthorDto build() {
            Objects.requireNonNull(this.instance.id, "id must be set in AuthorDto");
            Objects.requireNonNull(this.instance.firstName, "firstName must be set in AuthorDto");
            Objects.requireNonNull(this.instance.lastName, "lastName must be set in AuthorDto");

            return this.instance;
        }
    }
}
