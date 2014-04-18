package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;

import java.net.URL;

/**
 * Created by itwenty on 4/18/14.
 */
public class FacebookDetailView extends RelativeLayout
{
    private ImageView imgvPicture;
    private TextView tvName;
    private TextView tvDescription;
    private TextView tvCaption;

    public FacebookDetailView( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookDetailView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookDetailView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        LayoutInflater.from( context ).inflate( R.layout.facebook_detail_view, this );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        tvName = ( TextView ) findViewById( R.id.tv_name );
        tvDescription = ( TextView ) findViewById( R.id.tv_description );
        tvCaption = ( TextView ) findViewById( R.id.tv_caption );
    }

    public void showDetailsFromPost( Post post )
    {
        UrlImageViewHelper.setUrlDrawable( imgvPicture, post.getPicture().toString(), getResources().getDrawable( R.drawable.drawable_image_loading ) );
        tvName.setText( post.getName() );
        tvDescription.setText( post.getDescription() );
        tvCaption.setText( post.getCaption() );
    }
}
