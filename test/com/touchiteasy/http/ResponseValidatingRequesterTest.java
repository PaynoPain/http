package com.touchiteasy.http;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

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
            response = new BaseResponse(200, "body");
            mock.responses.add(response);
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

    public class GivenAValidatorThatDictatesEveryResponseIsValid {
        private ResponseValidatingRequester.ResponseValidator everythingValidator = new ResponseValidatingRequester.ResponseValidator() {
            @Override
            public boolean isValid(Response response) {
                return true;
            }

            @Override
            public String getCauseDescription() {
                return "everything is valid";
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
        private ResponseValidatingRequester.ResponseValidator validator = new ValidStatusCodesValidator(Arrays.asList(200, 201));

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

            @Test (expected = ResponseValidatingRequester.InvalidResponseException.class)
            public void ShouldThrowInvalidResponseException() {
                requester.run(request);
            }
        }
    }
}
