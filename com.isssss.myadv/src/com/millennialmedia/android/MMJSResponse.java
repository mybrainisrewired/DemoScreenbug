package com.millennialmedia.android;

import com.mopub.common.Preconditions;
import org.json.JSONException;
import org.json.JSONObject;

class MMJSResponse {
    static final int ERROR = 0;
    static final int SUCCESS = 1;
    private static final String TAG = "MMJSResponse";
    String className;
    byte[] dataResponse;
    String methodName;
    Object response;
    int result;

    MMJSResponse() {
    }

    static MMJSResponse responseWithError() {
        return responseWithError("An unknown error occured.");
    }

    static MMJSResponse responseWithError(String error) {
        MMJSResponse response = new MMJSResponse();
        response.result = 0;
        response.response = error;
        return response;
    }

    static MMJSResponse responseWithSuccess() {
        return responseWithSuccess("Success.");
    }

    static MMJSResponse responseWithSuccess(String success) {
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = success;
        return response;
    }

    String toJSONString() {
        try {
            JSONObject object = new JSONObject();
            if (this.className != null) {
                object.put("class", this.className);
            }
            if (this.methodName != null) {
                object.put("call", this.methodName);
            }
            object.put("result", this.result);
            if (this.response != null) {
                object.put("response", this.response);
            } else if (this.dataResponse == null) {
                return Preconditions.EMPTY_ARGUMENTS;
            } else {
                object.put("response", Base64.encodeToString(this.dataResponse, false));
            }
            return object.toString();
        } catch (JSONException e) {
            MMLog.e(TAG, e.getMessage());
            return Preconditions.EMPTY_ARGUMENTS;
        }
    }
}