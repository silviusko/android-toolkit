PermissionManager
=================

Help to easy to ask end user to request the permission for your features on Android 6.0

- Request the permissions

    ```
    final String[] PERMISSIONS = {
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    
    if (PermissionManager.requestPermissions(this, PERMISSIONS)) {
        // do something
    }
    ```

- Override onRequestPermissionsResult in Activity, and call PermissionManager.checkAllGranted to check 

    ```
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    
        PermissionManager.ResultStatus status = PermissionManager.checkAllGranted(requestCode, grantResults);
    
        switch (status) {
            case PASSED:
                // do passed thing
                break;
                
            case FAILED:
                // do failed thing
                break;
        }
    }
    ```
