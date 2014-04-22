package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

/**
 * Created by itwenty on 4/14/14.
 */
public class FacebookListRow extends SitesListRow
{
    private ImageView          imgvProfileImage;
    private TextView           tvUserNameOrStory;
    private TextView           tvCreatedTime;
    private TextView           tvMessage;
    private FacebookExpandView facebookExpandView;

    protected FacebookListRow( Context context )
    {
        this( context, null, 0 );
    }

    protected FacebookListRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    protected FacebookListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        LayoutInflater.from( context ).inflate( R.layout.fragment_facebook_list_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvUserNameOrStory = ( TextView ) findViewById( R.id.tv_user_name_or_story );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        facebookExpandView = ( FacebookExpandView ) findViewById( R.id.facebook_expand_view );
    }

    @Override
    public void updateRow( final Object data )
    {
        Post post = ( Post ) data;
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, Helper.getFacebookPictureUrl( post.getFrom().getId() ), getResources().getDrawable( R.drawable.drawable_image_loading ), HashtaggerApp.CACHE_DURATION_MS );
        tvUserNameOrStory.setText( post.getStory() == null ? post.getFrom().getName() : post.getStory() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
        tvMessage.setText( post.getMessage() );
    }

    @Override
    public void expandRow( final Object data, boolean animate )
    {
        super.expandRow( data, animate );
        final Post post = ( Post ) data;
        facebookExpandView.expandPost( post, animate );
    }

    @Override
    public void collapseRow( boolean animate )
    {
        super.collapseRow( animate );
        facebookExpandView.collapsePost( animate );
    }
}
