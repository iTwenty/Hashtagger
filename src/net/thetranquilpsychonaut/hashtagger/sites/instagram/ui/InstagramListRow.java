package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 6/25/14.
 */
public class InstagramListRow extends SitesListRow
{
    private TextView tvMediaText;
    private Media    media;

    protected InstagramListRow( Context context )
    {
        super( context );
    }

    protected InstagramListRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected InstagramListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.instagram_list_row, this );
        tvMediaText = ( TextView ) findViewById( R.id.tv_media_text );
        super.init( context );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.instagram_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        this.media = ( Media ) result;
        tvMediaText.setText( Helper.getFuzzyDateTime( media.getCreatedTime() ) );
    }
}
