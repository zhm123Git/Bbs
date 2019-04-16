/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.github.pig.admin;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.StandardEnvironment;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 配置文件加解密工具类
 *
 * @author lengleng
 */
public class PigAdminApplicationTest {
    @Autowired
    private Jedis jedis;
    private static final String JASYPT_ENCRYPTOR_PASSWORD = "jasypt.encryptor.password";

    /**
     * jasypt.encryptor.password 对应 配置中心 application-dev.yml 中的密码
     */
    @Test
    public void testEnvironmentProperties() {
        System.setProperty(JASYPT_ENCRYPTOR_PASSWORD, "lengleng");
        StringEncryptor stringEncryptor = new DefaultLazyEncryptor(new StandardEnvironment());

        //加密方法
        System.out.println(stringEncryptor.encrypt("123456"));
        //解密方法
        System.out.println(stringEncryptor.decrypt("saRv7ZnXsNAfsl3AL9OpCQ=="));
    }

    public final static String QUEUE_NAME="rabbitMQ.test";
@Test
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ相关信息
        factory.setHost("localhost");
        //factory.setUsername("lp");
        //factory.setPassword("");
        // factory.setPort(2088);
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
//        //  声明一个队列        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
////        String message = "Hello RabbitMQ成功了";
////        //发送消息到队列中
////        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
////        System.out.println("Producer Send +'" + message + "'");
//    channel.queueDeclare(QUEUE_NAME,true,false,false,null);
    //分发信息
    for (int i=0;i<10;i++){
        String message="Hello RabbitMQ"+i;
        channel.basicPublish("",QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        System.out.println("NewTask send '"+message+"'");
    }
        //关闭通道和连接
        channel.close();
        connection.close();
    }

}