package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.widget.ArrayAdapter;
import facebook4j.Post;

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
}
