package com.github.pig.bbs.posts.controller;


import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BbsPostsControllerTest {
    public final static String QUEUE_NAME="rabbitMQ.test";
    @Test
    public static void main(String[] args) throws IOException, TimeoutException {
    // 创建连接工厂
    ConnectionFactory factory = new ConnectionFactory();
    //设置RabbitMQ地址
    factory.setHost("localhost");
    //创建一个新的连接
    Connection connection = factory.newConnection();
    //创建一个通道
    Channel channel = connection.createChannel();
    //声明要关注的队列
    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    System.out.println("Customer Waiting Received messages");


    //每次从队列获取的数量
    channel.basicQos(1);
    //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
    // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
    Consumer consumer = new DefaultConsumer(channel) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope,
                                   AMQP.BasicProperties properties, byte[] body)
                throws IOException {
            String message = new String(body, "UTF-8");
            System.out.println("Customer Received '" + message + "'");
            try {
                throw  new Exception();
                //doWork(message);
            }catch (Exception e){
                channel.abort();
            }finally {
                System.out.println("Worker1 Done");
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        }
    };
    //自动回复队列应答 -- RabbitMQ中的消息确认机制
    channel.basicConsume(QUEUE_NAME, false, consumer);
}
    private static void doWork(String task) {
        try {
            Thread.sleep(1000); // 暂停1秒钟
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
//    @Test
//    public static void main(String[] args) {
//        StringBuilder stb=new StringBuilder("0,0");//容器!
//        String str="acb125vf13679dD4562789ABCDEF";//原串!
//        Matcher matcher= Pattern.compile("\\d+").matcher(str);//匹配!取正则数窜
//        for(int count=0;matcher.find();) {//挑选!
//            str=matcher.group();
//            System.out.println("str="+str);
//            for(int i=0;i<str.length()-1;i++) {//查看指定规则次数:
//                if(str.charAt(i)<str.charAt(i+1)) {
//                    count++;
//                }else
//                    break;
//            }//如果,重新获取的比以前存入的次数更多,就放弃原来存入新的!
//            if(Integer.parseInt(stb.substring(0,stb.lastIndexOf(",")))<count) {
//                stb.delete(0, stb.length());
//                stb.append(count+","+str);
//            }//每次查看,为了查看,这个可有可无!
//            System.out.println(str+"\t递增次数:\t"+count);
//            count=0;//计数器归零!
//        }//最后容器里面存入的就是最大的,取出来即可!
//        System.out.println("最多的:"+stb.substring(stb.lastIndexOf(",")+1)+"\t次数:\t"+stb.substring(0,stb.lastIndexOf(",")));
//    }
//
//
//    public String find (String s){
//        StringBuilder sbd = new StringBuilder("0,0");
//        Matcher matcher = Pattern.compile("\\d+").matcher(s);
//        for (int count = 0;matcher.find();){
//            s=matcher.group();
//            for (int i=0;i<s.length()-1;i++){
//                if(s.charAt(i)<s.charAt(i+1)){
//                    count++;
//                }else
//                    break;
//            }
//            if (Integer.parseInt(sbd.substring(0,sbd.lastIndexOf(",")))<count){
//                sbd.delete(0,sbd.length());
//                sbd.append(count+","+s);
//            }
//
//            count=0;
//        }
//        s=sbd.substring(sbd.lastIndexOf(",")+1);
//        return s;
//    }
//

}