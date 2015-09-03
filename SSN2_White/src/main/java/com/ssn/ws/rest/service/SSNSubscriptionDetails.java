/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ws.rest.service;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author asingh8
 */
public class SSNSubscriptionDetails {
    
    private SSNSubscriptionPlans subscriptionPlans;
    private SSNAvailableSpaceDetails availableSpaceDetails;
    private String is_grace_period;

    @JsonProperty("subscription_plans")
    public SSNSubscriptionPlans getSubscriptionPlans() {
        return subscriptionPlans;
    }

    public void setSubscriptionPlans(SSNSubscriptionPlans subscriptionPlans) {
        this.subscriptionPlans = subscriptionPlans;
    }

    @JsonProperty("AvailableSpaceDetails")
    public SSNAvailableSpaceDetails getAvailableSpaceDetails() {
        return availableSpaceDetails;
    }

    public void setAvailableSpaceDetails(SSNAvailableSpaceDetails AvailableSpaceDetails) {
        this.availableSpaceDetails = AvailableSpaceDetails;
    }

    public String getIs_grace_period() {
        return is_grace_period;
    }

    public void setIs_grace_period(String is_grace_period) {
        this.is_grace_period = is_grace_period;
    }
    
    
}
