package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by itwenty on 6/9/14.
 */
public class TwitterDateDeserializer implements JsonDeserializer<Date>
{
    private static final String  STATUS_DATE_FORMAT     = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    private static final String  TRENDS_DATE_FORMAT     = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String  TRENDS_DATE_FORMAT_ALT = "EEE, d MMM yyyy HH:mm:ss z";
    private static final Pattern STATUS_DATE_PATTERN    = Pattern.compile( "\\w{3} \\w{3} \\d{2} \\d{2}:\\d{2}:\\d{2} \\+\\d{1,4} \\d{4}" );

    SimpleDateFormat sdf;

    public TwitterDateDeserializer()
    {
        sdf = new SimpleDateFormat();
        sdf.setLenient( true );
    }

    @Override
    public Date deserialize( JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext ) throws JsonParseException
    {
        String dateStr = jsonElement.getAsJsonPrimitive().getAsString();
        try
        {
            if ( STATUS_DATE_PATTERN.matcher( dateStr ).matches() )
            {
                return deserializeAsStatusDate( dateStr );
            }
            else
            {
                return deserializeAsTrendsDate( dateStr );
            }
        }
        catch ( ParseException e )
        {
            throw new RuntimeException( "Unexpected date format from Twitter : " + dateStr );
        }
    }

    private Date deserializeAsTrendsDate( String dateStr ) throws ParseException
    {
        switch ( dateStr.length() )
        {
            case 10:
                return new Date( Long.parseLong( dateStr ) * 1000 );
            case 20:
                sdf.applyPattern( TRENDS_DATE_FORMAT );
                return sdf.parse( dateStr );
            default:
                sdf.applyPattern( TRENDS_DATE_FORMAT_ALT );
                return sdf.parse( dateStr );
        }
    }

    private Date deserializeAsStatusDate( String dateStr ) throws ParseException
    {
        sdf.applyPattern( STATUS_DATE_FORMAT );
        return sdf.parse( dateStr );
    }
}
