package com.official.barberinc;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.text.SimpleDateFormat;

public class VisitView extends CoordinatorLayout {

    private Visit visit;
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

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

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
}
