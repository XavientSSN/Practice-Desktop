package com.ssn.ws.rest.response;

/**
 *
 * @author vkvarma
 */

public class SSNUserDataResponse {
    private SSNUserResponse User;
    
    public SSNUserResponse getUser() {
        return User;
    }

    public void setUser(SSNUserResponse User) {
        this.User = User;
    }
}
