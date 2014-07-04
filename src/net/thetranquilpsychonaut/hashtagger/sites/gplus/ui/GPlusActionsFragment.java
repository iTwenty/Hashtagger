package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.Actions;
import net.thetranquilpsychonaut.hashtagger.events.CommentFeedEvent;
import net.thetranquilpsychonaut.hashtagger.events.PeopleFeedEvent;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.GPlus;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Comment;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.CommentFeed;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.ListByActivityParams;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.PeopleFeed;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Person;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesActionsFragment;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/13/14.
 */
public class GPlusActionsFragment extends SitesActionsFragment implements AdapterView.OnItemClickListener
{
    public static final String TAG = GPlusActionsFragment.class.getSimpleName();

    private static final String PLUSONERS_KEY = "ps";
    private static final String RESHARERS_KEY = "rs";
    private static final String COMMENTS_KEY  = "cmts";

    private Activity activity;
    private int      actionType;

    private List<Person>   plusoners;
    private ListView       lvPlusOners;
    private TextView       lvPlusOnersEmpty;
    private PersonsAdapter plusonersAdapter;

    private List<Comment>   comments;
    private ListView        lvComments;
    private TextView        lvCommentsEmpty;
    private CommentsAdapter commentsAdapter;

    private List<Person>   resharers;
    private ListView       lvResharers;
    private TextView       lvResharersEmpty;
    private PersonsAdapter resharersAdapter;

    public static final GPlusActionsFragment newInstance( Activity activity, int actionType )
    {
        GPlusActionsFragment f = new GPlusActionsFragment();
        Bundle b = new Bundle();
        b.putSerializable( "activity", activity );
        b.putInt( "actionType", actionType );
        f.setArguments( b );
        return f;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        activity = ( Activity ) getArguments().getSerializable( "activity" );
        actionType = getArguments().getInt( "actionType" );
        plusoners = null == savedInstanceState ?
                new ArrayList<Person>() :
                ( List<Person> ) savedInstanceState.getSerializable( PLUSONERS_KEY );
        comments = null == savedInstanceState ?
                new ArrayList<Comment>() :
                ( List<Comment> ) savedInstanceState.getSerializable( COMMENTS_KEY );
        resharers = null == savedInstanceState ?
                new ArrayList<Person>() :
                ( List<Person> ) savedInstanceState.getSerializable( RESHARERS_KEY );
        plusonersAdapter = new PersonsAdapter( plusoners );
        commentsAdapter = new CommentsAdapter( comments );
        resharersAdapter = new PersonsAdapter( resharers );
    }

    @Override
    protected SitesActionsPagerAdapter initSitesActionsPagerAdapter()
    {
        return new GPlusActionsPagerAdapter();
    }

