package com.taha.alrehab.Helpers;


public class ConnectionHelper {
    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }
}
