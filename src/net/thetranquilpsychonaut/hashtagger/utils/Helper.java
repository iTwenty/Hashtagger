package net.thetranquilpsychonaut.hashtagger.utils;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.JsonParser;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Trend;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Trends;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by itwenty on 2/7/14.
 */
public final class Helper
{
    private Helper()
    {
        throw new RuntimeException( "Class Helper must not be instantiated" );
    }

    private static final boolean DEBUG = true;

    public static void debug( String s )
    {
        if ( DEBUG )
        {
            Log.d( "twtr", s );
        }
    }

    public static CharSequence getFuzzyDateTime( long time )
    {
        if ( time > System.currentTimeMillis() )
        {
            return "now";
        }

        return DateUtils.getRelativeDateTimeString(
                HashtaggerApp.app,
                time,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL )
                .toString();
    }

    public static int convertPxToDp( int px )
    {
        DisplayMetrics displayMetrics = HashtaggerApp.app.getResources().getDisplayMetrics();
        return ( int ) ( ( px / displayMetrics.density ) + 0.5 );
    }

    public static int convertDpToPx( int dp )
    {
        DisplayMetrics displayMetrics = HashtaggerApp.app.getResources().getDisplayMetrics();
        return ( int ) ( ( dp * displayMetrics.density ) + 0.5 );
    }

    public static int getLineCount( String message )
    {
        int lineCount = 0;
        int length = message.length();
        char c;
        for ( int pos = 0; pos < length; ++pos )
        {
            c = message.charAt( pos );
            if ( c == '\r' )
            {
                lineCount++;
                if ( pos + 1 < length && message.charAt( pos + 1 ) == '\n' )
                {
                    ++pos;
                }
            }
            else if ( c == '\n' )
            {
                lineCount++;
            }
        }
        return lineCount;
    }

    public static void showNoNetworkToast( Context context )
    {
        Toast.makeText( context, "Connect to a network first", Toast.LENGTH_SHORT ).show();
    }

    public static ArrayList<String> createStringArrayList( String... strings )
    {
        ArrayList<String> list = new ArrayList<String>( strings.length );
        for ( String s : strings )
        {
            list.add( s );
        }
        return list;
    }

    public static ArrayList<String> createTrendsArrayList( Trends trends )
    {
        ArrayList<String> list = new ArrayList<String>( trends.getTrends().size() );
        for ( Trend t : trends.getTrends() )
        {
            list.add( t.getName() );
        }
        return list;
    }

    public static final String extractJsonStringField( String jsonBody, String field )
    {
        return new JsonParser()
                .parse( jsonBody )
                .getAsJsonObject()
                .get( field )
                .toString()
                .replaceAll( "\"", "" );
    }

    public static boolean isNullOrEmpty( final Collection<?> c )
    {
        return null == c || c.isEmpty();
    }
}
