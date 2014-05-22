package net.thetranquilpsychonaut.hashtagger.utils;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.google.api.services.plus.model.Activity;
import com.twitter.Autolink;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import twitter4j.Status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * Created by itwenty on 2/7/14.
 */
public class Helper
{

    private static final String  FACEBOOK_PROFILE_PICTURE_URL = "http://graph.facebook.com/%s/picture?type=square";
    private static final boolean DEBUG                        = true;
//    private static final Pattern hashtagPattern               = Pattern.compile( "#([A-Za-z0-9_-]+)" );
//
//    private static final String twitterHashtagScheme = "http://www.twitter.com/search/";
//
//    private static final Pattern twitterMentionPattern = Pattern.compile( "@([A-Za-z0-9_-]+)" );
//    private static final String  twitterMentionScheme  = "http://www.twitter.com/";

    private static final Linkify.TransformFilter filter = new Linkify.TransformFilter()
    {
        public final String transformUrl( final Matcher match, String url )
        {
            return match.group();
        }
    };

    public static void debug( String s )
    {
        if ( DEBUG )
        {
            Log.d( "twtr", s );
        }
    }

    public static CharSequence getFuzzyDateTime( long time )
    {
        String timeStr = DateUtils.getRelativeDateTimeString(
                HashtaggerApp.app.getApplicationContext(),
                time,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL ).toString();
        return timeStr.startsWith( "in" ) ? "just now" : timeStr;

    }

    public static String getStringDate( Date date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "hh:mm:ss dd/MM/yyyy" );
        return sdf.format( date );
    }

    public static String getLinkedTweetText( String tweetText )
    {
        return new Autolink().autoLink( tweetText );
    }

    //    public static void linkifyTwitter( TextView tv )
//    {
//        Linkify.addLinks( tv, twitterMentionPattern, twitterMentionScheme, null, filter );
//        Linkify.addLinks( tv, hashtagPattern, twitterHashtagScheme, null, filter );
//        Linkify.addLinks( tv, Patterns.WEB_URL, null, null, filter );
//        stripUnderlines( tv );
//
//    }
//
    public static Spannable stripUnderlines( Spannable spannable )
    {
        URLSpan[] spans = spannable.getSpans( 0, spannable.length(), URLSpan.class );
        for ( URLSpan span : spans )
        {
            int start = spannable.getSpanStart( span );
            int end = spannable.getSpanEnd( span );
            spannable.removeSpan( span );
            span = new URLSpanNoUnderline( span.getURL() );
            spannable.setSpan( span, start, end, 0 );
        }
        return spannable;
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

    private static class URLSpanNoUnderline extends URLSpan
    {
        public URLSpanNoUnderline( String url )
        {
            super( url );
        }

        @Override
        public void updateDrawState( TextPaint ds )
        {
            super.updateDrawState( ds );
            ds.setUnderlineText( false );
        }
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