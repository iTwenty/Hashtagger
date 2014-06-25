package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 6/25/14.
 */
public class InstagramHeader extends RelativeLayout implements View.OnClickListener
{
    private ImageView imgvProfileImage;
    private TextView tvUsername;
    private TextView tvCreatedTime;
    private Media media;

    public InstagramHeader( Context context )
    {
        this( context, null, 0 );
    }

    public InstagramHeader( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public InstagramHeader( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.instagram_header, this );
        this.imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        this.tvUsername = ( TextView ) findViewById( R.id.tv_user_name );
        this.tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        this.imgvProfileImage.setOnClickListener( this );
    }

    public void showHeader( Media media )
    {
        this.media = media;
        Picasso.with( getContext() )
                .load( media.getUser().getProfilePicture() )
                .fit()
                .centerCrop()
                .into( imgvProfileImage );
        tvUsername.setText( media.getUser().getUserName() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( media.getCreatedTime() ) );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imgvProfileImage ) )
        {

        }
    }
}
