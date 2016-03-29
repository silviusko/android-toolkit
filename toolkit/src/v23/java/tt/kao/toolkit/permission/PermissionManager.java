package tt.kao.toolkit.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luke_kao
 */
public class PermissionManager {
    private static final String TAG = PermissionManager.class.getSimpleName();

    public enum ResultStatus {
        NONE,
        PASSED,
        FAILED,
    }

    // requestCode must be 8 bits
    private final static int PERMISSION_REQUEST_CODE = 0x38;

    public static boolean requestPermissions(Activity activity, final String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("permissions can't be null or empty");
        }

        String[] permissionNotGranted = permissionNotGranted(activity, permissions);

        boolean result = permissionNotGranted.length == 0;

        if (!result) {
            ActivityCompat.requestPermissions(activity, permissionNotGranted, PERMISSION_REQUEST_CODE);
        }

        return result;
    }

    private static String[] permissionNotGranted(Context context, final String[] permissions) {
        List<String> notGrantedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions.add(permission);
            }
        }

        String[] array = new String[notGrantedPermissions.size()];
        notGrantedPermissions.toArray(array);

        return array;
    }

    public static ResultStatus checkAllGranted(int requestCode, int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) return ResultStatus.NONE;


        ResultStatus status = ResultStatus.PASSED;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                status = ResultStatus.FAILED;
                break;
            }
        }

        return status;
    }
}
