package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;
import twitter4j.Status;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterMediaRow extends TwitterListRow implements View.OnClickListener
{
    private ImageView     imgvMediaThumb;

    protected TwitterMediaRow( Context context )
    {
        super( context );
    }

    protected TwitterMediaRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected TwitterMediaRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.twitter_media_row, this );
        imgvMediaThumb = ( ImageView ) findViewById( R.id.imgv_media_thumb );
        imgvMediaThumb.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected TwitterHeader initTwitterHeader()
    {
        return  ( TwitterHeader ) findViewById( R.id.twitter_header );
    }

    @Override
    protected TextView initStatusText()
    {
        return ( TextView ) findViewById( R.id.tv_tweet );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.twitter_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        Picasso.with( getContext() )
                .load( status.getMediaEntities()[0].getMediaURL() + ":thumb" )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvMediaThumb );
    }

    @Override
    public void onClick( View v )
    {
        Intent intent = new Intent( getContext(), ViewImageActivity.class );
        intent.putExtra( ViewImageActivity.IMAGE_URL_KEY, status.getMediaEntities()[0].getMediaURL() + ":large" );
        getContext().startActivity( intent );
    }
}
