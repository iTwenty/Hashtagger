package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.FacebookActionClickedEvent;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Comment;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.From;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.Collections;
import java.util.List;

/**
 * Created by itwenty on 6/13/14.
 */
public class FacebookActionsFragment extends DialogFragment implements AdapterView.OnItemClickListener
{
    public static final String TAG = "FacebookActionsFragment";

    private ViewPager                   facebookActionsPager;
    private FacebookActionsPagerAdapter facebookActionsPagerAdapter;
    private Post                        post;
    private int                         actionType;

    private List<From>   likes;
    private ListView     lvLikes;
    private TextView     lvLikesEmpty;
    private LikesAdapter likesAdapter;

    private List<Comment>   comments;
    private ListView        lvComments;
    private TextView        lvCommentsEmpty;
    private CommentsAdapter commentsAdapter;

    public static final FacebookActionsFragment newInstance( Post post, int actionType )
    {
        FacebookActionsFragment f = new FacebookActionsFragment();
        Bundle b = new Bundle();
        b.putSerializable( "post", post );
        b.putInt( "actionType", actionType );
        f.setArguments( b );
        return f;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        post = ( Post ) getArguments().getSerializable( "post" );
        actionType = getArguments().getInt( "actionType" );
        facebookActionsPagerAdapter = new FacebookActionsPagerAdapter();
        likes = null == post.getLikes() || Helper.isNullOrEmpty( post.getLikes().getData() ) ?
                Collections.<From>emptyList() :
                post.getLikes().getData();
        comments = null == post.getComments() || Helper.isNullOrEmpty( post.getComments().getData() ) ?
                Collections.<Comment>emptyList() :
                post.getComments().getData();
        likesAdapter = new LikesAdapter( likes );
        commentsAdapter = new CommentsAdapter( comments );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        facebookActionsPager = ( ViewPager ) inflater.inflate( R.layout.fragment_facebook_actions, container, false );
        facebookActionsPager.setAdapter( facebookActionsPagerAdapter );
        getDialog().requestWindowFeature( Window.FEATURE_NO_TITLE );
        if ( actionType == FacebookActionClickedEvent.ACTION_COMMENT )
        {
            facebookActionsPager.setCurrentItem( 1 );
        }
        return facebookActionsPager;
    }

    private Object initLikes( ViewGroup container )
    {
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.facebook_actions_likes, container, false );
        lvLikes = ( ListView ) v.findViewById( R.id.lv_likes );
        lvLikesEmpty = ( TextView ) v.findViewById( R.id.lv_likes_empty );
        lvLikesEmpty.setText( "No likes" );
        lvLikes.setEmptyView( lvLikesEmpty );
        lvLikes.setAdapter( likesAdapter );
        lvLikes.setOnItemClickListener( this );
        container.addView( v );
        return v;
    }

    private Object initComments( ViewGroup container )
    {
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.facebook_actions_comments, container, false );
        lvComments = ( ListView ) v.findViewById( R.id.lv_comments );
        lvCommentsEmpty = ( TextView ) v.findViewById( R.id.lv_comments_empty );
        lvCommentsEmpty.setText( "No comments" );
        lvComments.setEmptyView( lvCommentsEmpty );
        lvComments.setAdapter( commentsAdapter );
        lvComments.setOnItemClickListener( this );
        container.addView( v );
        return v;
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        String idStr;
        if ( parent.equals( lvLikes ) )
        {
            idStr = ( ( From ) parent.getItemAtPosition( position ) ).getId();
        }
        else
        {
            idStr = ( ( Comment ) parent.getItemAtPosition( position ) ).getId();
        }
        Intent i = new Intent( Intent.ACTION_VIEW );
        i.setData( Uri.parse( "https://facebook.com/" + idStr ) );
        startActivity( i );
    }

    private class FacebookActionsPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public boolean isViewFromObject( View view, Object o )
        {
            return view == o;
        }

        @Override
        public CharSequence getPageTitle( int position )
        {
            switch ( position )
            {
                case 0:
                    return "Likes";
                case 1:
                    return "Comments";
            }
            return null;
        }

        @Override
        public Object instantiateItem( ViewGroup container, int position )
        {
            switch ( position )
            {
                case 0:
                    return initLikes( container );
                case 1:
                    return initComments( container );
            }
            return null;
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView( ( View ) object );
        }
    }

    private static class LikesAdapter extends BaseAdapter
    {
        private List<From> likes;

        public LikesAdapter( List<From> likes )
        {
            super();
            this.likes = likes;
        }

        @Override
        public int getCount()
        {
            return likes.size();
        }

        @Override
        public Object getItem( int position )
        {
            return likes.get( position );
        }

        @Override
        public long getItemId( int position )
        {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            FacebookLikeView view;
            if ( null == convertView )
            {
                view = new FacebookLikeView( parent.getContext() );
            }
            else
            {
                view = ( FacebookLikeView ) convertView;
            }
            view.update( ( From ) getItem( position ) );
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
            FacebookCommentView view;
            if ( null == convertView )
            {
                view = new FacebookCommentView( parent.getContext() );
            }
            else
            {
                view = ( FacebookCommentView ) convertView;
            }
            view.update( ( Comment ) getItem( position ) );
            return view;
        }
    }
}
