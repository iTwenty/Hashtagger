package net.thetranquilpsychonaut.hashtagger.savedhashtags;

import android.provider.BaseColumns;

/**
 * Created by itwenty on 4/22/14.
 */
public final class SavedHashtagsDBContract
{
    public static final String DB_NAME    = "hashtagger.db";
    public static final int    DB_VERSION = 1;

    public SavedHashtagsDBContract() {}

    public static abstract class SavedHashtags implements BaseColumns
    {
        public static final String TABLE_NAME         = "saved_hashtags";
        public static final String COLUMN_HASHTAG     = "hashtag";
        public static final String SORT_ORDER_DEFAULT = COLUMN_HASHTAG + " ASC";
    }
}
