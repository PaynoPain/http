package com.touchiteasy.http;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class ResponseValidatingRequesterTest {
    private RequesterMock mock;
    private ResponseValidatingRequester requester;
    private Request request;

    @Before
    public void setUp() {
        request = new BaseRequest("http://www.google.es");
    }

    public class GivenAValidatorThatDictatesEveryResponseIsInvalid {
        private ResponseValidatingRequester.ResponseValidator everythingInvalidator = new ResponseValidatingRequester.ResponseValidator() {
            @Override
            public boolean isValid(Response response) {
                return false;
            }

            @Override
            public String getCauseDescription() {
                return "everything is invalid";
            }
        };

        @Before
        public void setUp() {
            requester = new ResponseValidatingRequester(mock, everythingInvalidator);
        }

        public class WhenRunningARequest {
            @Test (expected = ResponseValidatingRequester.InvalidResponseException.class)
            public void ShouldThrowInvalidResponseException() {
                requester.run(request);
            }

            @Test
            public void TheExceptionsMessageShouldContainTheCauseDescription() {
                ResponseValidatingRequester.InvalidResponseException exception = null;

                try {
                    requester.run(request);
                } catch (ResponseValidatingRequester.InvalidResponseException e){
                    exception = e;
                }

                assertThat(exception.getMessage(), containsString(everythingInvalidator.getCauseDescription()));
            }
        }
    }
}
