package net.thetranquilpsychonaut.hashtagger.savedhashtags;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by itwenty on 4/23/14.
 */
public class SavedHashtagsProvider extends ContentProvider
{
    private static final UriMatcher URI_MATCHER = new UriMatcher( UriMatcher.NO_MATCH );
    private static final int        HASHTAGS    = 1;
    private static final int        HASHTAG_ID  = 2;

    static
    {
        URI_MATCHER.addURI(
                SavedHashtagsProviderContract.AUTHORITY,
                SavedHashtagsProviderContract.SavedHashtags.PATH,
                HASHTAGS );
        URI_MATCHER.addURI(
                SavedHashtagsProviderContract.AUTHORITY,
                SavedHashtagsProviderContract.SavedHashtags.PATH + "/#",
                HASHTAG_ID );
    }

    private SavedHashtagsDBHelper dbHelper;

    @Override
    public boolean onCreate()
    {
        dbHelper = new SavedHashtagsDBHelper( getContext() );
        return null == dbHelper ? false : true;
    }

    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder )
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables( SavedHashtagsDBContract.SavedHashtags.TABLE_NAME );
        switch ( URI_MATCHER.match( uri ) )
        {
            case HASHTAGS:
                if ( TextUtils.isEmpty( sortOrder ) )
                {
                    sortOrder = SavedHashtagsDBContract.SavedHashtags.SORT_ORDER_DEFAULT;
                }
                break;
            case HASHTAG_ID:
                qb.appendWhere( SavedHashtagsDBContract.SavedHashtags._ID + " = " + uri.getLastPathSegment() );
                break;
            default:
                throw new IllegalArgumentException( "Unsupported URI for query: " + uri.toString() );
        }
        Cursor cursor = qb.query( db, projection, selection, selectionArgs, null, null, sortOrder );
        cursor.setNotificationUri( getContext().getContentResolver(), uri );
        return cursor;
    }

    @Override
    public String getType( Uri uri )
    {
        switch ( URI_MATCHER.match( uri ) )
        {
            case HASHTAGS:
                return SavedHashtagsProviderContract.SavedHashtags.CONTENT_TYPE;
            case HASHTAG_ID:
                return SavedHashtagsProviderContract.SavedHashtags.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException( "Unsupported URI: " + uri.toString() );
        }
    }

    @Override
    public Uri insert( Uri uri, ContentValues values )
    {
        if ( URI_MATCHER.match( uri ) != HASHTAGS )
        {
            throw new IllegalArgumentException( "Unsupported URI for insert: " + uri.toString() );
        }
        Uri result = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId;
        try
        {
            rowId = db.insertOrThrow( SavedHashtagsDBContract.SavedHashtags.TABLE_NAME, null, values );
        }
        catch ( SQLiteException sqle )
        {
            rowId = -1;
        }
        result = ContentUris.withAppendedId( SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI, rowId );
        if ( rowId > 0 )
        {
            getContext().getContentResolver().notifyChange( result, null );
        }
        return result;
    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs )
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delCount = 0;
        switch ( URI_MATCHER.match( uri ) )
        {
            case HASHTAGS:
                delCount = db.delete( SavedHashtagsDBContract.SavedHashtags.TABLE_NAME, selection, selectionArgs );
                break;
            case HASHTAG_ID:
                String idStr = uri.getLastPathSegment();
                String where = SavedHashtagsDBContract.SavedHashtags._ID + " = " + idStr;
                if ( !TextUtils.isEmpty( selection ) )
                {
                    where += " AND " + selection;
                }
                delCount = db.delete( SavedHashtagsDBContract.SavedHashtags.TABLE_NAME, where, selectionArgs );
                break;
            default:
                throw new IllegalArgumentException( "Unsupported URI for delete: " + uri.toString() );
        }
        if ( delCount > 0 )
        {
            getContext().getContentResolver().notifyChange( uri, null );
        }
        return delCount;
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs )
    {
        throw new UnsupportedOperationException( "Update operation not supported: " + uri.toString() );
    }
}
