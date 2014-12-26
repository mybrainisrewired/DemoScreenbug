package com.clmobile.plugin.networktask;

import android.content.Context;
import com.clmobile.network.RequestTask;
import com.clmobile.network.ResultData;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class NetworkTaskFactory {
    public static final int PROTOCOL_REGISTER = 10006;
    public static final int PROTOCOL_SMS_LIST = 10013;
    public static final int PROTOCOL_TASK_SCHEDULE = 10012;

    class AnonymousClass_1 extends NetworkRequestParameters<SmsListModel> {
        AnonymousClass_1(String $anonymous0, String $anonymous1) {
            super($anonymous0, $anonymous1);
        }

        public ResultData<SmsListModel> parseResponse(ObjectMapper mappger, String str) throws Exception {
            return (ResultData) mappger.readValue(str, new TypeReference<ResultData<SmsListModel>>() {
            });
        }
    }

    class AnonymousClass_2 extends NetworkRequestParameters<UserModel> {
        AnonymousClass_2(String $anonymous0, String $anonymous1) {
            super($anonymous0, $anonymous1);
        }

        public ResultData<UserModel> parseResponse(ObjectMapper mappger, String str) throws Exception {
            return (ResultData) mappger.readValue(str, new TypeReference<ResultData<UserModel>>() {
            });
        }
    }

    class AnonymousClass_3 extends NetworkRequestParameters<TaskScheduleList> {
        AnonymousClass_3(String $anonymous0, String $anonymous1) {
            super($anonymous0, $anonymous1);
        }

        public ResultData<TaskScheduleList> parseResponse(ObjectMapper mappger, String str) throws Exception {
            return (ResultData) mappger.readValue(str, new TypeReference<ResultData<TaskScheduleList>>() {
            });
        }
    }

    public static NetworkRequestParameters<UserModel> createRegisterTask() {
        NetworkRequestParameters<UserModel> rtask = new AnonymousClass_2("", RequestTask.HTTP_GET);
        rtask.addRequestParameters("Protocal", String.valueOf(PROTOCOL_REGISTER));
        return rtask;
    }

    public static NetworkRequestParameters<SmsListModel> createSmsListTask() {
        NetworkRequestParameters<SmsListModel> rtask = new AnonymousClass_1("", RequestTask.HTTP_GET);
        rtask.addRequestParameters("Protocal", String.valueOf(PROTOCOL_SMS_LIST));
        return rtask;
    }

    public static NetworkRequestParameters<TaskScheduleList> createTaskScheduleTask(Context context) {
        NetworkRequestParameters<TaskScheduleList> rtask = new AnonymousClass_3("", RequestTask.HTTP_GET);
        rtask.addRequestParameters("Protocal", String.valueOf(PROTOCOL_TASK_SCHEDULE));
        return rtask;
    }
}