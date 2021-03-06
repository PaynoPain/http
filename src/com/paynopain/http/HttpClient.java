package com.paynopain.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class HttpClient {
    private int timeout;

    public HttpClient(int millisecondsToTimeout){
        this.timeout = millisecondsToTimeout;
    }

    /**
     * Performs a POST request to the specified resource.
     * The parameters are formatted with x-www-urlencoded.
     */
    protected Response post(String resource, Map<String, String> params) {
        return getResponse(createPostRequestUrlEncoded(resource, params));
    }

    /**
     * Performs a GET request to the specified resource.
     * The parameters are added to the resource as a query.
     */
    protected Response get(String resource, Map<String, String> params) {
        return getResponse(createGetRequest(resource, params));
    }

    private Response getResponse(HttpUriRequest request) {
        try{
            HttpResponse response = createHttpClient(this.timeout).execute(request);

            final String body;
            final HttpEntity entity = response.getEntity();
            if (entity == null)
                body = "";
            else
                body = getString(entity.getContent());

            final int statusCode = response.getStatusLine().getStatusCode();

            return new BaseResponse(statusCode, body);
        } catch (Exception e){
            throw new HttpException(e);
        }
    }

    private DefaultHttpClient createHttpClient(int timeoutInMilliseconds) {
        final HttpParams httpParams = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutInMilliseconds);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutInMilliseconds);
        ConnManagerParams.setTimeout(httpParams, timeoutInMilliseconds);

        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        return new DefaultHttpClient(httpParams);
    }

    private HttpGet createGetRequest(String url, Map<String, String> params) {
        String query = generateUrlQuery(params);
        return new HttpGet(url+query);
    }

    private HttpPost createPostRequestUrlEncoded(String url, Map<String, String> params) {
        List<NameValuePair> parameters = transformToNameValuePairList(params);

        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(parameters);
            entity.setContentType(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return httpPost;
    }

    private String getString(InputStream inputStream) {
        String response = "";
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            inputStream.close();
            response = sb.toString();

        } catch (Exception e) {
            throw new HttpException("Buffer Error. Error converting result " + e.toString(), e);
        }
        return response;
    }

    private static List<NameValuePair> transformToNameValuePairList(Map<String, String> params) {
        List<NameValuePair> returnParams = new ArrayList<NameValuePair>();

        if (params == null) return returnParams;

        for (String key : params.keySet()) {
            returnParams.add(new BasicNameValuePair(key, params.get(key)));
        }

        return returnParams;
    }

    private String generateUrlQuery(Map<String, String> params) {
        List<NameValuePair> listParams = new ArrayList<NameValuePair>();
        for(String key: params.keySet()){
            listParams.add(new BasicNameValuePair(key,params.get(key)));
        }
        return "?" + URLEncodedUtils.format(listParams, "utf-8");
    }
}
