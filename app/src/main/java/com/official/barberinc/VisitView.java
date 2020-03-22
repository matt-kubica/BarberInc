package com.official.barberinc;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class VisitView extends CoordinatorLayout {

    public static final String TAG = "VisitView";

    private Visit visit;
    private Context context;
    private TextView idView, nameView, timeView, tagNameView;

    public VisitView(Context context) {
        super(context);
        inflate(context, R.layout.view_visit, this);
        init(context,null);
    }

    public VisitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_visit, this);
        init(context, attrs);
    }

    public VisitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_visit, this);
        init(context, attrs);
    }



    private void init(Context context, @Nullable AttributeSet attributeSet) {
        this.context = context;

        idView = findViewById(R.id.visit_id);
        nameView = findViewById(R.id.name);
        timeView = findViewById(R.id.time);
        tagNameView = findViewById(R.id.tag_name);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.VisitView, 0, 0);
        attributes.recycle();
//        try {
//            name = attributes.getString(R.styleable.VisitView_name);
//        } finally {
//            attributes.recycle();
//        }
    }

    public void setLayout() {
        RelativeLayout.LayoutParams visitViewParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Utils.dpToPx(visit.getDurationMinutes(), context)
        );
        visitViewParams.setMargins(0,Utils.dpToPx(visit.getMinutesSinceStart(),context), 0, 0);

        setBackgroundResource(getBackgroundDependentOnVisit(visit));
        requestLayout();
        setInnerViews();

        setLayoutParams(visitViewParams);
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public Visit getVisit() { return this.visit; }

    public void setInnerViews() {
        idView.setText(Integer.toString(visit.getId()));
        timeView.setText(new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(visit.getStart()) + " - " +
                new SimpleDateFormat(Utils.DateFormats.TIME_FORMAT).format(visit.getEnd()));
        nameView.setText(visit.getName());
        tagNameView.setText(getTagNameDependentOnVisit(visit));
    }

    private String getTagNameDependentOnVisit(Visit visit) {
        String tagName;
        switch (visit.getTag()) {
            case Utils.VisitTypes.HAIRCUT:
                tagName = getResources().getString(R.string.tag_name_haircut);
                break;
            case Utils.VisitTypes.BARBER:
                tagName = getResources().getString(R.string.tag_name_barber);
                break;
            case Utils.VisitTypes.COMBO:
                tagName = getResources().getString(R.string.tag_name_combo);
                break;
            default:
                tagName = "";
        }
        return tagName;
    }

    private int getBackgroundDependentOnVisit(Visit visit) {
        if(visit.getTag() == Utils.VisitTypes.HAIRCUT)
            return R.drawable.visit_haircut_background;
        else if(visit.getTag() == Utils.VisitTypes.BARBER)
            return R.drawable.visit_barber_background;
        else
            return R.drawable.visit_combo_background;
    }
}
