package com.official.barberinc;

public final class Utils {

    public static class DateFormats {
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String TIME_FORMAT = "HH:mm";
    }

    public static class Durations {
        public static final int HAIRCUT_DURATION_MINUTES = 30;
        public static final int BARBER_DURATION_MINUTES = 20;
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

    public static final String DATE_INTENT = "com.official.barberinc.SELECTED_DATE_INTENT";
}
