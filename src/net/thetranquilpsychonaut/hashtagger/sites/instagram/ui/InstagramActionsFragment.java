package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.Actions;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Comment;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.From;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesActionsFragment;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.Collections;
import java.util.List;

/**
 * Created by itwenty on 7/4/14.
 */
public class InstagramActionsFragment extends SitesActionsFragment implements AdapterView.OnItemClickListener
{
    public static final String TAG = InstagramActionsFragment.class.getSimpleName();

    private Media media;
    private int   actionType;

    private List<From>   likes;
    private ListView     lvLikes;
    private TextView     lvLikesEmpty;
    private LikesAdapter likesAdapter;

    private List<Comment>   comments;
    private ListView        lvComments;
    private TextView        lvCommentsEmpty;
    private CommentsAdapter commentsAdapter;

    public static InstagramActionsFragment newInstance( Media media, int actionType )
    {
        InstagramActionsFragment fragment = new InstagramActionsFragment();
        Bundle b = new Bundle();
        b.putSerializable( "media", media );
        b.putInt( "actionType", actionType );
        fragment.setArguments( b );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        media = ( Media ) getArguments().getSerializable( "media" );
        actionType = getArguments().getInt( "actionType" );
        likes = null == media.getLikes() || Helper.isNullOrEmpty( media.getLikes().getData() ) ?
                Collections.<From>emptyList() :
                media.getLikes().getData();
        comments = null == media.getComments() || Helper.isNullOrEmpty( media.getComments().getData() ) ?
                Collections.<Comment>emptyList() :
                media.getComments().getData();
        likesAdapter = new LikesAdapter( likes );
        commentsAdapter = new CommentsAdapter( comments );
    }

    @Override
    protected SitesActionsPagerAdapter initSitesActionsPagerAdapter()
    {
        return new InstagramActionsPagerAdaper();
    }

    @Override
    protected int getSelectedAction()
    {
        switch ( actionType )
        {
            case Actions.ACTION_INSTAGRAM_COMMENT:
                return 1;
            case Actions.ACTION_INSTAGRAM_LIKE: // fall through
            default:
                return 0;
        }
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {

    }

    private Object initLikes( ViewGroup container )
    {
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.instagram_actions_likes, container, false );
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
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.instagram_actions_comments, container, false );
        lvComments = ( ListView ) v.findViewById( R.id.lv_comments );
        lvCommentsEmpty = ( TextView ) v.findViewById( R.id.lv_comments_empty );
        lvCommentsEmpty.setText( "No comments" );
        lvComments.setEmptyView( lvCommentsEmpty );
        lvComments.setAdapter( commentsAdapter );
        lvComments.setOnItemClickListener( this );
        container.addView( v );
        return v;
    }

    private class InstagramActionsPagerAdaper extends SitesActionsPagerAdapter
    {
        @Override
        public int getCount()
        {
            return 2;
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

        @Override
        public int getIconResId( int position )
        {
            switch ( position )
            {
                case 0:
                    return R.drawable.instagram_like;
                case 1:
                    return R.drawable.comment;
            }
            return -1;
        }
    }

    private static class LikesAdapter extends BaseAdapter
    {
        private List<From> likes;

        public LikesAdapter( List<From> likes )
        {
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
            InstagramLikeView view;
            if ( null == convertView )
            {
                view = new InstagramLikeView( parent.getContext() );
            }
            else
            {
                view = ( InstagramLikeView ) convertView;
            }
            view.update( likes.get( position ) );
            return view;
        }
    }

    private static class CommentsAdapter extends BaseAdapter
    {
        private List<Comment> comments;

        public CommentsAdapter( List<Comment> comments )
        {
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
            InstagramCommentView view;
            if ( null == convertView )
            {
                view = new InstagramCommentView( parent.getContext() );
            }
            else
            {
                view = ( InstagramCommentView ) convertView;
            }
            view.update( comments.get( position ) );
            return view;
        }
    }
}
