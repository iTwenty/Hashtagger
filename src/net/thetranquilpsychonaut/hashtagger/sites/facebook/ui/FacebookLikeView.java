package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.From;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;

/**
 * Created by itwenty on 6/14/14.
 */
public class FacebookLikeView extends RelativeLayout
{
    private ImageView likeImage;
    private TextView  likeName;

    public FacebookLikeView( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookLikeView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookLikeView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_like_view, this );
        likeImage = ( ImageView ) findViewById( R.id.like_image );
        likeName = ( TextView ) findViewById( R.id.like_name );
    }

    public void update( From from )
    {
        Picasso.with( getContext() )
                .load( UrlModifier.getFacebookProfilePictureUrl( from.getId() ) )
                .fit()
                .centerCrop()
                .into( likeImage );
        likeName.setText( from.getName() );
    }
}
