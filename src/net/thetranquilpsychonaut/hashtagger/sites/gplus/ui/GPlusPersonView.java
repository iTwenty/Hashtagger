package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Person;

/**
 * Created by itwenty on 6/14/14.
 */
public class GPlusPersonView extends RelativeLayout
{
    private ImageView personImage;
    private TextView  personName;
    private Person    person;

    public GPlusPersonView( Context context )
    {
        this( context, null, 0 );
    }

    public GPlusPersonView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public GPlusPersonView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.gplus_person_view, this );
        personImage = ( ImageView ) findViewById( R.id.person_image );
        personName = ( TextView ) findViewById( R.id.person_name );
    }

    public void update( Person person )
    {
        this.person = person;
        Picasso.with( getContext() )
                .load( person.getImage().getUrl() )
                .centerCrop()
                .fit()
                .into( personImage );
        personName.setText( person.getDisplayName() );
    }
}
