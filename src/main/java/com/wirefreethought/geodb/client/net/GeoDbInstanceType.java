package com.wirefreethought.geodb.client.net;

import lombok.Getter;

@Getter
public enum GeoDbInstanceType
{
    FREE("http://geodb-free-service.wirefreethought.com"),
    PRO("https://wft-geo-db.p.mashape.com");

    private String instanceUri;

    GeoDbInstanceType(String uri)
    {
        this.instanceUri = uri;
    }
}