package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public abstract class MyIntentService extends Service
{
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

    public MyIntentService( String name )
    {
        super();
        mName = name;
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

    protected abstract void onHandleIntent( Intent intent );
}
