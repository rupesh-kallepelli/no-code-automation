package com.vr.actions.uitl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

public class ScreenshotUtil {

    public static void saveBase64Image(String base64, String filePath) throws Exception {
        byte[] imageBytes = Base64.getDecoder().decode(base64);

        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageBytes);
        }
    }
}
