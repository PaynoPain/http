package com.touchiteasy.http.validation;

import com.touchiteasy.http.IdentifiableResponse;
import com.touchiteasy.http.Response;

public abstract class ResponseValidator {
    protected static class ValidationResult {
        protected static ValidationResult invalid(String causeDescription) {
            return new ValidationResult(false, causeDescription);
        }

        protected static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        public final boolean isValid;
        public final String causeDescription;

        private ValidationResult(boolean isValid, String causeDescription){
            this.isValid = isValid;
            this.causeDescription = causeDescription;
        }
    }

    public static class InvalidResponseException extends RuntimeException {
        public InvalidResponseException(String message){
            super(message);
        }
    }

    protected abstract ValidationResult analyse(Response response);

    public void validate(Response response) throws InvalidResponseException {
        final ValidationResult result = analyse(response);
        if (!result.isValid)
            throw new InvalidResponseException(String.format(
                    "The following response is invalid because %s.\n%s",
                    result.causeDescription,
                    new IdentifiableResponse(response).toString()
            ));
    }
}
