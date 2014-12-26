package com.facebook.ads.internal;

import com.mopub.common.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;

public class AdClientEventManager {
    private static List<AdClientEvent> clientEvents;

    static {
        clientEvents = new ArrayList();
    }

    public static void addClientEvent(AdClientEvent event) {
        synchronized (clientEvents) {
            clientEvents.add(event);
        }
    }

    public static String dumpClientEventToJson() {
        synchronized (clientEvents) {
            if (clientEvents.isEmpty()) {
                String str = Preconditions.EMPTY_ARGUMENTS;
                return str;
            } else {
                List<AdClientEvent> temp = new ArrayList(clientEvents);
                clientEvents.clear();
                JSONArray jsonArray = new JSONArray();
                Iterator i$ = temp.iterator();
                while (i$.hasNext()) {
                    jsonArray.put(((AdClientEvent) i$.next()).getClientEventJson());
                }
                return jsonArray.toString();
            }
        }
    }
}