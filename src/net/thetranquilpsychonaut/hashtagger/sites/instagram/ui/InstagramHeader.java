package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;

/**
 * Created by itwenty on 6/25/14.
 */
public class InstagramHeader extends SitesHeader
{
    private TextView tvUsername;
    private TextView tvCreatedTime;
    private Media    media;

    public InstagramHeader( Context context )
    {
        super( context );
    }

    public InstagramHeader( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public InstagramHeader( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.instagram_header, this );
        this.tvUsername = ( TextView ) findViewById( R.id.tv_user_name );
        this.tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
    }

    @Override
    protected ImageView initProfileImage()
    {
        return ( ImageView ) findViewById( R.id.imgv_profile_image );
    }

    @Override
    protected String getProfileUrl()
    {
        return UrlModifier.getInstagramUserUrl( media.getUser().getUserName() );
    }

    @Override
    protected void updateHeader( Object result )
    {
        this.media = ( Media ) result;
        Picasso.with( getContext() )
                .load( media.getUser().getProfilePicture() )
                .fit()
                .centerCrop()
                .into( profileImage );
        tvUsername.setText( media.getUser().getUserName() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( media.getCreatedTime() ) );
    }
}
