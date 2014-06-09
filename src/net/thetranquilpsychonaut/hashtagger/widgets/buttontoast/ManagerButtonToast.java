package net.thetranquilpsychonaut.hashtagger.widgets.buttontoast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.LinkedList;

class ManagerButtonToast extends Handler
{
    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "ManagerButtonToast";

    private static final class Messages
    {
        private static final int DISPLAY = 0x44534154;
        private static final int REMOVE  = 0x52534154;
    }

    private static ManagerButtonToast mManagerButtonToast;

    private final LinkedList<ButtonToast> mList;

    private ManagerButtonToast()
    {
        mList = new LinkedList<ButtonToast>();
    }

    protected static synchronized ManagerButtonToast getInstance()
    {
        if ( mManagerButtonToast != null )
        {
            return mManagerButtonToast;
        }
        else
        {
            mManagerButtonToast = new ManagerButtonToast();
            return mManagerButtonToast;
        }
    }

    void add( ButtonToast buttonToast )
    {
        mList.add( buttonToast );
        this.showNextSuperToast();
    }

    private void showNextSuperToast()
    {
        final ButtonToast buttonToast = mList.peek();
        if ( mList.isEmpty() || buttonToast.getActivity() == null )
        {
            return;
        }

        if ( !buttonToast.isShowing() )
        {
            final Message message = obtainMessage( Messages.DISPLAY );
            message.obj = buttonToast;
            sendMessage( message );
        }
    }

    @Override
    public void handleMessage( Message message )
    {
        final ButtonToast buttonToast = ( ButtonToast ) message.obj;
        switch ( message.what )
        {
            case Messages.DISPLAY:
                displaySuperToast( buttonToast );
                break;
            case Messages.REMOVE:
                removeSuperToast( buttonToast );
                break;
            default:
            {
                super.handleMessage( message );
                break;
            }
        }
    }

    private void displaySuperToast( ButtonToast buttonToast )
    {
        if ( buttonToast.isShowing() )
        {
            return;
        }
        final ViewGroup viewGroup = buttonToast.getViewGroup();
        final View toastView = buttonToast.getView();
        if ( viewGroup != null )
        {
            try
            {
                toastView.setAlpha( 0f );
                viewGroup.addView( toastView );
                toastView.animate().alpha( 1f ).setDuration( 500 ).start();
            }
            catch ( IllegalStateException e )
            {
                this.cancelAllButtonToastsForActivity( buttonToast.getActivity() );
            }
        }

        if ( !buttonToast.isIndeterminate() )
        {
            Message message = obtainMessage( Messages.REMOVE );
            message.obj = buttonToast;
            sendMessageDelayed( message, buttonToast.getDuration() );
        }
    }

    void removeSuperToast( final ButtonToast buttonToast )
    {
        if ( !buttonToast.isShowing() )
        {
            buttonToast.getView().animate().alpha( 0f ).setDuration( 500 ).setListener( new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd( Animator animation )
                {
                    mList.remove( buttonToast );
                }
            } ).start();
            return;
        }

        removeMessages( Messages.REMOVE, buttonToast );
        final ViewGroup viewGroup = buttonToast.getViewGroup();
        final View toastView = buttonToast.getView();
        if ( viewGroup != null )
        {
            viewGroup.removeView( toastView );
            mList.poll();
        }
    }

    void cancelAllButtonToasts()
    {
        removeMessages( Messages.DISPLAY );
        removeMessages( Messages.REMOVE );
        for ( ButtonToast buttonToast : mList )
        {
            if ( buttonToast.isShowing() )
            {
                buttonToast.getViewGroup().removeView( buttonToast.getView() );
                buttonToast.getViewGroup().invalidate();
            }
        }
        mList.clear();
    }

    void cancelAllButtonToastsForActivity( Activity activity )
    {
        Iterator<ButtonToast> buttonToastIterator = mList.iterator();
        while ( buttonToastIterator.hasNext() )
        {
            ButtonToast buttonToast = buttonToastIterator.next();
            if ( ( buttonToast.getActivity() ) != null && buttonToast.getActivity().equals( activity ) )
            {
                if ( buttonToast.isShowing() )
                {
                    buttonToast.getViewGroup().removeView( buttonToast.getView() );
                }
                removeMessages( Messages.DISPLAY, buttonToast );
                removeMessages( Messages.REMOVE, buttonToast );
                buttonToastIterator.remove();
            }
        }
    }

    LinkedList<ButtonToast> getList()
    {
        return mList;
    }
}
