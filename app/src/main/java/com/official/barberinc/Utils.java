package com.official.barberinc;

import android.content.Context;
import android.util.TypedValue;

public final class Utils {

    public static class DateFormats {
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String TIME_FORMAT = "HH:mm";
    }

    public static class Durations {
        public static final int HAIRCUT_DURATION_MINUTES = 45;
        public static final int BARBER_DURATION_MINUTES = 30;
        public static final int COMBO_DURATION_MINUTES = 60;
    }

    public static class VisitTypes {
        public static final int HAIRCUT = 1;
        public static final int BARBER = 2;
        public static final int COMBO = 3;
        public static final int HOMMIE = 4;
    }

    public static class SpecialTags {
        public static final int WRONG = -1;
        public static final int BLANK = 0;
    }

    public static final String [] HOURS_LAYOUT_VALUES = { "8:00", "9:00", "10:00", "11:00", "12:00", "13:00",
                                                  "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
                                                  "20:00", "21:00", "22:00", "23:00" };

    public static final String WORKING_HOURS_START = "8:00";
    public static final String WORKING_HOURS_END = "23:00";

    public static final String [] DAY_NAMES = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };



    public static final String DATE_INTENT = "com.official.barberinc.SELECTED_DATE_INTENT";
    public static final String VISIT_ID_INTENT = "com.official.barberinc.VISIT_ID_INTENT";

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
