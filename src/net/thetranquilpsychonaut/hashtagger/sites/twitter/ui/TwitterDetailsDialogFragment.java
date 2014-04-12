package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import twitter4j.Status;

/**
 * Created by itwenty on 3/31/14.
 */
public class TwitterDetailsDialogFragment extends DialogFragment
{
    ImageView imgvProfileImage;
    TextView  tvUserName;
    TextView  tvScreenName;
    TextView  tvStatusText;
    TextView  tvCreatedAt;
    ImageView imgvMediaEntity;

    public static TwitterDetailsDialogFragment getInstance( Status status )
    {
        TwitterDetailsDialogFragment fragment = new TwitterDetailsDialogFragment();
        Bundle b = new Bundle();
        b.putSerializable( HashtaggerApp.TWITTER_STATUS_KEY, status );
        fragment.setArguments( b );
        return fragment;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.dialog_fragment_twitter_details, container, false );
        Status status = ( Status ) getArguments().getSerializable( HashtaggerApp.TWITTER_STATUS_KEY );
        imgvProfileImage = ( ImageView ) v.findViewById( R.id.imgv_profile_image );
        tvUserName = ( TextView ) v.findViewById( R.id.tv_user_name );
        tvScreenName = ( TextView ) v.findViewById( R.id.tv_screen_name );
        tvStatusText = ( TextView ) v.findViewById( R.id.tv_message );
        tvCreatedAt = ( TextView ) v.findViewById( R.id.tv_created_time );
        imgvMediaEntity = ( ImageView ) v.findViewById( R.id.imgv_media_entity );

        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, status.getUser().getBiggerProfileImageURL() );
        tvUserName.setText( status.getUser().getName() );
        tvScreenName.setText( "@" + status.getUser().getScreenName() );
        tvStatusText.setText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
        Helper.linkifyTwitter( tvStatusText );
        tvCreatedAt.setText( Helper.getStringDate( status.getCreatedAt() ) );
        if ( status.getMediaEntities().length != 0 )
        {
            imgvMediaEntity.setVisibility( View.VISIBLE );
            UrlImageViewHelper.setUrlDrawable( imgvMediaEntity, status.getMediaEntities()[0].getMediaURL() );
        }
        return v;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        Dialog dialog = super.onCreateDialog( savedInstanceState );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        return dialog;
    }
}
