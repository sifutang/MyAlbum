package com.example.android.myalbum.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by android on 17-8-17.
 */

public final class CloseUtils {

    private CloseUtils() { }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
