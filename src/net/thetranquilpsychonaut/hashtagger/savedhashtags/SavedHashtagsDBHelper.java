package net.thetranquilpsychonaut.hashtagger.savedhashtags;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by itwenty on 4/22/14.
 */
public class SavedHashtagsDBHelper extends SQLiteOpenHelper
{
    public SavedHashtagsDBHelper( Context context )
    {
        super( context, SavedHashtagsDBContract.DB_NAME, null, SavedHashtagsDBContract.DB_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {
        db.execSQL( "CREATE TABLE " + SavedHashtagsDBContract.SavedHashtags.TABLE_NAME + " ( "
                + SavedHashtagsDBContract.SavedHashtags._ID + " INTEGER PRIMARY KEY, "
                + SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG + " TEXT NOT NULL UNIQUE );" );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        // Nothing to do
    }
}
