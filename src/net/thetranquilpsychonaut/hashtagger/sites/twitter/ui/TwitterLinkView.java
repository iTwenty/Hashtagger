package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
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
        inflate( context, R.layout.twitter_link_view, this );
        imgvFavicon = ( ImageView ) findViewById( R.id.imgv_favicon );
        tvExpandedUrl = ( TextView ) findViewById( R.id.tv_expanded_url );
    }

    public void showLinkFromStatus( final Status status )
    {
        UrlImageViewHelper.setUrlDrawable( imgvFavicon, "http://g.etfv.co/http://" + status.getURLEntities()[0].getDisplayURL(), null, HashtaggerApp.CACHE_DURATION_MS );
        tvExpandedUrl.setText( status.getURLEntities()[0].getExpandedURL() );
        this.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( status.getURLEntities()[0].getExpandedURL() ) );
                getContext().startActivity( intent );
            }
        } );
    }

    public void clearView()
    {
        imgvFavicon.setImageDrawable( null );
        tvExpandedUrl.setText( "" );
        this.setOnClickListener( null );
    }
}
