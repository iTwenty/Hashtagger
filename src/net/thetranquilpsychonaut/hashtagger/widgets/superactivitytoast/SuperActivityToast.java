package net.thetranquilpsychonaut.hashtagger.widgets.superactivitytoast;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;
import net.thetranquilpsychonaut.hashtagger.R;

import java.util.LinkedList;


@SuppressWarnings( { "UnusedDeclaration", "BooleanMethodIsAlwaysInverted", "ConstantConditions" } )
public class SuperActivityToast
{

    private static final String TAG         = "SuperActivityToast";
    private static final String MANAGER_TAG = "SuperActivityToast Manager";

    private static final String ERROR_ACTIVITYNULL              = " - You cannot pass a null Activity as a parameter.";
    private static final String ERROR_NOTBUTTONTYPE             = " - is only compatible with BUTTON type SuperActivityToasts.";
    private static final String ERROR_NOTPROGRESSHORIZONTALTYPE = " - is only compatible with PROGRESS_HORIZONTAL type SuperActivityToasts.";
    private static final String ERROR_NOTEITHERPROGRESSTYPE     = " - is only compatible with PROGRESS_HORIZONTAL or PROGRESS type SuperActivityToasts.";


    private static final String BUNDLE_TAG = "0x532e412e542e";

    private Activity mActivity;
    private boolean  mIsIndeterminate;
    private boolean  mIsTouchDismissible;
    private boolean  isProgressIndeterminate;
    private boolean  showImmediate;
    private Button   mButton;
    private int mDuration     = Toast.LENGTH_SHORT;
    private int mDividerColor = Color.LTGRAY;
    private int mBackground   = R.color.gplus_gray;
    private int mButtonIcon   = R.drawable.ic_action_remove_small;
    private int mIcon;
    private int mTypefaceStyle       = Typeface.NORMAL;
    private int mButtonTypefaceStyle = Typeface.BOLD;
    private LayoutInflater   mLayoutInflater;
    private LinearLayout     mRootLayout;
    private OnDismissWrapper mOnDismissWrapper;
    private OnClickWrapper   mOnClickWrapper;
    private Parcelable       mToken;
    private ProgressBar      mProgressBar;
    private String           mOnClickWrapperTag;
    private String           mOnDismissWrapperTag;
    private TextView         mMessageTextView;
    private View             mDividerView;
    private ViewGroup        mViewGroup;
    private View             mToastView;


    public SuperActivityToast( Activity activity )
    {

        if ( activity == null )
        {

            throw new IllegalArgumentException( TAG + ERROR_ACTIVITYNULL );

        }

        this.mActivity = activity;

        mLayoutInflater = ( LayoutInflater ) activity
                .getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        mViewGroup = ( ViewGroup ) activity
                .findViewById( android.R.id.content );

        mToastView = mLayoutInflater.inflate(
                R.layout.superactivitytoast_button, mViewGroup, false );

        mButton = ( Button ) mToastView
                .findViewById( R.id.button );

        mDividerView = mToastView
                .findViewById( R.id.divider );

        mButton.setOnClickListener( mButtonListener );

        mMessageTextView = ( TextView ) mToastView
                .findViewById( R.id.message_textview );

        mRootLayout = ( LinearLayout ) mToastView
                .findViewById( R.id.root_layout );

    }


    public void show()
    {

        ManagerSuperActivityToast.getInstance().add( this );

    }


    public void setText( CharSequence text )
    {

        mMessageTextView.setText( text );

    }


    public CharSequence getText()
    {

        return mMessageTextView.getText();

    }


    public void setTypefaceStyle( int typeface )
    {

        mTypefaceStyle = typeface;

        mMessageTextView.setTypeface( mMessageTextView.getTypeface(), typeface );

    }


    public int getTypefaceStyle()
    {

        return mTypefaceStyle;

    }


    public void setTextColor( int textColor )
    {

        mMessageTextView.setTextColor( textColor );

    }


    public int getTextColor()
    {

        return mMessageTextView.getCurrentTextColor();

    }


    public void setTextSize( int textSize )
    {

        mMessageTextView.setTextSize( textSize );

    }


    private void setTextSizeFloat( float textSize )
    {

        mMessageTextView.setTextSize( TypedValue.COMPLEX_UNIT_PX, textSize );

    }


