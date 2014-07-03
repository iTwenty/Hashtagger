package net.thetranquilpsychonaut.hashtagger.sites.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;

/**
 * Created by itwenty on 6/16/14.
 */
public abstract class SitesActionsFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        Dialog d = super.onCreateDialog( savedInstanceState );
        d.requestWindowFeature( Window.FEATURE_NO_TITLE );
        return d;
    }
}
