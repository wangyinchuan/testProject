package com.example.xapiservice.app;

import com.example.xapiservice.app.utils.KafkaUtils;

import java.util.List;

public class Kafkatest {

    public static void main(String[] args) {
        //xapi正式线上
        //KafkaUtils kafka=new KafkaUtils("10.100.136.119:9093,10.100.17.176:9093,10.96.164.31:9093","D:\\kafkaclient.keystore.jks","D:\\kafka.client.truststore.jks","open123456");
        //本地正式
        //KafkaUtils kafka=new KafkaUtils("10.100.136.44:9092,10.100.136.45:9092,10.100.136.46:9092","","","");
        //本地测试
        KafkaUtils kafka=new KafkaUtils("10.100.136.33:9092,10.100.136.34:9092,10.100.136.35:9092","","","");
        kafka.produce("pro-t0001-gczx-xapi","testokokok");

        kafka.consume("pro-t0001-gczx-xapi");

    }
}
