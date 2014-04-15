package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import net.thetranquilpsychonaut.hashtagger.R;
import twitter4j.Status;

/**
 * Created by itwenty on 4/15/14.
 */
public class TwitterLinkEntity extends RelativeLayout
{
    ImageView imgvFavicon;
    TextView  tvDisplayUrl;
    TextView  tvExpandedUrl;

    public TwitterLinkEntity( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterLinkEntity( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterLinkEntity( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        LayoutInflater.from( context ).inflate( R.layout.twitter_entity_link, this );
        imgvFavicon = ( ImageView ) findViewById( R.id.imgv_favicon );
        tvDisplayUrl = ( TextView ) findViewById( R.id.tv_display_url );
        tvExpandedUrl = ( TextView ) findViewById( R.id.tv_expanded_url );
    }

    public void setLinkFromStatus( Status status )
    {
        UrlImageViewHelper.setUrlDrawable( imgvFavicon, "http://g.etfv.co" + status.getURLEntities()[0].getDisplayURL() );
        tvDisplayUrl.setText( status.getURLEntities()[0].getDisplayURL() );
        tvExpandedUrl.setText( status.getURLEntities()[0].getExpandedURL() );
    }
}
