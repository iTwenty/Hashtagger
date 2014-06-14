package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Comment;

/**
 * Created by itwenty on 6/15/14.
 */
public class GPlusCommentView extends RelativeLayout
{
    private ImageView actorImage;
    private TextView  actorName;
    private TextView  content;

    public GPlusCommentView( Context context )
    {
        this( context, null, 0 );
    }

    public GPlusCommentView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public GPlusCommentView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.gplus_comment_view, this );
        actorImage = ( ImageView ) findViewById( R.id.actor_image );
        actorName = ( TextView ) findViewById( R.id.actor_name );
        content = ( TextView ) findViewById( R.id.content );
    }

    public void update( Comment comment )
    {
        Picasso.with( getContext() )
                .load( comment.getActor().getImage().getUrl() )
                .fit()
                .centerCrop()
                .into( actorImage );
        actorName.setText( comment.getActor().getDisplayName() );
        content.setText( comment.getObject().getContent() );
    }
}
