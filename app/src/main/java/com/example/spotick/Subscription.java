package com.example.spotick;

public class Subscription {

    String endpoint;
    String auth;
    String p256dh;

    public Subscription( String endpoint, String auth, String p256dh) {
        this.endpoint = endpoint;
        this.auth = auth;
        this.p256dh = p256dh;
    }

    void setEndpoint(String c) {
        endpoint = c;
    }
    void setAuth(String c) {
        auth = c;
    }
    void setP256dh(String c) {
        p256dh = c;
    }
    public String getEndpoint() {
        return endpoint;
    }
    public String  getAuth() {
        return auth;
    }
    public String  getP256dh() {
        return p256dh;
    }

}
