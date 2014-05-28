package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;

/**
 * Created by itwenty on 5/16/14.
 */
public class GPlusDetailActivity extends SitesDetailActivity implements View.OnClickListener
{
    private TextView    tvContent;
    private GPlusHeader gPlusHeader;
    private Activity    activity;
    private ImageView   imgvMediaImage;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_gplus_detail );
        tvContent = ( TextView ) findViewById( R.id.tv_content );
        gPlusHeader = ( GPlusHeader ) findViewById( R.id.gplus_header );
        imgvMediaImage = ( ImageView ) findViewById( R.id.imgv_media_image );
        activity = GPlusData.ActivityData.popActivity();
        if ( null == activity )
        {
            finish();
        }
        gPlusHeader.updateHeader( activity );
        tvContent.setText( Html.fromHtml( activity.getObject().getContent() ) );
        tvContent.setMovementMethod( LinkMovementMethod.getInstance() );
        imgvMediaImage.setOnClickListener( this );

        if ( GPlusListAdapter.getActivityType( this.activity ) == GPlusListAdapter.ACTIVITY_TYPE_MEDIA )
        {
            Picasso.with( this )
                    .load( activity.getObject().getAttachments().get( 0 ).getImage().getUrl() )
                    .into( imgvMediaImage, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            if ( "video".equals( activity.getObject().getAttachments().get( 0 ).getObjectType() ) )
                            {
                                findViewById( R.id.imgv_play ).setVisibility( View.VISIBLE );
                            }
                            findViewById( R.id.fl_wrapper ).setVisibility( View.VISIBLE );
                        }

                        @Override
                        public void onError()
                        {

                        }
                    } );
        }
    }

    @Override
    protected TextView getLinkedTextView()
    {
        return tvContent;
    }

    @Override
    public void onClick( View v )
    {

        String objectType = activity.getObject().getAttachments().get( 0 ).getObjectType();
        if ( "photo".equals( objectType ) )
        {
            Intent i = new Intent( this, ViewImageActivity.class );
            String imageUrl;
            if ( null != activity.getObject().getAttachments().get( 0 ).getFullImage() )
            {
                imageUrl = activity.getObject().getAttachments().get( 0 ).getFullImage().getUrl();
            }
            else
            {
                imageUrl = activity.getObject().getAttachments().get( 0 ).getImage().getUrl();
            }
            i.putExtra( ViewImageActivity.IMAGE_URL_KEY, imageUrl );
            startActivity( i );
        }
        else if ( "video".equals( objectType ) )
        {
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setData( Uri.parse( activity.getObject().getAttachments().get( 0 ).getUrl() ) );
            startActivity( i );
        }
    }
}