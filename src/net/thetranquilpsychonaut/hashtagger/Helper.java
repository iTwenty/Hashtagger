package net.thetranquilpsychonaut.hashtagger;

import android.text.format.DateUtils;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by itwenty on 2/7/14.
 */
public class Helper
{
    public static void debug( String s )
    {
        if ( HashtaggerApp.DEBUG )
        {
            Log.d( "twtr", s );
        }
    }

    public static CharSequence getFuzzyDateTime( long time )
    {
        return DateUtils.getRelativeDateTimeString( HashtaggerApp.app.getApplicationContext(),
                time,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL );
    }

    public static String getStringDate( Date date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "hh:mm:ss dd/MM/yyyy" );
        return sdf.format( date );
    }

    public static void linkifyTwitter( TextView tv )
    {
        Linkify.TransformFilter filter = new Linkify.TransformFilter()
        {
            public final String transformUrl( final Matcher match, String url )
            {
                return match.group();
            }
        };

        Pattern mentionPattern = Pattern.compile( "@([A-Za-z0-9_-]+)" );
        String mentionScheme = "http://www.twitter.com/";
        Linkify.addLinks( tv, mentionPattern, mentionScheme, null, filter );

        Pattern hashtagPattern = Pattern.compile( "#([A-Za-z0-9_-]+)" );
        String hashtagScheme = "http://www.twitter.com/search/";
        Linkify.addLinks( tv, hashtagPattern, hashtagScheme, null, filter );

        Pattern urlPattern = Patterns.WEB_URL;
        Linkify.addLinks( tv, urlPattern, null, null, filter );
    }

    public static String getFacebookPictureUrl( String userId )
    {
        return String.format( HashtaggerApp.FACEBOOK_PROFILE_PICTURE_URL, userId );
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
}
