package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;

import java.util.List;

/**
 * Created by itwenty on 6/3/14.
 */
public class TrendingHashtagsAdapter extends BaseAdapter
{
    private List<String>   trendingHashtags;
    private Context        context;
    private LayoutInflater inflater;

    public TrendingHashtagsAdapter( Context context, List<String> trendingHashtags )
    {
        super();
        this.context = context;
        this.trendingHashtags = trendingHashtags;
        inflater = LayoutInflater.from( context );
    }

    @Override
    public int getCount()
    {
        return trendingHashtags.size();
    }

    @Override
    public Object getItem( int position )
    {
        return trendingHashtags.get( position );
    }

    @Override
    public long getItemId( int position )
    {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        TextView tv;
        if ( null == convertView )
        {
            tv = ( TextView ) inflater.inflate( R.layout.trending_hashtags_list_row, parent, false );
        }
        else
        {
            tv = ( TextView ) convertView;
        }
        tv.setText( ( String ) getItem( position ) );
        return tv;
    }
}
