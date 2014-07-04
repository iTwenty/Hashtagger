package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 7/4/14.
 */
public abstract class PersonView extends RelativeLayout
{
    private ImageView personImage;
    private TextView  personName;

    public PersonView( Context context )
    {
        this( context, null, 0 );
    }

    public PersonView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public PersonView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.person_view, this );
        personImage = ( ImageView ) findViewById( R.id.person_image );
        personName = ( TextView ) findViewById( R.id.person_name );
    }

    public void update( Object result )
    {
        Picasso.with( getContext() )
                .load( getPersonImageUrl( result ) )
                .fit()
                .centerCrop()
                .into( personImage );
        personName.setText( getPersonName( result ) );
    }

    protected abstract String getPersonImageUrl( Object result );

    protected abstract String getPersonName( Object result );
}
