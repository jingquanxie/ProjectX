package am.widget.cameraview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Process;

/**
 * 版本兼容器
 * Created by Alex on 2016/11/28.
 */

class Compat {

    private Compat() {
    }

    private interface CompatImpl {
        int checkSelfPermission(Context context, String permission);

        void setHotspot(Drawable drawable, float x, float y);
    }

    private static class CompatBase implements CompatImpl {

        @Override
        public int checkSelfPermission(Context context, String permission) {
            return context.checkPermission(permission, Process.myPid(), Process.myUid());
        }

        @Override
        public void setHotspot(Drawable drawable, float x, float y) {
            // do nothing
        }
    }

    @TargetApi(21)
    private static class CompatLollipop extends CompatBase {

        @Override
        public void setHotspot(Drawable drawable, float x, float y) {
            drawable.setHotspot(x, y);
        }
    }

    @TargetApi(23)
    private static class CompatAPI23 extends CompatBase {
        @Override
        public int checkSelfPermission(Context context, String permission) {
            return context.checkSelfPermission(permission);
        }
    }

    private static final CompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new CompatAPI23();
        } else if (version >= 21) {
            IMPL = new CompatLollipop();
        } else {
            IMPL = new CompatBase();
        }
    }

    static int checkSelfPermission(Context context, String permission) {
        return IMPL.checkSelfPermission(context, permission);
    }

    static void setHotspot(Drawable drawable, float x, float y) {
        IMPL.setHotspot(drawable, x, y);
    }
}
