/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ws.rest.service;

/**
 *
 * @author asingh8
 */
public class SSNSubscriptionPlans {
    
    private long id;
    private String name;
    private long subscription_plan_type_id;
    private String storage_limit;
    private String price;
    private String payment_term;
    private String is_active;
    private String is_recurring;
    private String plan_type;
    private String availble_space;
    private String percentage;
    private String subscription_purchased_via;
    private String is_default_plan;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSubscription_plan_type_id() {
        return subscription_plan_type_id;
    }

    public void setSubscription_plan_type_id(long subscription_plan_type_id) {
        this.subscription_plan_type_id = subscription_plan_type_id;
    }

    public String getStorage_limit() {
        return storage_limit;
    }

    public void setStorage_limit(String storage_limit) {
        this.storage_limit = storage_limit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayment_term() {
        return payment_term;
    }

    public void setPayment_term(String payment_term) {
        this.payment_term = payment_term;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getIs_recurring() {
        return is_recurring;
    }

    public void setIs_recurring(String is_recurring) {
        this.is_recurring = is_recurring;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getAvailble_space() {
        return availble_space;
    }

    public void setAvailble_space(String availble_space) {
        this.availble_space = availble_space;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getSubscription_purchased_via() {
        return subscription_purchased_via;
    }

    public void setSubscription_purchased_via(String subscription_purchased_via) {
        this.subscription_purchased_via = subscription_purchased_via;
    }

    public String getIs_default_plan() {
        return is_default_plan;
    }

    public void setIs_default_plan(String is_default_plan) {
        this.is_default_plan = is_default_plan;
    }
    
    
}
