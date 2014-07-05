package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;

/**
 * Created by itwenty on 5/11/14.
 */
public class FacebookHeader extends SitesHeader
{
    private TextView tvUserName;
    private TextView tvCreatedTime;
    private Post     post;

    public FacebookHeader( Context context )
    {
        super( context );
    }

    public FacebookHeader( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public FacebookHeader( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_header, this );
        tvUserName = ( TextView ) findViewById( R.id.tv_user_name );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
    }

    @Override
    protected ImageView initProfileImage()
    {
        return ( ImageView ) findViewById( R.id.imgv_profile_image );
    }

    @Override
    protected String getProfileUrl()
    {
        return UrlModifier.getFacebookUserUrl( post.getFrom().getId() );
    }

    @Override
    protected void updateHeader( Object result )
    {
        this.post = ( Post ) result;
        Picasso.with( getContext() )
                .load( UrlModifier.getFacebookProfilePictureUrl( post.getFrom().getId() ) )
                .fit()
                .centerCrop()
                .into( profileImage );
        tvUserName.setText( post.getFrom().getName() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
    }
}
