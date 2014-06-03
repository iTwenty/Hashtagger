package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;

public abstract class SitesService extends Service implements SearchActionName, LoginActionName
{
    private static final String SITES_SERVICE_NAME = HashtaggerApp.NAMESPACE + "sites_service_name";

    private volatile Looper         mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private          String         mName;
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
            onHandleIntent( ( Intent ) msg.obj );
            stopSelf( msg.arg1 );
        }
    }

    public SitesService()
    {
        super();
        mName = SITES_SERVICE_NAME;
    }

    public void setIntentRedelivery( boolean enabled )
    {
        mRedelivery = enabled;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        HandlerThread thread = new HandlerThread( "IntentService[" + mName + "]" );
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
        mServiceHandler.removeCallbacksAndMessages( null );
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

    protected void onHandleIntent( Intent intent )
    {
        int actionType = intent.getIntExtra( ActionType.ACTION_TYPE_KEY, -1 );
        Intent resultIntent = new Intent();
        if ( actionType == ActionType.SEARCH )
        {
            resultIntent = doSearch( intent );
            resultIntent.setAction( getSearchActionName() );
        }
        else if ( actionType == ActionType.AUTH )
        {
            resultIntent = doAuth( intent );
            resultIntent.setAction( getLoginActionName() );
        }
        resultIntent.addCategory( Intent.CATEGORY_DEFAULT );
        sendBroadcast( resultIntent );
    }

    protected abstract Intent doSearch( Intent intent );

    protected abstract Intent doAuth( Intent intent );
}
