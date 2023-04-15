package at.fhv.msp.bookmanagementapplication.application.dto.book;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class BookCreateDto {
    private String isbn;
    private String title;
    private LocalDate publicationDate;
    private BigDecimal price;
    private Long genreId;
    private List<Long> authorIds;

    public static Builder builder() {
        return new Builder();
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
    public Long genreId() {
        return genreId;
    }

    @JsonGetter
    public List<Long> authorIds() { return authorIds; }

    private BookCreateDto() {}

    public static class Builder {
        private final BookCreateDto instance;

        private Builder() {
            this.instance = new BookCreateDto();
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

        public Builder withGenreId(Long genreId) {
            this.instance.genreId = genreId;
            return this;
        }

        public Builder withAuthorIds(List<Long> authorIds) {
            this.instance.authorIds = authorIds;
            return this;
        }

        public BookCreateDto build() {
            Objects.requireNonNull(this.instance.isbn, "isbn must be set in BookCreateDto");
            Objects.requireNonNull(this.instance.title, "title must be set in BookCreateDto");
            Objects.requireNonNull(this.instance.publicationDate, "publicationDate must be set in BookCreateDto");
            Objects.requireNonNull(this.instance.price, "price must be set in BookCreateDto");
            Objects.requireNonNull(this.instance.genreId, "genreId must be set in BookCreateDto");
            Objects.requireNonNull(this.instance.authorIds, "authorIds must be set in BookCreateDto");

            return this.instance;
        }
    }
}
