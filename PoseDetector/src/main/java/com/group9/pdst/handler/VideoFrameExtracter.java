package com.group9.pdst.handler;

import org.apache.commons.io.FileUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.RgbToBgr;
import org.jcodec.scale.Transform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class VideoFrameExtracter {
//

//    public File createImageToSuggest(File file, Path path) throws IOException, JCodecException {
//        double frameRate = 0;
//        try {
//            System.out.println("da vao VideoFrameExtracter ");
//            SeekableByteChannel bc = NIOUtils.readableFileChannel(String.valueOf(file));
//            MP4Demuxer dm = new MP4Demuxer(bc);
//            DemuxerTrack vt = dm.getVideoTrack();
//            frameRate = vt.getMeta().getTotalDuration();
//            System.out.println("frame rate ===== " + frameRate);
//        } catch (Exception e) {
//
//        }
//
//        File originalFile = file;
//        for (int i = 0; i < frameRate; i++) {
//            //Picture frame = FrameGrab.getFrameFromFile(file, i);
//            Picture frame = FrameGrab.getFrameAtSec(file, i);
//            //originalFile = File.createTempFile("Frame_" + i + 1, "_.png", new File(String.valueOf(path)));
//            originalFile = new File(path+"/Frame"+i+".png");
//            //File f = new File(path+"/Frame"+i+".png");
//            ImageIO.write(toBufferedImage(frame), "png", originalFile);
//        }
//        return originalFile;
//    }
    public File createImage(File file, Path path) throws IOException, JCodecException {
        double frameRate = 0;
        try {
            System.out.println("da vao VideoFrameExtracter ");
            SeekableByteChannel bc = NIOUtils.readableFileChannel(String.valueOf(file));
            MP4Demuxer dm = new MP4Demuxer(bc);
            DemuxerTrack vt = dm.getVideoTrack();
            frameRate = vt.getMeta().getTotalDuration();
            System.out.println("frame rate ===== " + frameRate);
        } catch (Exception e) {

        }

        File originalFile = file;
        for (int i = 0; i < frameRate/2; i++) {
            //Picture frame = FrameGrab.getFrameFromFile(file, i);

            Picture frame = FrameGrab.getFrameAtSec(file, i*2);
            //originalFile = File.createTempFile("Frame_" + i + 1, "_.png", new File(String.valueOf(path)));
            String frameIndex = i < 10 ? "0" + i : "" + i;
            originalFile = new File(path+"/Frame"+frameIndex+".png");
            //File f = new File(path+"/Frame"+i+".png");
            ImageIO.write(toBufferedImage(frame), "png", originalFile);

        }
        return originalFile;
    }

    // this method is from Jcodec AWTUtils.java.
    private BufferedImage toBufferedImage(Picture src) {
        if (src.getColor() != ColorSpace.RGB) {
            Transform transform = ColorUtil.getTransform(src.getColor(), ColorSpace.RGB);
            if (transform == null) {
                throw new IllegalArgumentException("Unsupported input colorspace: " + src.getColor());
            }
            Picture out = Picture.create(src.getWidth(), src.getHeight(), ColorSpace.RGB);
            transform.transform(src, out);
            new RgbToBgr().transform(out, out);
            src = out;
        }
        BufferedImage dst = new BufferedImage(src.getCroppedWidth(), src.getCroppedHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        if (src.getCrop() == null) {
            toBufferedImage2(src, dst);
        } else {
            toBufferedImageCropped(src, dst);
        }
        return dst;
    }

    // this method is from Jcodec AWTUtils.java.
    private void toBufferedImage2(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        byte[] srcData = src.getPlaneData(0);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (srcData[i] + 128);
        }
    }

    // this method is from Jcodec AWTUtils.java.
    private static void toBufferedImageCropped(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        byte[] srcData = src.getPlaneData(0);
        int dstStride = dst.getWidth() * 3;
        int srcStride = src.getWidth() * 3;
        for (int line = 0, srcOff = 0, dstOff = 0; line < dst.getHeight(); line++) {
            for (int id = dstOff, is = srcOff; id < dstOff + dstStride; id += 3, is += 3) {
                data[id] = (byte) (srcData[is] + 128);
                data[id + 1] = (byte) (srcData[is + 1] + 128);
                data[id + 2] = (byte) (srcData[is + 2] + 128);
            }
            srcOff += srcStride;
            dstOff += dstStride;
        }
    }


    public static void main(String[] args) {
//        VideoFrameExtracter videoFrameExtracter = new VideoFrameExtracter();
//
//        try {
//            URL url = new URL("https://firebasestorage.googleapis.com/v0/b/demouploadvideo-bdc04.appspot.com/o/KietPT-456789%2FVideo%2F1.mp4?alt=media&token=ea946831-2d01-4463-bbc1-dad770d7b5dc");
//            File file = new File("D:\\Faculty\\Capstone Project\\Image2\\abc.mp4");
//            System.out.println("aaaaaaaaaaaaaaaaa");
//            FileUtils.copyURLToFile(url, file);
//            File imageFrame = videoFrameExtracter.test(file);
//            System.out.println("input file name : " + file.getAbsolutePath());
//            System.out.println("output video frame file name  : " + imageFrame.getAbsolutePath());
//        } catch (JCodecException e) {
//            System.out.println("JCodec error occurred while extracting image : " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("IO error occurred while extracting image : " + e.getMessage());
//        }

    }


    public File test(File file) throws IOException, JCodecException {
        System.out.println("da vao day");
        double frameRate = 0;
        try {

            SeekableByteChannel bc = NIOUtils.readableFileChannel(String.valueOf(file));
            MP4Demuxer dm = new MP4Demuxer(bc);
            DemuxerTrack vt = dm.getVideoTrack();
            frameRate = vt.getMeta().getTotalDuration();
            System.out.println("frame rate ===== " + frameRate);
        } catch (Exception e) {

        }

        File originalFile = file;
        for (int i = 0; i < frameRate; i++) {
            //Picture frame = FrameGrab.getFrameFromFile(file, i);
            Picture frame = FrameGrab.getFrameAtSec(file, i);
            originalFile = File.createTempFile("Frame_" + i + 1, "_.png", new File("D:\\Faculty\\Capstone Project\\Image2"));
            ImageIO.write(toBufferedImage(frame), "png", originalFile);
        }
        return originalFile;
    }
}
