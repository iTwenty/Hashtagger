package net.thetranquilpsychonaut.hashtagger.widgets.buttontoast;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;
import net.thetranquilpsychonaut.hashtagger.R;

import java.util.LinkedList;


@SuppressWarnings({ "UnusedDeclaration", "BooleanMethodIsAlwaysInverted", "ConstantConditions" })
public class ButtonToast
{
    private static final String TAG         = "ButtonToast";
    private static final String MANAGER_TAG = "ButtonToast Manager";

    private static final String ERROR_ACTIVITYNULL = " - You cannot pass a null Activity as a parameter.";

    private static final String BUNDLE_TAG = "0x532e412e542e";

    private int mDuration            = Toast.LENGTH_SHORT;
    private int mDividerColor        = Color.LTGRAY;
    private int mBackground          = android.R.color.black;
    private int mButtonIcon          = R.drawable.icon_dark_undo;
    private int mTypefaceStyle       = Typeface.NORMAL;
    private int mButtonTypefaceStyle = Typeface.BOLD;

    private Activity         mActivity;
    private boolean          mIsIndeterminate;
    private boolean          mIsTouchDismissible;
    private boolean          showImmediate;
    private Button           mButton;
    private int              mIcon;
    private LayoutInflater   mLayoutInflater;
    private LinearLayout     mRootLayout;
    private OnDismissWrapper mOnDismissWrapper;
    private OnClickWrapper   mOnClickWrapper;
    private Parcelable       mToken;
    private String           mOnClickWrapperTag;
    private String           mOnDismissWrapperTag;
    private TextView         mMessageTextView;
    private View             mDividerView;
    private ViewGroup        mViewGroup;
    private View             mToastView;


    public ButtonToast( Activity activity )
    {
        if ( activity == null )
        {
            throw new IllegalArgumentException( TAG + ERROR_ACTIVITYNULL );
        }
        this.mActivity = activity;
        mLayoutInflater = ( LayoutInflater ) activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        mViewGroup = ( ViewGroup ) activity.findViewById( android.R.id.content );
        mToastView = mLayoutInflater.inflate(R.layout.button_toast, mViewGroup, false );
        mButton = ( Button ) mToastView.findViewById( R.id.button );
        mDividerView = mToastView.findViewById( R.id.divider );
        mButton.setOnClickListener( mButtonListener );
        mMessageTextView = ( TextView ) mToastView.findViewById( R.id.message_textview );
        mRootLayout = ( LinearLayout ) mToastView.findViewById( R.id.root_layout );
    }

    public void show()
    {
        ManagerButtonToast.getInstance().add( this );
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
        ManagerButtonToast.getInstance().removeSuperToast( this );
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
        return 0;
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
        return 0f;
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

    public static ButtonToast create( Activity activity, CharSequence textCharSequence, int durationInteger )
    {
        final ButtonToast buttonToast = new ButtonToast( activity );
        buttonToast.setText( textCharSequence );
        buttonToast.setDuration( durationInteger );
        return buttonToast;
    }

    public static void cancelAllButtonToasts()
    {
        ManagerButtonToast.getInstance().cancelAllButtonToasts();
    }

    public static void clearButtonToastsForActivity( Activity activity )
    {
        ManagerButtonToast.getInstance()
                .cancelAllButtonToastsForActivity( activity );
    }

    public static void onSaveState( Bundle bundle )
    {
        ReferenceHolder[] list = new ReferenceHolder[ManagerButtonToast.getInstance().getList().size()];
        LinkedList<ButtonToast> lister = ManagerButtonToast.getInstance().getList();

        for ( int i = 0; i < list.length; i++ )
        {
            list[i] = new ReferenceHolder( lister.get( i ) );
        }

        bundle.putParcelableArray( BUNDLE_TAG, list );
        ButtonToast.cancelAllButtonToasts();
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
                new ButtonToast( activity, ( ReferenceHolder ) parcelable, null, i );
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
                new ButtonToast( activity, ( ReferenceHolder ) parcelable, wrappers, i );
            }
        }
    }

    private ButtonToast( Activity activity, ReferenceHolder referenceHolder, Wrappers wrappers, int position )
    {
        ButtonToast buttonToast;
        buttonToast = new ButtonToast( activity );
        buttonToast.setButtonText( referenceHolder.mButtonText );
        buttonToast.setButtonTextSizeFloat( referenceHolder.mButtonTextSize );
        buttonToast.setButtonTextColor( referenceHolder.mButtonTextColor );
        buttonToast.setButtonIcon( referenceHolder.mButtonIcon );
        buttonToast.setDividerColor( referenceHolder.mDivider );
        buttonToast.setButtonTypefaceStyle( referenceHolder.mButtonTypefaceStyle );

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
            buttonToast.getRootLayout().setLayoutParams( layoutParams );

            if ( wrappers != null )
            {
                for ( OnClickWrapper onClickWrapper : wrappers.getOnClickWrappers() )
                {
                    if ( onClickWrapper.getTag().equalsIgnoreCase( referenceHolder.mClickListenerTag ) )
                    {
                        buttonToast.setOnClickWrapper( onClickWrapper, referenceHolder.mToken );
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
                    buttonToast.setOnDismissWrapper( onDismissWrapper );
                }
            }
        }

        buttonToast.setText( referenceHolder.mText );
        buttonToast.setTypefaceStyle( referenceHolder.mTypefaceStyle );
        buttonToast.setDuration( referenceHolder.mDuration );
        buttonToast.setTextColor( referenceHolder.mTextColor );
        buttonToast.setTextSizeFloat( referenceHolder.mTextSize );
        buttonToast.setIndeterminate( referenceHolder.mIsIndeterminate );
        buttonToast.setBackground( referenceHolder.mBackground );
        buttonToast.setTouchToDismiss( referenceHolder.mIsTouchDismissible );

        if ( position == 1 )
        {
            buttonToast.setShowImmediate( true );
        }

        buttonToast.show();
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

        public ReferenceHolder( ButtonToast buttonToast )
        {
            mButtonText = buttonToast.getButtonText().toString();
            mButtonTextSize = buttonToast.getButtonTextSize();
            mButtonTextColor = buttonToast.getButtonTextColor();
            mButtonIcon = buttonToast.getButtonIcon();
            mDivider = buttonToast.getDividerColor();
            mClickListenerTag = buttonToast.getOnClickWrapperTag();
            mButtonTypefaceStyle = buttonToast.getButtonTypefaceStyle();
            mToken = buttonToast.getToken();

            if ( buttonToast.getIconResource() != 0 )
            {
                mIcon = buttonToast.getIconResource();
            }

            mDismissListenerTag = buttonToast.getOnDismissWrapperTag();
            mText = buttonToast.getText().toString();
            mTypefaceStyle = buttonToast.getTypefaceStyle();
            mDuration = buttonToast.getDuration();
            mTextColor = buttonToast.getTextColor();
            mTextSize = buttonToast.getTextSize();
            mIsIndeterminate = buttonToast.isIndeterminate();
            mBackground = buttonToast.getBackground();
            mIsTouchDismissible = buttonToast.isTouchDismissible();
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