    @Override
    protected int getSelectedAction()
    {
        switch ( actionType )
        {
            case Actions.ACTION_GPLUS_ONE:
                return 0;
            case Actions.ACTION_GPLUS_COMMENT:
                return 1;
            case Actions.ACTION_GPLUS_RESHARE:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState )
    {
        super.onViewCreated( view, savedInstanceState );
        if ( null == savedInstanceState )
        {
            sitesActionsPager.post( new Runnable()
            {
                @Override
                public void run()
                {
                    if ( activity.getObject().getPlusoners().getTotalItems() != 0 )
                    {
                        lvPlusOnersEmpty.setText( getResources().getString( R.string.str_loading ) );
                        GPlus.api().listByActivity(
                                activity.getId(),
                                ListByActivityParams.PLUSONERS,
                                null,
                                new PeopleFeedCallback( ListByActivityParams.PLUSONERS ) );
                    }

                    if ( activity.getObject().getResharers().getTotalItems() != 0 )
                    {
                        lvResharersEmpty.setText( getResources().getString( R.string.str_loading ) );
                        GPlus.api().listByActivity(
                                activity.getId(),
                                ListByActivityParams.RESHARERS,
                                null,
                                new PeopleFeedCallback( ListByActivityParams.RESHARERS ) );
                    }

                    if ( activity.getObject().getReplies().getTotalItems() != 0 )
                    {
                        lvCommentsEmpty.setText( getResources().getString( R.string.str_loading ) );
                        GPlus.api().listComments(
                                activity.getId(),
                                null,
                                new CommentFeedCallback() );
                    }
                }
            } );
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putSerializable( PLUSONERS_KEY, ( java.io.Serializable ) plusoners );
        outState.putSerializable( COMMENTS_KEY, ( java.io.Serializable ) comments );
        outState.putSerializable( RESHARERS_KEY, ( java.io.Serializable ) resharers );
    }

    private Object initPlusoners( ViewGroup container )
    {
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.gplus_actions_plusoners, container, false );
        lvPlusOners = ( ListView ) v.findViewById( R.id.lv_plusoners );
        lvPlusOnersEmpty = ( TextView ) v.findViewById( R.id.lv_plusoners_empty );
        lvPlusOnersEmpty.setText( getResources().getString( R.string.str_no_plusoners ) );
        lvPlusOners.setEmptyView( lvPlusOnersEmpty );
        lvPlusOners.setAdapter( plusonersAdapter );
        lvPlusOners.setOnItemClickListener( this );
        container.addView( v );
        return v;
    }

    private Object initComments( ViewGroup container )
    {
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.gplus_actions_comments, container, false );
        lvComments = ( ListView ) v.findViewById( R.id.lv_comments );
        lvCommentsEmpty = ( TextView ) v.findViewById( R.id.lv_comments_empty );
        lvCommentsEmpty.setText( getResources().getString( R.string.str_no_replies ) );
        lvComments.setEmptyView( lvCommentsEmpty );
        lvComments.setAdapter( commentsAdapter );
        lvComments.setOnItemClickListener( this );
        container.addView( v );
        return v;
    }

