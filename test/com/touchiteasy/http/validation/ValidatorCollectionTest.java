package com.touchiteasy.http.validation;

import com.touchiteasy.http.BaseResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidatorCollectionTest {
    @Test (expected = IllegalArgumentException.class)
    public void GivenNoValidator_ShouldThrowException() {
        new ValidatorCollection(Arrays.<ResponseValidator>asList());
    }

    @Test
    public void GivenAValidator_ShouldReturnTheSameResultAsIt() {
        assertSameAnalysisAs(new ResponseValidatorStub(Arrays.asList(new InvalidationCause("::cause_of_invalidation::"))));
        assertSameAnalysisAs(new ResponseValidatorStub(Arrays.<InvalidationCause>asList()));
    }

    private void assertSameAnalysisAs(ResponseValidator stub) {
        final ValidatorCollection validatorCollection = new ValidatorCollection(Arrays.asList(
                stub
        ));
        final BaseResponse response = new BaseResponse(200, "::body::");

        final Collection<InvalidationCause> causesFromStub = stub.analyse(response);
        final Collection<InvalidationCause> causesFromCollection = validatorCollection.analyse(response);

        assertThat(causesFromCollection, is(causesFromStub));
    }
}
