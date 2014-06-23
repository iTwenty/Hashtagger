package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.WebView;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 6/18/14.
 */
public class AboutDialog extends DialogFragment
{
    public static final String TAG = AboutDialog.class.getSimpleName();
    private WebView wvAbout;

    public static AboutDialog newInstance()
    {
        AboutDialog d = new AboutDialog();
        return d;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        wvAbout = ( WebView ) getActivity().getLayoutInflater().inflate( R.layout.dialog_about, null );
        AlertDialog dialog = new AlertDialog.Builder( getActivity() )
                .setView( wvAbout )
                .setIcon( R.drawable.ic_launcher )
                .setTitle( "About" )
                .setPositiveButton( "Close", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        dialog.dismiss();
                    }
                } )
                .create();
        wvAbout.loadUrl( "file:///android_asset/about.html" );
        return dialog;
    }
}
