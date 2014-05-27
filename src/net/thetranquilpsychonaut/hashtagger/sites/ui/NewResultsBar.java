package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/20/14.
 */
public class NewResultsBar extends LinearLayout implements AbsListView.OnScrollListener
{
    public interface OnScrollToNewClickListener
    {
        public void onScrollToNewClicked( NewResultsBar bar, int resultCount );
    }

    private ImageView imgvScrollToNew;
    private TextView  tvNewResultsCount;
    private ImageView imgvDismissBar;
    private int count = 0;
    private OnScrollToNewClickListener listener;

    public NewResultsBar( Context context )
    {
        this( context, null, R.attr.newResultsBarStyle );
    }

    public NewResultsBar( Context context, AttributeSet attrs )
    {
        this( context, attrs, R.attr.newResultsBarStyle );
    }

    public NewResultsBar( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.new_results_bar, this );
        imgvScrollToNew = ( ImageView ) findViewById( R.id.imgv_scroll_to_new );
        tvNewResultsCount = ( TextView ) findViewById( R.id.tv_new_results_count );
        imgvDismissBar = ( ImageView ) findViewById( R.id.imgv_dismiss_bar );
        imgvDismissBar.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                NewResultsBar.this.dismissBar();
            }
        } );
        imgvScrollToNew.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                if ( null != listener )
                {
                    listener.onScrollToNewClicked( NewResultsBar.this, NewResultsBar.this.count );
                }
            }
        } );
    }

    public void setOnScrollToNewClickListener( OnScrollToNewClickListener listener )
    {
        this.listener = listener;
    }

    public void setResultsCount( int count )
    {
        this.count = count;
        tvNewResultsCount.setText( String.format( "%d New Result" + ( this.count == 1 ? "" : "s" ), this.count ) );
    }

    public int getResultsCount()
    {
        return this.count;
    }

    @Override
    public void onScrollStateChanged( AbsListView view, int scrollState )
    {
    }

    @Override
    public void onScroll( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount )
    {
        if ( getVisibility() != VISIBLE )
        {
            return;
        }
        if ( firstVisibleItem <= this.count )
        {
            setResultsCount( firstVisibleItem );
        }
        if ( firstVisibleItem == 0 )
        {
            this.dismissBar();
        }
    }

    public void dismissBar()
    {
        this.setVisibility( GONE );
        this.setResultsCount( 0 );
    }

    public void expandNewResultsText()
    {
        ObjectAnimator expandAnimator = ObjectAnimator.ofPropertyValuesHolder(
                tvNewResultsCount,
                PropertyValuesHolder.ofFloat( "scaleX", 1.5f ),
                PropertyValuesHolder.ofFloat( "scaleY", 1.5f ) );
        expandAnimator.setRepeatMode( ValueAnimator.REVERSE );
        expandAnimator.setRepeatCount( 1 );
        expandAnimator.setDuration( 500 );
        expandAnimator.start();
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState( superState, this.count, this.getVisibility() );
    }

    @Override
    protected void onRestoreInstanceState( Parcelable state )
    {
        SavedState savedState = ( SavedState ) state;
        super.onRestoreInstanceState( savedState.getSuperState() );
        setResultsCount( savedState.getCount() );
        setVisibility( savedState.getVisibility() );
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
        private int count;
        private int visibility;

        public SavedState( Parcel source )
        {
            super( source );
            this.count = source.readInt();
            this.visibility = source.readInt();
        }

        public SavedState( Parcelable superState, int count, int visibility )
        {
            super( superState );
            this.count = count;
            this.visibility = visibility;
        }

        public int getCount()
        {
            return this.count;
        }

        public int getVisibility()
        {
            return this.visibility;
        }

        @Override
        public void writeToParcel( Parcel dest, int flags )
        {
            super.writeToParcel( dest, flags );
            dest.writeInt( this.count );
            dest.writeInt( this.visibility );
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
