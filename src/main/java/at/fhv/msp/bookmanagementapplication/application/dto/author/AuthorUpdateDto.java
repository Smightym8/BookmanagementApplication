package at.fhv.msp.bookmanagementapplication.application.dto.author;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;
import java.util.Objects;

public class AuthorUpdateDto {
    private String firstName;
    private String lastName;
    private List<Long> bookIds;

    @JsonGetter
    public String firstName() {
        return firstName;
    }

    @JsonGetter
    public String lastName() {
        return lastName;
    }

    @JsonGetter
    public List<Long> bookIds() {
        return bookIds;
    }

    public static Builder builder() {
        return new Builder();
    }

    private AuthorUpdateDto() {}

    public static class Builder {
        private final AuthorUpdateDto instance;

        private Builder() {
            this.instance = new AuthorUpdateDto();
        }

        public Builder withFirstName(String firstName) {
            this.instance.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.instance.lastName = lastName;
            return this;
        }

        public Builder withBookIds(List<Long> bookIds) {
            this.instance.bookIds = bookIds;
            return this;
        }

        public AuthorUpdateDto build() {
            Objects.requireNonNull(this.instance.firstName, "firstName must be set in AuthorUpdateDto");
            Objects.requireNonNull(this.instance.lastName, "lastName must be set in AuthorUpdateDto");
            Objects.requireNonNull(this.instance.bookIds, "bookIds must be set in AuthorUpdateDto");

            return this.instance;
        }
    }
}
