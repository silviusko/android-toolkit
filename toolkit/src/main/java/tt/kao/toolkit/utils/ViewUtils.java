package tt.kao.toolkit.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author luke_kao
 */
public class ViewUtils {

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        if (params instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            marginLayoutParams.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
