package com.touchiteasy.http.validation;

import com.touchiteasy.http.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class ResponseValidatingRequesterTest {
    private RequesterMock mock;
    private ResponseValidatingRequester requester;
    private Request request;
    private Response response;

    @Before
    public void setUp() {
        mock = new RequesterMock();
        request = new BaseRequest("http://www.google.es");
    }

    public class GivenAValidatorThatDictatesEveryResponseIsInvalid {
        private ResponseValidator everythingInvalidator = new ResponseValidator() {
            @Override
            public Collection<InvalidationCause> analyse(Response response) {
                return Arrays.asList(new InvalidationCause("everything is invalid"));
            }
        };

        @Before
        public void setUp() {
            response = new BaseResponse(200, "body");
            mock.responses.add(response);
            requester = new ResponseValidatingRequester(mock, everythingInvalidator);
        }

        public class WhenRunningARequest {
            @Test (expected = InvalidResponseException.class)
            public void ShouldThrowInvalidResponseException() {
                requester.run(request);
            }

            @Test
            public void TheExceptionsMessageShouldContainTheCauseDescription() {
                InvalidResponseException exception = null;

                try {
                    requester.run(request);
                } catch (InvalidResponseException e){
                    exception = e;
                }

                assertThat(exception.getMessage(), containsString("everything is invalid"));
            }
        }
    }

    public class GivenAValidatorThatDictatesEveryResponseIsValid {
        private ResponseValidator everythingValidator = new ResponseValidator() {
            @Override
            public Collection<InvalidationCause> analyse(Response response) {
                return Arrays.asList();
            }
        };

        @Before
        public void setUp() {
            response = new BaseResponse(500, "body");
            mock.responses.add(response);
            requester = new ResponseValidatingRequester(mock, everythingValidator);
        }

        public class WhenRunningARequest {
            @Test
            public void SouldReturnTheResponse() {
                Response actual = requester.run(request);
                assertThat(actual, is(response));
            }
        }
    }

    public class GivenAValidatorThatOnlyAcceptsResponsesWithStatusCode200Or201 {
        private ResponseValidator validator = new ValidStatusCodesValidator(Arrays.asList(200, 201));

        public class WhenTheResponseHasStatusCode200 {
            @Before
            public void setUp() {
                response = new BaseResponse(200, "body");
                mock.responses.add(response);
                requester = new ResponseValidatingRequester(mock, validator);
            }

            @Test
            public void ShouldReturnTheResponse() {
                assertThat(requester.run(request), is(response));
            }
        }

        public class WhenTheResponseHasStatusCode201 {
            @Before
            public void setUp() {
                response = new BaseResponse(201, "body");
                mock.responses.add(response);
                requester = new ResponseValidatingRequester(mock, validator);
            }

            @Test
            public void ShouldReturnTheResponse() {
                assertThat(requester.run(request), is(response));
            }
        }

        public class WhenTheResponseHasStatusCodeIs500 {
            @Before
            public void setUp() {
                response = new BaseResponse(500, "body");
                mock.responses.add(response);
                requester = new ResponseValidatingRequester(mock, validator);
            }

            @Test (expected = InvalidResponseException.class)
            public void ShouldThrowInvalidResponseException() {
                requester.run(request);
            }

            @Test
            public void TheExceptionsMessageShouldContainTheCauseDescription() {
                InvalidResponseException exception = null;

                try {
                    requester.run(request);
                } catch (InvalidResponseException e){
                    exception = e;
                }

                assertThat(exception.getMessage(), containsString("the only valid status codes are [200, 201]"));
            }
        }
    }
}
