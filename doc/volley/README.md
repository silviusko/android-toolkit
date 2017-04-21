BaseVolleyClient
================

Implement a subclass
--------------------

Implement a subclass to extend BaseVolleyClient class.
 
For example:
```
public class VolleyClient extends BaseVolleyClient {

    private static VolleyClient sClient = null;

    public synchronized static VolleyClient getInstance() {
        if (sClient == null) {
            sClient = new VolleyClient(BaseApplication.getInstance());
        }
        return sClient;
    }

    private VolleyClient(Context context) {
        super(context);
    }
}
```


How to use
----------

Getting client instance from getInstance() of implementation to use loader or queue
```
VolleyClient.getInstance().getImageLoader()
```