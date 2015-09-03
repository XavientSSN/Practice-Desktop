package com.ssn.ws.rest.request;

import java.util.Map;

/**
 *
 * @author vkvarma
 */
public class SSNChangePasswordRequest {
    
    private Map<String,String> requestParameters = null;

    /**
     * @return the requestParameters
     */
    public Map<String,String> getRequestParameters() {
        return requestParameters;
    }

    /**
     * @param requestParameters the requestParameters to set
     */
    public void setRequestParameters(Map<String,String> requestParameters) {
        this.requestParameters = requestParameters;
    }
}