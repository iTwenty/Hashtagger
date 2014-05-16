package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusHeader extends RelativeLayout
{
    private ImageView imgvActorImage;
    private TextView  tvDisplayName;
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
        tvDisplayName = ( TextView ) findViewById( R.id.imgv_display_name );
        tvPublishedTime = ( TextView ) findViewById( R.id.tv_published_time );
    }

    public void updateHeader( Activity activity )
    {
        Picasso.with( getContext() ).load( activity.getActor().getImage().getUrl() ).into( imgvActorImage );
        tvDisplayName.setText( activity.getActor().getDisplayName() );
        tvPublishedTime.setText( Helper.getFuzzyDateTime( activity.getPublished().getValue() ) );
    }
}
