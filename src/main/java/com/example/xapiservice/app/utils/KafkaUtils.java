package com.example.xapiservice.app.utils;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KafkaUtils {

    private String bootstrapServers;//10.100.136.119:9093
    private String SSL_KEYSTORE_LOCATION_CONFIG;//D:\kafkaclient.keystore.jks
    private String SSL_TRUSTSTORE_LOCATION_CONFIG;//D:\kafka.client.truststore.jks
    private String SSL_KEYSTORE_PASSWORD_CONFIG;//open123456

    public KafkaUtils(){}

    public KafkaUtils(String bootstrapServers,String SSL_KEYSTORE,String SSL_TRUSTSTORE,String SSL_PASSWORD){
             this.bootstrapServers=bootstrapServers;
             this.SSL_KEYSTORE_LOCATION_CONFIG=SSL_KEYSTORE;
             this.SSL_TRUSTSTORE_LOCATION_CONFIG=SSL_TRUSTSTORE;
             this.SSL_KEYSTORE_PASSWORD_CONFIG=SSL_PASSWORD;
    }

    public  void consume(String topic) {
        Properties props = new Properties();
        /* 定义kakfa 服务的地址，不需要将所有broker指定上 */
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        /* 制定consumer group */
        props.put("group.id",topic);
        props.put("auto.offset.reset","earliest");
        /* 是否自动确认offset */
        if(!"".equals(SSL_KEYSTORE_LOCATION_CONFIG)){
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,SSL_KEYSTORE_LOCATION_CONFIG);
        }
        if(!"".equals(SSL_KEYSTORE_PASSWORD_CONFIG)){
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,SSL_KEYSTORE_PASSWORD_CONFIG);
        }
        if(!"".equals(SSL_TRUSTSTORE_LOCATION_CONFIG)){
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,SSL_TRUSTSTORE_LOCATION_CONFIG);
        }
        if(!"".equals(SSL_KEYSTORE_PASSWORD_CONFIG)){
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,SSL_KEYSTORE_PASSWORD_CONFIG);
            props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        }

        /* key的序列化类 */
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        /* value的序列化类 */
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        /* 定义consumer */
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        /* 消费者订阅的topic, 可同时订阅多个 */
        consumer.subscribe(Arrays.asList(topic));

        /* 读取数据，读取超时时间为100ms */
        List<String> result = new ArrayList<String>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records){
                System.out.println("拉取的消息个数: "+records.count());
                if(records.count()>0) {
                    System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value() + "\n");
                }
            }
                //result.add(record.value()+"");

            //consumer.commitSync();
        }

    }

    public  void produce(String topic ,String jsonString) {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
//         producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, "myApiKey");
        /*producerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,SSL_KEYSTORE_LOCATION_CONFIG);
        producerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,SSL_KEYSTORE_PASSWORD_CONFIG);
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,SSL_TRUSTSTORE_LOCATION_CONFIG);
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,SSL_KEYSTORE_PASSWORD_CONFIG);
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");*/

        if(!"".equals(SSL_KEYSTORE_LOCATION_CONFIG)){
            producerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,SSL_KEYSTORE_LOCATION_CONFIG);
        }
        if(!"".equals(SSL_KEYSTORE_PASSWORD_CONFIG)){
            producerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,SSL_KEYSTORE_PASSWORD_CONFIG);
        }
        if(!"".equals(SSL_TRUSTSTORE_LOCATION_CONFIG)){
            producerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,SSL_TRUSTSTORE_LOCATION_CONFIG);
        }
        if(!"".equals(SSL_KEYSTORE_PASSWORD_CONFIG)){
            producerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,SSL_KEYSTORE_PASSWORD_CONFIG);
            producerProps.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
            producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        }

        //“所有”设置将导致记录的完整提交阻塞，最慢的，但最持久的设置。
        producerProps.put("acks", "all");
       //如果请求失败，生产者也会自动重试，即使设置成０ the producer can automatically retry.
        producerProps.put("retries", 0);
        //The producer maintains buffers of unsent records for each partition.
        producerProps.put("batch.size", 16384);
        //默认立即发送，这里这是延时毫秒数
        producerProps.put("linger.ms", 1);
        //生产者缓冲大小，当缓冲区耗尽后，额外的发送调用将被阻塞。时间超过max.block.ms将抛出TimeoutException
        producerProps.put("buffer.memory", 33554432);


        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer(producerProps);
        //生成key
        long timestamp = System.currentTimeMillis();
        String key = topic + timestamp;
       producer.send(new ProducerRecord<String, String>(topic,key,jsonString));
        producer.close();
    }
}
