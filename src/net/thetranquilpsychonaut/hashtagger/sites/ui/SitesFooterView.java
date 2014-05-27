package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 3/29/14.
 */
public class SitesFooterView extends FrameLayout
{
    public static final int NORMAL  = 0;
    public static final int LOADING = 1;
    public static final int ERROR   = 2;

    private int activeView = NORMAL;
    private TextView    tvFooter;
    private ProgressBar pgbrFooter;

    public SitesFooterView( Context context )
    {
        this( context, null, R.attr.sitesFooterStyle );
    }

    public SitesFooterView( Context context, AttributeSet attrs )
    {
        this( context, attrs, R.attr.sitesFooterStyle );
    }

    public SitesFooterView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.sites_footer_view, this );
        tvFooter = ( TextView ) findViewById( R.id.tv_footer );
        pgbrFooter = ( ProgressBar ) findViewById( R.id.pgbr_footer );
        showView( NORMAL );
    }

    public void showView( int activeView )
    {
        switch ( activeView )
        {
            case NORMAL:
                showNormal();
                this.activeView = activeView;
                break;
            case LOADING:
                showLoading();
                this.activeView = activeView;
                break;
            case ERROR:
                showError();
                this.activeView = activeView;
                break;
            default:
                Helper.debug( "Invalid activeView for Footer : " + activeView );
        }
    }

    public int getActiveView()
    {
        return this.activeView;
    }

    private void showLoading()
    {
        tvFooter.setVisibility( INVISIBLE );
        pgbrFooter.setVisibility( VISIBLE );
        this.setClickable( false );
        this.setEnabled( false );
    }

    private void showError()
    {
        pgbrFooter.setVisibility( INVISIBLE );
        tvFooter.setVisibility( VISIBLE );
        tvFooter.setText( getResources().getString( R.string.str_footer_error ) );
        this.setClickable( true );
        this.setEnabled( true );
    }

    private void showNormal()
    {
        pgbrFooter.setVisibility( INVISIBLE );
        tvFooter.setVisibility( VISIBLE );
        tvFooter.setText( getResources().getString( R.string.str_load_older_results ) );
        this.setClickable( true );
        this.setEnabled( true );
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState( superState, this.activeView );
    }

    @Override
    protected void onRestoreInstanceState( Parcelable state )
    {
        SavedState savedState = ( SavedState ) state;
        super.onRestoreInstanceState( savedState.getSuperState() );
        showView( savedState.getActiveView() );
    }

    @Override
    protected void dispatchSaveInstanceState( SparseArray<Parcelable> container )
    {
        super.dispatchFreezeSelfOnly( container );
    }

    @Override
    protected void dispatchRestoreInstanceState( SparseArray<Parcelable> container )
    {
        super.dispatchThawSelfOnly( container );
    }

    protected static class SavedState extends BaseSavedState
    {
        private int activeView;

        public SavedState( Parcel source )
        {
            super( source );
            this.activeView = source.readInt();
        }

        public SavedState( Parcelable superState, int activeView )
        {
            super( superState );
            this.activeView = activeView;
        }

        public int getActiveView()
        {
            return this.activeView;
        }

        @Override
        public void writeToParcel( Parcel dest, int flags )
        {
            super.writeToParcel( dest, flags );
            dest.writeInt( this.activeView );
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>()
        {
            @Override
            public SavedState createFromParcel( Parcel source )
            {
                return new SavedState( source );
            }

            @Override
            public SavedState[] newArray( int size )
            {
                return new SavedState[size];
            }
        };
    }
}
