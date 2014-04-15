package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.components.FacebookSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.components.FacebookUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by itwenty on 2/10/14.
 */
public class FacebookFragment extends SitesFragment
{
    @Override
    protected SitesUserHandler initSitesUserHandler()
    {
        FacebookUserHandler facebookUserHandler = new FacebookUserHandler( this );
        return facebookUserHandler;
    }

    @Override
    protected SitesSearchHandler initSitesSearchHandler()
    {
        FacebookSearchHandler facebookSearchHandler = new FacebookSearchHandler( this );
        return facebookSearchHandler;
    }

    @Override
    protected SitesListAdapter initSitesListAdapter()
    {
        FacebookListAdapter facebookListAdapter = new FacebookListAdapter( getActivity(), R.layout.fragment_twitter_list_row, ( ArrayList<ExpandablePost> ) results );
        return facebookListAdapter;
    }

    @Override
    protected List<?> initResultsList()
    {
        List<ExpandablePost> results = new ArrayList<ExpandablePost>();
        return results;
    }

    @Override
    protected void searchHashtag( String hashtag )
    {

    }

    @Override
    protected int getNotLoggedInToastTextId()
    {
        return R.string.str_toast_facebook_not_logged_in;
    }

    @Override
    protected int getLoggedInToastTextId()
    {
        return R.string.str_toast_facebook_logged_in_as;
    }

    @Override
    protected int getLoginFailureToastTextId()
    {
        return R.string.str_toast_facebook_login_failed;
    }

    @Override
    protected int getLoginRequestCode()
    {
        return HashtaggerApp.FACEBOOK_LOGIN_REQUEST_CODE;
    }

    @Override
    protected Class<?> getLoginActivityClassName()
    {
        return FacebookLoginActivity.class;
    }

    @Override
    public void onUserLoggedIn()
    {
        ( ( FacebookSearchHandler ) sitesSearchHandler ).setAccessToken();
        super.onUserLoggedIn();
    }

    @Override
    public void onUserLoggedOut()
    {
        ( ( FacebookSearchHandler ) sitesSearchHandler ).clearAccessToken();
        super.onUserLoggedOut();
    }

    @Override
    protected void addToEnd( List<?> searchResults )
    {
        Collections.reverse( searchResults );
        for( Post post : ( ArrayList<Post> ) searchResults )
        {
            ( ( ArrayList<ExpandablePost> )results ).add( 0, new ExpandablePost( post, false ) );
        }
    }

    @Override
    protected void addToStart( List<?> searchResults )
    {
        for( Post post : ( ArrayList<Post> ) searchResults )
        {
            ( ( ArrayList<ExpandablePost> )results ).add( new ExpandablePost( post, false ) );
        }
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        ExpandablePost ep = ( ExpandablePost ) parent.getItemAtPosition( position );
        FacebookListRow facebookListRow = ( FacebookListRow ) view.getTag();
        if ( ep.isExpanded() )
        {
            facebookListRow.collapseRow();
            ep.setExpanded( false );
        }
        else
        {
            facebookListRow.expandRow( ep );
            ep.setExpanded( true );
        }
    }
}
