package com.github.pig.bbs.util;


import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
/*import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import it.sauronsoftware.jave.VideoInfo;*/

@Slf4j
public class FileUtil {
    /*
 * Java文件操作 获取文件扩展名
 *
 */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
    /*
     * Java文件操作 获取不带扩展名的文件名
     *
     *  Created on: 2011-8-2
     *      Author: blueeagle
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static void createFiles(String filePath){
        // 指定路径如果没有则创建并添加
        File file = new File(filePath);
        //获取父目录
        File fileParent = file.getParentFile();
        //判断是否存在
        if (!fileParent.exists()) {
            // 创建父目录文件
            fileParent.mkdirs();
        }
        //   file.createNewFile();
    }

    /*文件格式
    *
    */
    public static String[]  imsg ={"jpg","png","jpeg","bmp","gif","tmp"};//图片
    public static  String[]  videos={"mp4","rmvb","avi","wmv","mpg","3gp","mov","asf","asx","vob"};//视频



    public static Integer fetchFrame( String videofile,String  vFilePath)throws Exception {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
        //videofile视频路径，我用的是网络路径
        ff.start();
        int lenght = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < lenght) { // 过滤前5帧，避免出现全黑的图片
            f = ff.grabFrame();
            if ((i > 5) && (f.image != null)) {
                break; } i++;
        }
        int owidth = f.imageWidth ;
        int oheight = f.imageHeight ;
        // 对截取的帧进行等比例缩放
        int width = 400;
        int height = (int) (((double) width / owidth) * oheight);

        Java2DFrameConverter converter =new Java2DFrameConverter();
        BufferedImage fecthedImage =converter.getBufferedImage(f);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        File targetFile = new File(vFilePath); ImageIO.write(bi, "jpg", targetFile);
        ff.flush();
        ff.stop();
        return height;
    }

    /**
     * 文件类型判断
     *
     * @param fileName
     * @return
     */
    public static boolean checkFileType(String fileName,String[] gs ) {
        String fileName1 = fileName.toLowerCase();//转为小写
        Iterator<String> type = Arrays.asList(gs).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName1.toLowerCase().endsWith(ext) || fileName.toLowerCase().endsWith(ext) ) {
                return true;
            }
        }
        return false;
    }

    /*获取视频长度*/
  /*  public  static String  getOvTime(File file) throws Exception{
               Encoder encoder = new Encoder();
               long ls =0 ;

               *//*
               * jave  获取视频时间长度 有一个bug
               * 当视频格式损坏时  所依赖的第三方插件会弹出错误信息  如果不点确定 就无法返回数据
               * 一直卡死
               * jave 1.3 未提供判断视频损坏的方法
               * 所以暂时记下   留待优化
               * 就是下面使用插件时报错  没错 就是下面那一行会报错
               * TODO
               * *//*
                MultimediaInfo m = encoder.getInfo(file);
                ls = m.getDuration();  //ls是获取到的秒数
              // double sum1 = (double)sum;//总秒数
             //  double sum2 =sum1/3600;// 总小时数  转换成为了小时

        return  timeChangeDate(ls);
    }*/

    /*
    * 毫秒格式转换
    * */
    public static String timeChangeDate(long ms){
     /*   Date date = new Date(time);
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        String format = sdf.format(date);*/
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strHour+":"+strMinute + ":" + strSecond ;

    }



}
