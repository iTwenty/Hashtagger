package net.thetranquilpsychonaut.hashtagger;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.otto.Subscribe;

/**
 * Created by itwenty on 2/10/14.
 */
public class TwitterFragment extends Fragment
{
    TextView tvTwitter;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        HashtaggerApp.bus.register( this );
        View v = inflater.inflate( R.layout.fragment_twitter, container, false );
        tvTwitter = ( TextView )v.findViewById( R.id.tv_twitter );
        return v;
    }

    @Subscribe
    public void searchHashtag( String hashtag )
    {
        tvTwitter.setText( hashtag );
    }
}
