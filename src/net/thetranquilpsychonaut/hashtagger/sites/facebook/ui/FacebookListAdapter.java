package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;

import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookListAdapter extends ArrayAdapter<Post>
{
    Context ctx;

    public FacebookListAdapter( Context context, int textViewResourceId, List<Post> posts )
    {
        super( context, textViewResourceId, posts );
        this.ctx = context;
    }

    static class ViewHolder
    {
        ImageView              imgvProfileImage;
        TextView               tvUserNameOrStory;
        TextView               tvCreatedTime;
        TextView               tvMessage;
        FacebookAttachmentView favAttachment;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        View view = convertView;
        ViewHolder viewHolder;
        Post post = getItem( position );
        if ( view == null )
        {
            view = LayoutInflater.from( ctx ).inflate( R.layout.fragment_facebook_list_row, null );
            viewHolder = new ViewHolder();
            viewHolder.imgvProfileImage = ( ImageView ) view.findViewById( R.id.imgv_profile_image );
            viewHolder.tvUserNameOrStory = ( TextView ) view.findViewById( R.id.tv_user_name_or_story );
            viewHolder.tvCreatedTime = ( TextView ) view.findViewById( R.id.tv_created_time );
            viewHolder.tvMessage = ( TextView ) view.findViewById( R.id.tv_message );
            viewHolder.favAttachment = ( FacebookAttachmentView ) view.findViewById( R.id.fav_attachment );
            view.setTag( viewHolder );
        }
        else
        {
            viewHolder = ( ViewHolder ) view.getTag();
        }
        UrlImageViewHelper.setUrlDrawable( viewHolder.imgvProfileImage, Helper.getFacebookPictureUrl( post.getFrom().getId() ) );
        viewHolder.tvUserNameOrStory.setText( post.getStory() == null ? post.getFrom().getName() : post.getStory() );
        viewHolder.tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
        if ( null != post.getMessage() )
        {
            viewHolder.tvMessage.setVisibility( View.VISIBLE );
            viewHolder.tvMessage.setText( post.getMessage() );
        }
        else
        {
            viewHolder.tvMessage.setVisibility( View.GONE );
            viewHolder.tvMessage.setText( "" );
        }
        if( "status".equals( post.getType() ) )
        {
            viewHolder.favAttachment.setVisibility( View.GONE );
        }
        else
        {
            viewHolder.favAttachment.setVisibility( View.VISIBLE );
            viewHolder.favAttachment.setAttachmentFromPost( post );
        }
        return view;
    }
}
