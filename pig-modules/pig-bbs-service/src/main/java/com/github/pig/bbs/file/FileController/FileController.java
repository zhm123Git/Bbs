package com.github.pig.bbs.file.FileController;


import ai.olami.cloudService.APIConfiguration;
import ai.olami.cloudService.APIResponse;
import ai.olami.cloudService.TextRecognizer;
import ai.olami.nli.NLIResult;
import com.alibaba.fastjson.JSONObject;
import com.github.pig.bbs.util.BbsUtil;
import com.github.pig.bbs.util.FileUtil;
//import it.sauronsoftware.jave.InputFormatException;
import com.github.pig.bbs.util.VoiceUtil;
import com.github.pig.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

import static com.github.pig.bbs.util.FileUtil.*;

@Controller
@RequestMapping(value = "/uploadFile")
@lombok.extern.slf4j.Slf4j
@Api(value = "文件处理")
public class FileController {



    @PostMapping(value = "/baidu")
    @ResponseBody
    public  R  test2(@RequestParam("file") MultipartFile file ) throws Exception{
        String originalFilename = file.getOriginalFilename();
        file.transferTo(new File( "/home/test/"+originalFilename ));//写入
        //System.out.println(file.toString()+"文件读取到了");
        Map o = (Map) JSONObject.parse(VoiceUtil.voiiceZtest("/home/test/" + originalFilename).toString());
        List list = (List) o.get("result");
        Map ma = new HashMap();
        ma.put("text",list.get(0).toString());
        ma.put("unit",olamiNuit(list.get(0).toString()));
        return new R(ma);
    }

    @Value("${upload-path}")
    private String FilePath; //FilePath= "E:/temp/";

    @Value("${serverIp}")
    private String serverIp ; //="127.0.0.1:8003/";


    @PostMapping("/upload")
    @ResponseBody
    @ApiOperation(value = "接受上传的图片和视频，根据需要生产缩略图",notes = " 目前的缩略图为400 和600" +
            "\n  返回的Path是原图地址   400的缩略图在后边加上   !w400.图片后缀名 " +
            "\n  600缩略图同上 对图片和视频的格式进行了预定义   要是返回msg为  格式未预定义  " +
            "\n  联系我根据需要 加格式     ")
    public Object test1(@RequestParam("file") MultipartFile[] files
                        ) throws Exception {
        if(files==null){
            return "未接受到上传文件 请仔细查看!";
        }

        ArrayList<Map> arrayList = new ArrayList<Map>();
        Integer i =1;//定义key  从1开始
        for (MultipartFile file : files) {
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            boolean imgCheck = checkFileType(file.getOriginalFilename(), imsg);
            boolean videosCheck = checkFileType(file.getOriginalFilename(), videos);
            try {
                if(videosCheck)//视频处理
                objectObjectHashMap =uploadVideos(file,i++);
                if(imgCheck)//图片处理
                objectObjectHashMap = uploadImg(file,i++);
            } catch (Exception e) {
                log.error("图片或者视频 格式损坏，无法处理 ");
                objectObjectHashMap =  notFile("图片或者视频 格式损坏，无法处理 ");
                e.printStackTrace();
            }
            //其他处理
            if(!imgCheck&&!videosCheck)//TODO
             objectObjectHashMap =  notFile("文件格式未预定义，无法处理，请联系管理员");

            arrayList.add(objectObjectHashMap);
        }
        return arrayList;
    }

    private HashMap<String, Object> notFile(String  msg){
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("msg",  msg);
        return stringStringHashMap;
    }

    private HashMap<String, Object> uploadVideos(MultipartFile file,int i) throws Exception {
        String uploadFile = uploadFile(file);
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
 //       String ovTime = FileUtil.getOvTime(new File(FilePath+uploadFile));//获取视频时间长度
        stringStringHashMap.put("Path", serverIp+uploadFile );
        Integer height=  fetchFrame(FilePath+uploadFile ,FilePath+uploadFile+".jpg");
        stringStringHashMap.put("vPath", serverIp+uploadFile+".jpg" );
        stringStringHashMap.put("width", 400);
        stringStringHashMap.put("height", height );
        stringStringHashMap.put("Name",file.getOriginalFilename());
        stringStringHashMap.put("key", i);


     //   stringStringHashMap.put("videoDuration",ovTime);  不用时长了
        return stringStringHashMap;
    }


    /*写入非图片文件*/
    private String  uploadFile(MultipartFile file) throws IOException{
        //将文件写入 到静态路径
        String finallyFilePathName = getfileName(file);
        createFiles(FilePath + finallyFilePathName);//实体路径
        file.transferTo(new File(FilePath + finallyFilePathName  ));//写入

        return finallyFilePathName;
    }

    /*获取文件名*/
    private String getfileName(MultipartFile file) {
        String fileName = getExtensionName(file.getOriginalFilename());
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        String date = BbsUtil.getDateString();
        return date + "/" + name + "." + fileName;
    }

