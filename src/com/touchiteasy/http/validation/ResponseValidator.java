package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

public abstract class ResponseValidator {
    public static class ValidationResult {
        public static ValidationResult invalid(String causeDescription) {
            return new ValidationResult(false, causeDescription);
        }

        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        protected final boolean isValid;
        protected final String causeDescription;

        private ValidationResult(boolean isValid, String causeDescription){
            this.isValid = isValid;
            this.causeDescription = causeDescription;
        }
    }

    protected abstract ValidationResult analyse(Response response);
}
