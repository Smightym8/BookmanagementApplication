package at.fhv.msp.bookmanagementapplication.application.dto.author;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Objects;

public class AuthorCreateDto {
    private String firstName;
    private String lastName;

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

    private AuthorCreateDto() {}

    public static class Builder {
        private final AuthorCreateDto instance;

        private Builder() {
            this.instance = new AuthorCreateDto();
        }

        public Builder withFirstName(String firstName) {
            this.instance.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.instance.lastName = lastName;
            return this;
        }

        public AuthorCreateDto build() {
            Objects.requireNonNull(this.instance.firstName, "firstName must be set in AuthorCreateDto");
            Objects.requireNonNull(this.instance.lastName, "lastName must be set in AuthorCreateDto");

            return this.instance;
        }
    }
}
