package com.touchiteasy.http.validation;

import org.junit.Test;

public class ValidatorCollectionTest {
    @Test (expected = IllegalArgumentException.class)
    public void GivenNoValidator_ShouldThrowException() {
        new ValidatorCollection();
    }
}
