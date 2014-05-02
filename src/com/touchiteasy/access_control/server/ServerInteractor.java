package com.touchiteasy.access_control.server;

import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;
import com.touchiteasy.oauth.Tokens;
import com.touchiteasy.permission_manager.permissions.Permissions;
import com.touchiteasy.permission_manager.permissions.PermissionsGroups;

import java.io.Serializable;

public class ServerInteractor {
    private ResourceRequester postRequester;
    private Server server;
    private Tokens tokens;

    public ServerInteractor(ResourceRequester postRequester, Server server, Tokens tokens){
        this.postRequester = postRequester;
        this.server = server;
        this.tokens = tokens;
    }

    private static class PermissionGroups implements PermissionsGroups, Serializable {
        public PermissionGroups(String jsonPermissions){

        }

        @Override
        public Permissions get(int groupId) {
            return null;
        }
    }

    public PermissionsGroups getPermissionGroups(){
        final PermissionsRequest req = new PermissionsRequest(tokens, server);
        Response resp = this.postRequester.run(req);

        return new ServerInteractor.PermissionGroups(resp.getBody());
    }
}
