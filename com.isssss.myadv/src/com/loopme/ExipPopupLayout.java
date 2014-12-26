package com.loopme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.loopme.utilites.LanguageManager;

class ExipPopupLayout extends LinearLayout {
    private static final String NO_BG_COLOR = "#ffa7a9ac";
    private static final String ROOT_BG_COLOR = "#65000000";
    private static final String SHAPE_BG_COLOR = "#ff58595b";
    private static final String YES_BG_COLOR = "#ffd81921";
    private Button mBtnNo;
    private Button mBtnYes;

    public ExipPopupLayout(Context context) {
        super(context);
        int padding = Utilities.convertDpToPixel(10.0f, context);
        LanguageManager languageManager = LanguageManager.getInstance(context);
        setLayoutParams(new LayoutParams(-1, -1));
        setGravity(17);
        setBackgroundColor(Color.parseColor(ROOT_BG_COLOR));
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(1);
        LayoutParams main_layout_params = new LayoutParams(-1, -2);
        main_layout_params.setMargins(Utilities.convertDpToPixel(20.0f, context), 0, Utilities.convertDpToPixel(20.0f, context), 0);
        mainLayout.setLayoutParams(main_layout_params);
        mainLayout.setPadding(padding, padding, padding, padding);
        float[] fArr = new float[8];
        mainLayout.setBackgroundDrawable(new ExitShape(new RoundRectShape(new float[]{20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f}, null, null), Color.parseColor(SHAPE_BG_COLOR), -1, 2));
        View textView = title;
        Context context2 = context;
        title.setText(languageManager.getExitTitle());
        title.setTextSize(17.0f);
        title.setPadding(0, 0, 0, padding);
        title.setTypeface(null, 1);
        RelativeLayout.LayoutParams layoutParams = titleLayout;
        int i = -2;
        int i2 = -2;
        title.setTextColor(-1);
        title.setLayoutParams(titleLayout);
        mainLayout.addView(title);
        textView = text;
        context2 = context;
        text.setPadding(0, 0, 0, padding);
        text.setTextSize(17.0f);
        layoutParams = textLayout;
        i = -1;
        i2 = -2;
        text.setLayoutParams(textLayout);
        text.setText(languageManager.getExitText());
        text.setTextColor(-1);
        mainLayout.addView(text);
        Button button = button;
        context2 = context;
        this.mBtnYes = button;
        this.mBtnYes.setPadding(0, 0, 0, 0);
        ViewGroup.LayoutParams yesLayout = new LayoutParams(Utilities.convertDpToPixel(50.0f, context), Utilities.convertDpToPixel(40.0f, context));
        yesLayout.setMargins(0, 0, padding / 2, 0);
        yesLayout.weight = 1.0f;
        this.mBtnYes.setLayoutParams(yesLayout);
        this.mBtnYes.setText(languageManager.getExitYes());
        this.mBtnYes.setTextColor(-1);
        this.mBtnYes.setGravity(ApiEventType.API_MRAID_GET_SCREEN_SIZE);
        this.mBtnYes.setTextSize(17.0f);
        RectShape buttonRect = new RectShape();
        this.mBtnYes.setBackgroundDrawable(new ButtonShape(buttonRect, Color.parseColor(YES_BG_COLOR), Color.parseColor(YES_BG_COLOR), 2));
        button = button;
        context2 = context;
        this.mBtnNo = button;
        this.mBtnNo.setPadding(0, 0, 0, 0);
        LayoutParams noLayout = new LayoutParams(Utilities.convertDpToPixel(50.0f, context), Utilities.convertDpToPixel(40.0f, context));
        noLayout.setMargins(padding / 2, 0, 0, 0);
        noLayout.weight = 1.0f;
        this.mBtnNo.setLayoutParams(noLayout);
        this.mBtnNo.setText(languageManager.getExitNo());
        this.mBtnNo.setTextSize(17.0f);
        this.mBtnNo.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.mBtnNo.setGravity(ApiEventType.API_MRAID_GET_SCREEN_SIZE);
        this.mBtnNo.setBackgroundDrawable(new ButtonShape(buttonRect, Color.parseColor(NO_BG_COLOR), -16777216, 2));
        LinearLayout buttonsLayout = new LinearLayout(context);
        buttonsLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        buttonsLayout.setGravity(17);
        buttonsLayout.addView(this.mBtnYes);
        buttonsLayout.addView(this.mBtnNo);
        mainLayout.addView(buttonsLayout);
        addView(mainLayout);
        invalidate();
    }

    public Button getNoButton() {
        return this.mBtnNo;
    }

    public Button getYesButton() {
        return this.mBtnYes;
    }
}