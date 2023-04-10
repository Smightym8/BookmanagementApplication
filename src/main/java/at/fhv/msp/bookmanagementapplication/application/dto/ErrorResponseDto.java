package at.fhv.msp.bookmanagementapplication.application.dto;

import java.util.Objects;

public class ErrorResponseDto {
    private int statusCode;
    private String message;

    public static Builder builder() {
        return new Builder();
    }

    public int statusCode() {
        return statusCode;
    }

    public String message() {
        return message;
    }

    private ErrorResponseDto() {}

    public static class Builder {
        private final ErrorResponseDto instance;

        private Builder() {
            this.instance = new ErrorResponseDto();
        }

        public Builder withStatusCode(int statusCode) {
            this.instance.statusCode = statusCode;
            return this;
        }

        public Builder withMessage(String message) {
            this.instance.message = message;
            return this;
        }

        public ErrorResponseDto build() {
            Objects.requireNonNull(this.instance.message, "message must be set in ErrorResponseDto");

            return this.instance;
        }
    }
}
