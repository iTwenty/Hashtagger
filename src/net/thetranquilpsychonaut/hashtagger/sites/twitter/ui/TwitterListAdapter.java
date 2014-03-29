package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import twitter4j.Status;

import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterListAdapter extends ArrayAdapter<Status>
{
    Context ctx;

    public TwitterListAdapter( Context context, int textViewResourceId, List<Status> objects )
    {
        super( context, textViewResourceId, objects );
        ctx = context;
    }

    class ViewHolder
    {
        ImageView imgvProfileImage;
        TextView  tvUserName;
        TextView  tvScreenName;
        TextView  tvCreatedAt;
        TextView  tvTweetText;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        View view = convertView;
        ViewHolder viewHolder;
        Status status = getItem( position );
        LayoutInflater inflater = ( LayoutInflater ) ctx.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );

        if ( view == null )
        {
            view = inflater.inflate( R.layout.fragment_twitter_list_row, null );
            viewHolder = new ViewHolder();
            viewHolder.imgvProfileImage = ( ImageView ) view.findViewById( R.id.imgv_profile_image );
            viewHolder.tvUserName = ( TextView ) view.findViewById( R.id.tv_user_name );
            viewHolder.tvScreenName = ( TextView ) view.findViewById( R.id.tv_screen_name );
            viewHolder.tvCreatedAt = ( TextView ) view.findViewById( R.id.tv_created_at );
            viewHolder.tvTweetText = ( TextView ) view.findViewById( R.id.tv_tweet_text );
            view.setTag( viewHolder );
        }
        else
        {
            viewHolder = ( ViewHolder ) view.getTag();
        }
        viewHolder.imgvProfileImage.setImageDrawable( ctx.getResources().getDrawable( R.drawable.drawable_rect ) );
        viewHolder.tvUserName.setText( status.getUser().getName() );
        viewHolder.tvScreenName.setText( "@" + status.getUser().getScreenName() );
        viewHolder.tvCreatedAt.setText( Helper.getDate( status.getCreatedAt() ) );
        viewHolder.tvTweetText.setText( status.getText() );
        Helper.linkify( viewHolder.tvTweetText );
        return view;
    }
}
