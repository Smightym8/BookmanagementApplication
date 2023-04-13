package at.fhv.msp.bookmanagementapplication.application.dto.book;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class BookDto {
    private Long id;
    private String isbn;
    private String title;
    private LocalDate publicationDate;
    private BigDecimal price;
    private String genre;
    private List<String> authorNames;

    public static Builder builder() {
        return new Builder();
    }

    @JsonGetter
    public Long id() {
        return id;
    }

    @JsonGetter
    public String isbn() {
        return isbn;
    }

    @JsonGetter
    public String title() {
        return title;
    }

    @JsonGetter
    public LocalDate publicationDate() {
        return publicationDate;
    }

    @JsonGetter
    public BigDecimal price() {
        return price;
    }

    @JsonGetter
    public String genre() {
        return genre;
    }

    @JsonGetter
    public List<String> authorNames() { return authorNames; }

    private BookDto() {}

    public static class Builder {
        private final BookDto instance;

        private Builder() {
            this.instance = new BookDto();
        }

        public Builder withId(Long id) {
            this.instance.id = id;
            return this;
        }

        public Builder withIsbn(String isbn) {
            this.instance.isbn = isbn;
            return this;
        }

        public Builder withTitle(String title) {
            this.instance.title = title;
            return this;
        }

        public Builder withPublicationDate(LocalDate publicationDate) {
            this.instance.publicationDate = publicationDate;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.instance.price = price;
            return this;
        }

        public Builder withGenre(String genre) {
            this.instance.genre = genre;
            return this;
        }

        public Builder withAuthorNames(List<String> authorNames) {
            this.instance.authorNames = authorNames;
            return this;
        }

        public BookDto build() {
            Objects.requireNonNull(this.instance.id, "id must be set in BookDto");
            Objects.requireNonNull(this.instance.isbn, "isbn must be set in BookDto");
            Objects.requireNonNull(this.instance.title, "title must be set in BookDto");
            Objects.requireNonNull(this.instance.publicationDate, "publicationDate must be set in BookDto");
            Objects.requireNonNull(this.instance.price, "price must be set in BookDto");
            Objects.requireNonNull(this.instance.genre, "genre must be set in BookDto");
            Objects.requireNonNull(this.instance.authorNames, "authorNames must be set in BookDto");

            return this.instance;
        }
    }
}
