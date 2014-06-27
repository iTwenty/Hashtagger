package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusHeader extends SitesHeader
{
    private TextView  tvDisplayName;
    private TextView  tvSharedName;
    private TextView  tvPublishedTime;
    private Activity  activity;

    public GPlusHeader( Context context )
    {
        super( context );
    }

    public GPlusHeader( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public GPlusHeader( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.gplus_header, this );
        tvDisplayName = ( TextView ) findViewById( R.id.tv_display_name );
        tvPublishedTime = ( TextView ) findViewById( R.id.tv_published_time );
        tvSharedName = ( TextView ) findViewById( R.id.tv_shared_name );
    }

    @Override
    protected ImageView initProfileImage()
    {
        return ( ImageView ) findViewById( R.id.imgv_actor_image );
    }

    @Override
    protected String getProfileUrl()
    {
        return activity.getActor().getUrl();
    }

    @Override
    protected void updateHeader( Object result )
    {
        this.activity = ( Activity ) result;
        Picasso.with( getContext() )
            .load( activity.getActor().getImage().getUrl() )
            .fit()
            .centerCrop()
            .into( profileImage );
        tvDisplayName.setText( activity.getActor().getDisplayName() );
        tvPublishedTime.setText( Helper.getFuzzyDateTime( activity.getPublished().getTime() ) );
        if ( "share".equals( activity.getVerb() ) )
        {
            tvSharedName.setText( " of " + activity.getObject().getActor().getDisplayName() );
            tvSharedName.setVisibility( VISIBLE );
        }
        else
        {
            tvSharedName.setVisibility( GONE );
        }
    }
}
