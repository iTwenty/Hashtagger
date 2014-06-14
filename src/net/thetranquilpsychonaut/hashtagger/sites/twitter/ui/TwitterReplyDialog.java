package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterAction;

/**
 * Created by itwenty on 5/11/14.
 */
public class TwitterReplyDialog extends DialogFragment implements TextWatcher, View.OnFocusChangeListener
{
    public static final String TAG = "twitter_reply_dialog";
    String   inReplyToScreenName;
    String   inReplyToStatusId;
    EditText edtReplyText;
    TextView tvCharCounter;
    TextView tvReplyProgress;

    public static TwitterReplyDialog newInstance( String inReplyToScreenName, String inReplyToStatusId )
    {
        TwitterReplyDialog dialog = new TwitterReplyDialog();
        Bundle args = new Bundle();
        args.putString( "reply_to_screen_name", inReplyToScreenName );
        args.putString( "reply_to_status_id", inReplyToStatusId );
        dialog.setArguments( args );
        return dialog;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        this.inReplyToScreenName = getArguments().getString( "reply_to_screen_name" );
        this.inReplyToStatusId = getArguments().getString( "reply_to_status_id" );
        View layout = getActivity().getLayoutInflater().inflate( R.layout.dialog_twitter_reply, null );
        AlertDialog dialog = new AlertDialog.Builder( getActivity() )
                .setView( layout )
                .setIcon( android.R.drawable.ic_menu_edit )
                .setTitle( getResources().getString( R.string.str_reply ) )
                .setPositiveButton( getResources().getString( R.string.str_reply ), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        doReply();
                    }
                } )
                .setNegativeButton( getResources().getString( R.string.str_cancel ), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        TwitterReplyDialog.this.getDialog().cancel();
                    }
                } )
                .create();

        edtReplyText = ( EditText ) layout.findViewById( R.id.edt_reply_text );
        tvCharCounter = ( TextView ) layout.findViewById( R.id.tv_char_counter );
        tvReplyProgress = ( TextView ) layout.findViewById( R.id.tv_reply_progress );
        edtReplyText.addTextChangedListener( this );
        edtReplyText.setText( "@" + this.inReplyToScreenName + " " );
        edtReplyText.setSelection( edtReplyText.getText().length() );
        edtReplyText.setOnFocusChangeListener( this );
        return dialog;
    }

    public void doReply()
    {
        new TwitterAction().executeReplyAction( edtReplyText.getText().toString(), this.inReplyToStatusId );
    }

    @Override
    public void beforeTextChanged( CharSequence s, int start, int count, int after )
    {

    }

    @Override
    public void onTextChanged( CharSequence s, int start, int before, int count )
    {

    }

    @Override
    public void afterTextChanged( Editable s )
    {
        tvCharCounter.setText( s.length() + "/140" );
    }

    @Override
    public void onFocusChange( View v, boolean hasFocus )
    {
        if ( v.equals( edtReplyText ) && hasFocus )
        {
            getDialog().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE );
        }
    }
}
