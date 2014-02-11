package net.thetranquilpsychonaut.hashtagger;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by itwenty on 2/10/14.
 */
public class InstagramFragment extends Fragment
{
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_instagram, container, false );
        return v;
    }
}
