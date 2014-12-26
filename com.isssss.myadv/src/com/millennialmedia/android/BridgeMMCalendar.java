package com.millennialmedia.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.provider.CalendarContract.Events;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.isssss.myadv.dao.BannerInfoTable;
import com.mopub.common.Preconditions;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import mobi.vserv.android.ads.VservConstants;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class BridgeMMCalendar extends MMJSObject {
    private static final String ADD_EVENT = "addEvent";
    private static final String TAG;
    private static final String[] mraidCreateCalendarEventDateFormats;
    private static final SimpleDateFormat rruleUntilDateFormat;

    static {
        TAG = BridgeMMCalendar.class.getName();
        mraidCreateCalendarEventDateFormats = new String[]{"yyyy-MM-dd'T'HH:mmZZZ", "yyyy-MM-dd'T'HH:mm:ssZZZ"};
        rruleUntilDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    }

    BridgeMMCalendar() {
    }

    private String convertMraidDayToRRuleDay(int mraidDay) {
        switch (mraidDay) {
            case MMAdView.TRANSITION_FADE:
                return "MO";
            case MMAdView.TRANSITION_UP:
                return "TU";
            case MMAdView.TRANSITION_DOWN:
                return "WE";
            case MMAdView.TRANSITION_RANDOM:
                return "TH";
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                return "FR";
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return "SA";
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                return "SU";
            default:
                return null;
        }
    }

    private String convertRecurrence(JSONObject recurrence) {
        StringBuilder rrule = new StringBuilder();
        try {
            rrule.append("FREQ=").append(recurrence.getString("frequency")).append(";");
        } catch (JSONException e) {
            MMLog.d(TAG, "Unable to get calendar event recurrence frequency");
        }
        try {
            rrule.append("UNTIL=").append(rruleUntilDateFormat.format(DateUtils.parseDate(recurrence.getString("expires"), mraidCreateCalendarEventDateFormats))).append(";");
        } catch (DateParseException e2) {
            MMLog.e(TAG, "Error parsing calendar event recurrence expiration date");
        } catch (JSONException e3) {
            MMLog.d(TAG, "Unable to get calendar event recurrence expiration date");
        }
        try {
            JSONArray mraidDaysInWeek = recurrence.getJSONArray("daysInWeek");
            StringBuilder daysInWeek = new StringBuilder();
            int i = 0;
            while (i < mraidDaysInWeek.length()) {
                daysInWeek.append(convertMraidDayToRRuleDay(mraidDaysInWeek.getInt(i))).append(",");
                i++;
            }
            rrule.append("BYDAY=").append(daysInWeek).append(";");
        } catch (JSONException e4) {
            MMLog.d(TAG, "Unable to get days in week");
        }
        try {
            rrule.append("BYMONTHDAY=").append(recurrence.getString("daysInMonth").replaceAll("\\[", Preconditions.EMPTY_ARGUMENTS).replaceAll("\\]", Preconditions.EMPTY_ARGUMENTS)).append(";");
        } catch (JSONException e5) {
            MMLog.d(TAG, "Unable to get days in month");
        }
        try {
            rrule.append("BYMONTH=").append(recurrence.getString("monthsInYear:").replaceAll("\\[", Preconditions.EMPTY_ARGUMENTS).replaceAll("\\]", Preconditions.EMPTY_ARGUMENTS)).append(";");
        } catch (JSONException e6) {
            MMLog.d(TAG, "Unable to get months in year:");
        }
        try {
            rrule.append("BYYEARDAY=").append(recurrence.getString("daysInYear")).append(";");
        } catch (JSONException e7) {
            MMLog.d(TAG, "Unable to get days in year");
        }
        return rrule.toString().toUpperCase();
    }

    @TargetApi(14)
    public MMJSResponse addEvent(Map<String, String> parameters) {
        MMLog.d(TAG, "addEvent parameters: " + parameters);
        if (VERSION.SDK_INT < 14) {
            return MMJSResponse.responseWithError("Not supported");
        }
        if (!(parameters == null || parameters.get("parameters") == null)) {
            String description = null;
            String location = null;
            Date start = null;
            Date end = null;
            String title = null;
            String status = null;
            String reminder = null;
            String recurrenceRule = null;
            String transparency = null;
            try {
                JSONObject jsonParameters = new JSONObject((String) parameters.get("parameters"));
                try {
                    title = jsonParameters.getString(BannerInfoTable.COLUMN_DESCRIPTION);
                } catch (JSONException e) {
                    MMLog.e(TAG, "Unable to get calendar event description");
                }
                try {
                    description = jsonParameters.getString("summary");
                } catch (JSONException e2) {
                    MMLog.d(TAG, "Unable to get calendar event description");
                }
                try {
                    transparency = jsonParameters.getString("transparency");
                } catch (JSONException e3) {
                    MMLog.d(TAG, "Unable to get calendar event transparency");
                }
                try {
                    reminder = jsonParameters.getString("reminder");
                } catch (JSONException e4) {
                    MMLog.d(TAG, "Unable to get calendar event reminder");
                }
                try {
                    location = jsonParameters.getString("location");
                } catch (JSONException e5) {
                    MMLog.d(TAG, "Unable to get calendar event location");
                }
                try {
                    status = jsonParameters.getString("status");
                } catch (JSONException e6) {
                    MMLog.d(TAG, "Unable to get calendar event status");
                }
                try {
                    recurrenceRule = convertRecurrence(jsonParameters.getJSONObject("recurrence"));
                } catch (JSONException e7) {
                    MMLog.d(TAG, "Unable to get calendar event recurrence");
                }
                try {
                    start = DateUtils.parseDate(jsonParameters.getString(VservConstants.VPLAY0), mraidCreateCalendarEventDateFormats);
                } catch (DateParseException e8) {
                    MMLog.e(TAG, "Error parsing calendar event start date");
                } catch (JSONException e9) {
                    MMLog.e(TAG, "Unable to get calendar event start date");
                }
                try {
                    end = DateUtils.parseDate(jsonParameters.getString("end"), mraidCreateCalendarEventDateFormats);
                } catch (DateParseException e10) {
                    MMLog.e(TAG, "Error parsing calendar event end date");
                } catch (JSONException e11) {
                    MMLog.d(TAG, "Unable to get calendar event end date");
                }
                String str = TAG;
                String str2 = "Creating calendar event: title: %s, location: %s, start: %s, end: %s, status: %s, summary: %s, rrule: %s";
                Object[] objArr = new Object[7];
                objArr[0] = title;
                objArr[1] = location;
                objArr[2] = start;
                objArr[3] = end;
                objArr[4] = status;
                objArr[5] = description;
                objArr[6] = recurrenceRule;
                MMLog.d(str, String.format(str2, objArr));
                if (title == null || start == null) {
                    MMLog.e(TAG, "Description and start must be provided to create calendar event.");
                    return MMJSResponse.responseWithError("Calendar Event Creation Failed.  Minimum parameters not provided");
                } else {
                    Intent intent = new Intent("android.intent.action.INSERT").setData(Events.CONTENT_URI);
                    if (start != null) {
                        intent.putExtra("beginTime", start.getTime());
                    }
                    if (end != null) {
                        intent.putExtra("endTime", end.getTime());
                    }
                    if (title != null) {
                        intent.putExtra(BannerInfoTable.COLUMN_TITLE, title);
                    }
                    if (description != null) {
                        intent.putExtra(BannerInfoTable.COLUMN_DESCRIPTION, description);
                    }
                    if (location != null) {
                        intent.putExtra("eventLocation", location);
                    }
                    if (recurrenceRule != null) {
                        intent.putExtra("rrule", recurrenceRule);
                    }
                    if (status != null) {
                        MMLog.w(TAG, "Calendar addEvent does not support status");
                    }
                    if (transparency != null) {
                        MMLog.w(TAG, "Calendar addEvent does not support transparency");
                    }
                    if (reminder != null) {
                        MMLog.w(TAG, "Calendar addEvent does not support reminder");
                    }
                    Context context = (Context) this.contextRef.get();
                    if (context != null) {
                        IntentUtils.startActivity(context, intent);
                        Event.intentStarted(context, Event.INTENT_CALENDAR_EVENT, getAdImplId((String) parameters.get("PROPERTY_EXPANDING")));
                        return MMJSResponse.responseWithSuccess("Calendar Event Created");
                    }
                }
            } catch (JSONException e12) {
                MMLog.e(TAG, "Unable to parse calendar addEvent parameters");
                return MMJSResponse.responseWithError("Calendar Event Creation Failed.  Invalid parameters");
            }
        }
        return null;
    }

    MMJSResponse executeCommand(String name, Map<String, String> arguments) {
        return ADD_EVENT.equals(name) ? addEvent(arguments) : null;
    }
}