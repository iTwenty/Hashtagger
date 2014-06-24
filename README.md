Hashtagger is an Android application that allows you to search your favorite social networks for your favorite topics from one single place.

[Play Store link](https://google.com/)

# Instructions

This application was developed on Intellij IDEA. I don't know whether importing to Eclipse works or not.

To get it running, you first need to configure the app on Twitter, Google+ and Facebook developer pages.
When done properly, you should have the following keys with you:


- Twitter
  - Consumer Key
  - Consumer Secret
- Google+
  - Client ID
  - Client Secret
- Facebook
  - App ID
  - App Secret
  

Once you have these, create a folder called `config` under `src/net/thetranquilpsychonaut/hashtagger`.
In this folder, create following classes:
  
### TwitterConfig.java
  
```
package net.thetranquilpsychonaut.hashtagger.config;

public final class TwitterConfig
{
    public static final String TWITTER_OAUTH_CONSUMER_KEY    = "<TWITTER-CONSUMER_KEY>";
    public static final String TWITTER_OAUTH_CONSUMER_SECRET = "<TWITTER-CONSUMER-SECRET>";
}
```

### GPlusConfig.java

```
package net.thetranquilpsychonaut.hashtagger.config;

public final class GPlusConfig
{
    public static final String GPLUS_OAUTH_CLIENT_ID     = "<GPLUS-CLIENT-ID>";
    public static final String GPLUS_OAUTH_CLIENT_SECRET = "<GPLUS-CLIENT-SECRET>";
    public static final String GPLUS_ACCESS_SCOPE        = "https://www.googleapis.com/auth/plus.login";
}

```

### FacebookConfig.java

```
package net.thetranquilpsychonaut.hashtagger.config;

public final class FacebookConfig
{
    public static final String FACEBOOK_OAUTH_APP_ID     = "<FACEBOOK-APP-ID>";
    public static final String FACEBOOK_OAUTH_APP_SECRET = "<FACEBOOK-APP-SECRET>";
}

```

Then cross your fingers and compile! If something goes wrong, you can shoot me a mail at jaydeepmjoshi at gmail dot com.

# License

Licensed under Apache license. See LICENSE file.
