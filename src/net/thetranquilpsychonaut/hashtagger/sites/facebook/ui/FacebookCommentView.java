package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Comment;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;

/**
 * Created by itwenty on 6/14/14.
 */
public class FacebookCommentView extends RelativeLayout
{
    private ImageView fromImage;
    private TextView  fromName;
    private TextView  message;

    public FacebookCommentView( Context context )
    {
        super( context );
    }

    public FacebookCommentView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public FacebookCommentView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_comment_view, this );
        fromImage = ( ImageView ) findViewById( R.id.from_image );
        fromName = ( TextView ) findViewById( R.id.from_name );
        message = ( TextView ) findViewById( R.id.message );
    }

    public void update( Comment comment )
    {
        Picasso.with( getContext() )
                .load( UrlModifier.getFacebookProfilePictureUrl( comment.getFrom().getId() ) )
                .fit()
                .centerCrop()
                .into( fromImage );
        fromName.setText( comment.getFrom().getName() );
        message.setText( comment.getMessage() );
    }
}
