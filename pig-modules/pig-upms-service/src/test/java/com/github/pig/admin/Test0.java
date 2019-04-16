package com.github.pig.admin;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test0 {
    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection=factory.newConnection();
        Channel channel=connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");//fanout表示分发，所有的消费者得到同样的队列信息
        //分发信息
        for (int i=0;i<5;i++){
            String message="Hello World"+i;
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
            System.out.println("EmitLog Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }

    @Test
    public void test(){
        String s = "q23fd1267223fd231234567sf";
        Matcher matcher = Pattern.compile("\\d+").matcher(s);
        ArrayList<Integer> list = new ArrayList<>();
        while (matcher.find()){
            String group = matcher.group();
            Integer restul = maxNum(group);
            list.add(restul);
        }
        Integer maxNum = list.stream().max(Integer::compareTo).get();
        System.out.println(maxNum);
    }

    public Integer maxNum(String s){
        StringBuilder sbd = new StringBuilder();
        ArrayList<Integer> list = new ArrayList<>();
        sbd.append(s.charAt(0));
        for (int i= 0;i<s.length();i++){
            char a = s.charAt(i);
            char b = 0;
            if (i != s.length()-1){
                b = s.charAt(i+1);
            }
            if (b  - a<1){
                list.add(Integer.valueOf(sbd.toString()));
                sbd.delete(0, s.length());
            }
            sbd.append(b);
        }
        return list.stream().max(Integer::compareTo).get();
    }
}