    public float getTextSize()
    {

        return mMessageTextView.getTextSize();

    }

    public void setDuration( int duration )
    {

        this.mDuration = duration;

    }


    public int getDuration()
    {

        return this.mDuration;

    }


    public void setIndeterminate( boolean isIndeterminate )
    {

        this.mIsIndeterminate = isIndeterminate;

    }


    public boolean isIndeterminate()
    {

        return this.mIsIndeterminate;

    }


    public int getIconResource()
    {

        return this.mIcon;

    }

    public void setBackground( int background )
    {

        this.mBackground = background;

        mRootLayout.setBackgroundResource( background );

    }


    public int getBackground()
    {

        return this.mBackground;

    }


    public void setShowImmediate( boolean showImmediate )
    {

        this.showImmediate = showImmediate;
    }


    public boolean getShowImmediate()
    {

        return this.showImmediate;

    }


    public void setTouchToDismiss( boolean touchDismiss )
    {

        this.mIsTouchDismissible = touchDismiss;

        if ( touchDismiss )
        {

            mToastView.setOnTouchListener( mTouchDismissListener );

        }
        else
        {

            mToastView.setOnTouchListener( null );

        }

    }


    public boolean isTouchDismissible()
    {

        return this.mIsTouchDismissible;

    }


    public void setOnDismissWrapper( OnDismissWrapper onDismissWrapper )
    {

        this.mOnDismissWrapper = onDismissWrapper;
        this.mOnDismissWrapperTag = onDismissWrapper.getTag();

    }


    protected OnDismissWrapper getOnDismissWrapper()
    {

        return this.mOnDismissWrapper;

    }


    private String getOnDismissWrapperTag()
    {

        return this.mOnDismissWrapperTag;

    }


    public void dismiss()
    {

        ManagerSuperActivityToast.getInstance().removeSuperToast( this );

    }


    public void setOnClickWrapper( OnClickWrapper onClickWrapper )
    {
        this.mOnClickWrapper = onClickWrapper;
        this.mOnClickWrapperTag = onClickWrapper.getTag();

    }


    public void setOnClickWrapper( OnClickWrapper onClickWrapper, Parcelable token )
    {

        onClickWrapper.setToken( token );

        this.mToken = token;
        this.mOnClickWrapper = onClickWrapper;
        this.mOnClickWrapperTag = onClickWrapper.getTag();

    }


    private Parcelable getToken()
    {

        return this.mToken;

    }


    private String getOnClickWrapperTag()
    {

        return this.mOnClickWrapperTag;

    }

    public void setButtonIcon( int buttonIcon )
    {
        this.mButtonIcon = buttonIcon;

        if ( mButton != null )
        {

            mButton.setCompoundDrawablesWithIntrinsicBounds( mActivity
                    .getResources().getDrawable( buttonIcon ), null, null, null );

        }

    }

    public void setButtonIcon( int buttonIcon, CharSequence buttonText )
    {

        this.mButtonIcon = buttonIcon;

        if ( mButton != null )
        {

            mButton.setCompoundDrawablesWithIntrinsicBounds( mActivity
                    .getResources().getDrawable( buttonIcon ), null, null, null );

            mButton.setText( buttonText );

        }

    }

    public int getButtonIcon()
    {

        return this.mButtonIcon;

    }


    public void setDividerColor( int dividerColor )
    {

        this.mDividerColor = dividerColor;

        if ( mDividerView != null )
        {

            mDividerView.setBackgroundColor( dividerColor );

        }

    }


    public int getDividerColor()
    {

        return this.mDividerColor;

    }

    public void setButtonText( CharSequence buttonText )
    {


        if ( mButton != null )
        {

            mButton.setText( buttonText );

        }

    }

    public CharSequence getButtonText()
    {

        if ( mButton != null )
        {

            return mButton.getText();

        }

        return "";

    }

    public void setButtonTypefaceStyle( int typefaceStyle )
    {

        if ( mButton != null )
        {

            mButtonTypefaceStyle = typefaceStyle;

            mButton.setTypeface( mButton.getTypeface(), typefaceStyle );

        }

    }

    public int getButtonTypefaceStyle()
    {

        return this.mButtonTypefaceStyle;

    }

