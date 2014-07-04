package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 7/4/14.
 */
public abstract class CommentView extends RelativeLayout implements View.OnClickListener
{
    private ImageView commenterImage;
    private TextView  commenterName;
    private TextView  comment;

    public CommentView( Context context )
    {
        this( context, null, 0 );
    }

    public CommentView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public CommentView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.comment_view, this );
        commenterImage = ( ImageView ) findViewById( R.id.commenter_image );
        commenterName = ( TextView ) findViewById( R.id.commenter_name );
        comment = ( TextView ) findViewById( R.id.comment );
        comment.setOnClickListener( this );
    }

    public void update( Object result )
    {
        Picasso.with( getContext() )
                .load( getCommenterImageUrl( result ) )
                .fit()
                .centerCrop()
                .into( commenterImage );
        commenterName.setText( getCommenterName( result ) );
        comment.setText( getComment( result ) );
    }

    protected abstract String getCommenterImageUrl( Object result );

    protected abstract String getCommenterName( Object result );

    protected abstract String getComment( Object result );

    @Override
    public void onClick( View v )
    {
        if ( v.equals( comment ) )
        {
            comment.setMaxLines( Integer.MAX_VALUE );
        }
    }
}
