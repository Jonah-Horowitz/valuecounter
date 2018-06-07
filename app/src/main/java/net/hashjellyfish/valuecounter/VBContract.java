package net.hashjellyfish.valuecounter;

import android.provider.BaseColumns;

final class VBContract {
    /**
     * One should not instantiate this class.
     */
    private VBContract() {
    }

    public static class VBEntry implements BaseColumns {
        public static final String TABLE_NAME = "bundles";
        public static final String COLUMN_NAME_POSITION = "position";
        public static final String COLUMN_NAME_CAPTION = "caption";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_OP1 = "op1";
        public static final String COLUMN_NAME_OP2 = "op2";
        public static final String COLUMN_NAME_OP3 = "op3";
        public static final String COLUMN_NAME_LOG_LENGTH = "logLength";
        public static final String COLUMN_NAME_LOG = "log";
    }
}
