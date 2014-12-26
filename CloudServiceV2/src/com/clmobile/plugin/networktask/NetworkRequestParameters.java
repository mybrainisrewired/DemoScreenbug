package com.clmobile.plugin.networktask;

import com.clmobile.network.RequestTask;
import com.clmobile.network.ResultData;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class NetworkRequestParameters<T> extends RequestTask {
    private ResultData<T> result;

    public NetworkRequestParameters(String service, String type) {
        super(service, type);
    }

    public ResultData<T> getResult() {
        return this.result;
    }

    public ResultData<T> parseResponse(ObjectMapper mappger, String str) throws Exception {
        return (ResultData) mappger.readValue(str, new TypeReference<ResultData<T>>() {
        });
    }

    public void setResult(ResultData<T> result) {
        this.result = result;
    }
}