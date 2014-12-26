package com.mopub.mobileads.util.vast;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import com.mopub.common.HttpClient;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Strings;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;

public class VastXmlManagerAggregator extends AsyncTask<String, Void, List<VastXmlManager>> {
    static final int MAX_TIMES_TO_FOLLOW_VAST_REDIRECT = 20;
    private int mTimesFollowedVastRedirect;
    private final WeakReference<VastXmlManagerAggregatorListener> mVastXmlManagerAggregatorListener;

    static interface VastXmlManagerAggregatorListener {
        void onAggregationComplete(List<VastXmlManager> list);
    }

    VastXmlManagerAggregator(VastXmlManagerAggregatorListener vastXmlManagerAggregatorListener) {
        this.mVastXmlManagerAggregatorListener = new WeakReference(vastXmlManagerAggregatorListener);
    }

    protected List<VastXmlManager> doInBackground(String... strings) {
        List<VastXmlManager> vastXmlManagers = null;
        AndroidHttpClient httpClient = null;
        try {
            httpClient = HttpClient.getHttpClient();
            if (strings != null && strings.length > 0) {
                String vastXml = strings[0];
                List<VastXmlManager> vastXmlManagers2 = new ArrayList();
                while (vastXml != null) {
                    try {
                        if (vastXml.length() <= 0) {
                            break;
                        } else if (isCancelled()) {
                            vastXmlManagers = vastXmlManagers2;
                            break;
                        } else {
                            VastXmlManager xmlManager = new VastXmlManager();
                            xmlManager.parseVastXml(vastXml);
                            vastXmlManagers2.add(xmlManager);
                            vastXml = followVastRedirect(httpClient, xmlManager.getVastAdTagURI());
                        }
                    } catch (Exception e) {
                        e = e;
                        vastXmlManagers = vastXmlManagers2;
                    } catch (Throwable th) {
                        th = th;
                    }
                }
                vastXmlManagers = vastXmlManagers2;
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e2) {
            e = e2;
            try {
                Exception e3;
                MoPubLog.d("Failed to parse VAST XML", e3);
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Throwable th2) {
                Throwable th3 = th2;
                if (httpClient != null) {
                    httpClient.close();
                }
                throw th3;
            }
            return vastXmlManagers;
        }
        return vastXmlManagers;
    }

    String followVastRedirect(AndroidHttpClient httpClient, String redirectUrl) throws Exception {
        if (redirectUrl == null || this.mTimesFollowedVastRedirect >= 20) {
            return null;
        }
        this.mTimesFollowedVastRedirect++;
        HttpEntity entity = httpClient.execute(new HttpGet(redirectUrl)).getEntity();
        return entity != null ? Strings.fromStream(entity.getContent()) : null;
    }

    protected void onCancelled() {
        VastXmlManagerAggregatorListener listener = (VastXmlManagerAggregatorListener) this.mVastXmlManagerAggregatorListener.get();
        if (listener != null) {
            listener.onAggregationComplete(null);
        }
    }

    protected void onPostExecute(List<VastXmlManager> vastXmlManagers) {
        VastXmlManagerAggregatorListener listener = (VastXmlManagerAggregatorListener) this.mVastXmlManagerAggregatorListener.get();
        if (listener != null) {
            listener.onAggregationComplete(vastXmlManagers);
        }
    }

    @Deprecated
    void setTimesFollowedVastRedirect(int timesFollowedVastRedirect) {
        this.mTimesFollowedVastRedirect = timesFollowedVastRedirect;
    }
}