package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
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
    }

    @Override
    protected void init( Context context )
    {
        LayoutInflater.from( context ).inflate( R.layout.fragment_facebook_list_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvUserNameOrStory = ( TextView ) findViewById( R.id.tv_user_name_or_story );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        facebookExpandView = ( FacebookExpandView ) findViewById( R.id.facebook_expand_view );
        facebookExpandView.setVisibility( GONE );
    }


    @Override
    public void updateRow( final Object data )
    {
        Post post = ( Post ) data;
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, Helper.getFacebookPictureUrl( post.getFrom().getId() ) );
        tvUserNameOrStory.setText( post.getStory() == null ? post.getFrom().getName() : post.getStory() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
        tvMessage.setText( post.getMessage() );
    }

    @Override
    public void expandRow( final Object data, boolean animate )
    {
        super.expandRow( data, animate );
        final Post post = ( Post ) data;
        facebookExpandView.showPost( post );
        facebookExpandView.setVisibility( VISIBLE );
    }

    @Override
    public void collapseRow( boolean animate )
    {
        super.collapseRow( animate );
        facebookExpandView.setVisibility( GONE );
    }

    @Override
    public void updateExpandedRow( Object data )
    {
        final Post post = ( Post ) data;
        facebookExpandView.showPost( post );
    }
}