    public void setButtonTextColor( int buttonTextColor )
    {


        if ( mButton != null )
        {

            mButton.setTextColor( buttonTextColor );

        }

    }

    public int getButtonTextColor()
    {

        if ( mButton != null )
        {

            return mButton.getCurrentTextColor();

        }
        else
        {

            Log.e( TAG, "getButtonTextColor()" + ERROR_NOTBUTTONTYPE );

            return 0;

        }

    }

    public void setButtonTextSize( int buttonTextSize )
    {

        if ( mButton != null )
        {

            mButton.setTextSize( buttonTextSize );

        }

    }


    private void setButtonTextSizeFloat( float buttonTextSize )
    {

        mButton.setTextSize( TypedValue.COMPLEX_UNIT_PX, buttonTextSize );

    }

    public float getButtonTextSize()
    {

        if ( mButton != null )
        {

            return mButton.getTextSize();

        }
        else
        {

            Log.e( TAG, "getButtonTextSize()" + ERROR_NOTBUTTONTYPE );

            return 0.0f;

        }

    }

    public void setProgress( int progress )
    {

        if ( mProgressBar != null )
        {

            mProgressBar.setProgress( progress );

        }

    }

    public int getProgress()
    {

        if ( mProgressBar != null )
        {

            return mProgressBar.getProgress();

        }
        else
        {

            Log.e( TAG, "getProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE );

            return 0;

        }

    }

    public void setMaxProgress( int maxProgress )
    {

        if ( mProgressBar != null )
        {

            mProgressBar.setMax( maxProgress );

        }

    }

    public int getMaxProgress()
    {

        if ( mProgressBar != null )
        {

            return mProgressBar.getMax();

        }
        else
        {

            Log.e( TAG, "getMaxProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE );

            return 0;

        }

    }

    public void setProgressIndeterminate( boolean isIndeterminate )
    {

        this.isProgressIndeterminate = isIndeterminate;

        if ( mProgressBar != null )
        {

            mProgressBar.setIndeterminate( isIndeterminate );

        }

    }

    public boolean getProgressIndeterminate()
    {

        return this.isProgressIndeterminate;

    }


    public TextView getTextView()
    {

        return mMessageTextView;

    }


    public View getView()
    {

        return mToastView;

    }


    public boolean isShowing()
    {

        return mToastView != null && mToastView.isShown();

    }


    public Activity getActivity()
    {

        return mActivity;

    }


    public ViewGroup getViewGroup()
    {

        return mViewGroup;

    }


    private LinearLayout getRootLayout()
    {

        return mRootLayout;

    }


    public static SuperActivityToast create( Activity activity, CharSequence textCharSequence, int durationInteger )
    {

        final SuperActivityToast superActivityToast = new SuperActivityToast( activity );
        superActivityToast.setText( textCharSequence );
        superActivityToast.setDuration( durationInteger );

        return superActivityToast;

    }


    public static void cancelAllSuperActivityToasts()
    {

        ManagerSuperActivityToast.getInstance().cancelAllSuperActivityToasts();

    }


    public static void clearSuperActivityToastsForActivity( Activity activity )
    {

        ManagerSuperActivityToast.getInstance()
                .cancelAllSuperActivityToastsForActivity( activity );

    }


    public static void onSaveState( Bundle bundle )
    {

        ReferenceHolder[] list = new ReferenceHolder[ManagerSuperActivityToast
                .getInstance().getList().size()];

        LinkedList<SuperActivityToast> lister = ManagerSuperActivityToast
                .getInstance().getList();

        for ( int i = 0; i < list.length; i++ )
        {

            list[i] = new ReferenceHolder( lister.get( i ) );

        }

        bundle.putParcelableArray( BUNDLE_TAG, list );

        SuperActivityToast.cancelAllSuperActivityToasts();

    }


    public static void onRestoreState( Bundle bundle, Activity activity )
    {

        if ( bundle == null )
        {

            return;
        }

        Parcelable[] savedArray = bundle.getParcelableArray( BUNDLE_TAG );

        int i = 0;

        if ( savedArray != null )
        {

            for ( Parcelable parcelable : savedArray )
            {

                i++;

                new SuperActivityToast( activity, ( ReferenceHolder ) parcelable, null, i );

            }

        }

    }


    public static void onRestoreState( Bundle bundle, Activity activity, Wrappers wrappers )
    {

        if ( bundle == null )
        {

            return;
        }

        Parcelable[] savedArray = bundle.getParcelableArray( BUNDLE_TAG );

        int i = 0;

        if ( savedArray != null )
        {

            for ( Parcelable parcelable : savedArray )
            {

                i++;

                new SuperActivityToast( activity, ( ReferenceHolder ) parcelable, wrappers, i );

            }

        }

    }


    private SuperActivityToast( Activity activity, ReferenceHolder referenceHolder, Wrappers wrappers, int position )
    {

        SuperActivityToast superActivityToast;
        superActivityToast = new SuperActivityToast( activity );
        superActivityToast.setButtonText( referenceHolder.mButtonText );
        superActivityToast.setButtonTextSizeFloat( referenceHolder.mButtonTextSize );
        superActivityToast.setButtonTextColor( referenceHolder.mButtonTextColor );
        superActivityToast.setButtonIcon( referenceHolder.mButtonIcon );
        superActivityToast.setDividerColor( referenceHolder.mDivider );
        superActivityToast.setButtonTypefaceStyle( referenceHolder.mButtonTypefaceStyle );

        int screenSize = activity.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;


        if ( screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE )
        {

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );

            layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            layoutParams.bottomMargin = ( int ) activity.getResources().getDimension( R.dimen.content_padding );
            layoutParams.rightMargin = ( int ) activity.getResources().getDimension( R.dimen.content_padding );
            layoutParams.leftMargin = ( int ) activity.getResources().getDimension( R.dimen.content_padding );

            superActivityToast.getRootLayout().setLayoutParams( layoutParams );


            if ( wrappers != null )
            {

                for ( OnClickWrapper onClickWrapper : wrappers.getOnClickWrappers() )
                {

                    if ( onClickWrapper.getTag().equalsIgnoreCase( referenceHolder.mClickListenerTag ) )
                    {

                        superActivityToast.setOnClickWrapper( onClickWrapper, referenceHolder.mToken );

                    }

                }
            }

        }


        if ( wrappers != null )
        {

            for ( OnDismissWrapper onDismissWrapper : wrappers.getOnDismissWrappers() )
            {

                if ( onDismissWrapper.getTag().equalsIgnoreCase( referenceHolder.mDismissListenerTag ) )
                {

                    superActivityToast.setOnDismissWrapper( onDismissWrapper );

                }

            }
        }

        superActivityToast.setText( referenceHolder.mText );
        superActivityToast.setTypefaceStyle( referenceHolder.mTypefaceStyle );
        superActivityToast.setDuration( referenceHolder.mDuration );
        superActivityToast.setTextColor( referenceHolder.mTextColor );
        superActivityToast.setTextSizeFloat( referenceHolder.mTextSize );
        superActivityToast.setIndeterminate( referenceHolder.mIsIndeterminate );
        superActivityToast.setBackground( referenceHolder.mBackground );
        superActivityToast.setTouchToDismiss( referenceHolder.mIsTouchDismissible );


        if ( position == 1 )
        {

            superActivityToast.setShowImmediate( true );

        }

        superActivityToast.show();

    }


    private OnTouchListener mTouchDismissListener = new OnTouchListener()
    {

        int timesTouched;

        @Override
        public boolean onTouch( View view, MotionEvent motionEvent )
        {


            if ( timesTouched == 0 )
            {

                if ( motionEvent.getAction() == MotionEvent.ACTION_DOWN )
                {

                    dismiss();

                }

            }

            timesTouched++;

            return false;

        }

    };


    private View.OnClickListener mButtonListener = new View.OnClickListener()
    {

        @Override
        public void onClick( View view )
        {

            if ( mOnClickWrapper != null )
            {

                mOnClickWrapper.onClick( view, mToken );

            }

            dismiss();


            mButton.setClickable( false );

        }
    };


    private static class ReferenceHolder implements Parcelable
    {

        boolean    mIsIndeterminate;
        boolean    mIsTouchDismissible;
        float      mTextSize;
        float      mButtonTextSize;
        int        mDuration;
        int        mTextColor;
        int        mIcon;
        int        mBackground;
        int        mTypefaceStyle;
        int        mButtonTextColor;
        int        mButtonIcon;
        int        mDivider;
        int        mButtonTypefaceStyle;
        Parcelable mToken;
        String     mText;
        String     mButtonText;
        String     mClickListenerTag;
        String     mDismissListenerTag;

        public ReferenceHolder( SuperActivityToast superActivityToast )
        {

            mButtonText = superActivityToast.getButtonText().toString();
            mButtonTextSize = superActivityToast.getButtonTextSize();
            mButtonTextColor = superActivityToast.getButtonTextColor();
            mButtonIcon = superActivityToast.getButtonIcon();
            mDivider = superActivityToast.getDividerColor();
            mClickListenerTag = superActivityToast.getOnClickWrapperTag();
            mButtonTypefaceStyle = superActivityToast.getButtonTypefaceStyle();
            mToken = superActivityToast.getToken();


            if ( superActivityToast.getIconResource() != 0 )
            {

                mIcon = superActivityToast.getIconResource();

            }

            mDismissListenerTag = superActivityToast.getOnDismissWrapperTag();
            mText = superActivityToast.getText().toString();
            mTypefaceStyle = superActivityToast.getTypefaceStyle();
            mDuration = superActivityToast.getDuration();
            mTextColor = superActivityToast.getTextColor();
            mTextSize = superActivityToast.getTextSize();
            mIsIndeterminate = superActivityToast.isIndeterminate();
            mBackground = superActivityToast.getBackground();
            mIsTouchDismissible = superActivityToast.isTouchDismissible();

        }

        public ReferenceHolder( Parcel parcel )
        {

            mButtonText = parcel.readString();
            mButtonTextSize = parcel.readFloat();
            mButtonTextColor = parcel.readInt();
            mButtonIcon = parcel.readInt();
            mDivider = parcel.readInt();
            mButtonTypefaceStyle = parcel.readInt();
            mClickListenerTag = parcel.readString();
            mToken = parcel.readParcelable( ( ( Object ) this ).getClass().getClassLoader() );


            boolean hasIcon = parcel.readByte() != 0;

            if ( hasIcon )
            {

                mIcon = parcel.readInt();

            }

            mDismissListenerTag = parcel.readString();
            mText = parcel.readString();
            mTypefaceStyle = parcel.readInt();
            mDuration = parcel.readInt();
            mTextColor = parcel.readInt();
            mTextSize = parcel.readFloat();
            mIsIndeterminate = parcel.readByte() != 0;
            mBackground = parcel.readInt();
            mIsTouchDismissible = parcel.readByte() != 0;

        }


        @Override
        public void writeToParcel( Parcel parcel, int i )
        {

            parcel.writeString( mButtonText );
            parcel.writeFloat( mButtonTextSize );
            parcel.writeInt( mButtonTextColor );
            parcel.writeInt( mButtonIcon );
            parcel.writeInt( mDivider );
            parcel.writeInt( mButtonTypefaceStyle );
            parcel.writeString( mClickListenerTag );
            parcel.writeParcelable( mToken, 0 );


            if ( mIcon != 0 )
            {

                parcel.writeByte( ( byte ) 1 );

                parcel.writeInt( mIcon );

            }
            else
            {

                parcel.writeByte( ( byte ) 0 );

            }

            parcel.writeString( mDismissListenerTag );
            parcel.writeString( mText );
            parcel.writeInt( mTypefaceStyle );
            parcel.writeInt( mDuration );
            parcel.writeInt( mTextColor );
            parcel.writeFloat( mTextSize );
            parcel.writeByte( ( byte ) ( mIsIndeterminate ? 1 : 0 ) );
            parcel.writeInt( mBackground );
            parcel.writeByte( ( byte ) ( mIsTouchDismissible ? 1 : 0 ) );

        }

        @Override
        public int describeContents()
        {

            return 0;

        }

        public static final Parcelable.Creator CREATOR = new Creator()
        {

            public ReferenceHolder createFromParcel( Parcel parcel )
            {

                return new ReferenceHolder( parcel );

            }

            public ReferenceHolder[] newArray( int size )
            {

                return new ReferenceHolder[size];

            }

        };

    }

}