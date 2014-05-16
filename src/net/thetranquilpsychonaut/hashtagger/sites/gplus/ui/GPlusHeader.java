package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusHeader extends RelativeLayout
{
    private ImageView imgvActorImage;
    private TextView  tvDisplayName;
    private TextView  tvSharedName;
    private TextView  tvPublishedTime;

    public GPlusHeader( Context context )
    {
        this( context, null, 0 );
    }

    public GPlusHeader( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public GPlusHeader( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.gplus_header, this );
        imgvActorImage = ( ImageView ) findViewById( R.id.imgv_actor_image );
        tvDisplayName = ( TextView ) findViewById( R.id.tv_display_name );
        tvPublishedTime = ( TextView ) findViewById( R.id.tv_published_time );
        tvSharedName = ( TextView ) findViewById( R.id.tv_shared_name );
    }

    public void updateHeader( Activity activity )
    {
        Picasso.with( HashtaggerApp.app ).load( activity.getActor().getImage().getUrl() ).into( imgvActorImage );
        tvDisplayName.setText( activity.getActor().getDisplayName() );
        tvPublishedTime.setText( Helper.getFuzzyDateTime( activity.getPublished().getValue() ) );
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
