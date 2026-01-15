package com.vr.cdp.client.ws;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

public class ScreencastImageSaver {

    private final File outputDir;
    private static final AtomicLong COUNTER = new AtomicLong();

    private final FFmpegFrameRecorder recorder;
    private final Java2DFrameConverter converter = new Java2DFrameConverter();

    public ScreencastImageSaver(String directory, int width, int height, String videoFile) throws Exception {
        this.outputDir = new File(directory);
        Files.createDirectories(outputDir.toPath());


        // Init video recorder
        this.recorder = new FFmpegFrameRecorder(videoFile, width, height);
        this.recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
        this.recorder.setFormat("mp4");
        this.recorder.setFrameRate(10); // adjust FPS as needed
        this.recorder.setPixelFormat(org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_YUV420P);

        this.recorder.start();
    }

    public void saveBase64Image(String base64, String format) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(base64);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));

        if (img == null) {
            throw new RuntimeException("Failed to decode image from Base64");
        }

        // Optional: save as individual image
        long index = COUNTER.incrementAndGet();
        File file = new File(outputDir, String.format("frame_%06d.%s", index, format));
        ImageIO.write(img, format, file);

        // Convert BufferedImage to frame and record to video
        recorder.record(converter.convert(img));
    }

    public void close() throws Exception {
        recorder.stop();
        recorder.release();
    }
}
