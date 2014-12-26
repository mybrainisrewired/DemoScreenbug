package com.inmobi.commons.network.abstraction;

import com.inmobi.commons.network.Request;

public interface INetworkServiceProvider {
    void executeTask(Request request, INetworkListener iNetworkListener);
}