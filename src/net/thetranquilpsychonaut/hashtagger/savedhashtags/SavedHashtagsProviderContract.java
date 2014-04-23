package net.thetranquilpsychonaut.hashtagger.savedhashtags;

import android.net.Uri;
import android.provider.BaseColumns;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 4/23/14.
 */
public final class SavedHashtagsProviderContract
{
    public static final String AUTHORITY   = HashtaggerApp.PACKAGE_NAMESPACE + ".saved_hashtags";
    public static final Uri    CONTENT_URI = Uri.parse( "content://" + AUTHORITY );

    public static final class SavedHashtags implements BaseColumns
    {
        public static final String PATH               = "saved_hashtags";
        public static final Uri    CONTENT_URI        = Uri.withAppendedPath( SavedHashtagsProviderContract.CONTENT_URI, PATH );
        public static final String COLUMN_HASHTAG     = "hashtag";
        public static final String CONTENT_TYPE       = "vnd.android.cursor.dir/vnd.thetranquilpsychonaut.saved_hashtags";
        public static final String CONTENT_ITEM_TYPE  = "vnd.android.cursor.item/vnd.thetranquilpsychonaut.saved_hashtags";
        public static final String SORT_ORDER_DEFAULT = COLUMN_HASHTAG + " ASC";
    }
}
