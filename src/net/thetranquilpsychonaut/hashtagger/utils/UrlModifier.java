package net.thetranquilpsychonaut.hashtagger.utils;

import android.net.Uri;
import android.text.TextUtils;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/**
 * Created by itwenty on 6/13/14.
 */
public final class UrlModifier
{
    private static final Pattern FB_INTERNAL_URL_PATTERN = Pattern.compile( "_s.|_t." );
    private static final Pattern GPLUS_URL_PATTERN       = Pattern.compile( "w\\d+-h\\d+(.*?/)" );
    private static final Pattern TWITTER_URL_PATTERN     = Pattern.compile( ":thumb$|:small$|:medium$|:large$|$" );

    private UrlModifier()
    {
        throw new RuntimeException( "UrlModifier class must not be instantiated!" );
    }

    public static String getFacebookProfilePictureUrl( String userId )
    {
        return String.format( "http://graph.facebook.com/%s/picture?type=square", userId );
    }

    public static String getInstagramUserUrl( String userName )
    {
        return String.format( "https://instagram.com/%s", userName );
    }

    public static Uri getTwitterStatusUrl( Status status )
    {
        return Uri.parse( String.format( "http://twitter.com/%s/status/%s", status.getUser().getIdStr(), status.getIdStr() ) );
    }

    public static Uri getFacebookPostUrl( Post post )
    {
        return Uri.parse( "http://facebook.com/" + post.getId() );
    }

    public static Uri getGPlusActivityUrl( Activity activity )
    {
        return Uri.parse( activity.getUrl() );
    }

    public static Uri getInstagramMediaUrl( Media media )
    {
        // TODO
        return null;
    }

    public static String getFacebookLargePhotoUrl( String smallPhotoUrl )
    {
        String newUrl = FB_INTERNAL_URL_PATTERN.matcher( smallPhotoUrl ).replaceAll( "_o." );
        if ( TextUtils.equals( smallPhotoUrl, newUrl ) && smallPhotoUrl.contains( "fbexternal" ) )
        {
            Uri uri = Uri.parse( smallPhotoUrl );
            String imageUrl = uri.getQueryParameter( "url" );
            if ( !TextUtils.isEmpty( imageUrl ) )
            {
                try
                {
                    newUrl = URLDecoder.decode( imageUrl, "UTF-8" );
                    Helper.debug( "fbexternal image URL decoded as : " + newUrl );
                }
                catch ( UnsupportedEncodingException e )
                {
                    e.printStackTrace();
                }
            }
        }
        return newUrl;
    }

    public static String getGPlusFullAlbumImageUrl( String albumThumbnailUrl )
    {
        return GPLUS_URL_PATTERN.matcher( albumThumbnailUrl ).replaceAll( "" );
    }

    public static String getGPlusSmallPhotoUrl( String largePhotoUrl )
    {
        return GPLUS_URL_PATTERN.matcher( largePhotoUrl ).replaceAll( "w200-h200$1" );
    }

    public static String getTwitterLargePhotoUrl( String smallPhotoUrl )
    {
        return TWITTER_URL_PATTERN.matcher( smallPhotoUrl ).replaceAll( ":large" );
    }
}
