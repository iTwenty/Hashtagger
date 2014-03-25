package net.thetranquilpsychonaut.hashtagger;

import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by itwenty on 2/7/14.
 */
public class Helper
{
    static Date now;
    static SimpleDateFormat timeFormat = new SimpleDateFormat( "kk:mm:ss" );
    static SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yy" );

    public static void debug( String s )
    {
        if ( HashtaggerApp.DEBUG )
        {
            Log.d( "twtr", s );
        }
    }

    public static String getDate( Date then )
    {
        now = Calendar.getInstance().getTime();
        long dateDiff = now.getTime() - then.getTime();
        if ( dateDiff < 24 * 60 * 60 * 1000 )
            return timeFormat.format( then );
        else
            return dateFormat.format( then );
    }

    public static void linkify( TextView tv )
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
}
