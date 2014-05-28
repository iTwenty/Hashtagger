package net.thetranquilpsychonaut.hashtagger.utils;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;
import com.google.api.services.plus.model.Activity;
import com.twitter.Autolink;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import twitter4j.Status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by itwenty on 2/7/14.
 */
public class Helper
{
    private static final String  FACEBOOK_PROFILE_PICTURE_URL = "http://graph.facebook.com/%s/picture?type=square";
    private static final boolean DEBUG                        = true;

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
                HashtaggerApp.app.getApplicationContext(),
                time,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL ).toString();


    }

    public static String getStringDate( Date date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "hh:mm:ss dd/MM/yyyy" );
        return sdf.format( date );
    }

    public static String getLinkedStatusText( String tweetText )
    {
        return new Autolink().autoLink( tweetText );
    }

    public static void linkifyFacebook( TextView tv )
    {
        Linkify.TransformFilter filter = new Linkify.TransformFilter()
        {
            @Override
            public String transformUrl( Matcher match, String url )
            {
                return match.group();
            }
        };

        Pattern hashtagPattern = Pattern.compile( "#([A-Za-z0-9_-]+)" );
        String hashtagScheme = "http://www.facebook.com/hashtag/";
        Linkify.addLinks( tv, hashtagPattern, hashtagScheme, null, filter );

        Pattern urlPattern = Patterns.WEB_URL;
        Linkify.addLinks( tv, urlPattern, null, null, filter );
    }

    public static String getFacebookPictureUrl( String userId )
    {
        return String.format( FACEBOOK_PROFILE_PICTURE_URL, userId );
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

    public static Uri getTwitterStatusUrl( Status status )
    {
        return Uri.parse( "http://twitter.com/" + status.getUser().getId() + "/status/" + status.getId() );
    }

    public static Uri getFacebookPostUrl( Post post )
    {
        return Uri.parse( "http://facebook.com/" + post.getId() );
    }

    public static Uri getGPlusActivityUrl( Activity activity )
    {
        return Uri.parse( activity.getUrl() );
    }
}
