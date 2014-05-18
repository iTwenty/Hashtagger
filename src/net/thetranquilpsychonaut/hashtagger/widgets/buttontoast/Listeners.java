package net.thetranquilpsychonaut.hashtagger.widgets.buttontoast;

import android.os.Parcelable;
import android.view.View;

/**
 * Created by itwenty on 5/17/14.
 */
public class Listeners
{
    public interface OnClickListener
    {

        public void onClick( View view, Parcelable token );

    }

    public interface OnDismissListener
    {

        public void onDismiss( View view );

    }
}