    private Object initResharers( ViewGroup container )
    {
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.gplus_actions_resharers, container, false );
        lvResharers = ( ListView ) v.findViewById( R.id.lv_resharers );
        lvResharersEmpty = ( TextView ) v.findViewById( R.id.lv_resharers_empty );
        lvResharersEmpty.setText( getResources().getString( R.string.str_no_resharers ) );
        lvResharers.setEmptyView( lvResharersEmpty );
        lvResharers.setAdapter( resharersAdapter );
        lvResharers.setOnItemClickListener( this );
        container.addView( v );
        return v;
    }

    @Subscribe
    public void onListByActivityDone( PeopleFeedEvent event )
    {
        if ( event.isSuccess() )
        {
            if ( TextUtils.equals( ListByActivityParams.PLUSONERS, event.getCollection() ) )
            {
                if ( Helper.isNullOrEmpty( event.getPeopleFeed().getItems() ) )
                {
                    lvPlusOnersEmpty.setText( getResources().getString( R.string.str_no_plusoners ) );
                }
                else
                {
                    plusoners.addAll( event.getPeopleFeed().getItems() );
                    plusonersAdapter.notifyDataSetChanged();
                }
            }
            else
            {
                if ( Helper.isNullOrEmpty( event.getPeopleFeed().getItems() ) )
                {
                    lvResharersEmpty.setText( getResources().getString( R.string.str_no_resharers ) );
                }
                else
                {
                    resharers.addAll( event.getPeopleFeed().getItems() );
                    resharersAdapter.notifyDataSetChanged();
                }
            }
        }
        else
        {
            if ( TextUtils.equals( ListByActivityParams.RESHARERS, event.getCollection() ) )
            {
                lvPlusOnersEmpty.setText( getResources().getString( R.string.str_load_plusoners_error ) );
            }
            else
            {
                lvResharersEmpty.setText( getResources().getString( R.string.str_load_resharers_error ) );
            }
        }
    }

    @Subscribe
    public void onListCommentsDone( CommentFeedEvent event )
    {
        if ( event.isSuccess() )
        {
            if ( Helper.isNullOrEmpty( event.getCommentFeed().getItems() ) )
            {
                lvCommentsEmpty.setText( getResources().getString( R.string.str_no_replies ) );
            }
            else
            {
                comments.addAll( event.getCommentFeed().getItems() );
                commentsAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            lvCommentsEmpty.setText( getResources().getString( R.string.str_load_replies_error ) );
        }
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        if ( parent.equals( lvPlusOners ) || parent.equals( lvResharers ) )
        {
            Person person = ( Person ) parent.getItemAtPosition( position );
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setData( Uri.parse( person.getUrl() ) );
            startActivity( i );
        }
    }

    private class GPlusActionsPagerAdapter extends SitesActionsPagerAdapter
    {
        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public Object instantiateItem( ViewGroup container, int position )
        {
            switch ( position )
            {
                case 0:
                    return initPlusoners( container );
                case 1:
                    return initComments( container );
                case 2:
                    return initResharers( container );
            }
            return null;
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView( ( View ) object );
        }

        @Override
        public int getIconResId( int position )
        {
            switch ( position )
            {
                case 0:
                    return R.drawable.plus;
                case 1:
                    return R.drawable.comment;
                case 2:
                    return R.drawable.share;
            }
            return -1;
        }
    }

    private static class PersonsAdapter extends BaseAdapter
    {
        private List<Person> persons;

        public PersonsAdapter( List<Person> persons )
        {
            super();
            this.persons = persons;
        }

        @Override
        public int getCount()
        {
            return persons.size();
        }

        @Override
        public Object getItem( int position )
        {
            return persons.get( position );
        }

        @Override
        public long getItemId( int position )
        {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            PlusoneView view;
            if ( null == convertView )
            {
                view = new PlusoneView( parent.getContext() );
            }
            else
            {
                view = ( PlusoneView ) convertView;
            }
            view.update( persons.get( position ) );
            return view;
        }
    }

    private static class CommentsAdapter extends BaseAdapter
    {
        private List<Comment> comments;

        public CommentsAdapter( List<Comment> comments )
        {
            super();
            this.comments = comments;
        }

        @Override
        public int getCount()
        {
            return comments.size();
        }

        @Override
        public Object getItem( int position )
        {
            return comments.get( position );
        }

        @Override
        public long getItemId( int position )
        {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            GPlusCommentView view;
            if ( null == convertView )
            {
                view = new GPlusCommentView( parent.getContext() );
            }
            else
            {
                view = ( GPlusCommentView ) convertView;
            }
            view.update( comments.get( position ) );
            return view;
        }
    }

    private static class PeopleFeedCallback implements Callback<PeopleFeed>
    {
        private String collection;

        public PeopleFeedCallback( String collection )
        {
            this.collection = collection;
        }

        @Override
        public void success( PeopleFeed peopleFeed, Response response )
        {
            // Subscriber : GPlusActionsFragment : onListByActivityDone()
            HashtaggerApp.bus.post( new PeopleFeedEvent( peopleFeed, true, collection ) );
        }

        @Override
        public void failure( RetrofitError retrofitError )
        {
            // Subscriber : GPlusActionsFragment : onListByActivityDone()
            HashtaggerApp.bus.post( new PeopleFeedEvent( null, false, collection ) );
        }
    }

    private static class CommentFeedCallback implements Callback<CommentFeed>
    {
        @Override
        public void success( CommentFeed feed, Response response )
        {
            for ( Comment comment : feed.getItems() )
            {
                comment.getObject().setOriginalContent( Html.fromHtml( comment.getObject().getContent() ).toString() );
            }
            // Subscriber : GPlusActionsFragment : onListCommentsDone()
            HashtaggerApp.bus.post( new CommentFeedEvent( feed, true ) );
        }

        @Override
        public void failure( RetrofitError retrofitError )
        {
            // Subscriber : GPlusActionsFragment : onListCommentsDone()
            HashtaggerApp.bus.post( new CommentFeedEvent( null, false ) );
        }
    }
}
