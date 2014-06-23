package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;

public abstract class SitesService extends Service
{
    private static final String SITES_SERVICE_NAME = HashtaggerApp.NAMESPACE + "sites_service_name";

    private volatile Looper         mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private          boolean        mRedelivery;

    private final class ServiceHandler extends Handler
    {
        public ServiceHandler( Looper looper )
        {
            super( looper );
        }

        @Override
        public void handleMessage( Message msg )
        {
            Intent intent = ( Intent ) msg.obj;
            int actionType = msg.what;
            if ( actionType == ActionType.SEARCH )
            {
                doSearch( intent );
            }
            else if ( actionType == ActionType.AUTH )
            {
                doAuth( intent );
            }
            stopSelf( msg.arg1 );
        }
    }

    public void setIntentRedelivery( boolean enabled )
    {
        mRedelivery = enabled;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        HandlerThread thread = new HandlerThread( "IntentService[" + SITES_SERVICE_NAME + "]" );
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler( mServiceLooper );
    }

    @Override
    public void onStart( Intent intent, int startId )
    {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        msg.what = intent.getIntExtra( ActionType.ACTION_TYPE_KEY, -1 );
        mServiceHandler.removeMessages( msg.what );
        mServiceHandler.sendMessage( msg );
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId )
    {
        onStart( intent, startId );
        return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        mServiceLooper.quit();
    }

    @Override
    public IBinder onBind( Intent intent )
    {
        return null;
    }

    protected abstract void doSearch( Intent intent );

    protected abstract void doAuth( Intent intent );
}
