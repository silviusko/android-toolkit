# BasePreferences

Use simple annotations to access preferences. 

1. implement your preferences class and extend from BasePreferences
    ```java
    public class Preferences extends BasePreferences {
        ...
    }
    ```
2. define your key names of preferences
    ```java
    public static final String KEY_API_KEY = "API_KEY";
    public static final String KEY_UID = "UID";
    public static final String KEY_PSID = "PSID";
    ```
3. declare member fields with PreferenceProperty annotation, PreferenceProperty must setup the key and the value of specified type
    ```java
    @PreferenceProperty(key = KEY_UID, stringValue = "USER_1234")
    public String uid;
    @PreferenceProperty(key = KEY_IS_PASSED, boolValue = true)
    public boolean isPassed;
    @PreferenceProperty(key = KEY_EXPIRE_TIME, longValue = 123456789L)
    public long expireTime;
    @PreferenceProperty(key = KEY_RATIO, floatValue = 1.67f)
    public long ratio;
    ```
4. call loadAll() to load all preferences
5. save a preference with two steps
 * assign the value into the field
 * call save(KEY) method to save the value from field into preference, the KEY is your definitions in step 2
     ```
     Preferences preferences = new Preferences(context)
     preferences.uid = "USER_5678";
     preferences.save(Preferences.KEY_UID);
     ```
6. you can also save all by calling saveAll() method