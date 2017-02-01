package es.nkmem.da.SIMhider;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedHook implements IXposedHookLoadPackage {
    public static String TAG = "[Hide SIM Icon] ";
    public static final String PACKAGE_SYSTEMUI = "com.android.systemui";
    private static final String CLASS_SIGNAL_CLUSTER_VIEW = "com.android.systemui.statusbar.SignalClusterView";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals(PACKAGE_SYSTEMUI)) {
            return;
        }
       // XposedBridge.log(TAG + "Hooking SignalClusterView");
        Class<?> classSignalClusterView = XposedHelpers.findClass(CLASS_SIGNAL_CLUSTER_VIEW, lpparam.classLoader);
        //in Oneplus 3T (Oxygen OS, MM, 3.5.4)
        XposedHelpers.findAndHookMethod(classSignalClusterView, "inflatePhoneState", int.class, int.class, onApplyHook);
    }

    private static XC_MethodHook onApplyHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            int subid = (int) param.args[0];
            int slotid = (int) param.args[1];
            if ((slotid == 0) & (subid == 2)) { //if it is SIM #1...
//                param.args[1] = 1;
                XposedBridge.log(TAG + "SIM " + String.valueOf(slotid+1) + " hidden!");
                param.setResult(null); //we act as if nothing happens (thus no icon)
            }
//            String volteVisible = "mVolteVisible";
//            if (XposedHelpers.getBooleanField(param.thisObject, volteVisible)) {
              //  XposedBridge.log(TAG + "inside function! " + String.valueOf(param.args[0]) + " " + String.valueOf(param.args[1]));
//                XposedHelpers.setBooleanField(param.thisObject, volteVisible, false);
//            }
        }
    };
}