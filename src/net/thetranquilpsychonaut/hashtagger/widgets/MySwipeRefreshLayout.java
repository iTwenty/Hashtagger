package net.thetranquilpsychonaut.hashtagger.widgets;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.SparseArray;

/**
 * Created by itwenty on 5/22/14.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout
{
    public MySwipeRefreshLayout( Context context )
    {
        super( context );
    }

    public MySwipeRefreshLayout( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState( superState, this.isRefreshing() );
    }

    @Override
    protected void onRestoreInstanceState( Parcelable state )
    {
        SavedState savedState = ( SavedState ) state;
        super.onRestoreInstanceState( savedState.getSuperState() );
        this.setRefreshing( savedState.isRefreshing() );
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
        private boolean isRefreshing;

        public SavedState( Parcel source )
        {
            super( source );
            this.isRefreshing = source.readInt() == 0 ? false : true;
        }

        public SavedState( Parcelable superState, boolean isRefreshing )
        {
            super( superState );
            this.isRefreshing = isRefreshing;
        }

        public boolean isRefreshing()
        {
            return this.isRefreshing;
        }

        @Override
        public void writeToParcel( Parcel dest, int flags )
        {
            super.writeToParcel( dest, flags );
            dest.writeInt( isRefreshing ? 1 : 0 );
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
