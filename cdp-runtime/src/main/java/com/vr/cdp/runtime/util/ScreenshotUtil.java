package com.vr.cdp.runtime.util;

import java.io.FileOutputStream;
import java.util.Base64;

public class ScreenshotUtil {

    public static void saveBase64Image(String base64, String filePath) throws Exception {
        byte[] imageBytes = Base64.getDecoder().decode(base64);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageBytes);
        }
    }
}
