package android.support.v4.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mopub.common.Preconditions;

public class SimpleCursorAdapter extends ResourceCursorAdapter {
    private CursorToStringConverter mCursorToStringConverter;
    protected int[] mFrom;
    String[] mOriginalFrom;
    private int mStringConversionColumn;
    protected int[] mTo;
    private ViewBinder mViewBinder;

    public static interface CursorToStringConverter {
        CharSequence convertToString(Cursor cursor);
    }

    public static interface ViewBinder {
        boolean setViewValue(View view, Cursor cursor, int i);
    }

    @Deprecated
    public SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c);
        this.mStringConversionColumn = -1;
        this.mTo = to;
        this.mOriginalFrom = from;
        findColumns(from);
    }

    public SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, flags);
        this.mStringConversionColumn = -1;
        this.mTo = to;
        this.mOriginalFrom = from;
        findColumns(from);
    }

    private void findColumns(String[] from) {
        if (this.mCursor != null) {
            int count = from.length;
            if (this.mFrom == null || this.mFrom.length != count) {
                this.mFrom = new int[count];
            }
            int i = 0;
            while (i < count) {
                this.mFrom[i] = this.mCursor.getColumnIndexOrThrow(from[i]);
                i++;
            }
        } else {
            this.mFrom = null;
        }
    }

    public void bindView(View view, Context context, Cursor cursor) {
        ViewBinder binder = this.mViewBinder;
        int count = this.mTo.length;
        int[] from = this.mFrom;
        int[] to = this.mTo;
        int i = 0;
        while (i < count) {
            View v = view.findViewById(to[i]);
            if (v != null) {
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }
                if (!bound) {
                    String text = cursor.getString(from[i]);
                    if (text == null) {
                        text = Preconditions.EMPTY_ARGUMENTS;
                    }
                    if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        setViewImage((ImageView) v, text);
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " + " view that can be bounds by this SimpleCursorAdapter");
                    }
                }
            }
            i++;
        }
    }

    public void changeCursorAndColumns(Cursor c, String[] from, int[] to) {
        this.mOriginalFrom = from;
        this.mTo = to;
        super.changeCursor(c);
        findColumns(this.mOriginalFrom);
    }

    public CharSequence convertToString(Cursor cursor) {
        if (this.mCursorToStringConverter != null) {
            return this.mCursorToStringConverter.convertToString(cursor);
        }
        return this.mStringConversionColumn > -1 ? cursor.getString(this.mStringConversionColumn) : super.convertToString(cursor);
    }

    public CursorToStringConverter getCursorToStringConverter() {
        return this.mCursorToStringConverter;
    }

    public int getStringConversionColumn() {
        return this.mStringConversionColumn;
    }

    public ViewBinder getViewBinder() {
        return this.mViewBinder;
    }

    public void setCursorToStringConverter(CursorToStringConverter cursorToStringConverter) {
        this.mCursorToStringConverter = cursorToStringConverter;
    }

    public void setStringConversionColumn(int stringConversionColumn) {
        this.mStringConversionColumn = stringConversionColumn;
    }

    public void setViewBinder(ViewBinder viewBinder) {
        this.mViewBinder = viewBinder;
    }

    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            v.setImageURI(Uri.parse(value));
        }
    }

    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    public Cursor swapCursor(Cursor c) {
        Cursor res = super.swapCursor(c);
        findColumns(this.mOriginalFrom);
        return res;
    }
}