    /*图片返回*/
    private HashMap<String, Object> uploadImg(  MultipartFile file,int i) throws IOException {
        HashMap<String, Object> stringStringHashMap = new HashMap<>();

        /*
        * 根据需求  生成 宽400  600  原图 三份图     !w400.后缀名
        * */
        Map ma =  uploadFiles(file);
        stringStringHashMap.put("Path",ma.get("path"));
        stringStringHashMap.put("width",ma.get("width"));
        stringStringHashMap.put("height",ma.get("height"));
        stringStringHashMap.put("Name",file.getOriginalFilename());
        stringStringHashMap.put("key",i);
        return stringStringHashMap;
    }


    private Map  uploadFiles(MultipartFile file)throws IOException{
        BufferedImage sourceImg = ImageIO.read(file.getInputStream());
        int imgWidth = sourceImg.getWidth();
        int imgHeight = sourceImg.getHeight();
        String name = UUID.randomUUID().toString().replaceAll("-", "");
       // uploadFile(file,400,getHeight(imgWidth,imgHeight,400),"!w400",name);//生成400 缩略图
       // uploadFile(file,600,getHeight(imgWidth,imgHeight,600),"!w600",name);//生成600 缩略图
       // uploadFile(file,imgWidth,imgHeight,null,name);;//返回原图地址
        return  uploadFile(file,400,getHeight(imgWidth,imgHeight,400),"!w400",name);//生成400 缩略图
    }

    private int getHeight(int imgWidth, int imgHeight,int bs) {
        double w= (double)imgWidth/(double)bs;
        double v = (double) imgHeight / w;
        return  BbsUtil.DoubleChangeInt(v);
    }

/*    public static void main(String[] args) {
        Double i = 600.0;
        Double i1 = 400.0;
        Double i2 = i1/i;

        System.out.println(i * i2);
        System.out.println(i2);


    }*/

    /*处理图片*/
    private Map uploadFile( MultipartFile file,  Integer width,  Integer height, String finallyName ,String name) throws IOException {
        String zzName ="";

        //尺寸大小为像素
        //定义一个BufferedImage对象，用于保存缩小后的位图
        String fileName = getExtensionName(file.getOriginalFilename());

        BufferedImage bufferedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics graphics=bufferedImage.getGraphics();
        //读取原始位图
        //  Image srcImage= ImageIO.read(file.getInputStream());
        BufferedImage sourceImg = ImageIO.read(file.getInputStream());

        /*int imgWidth = sourceImg.getWidth();
        int imgHeight = sourceImg.getHeight();*/

        //将原始位图缩小后绘制到bufferedImage对象中
        graphics.drawImage(sourceImg,0,0,width,height,null);
        String date = BbsUtil.getDateString();
        String finallyFilePathName = FilePath + date + "/" + name + "." + fileName;
        createFiles(finallyFilePathName);
        //将bufferedImage对象输出到磁盘上

        if(finallyName!=null&&!"".equals(finallyName)){
            zzName = finallyName+ "." + fileName;
        }

        ImageIO.write(bufferedImage,fileName,new File(finallyFilePathName+zzName));//zzName  为缩略图拼接名
        //定义返回给前台的映射路径
        //IP 加  端口号   127.0.0.1:8003/

        Map ma = new HashMap();
        ma.put("path",serverIp+date+"/"+name+"."+fileName +zzName);
        ma.put("width",width);
        ma.put("height",height);


        return ma;
    }



    private static String olamiNuit(String StringNIL) throws NoSuchAlgorithmException, IOException {
        // 设置您的 Key 相关信息
        String myAppKey = "957cfbc873614df2abb8f83b67bf4071";
        String myAppSecret = "148e3de2595f4890a42ad1b14a6b81e4";

        // 创建 APIConfiguration 对象
        APIConfiguration config = new APIConfiguration(myAppKey, myAppSecret, APIConfiguration.LOCALIZE_OPTION_SIMPLIFIED_CHINESE);

        // 创建文本识别器对象
        TextRecognizer recoginzer = new TextRecognizer(config);

        // 请求 "今天星期几" 的分词结果
        APIResponse response1 = recoginzer.requestWordSegmentation(StringNIL);

        // 检查请求结果
        if (response1.ok() && response1.hasData()) {
            // 取得分词结果
            String[] ws = response1.getData().getWordSegmentation();
        }

        // 请求 "今天星期几" 的 NLI 语义分析或 IDS 智能答复与数据
        APIResponse response2 = recoginzer.requestNLI(StringNIL);

        // 检查请求结果
        if (response2.ok() && response2.hasData()) {
            // 取得 NLI 语义分析或 IDS 智能答复与数据
            NLIResult[] nliResults = response2.getData().getNLIResults();
            System.out.println(nliResults[0].getDescObject().getReplyAnswer());
            return nliResults[0].getDescObject().getReplyAnswer();
        }
        return "我没听懂";
    }

}
