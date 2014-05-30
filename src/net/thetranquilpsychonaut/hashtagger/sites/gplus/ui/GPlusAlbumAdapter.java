package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;

import java.util.List;

/**
 * Created by itwenty on 5/29/14.
 */
public class GPlusAlbumAdapter extends ArrayAdapter<String>
{
    public GPlusAlbumAdapter( Context context, int resource, List<String> albumImageUrls )
    {
        super( context, resource, albumImageUrls );
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        ImageView imageView;
        if ( null == convertView )
        {
            LayoutInflater inflater = ( LayoutInflater ) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            imageView = ( ImageView ) inflater.inflate( R.layout.gplus_album_image, parent, false );
        }
        else
        {
            imageView = ( ImageView ) convertView;
        }
        Picasso.with( getContext() ).load( getItem( position ) ).fit().centerCrop().into( imageView );
        return imageView;
    }
}
