package com.paynopain.http.validation;

import com.paynopain.http.BaseResponse;
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

    @Test
    public void GivenThreeValidators_ShouldReturnTheirInvalidationCausesTogether() {
        final ValidatorCollection validatorCollection = new ValidatorCollection(Arrays.<ResponseValidator>asList(
                new ResponseValidatorStub(Arrays.asList(
                        new InvalidationCause("::cause_from_validator1::")
                )),
                new ResponseValidatorStub(Arrays.<InvalidationCause>asList()),
                new ResponseValidatorStub(Arrays.asList(
                        new InvalidationCause("::cause_from_validator3::")
                ))
        ));

        final BaseResponse response = new BaseResponse(200, "::body::");

        final Collection<InvalidationCause> actualCauses = validatorCollection.analyse(response);
        final Collection<InvalidationCause> expectedCauses = Arrays.asList(
                new InvalidationCause("::cause_from_validator1::"),
                new InvalidationCause("::cause_from_validator3::")
        );

        assertThat(actualCauses, is(expectedCauses));
    }
}
