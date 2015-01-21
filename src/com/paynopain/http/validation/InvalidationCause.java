package com.paynopain.http.validation;

public class InvalidationCause {
    protected final String causeDescription;

    public InvalidationCause(String causeDescription) {
        this.causeDescription = causeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvalidationCause that = (InvalidationCause) o;

        if (causeDescription != null ? !causeDescription.equals(that.causeDescription) : that.causeDescription != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return causeDescription != null ? causeDescription.hashCode() : 0;
    }
}