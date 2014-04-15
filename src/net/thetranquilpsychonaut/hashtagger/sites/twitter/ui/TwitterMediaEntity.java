package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import net.thetranquilpsychonaut.hashtagger.R;
import twitter4j.Status;

/**
 * Created by itwenty on 4/15/14.
 */
public class TwitterMediaEntity extends RelativeLayout
{
    ImageView imgvMedia;

    public TwitterMediaEntity( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterMediaEntity( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterMediaEntity( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        LayoutInflater.from( context ).inflate( R.layout.twitter_entity_media, this );
        imgvMedia = ( ImageView ) findViewById( R.id.imgv_media );
    }

    public void showMediaFromStatus( Status status )
    {
        UrlImageViewHelper.setUrlDrawable( imgvMedia, status.getMediaEntities()[0].getMediaURL() );
    }
}
