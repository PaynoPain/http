package com.touchiteasy.access_control.server;

import com.touchiteasy.http.BaseRequest;
import com.touchiteasy.oauth.ResourceRequest;
import com.touchiteasy.oauth.Tokens;

public class PermissionsRequest extends ResourceRequest {
    public PermissionsRequest(Tokens tokens, Server server) {
        super(tokens, new BaseRequest(String.format("%s/permissions.json", server.domain)));
    }
}
