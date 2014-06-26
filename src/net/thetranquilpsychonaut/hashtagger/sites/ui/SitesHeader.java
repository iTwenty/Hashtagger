package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by itwenty on 6/26/14.
 */
public abstract class SitesHeader extends RelativeLayout implements View.OnClickListener
{
    protected ImageView profileImage;

    public SitesHeader( Context context )
    {
        this( context, null, 0 );
    }

    public SitesHeader( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public SitesHeader( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        init( context );
        profileImage = initProfileImage();
        profileImage.setOnClickListener( this );
    }

    protected abstract void init( Context context );

    protected abstract ImageView initProfileImage();

    @Override
    public void onClick( View v )
    {
        Intent i = new Intent( Intent.ACTION_VIEW );
        i.setData( Uri.parse( getProfileUrl() ) );
        getContext().startActivity( i );
    }

    protected abstract String getProfileUrl();

    protected abstract void updateHeader( Object result );
}
