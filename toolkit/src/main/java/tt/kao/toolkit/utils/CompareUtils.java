package tt.kao.toolkit.utils;

import android.text.TextUtils;

/**
 * @author luke_kao
 */
public class CompareUtils {
    public static boolean equals(String str1, String str2) {
        if (TextUtils.isEmpty(str1) && TextUtils.isEmpty(str2))
            return true;
        if (TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2))
            return false;
        return str1.equals(str2);
    }

    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null)
            return true;
        if (obj1 == null || obj2 == null)
            return false;
        return obj1.equals(obj2);
    }
}
