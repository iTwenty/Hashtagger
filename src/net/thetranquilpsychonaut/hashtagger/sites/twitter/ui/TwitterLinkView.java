package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import twitter4j.Status;

/**
 * Created by itwenty on 4/15/14.
 */
public class TwitterLinkView extends RelativeLayout
{
    ImageView imgvFavicon;
    TextView  tvDisplayUrl;
    TextView  tvExpandedUrl;

    public TwitterLinkView( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterLinkView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterLinkView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        LayoutInflater.from( context ).inflate( R.layout.twitter_link_view, this );
        imgvFavicon = ( ImageView ) findViewById( R.id.imgv_favicon );
        tvDisplayUrl = ( TextView ) findViewById( R.id.tv_display_url );
        tvExpandedUrl = ( TextView ) findViewById( R.id.tv_expanded_url );
    }

    public void showLinkFromStatus( Status status )
    {
        UrlImageViewHelper.setUrlDrawable( imgvFavicon, "http://g.etfv.co/http://" + status.getURLEntities()[0].getDisplayURL(), null, HashtaggerApp.CACHE_DURATION_MS );
        tvDisplayUrl.setText( status.getURLEntities()[0].getDisplayURL() );
        tvExpandedUrl.setText( status.getURLEntities()[0].getExpandedURL() );
    }

    public void clearView()
    {
        imgvFavicon.setImageDrawable( null );
        tvDisplayUrl.setText( "" );
        tvExpandedUrl.setText( "" );
    }
}
