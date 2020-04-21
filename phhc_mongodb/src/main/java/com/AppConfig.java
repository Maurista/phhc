package com;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

@Configuration
public class AppConfig {

    @Value("${myself.mongodb}")
    private List<String> uri;

    private final static Map<String, String> conf = new HashMap<>();

    @Bean
    public MongoClient mongoClient(){
        Map<String, Object> map = deal(uri.get(0));
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Collections.singletonList((ServerAddress) map.get("address"))))
                        .credential(MongoCredential.createCredential(map.get("username").toString(), map.get("database").toString(), map.get("password").toString().toCharArray()))
                        .build());
        conf.put("0", map.get("database").toString());
        return mongoClient;
    }

    @Bean
    public MongoClient mongoClient2(){
        Map<String, Object> map = deal(uri.get(1));
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Collections.singletonList((ServerAddress) map.get("address"))))
                        .credential(MongoCredential.createCredential(map.get("username").toString(), map.get("database").toString(), map.get("password").toString().toCharArray()))
                        .build());
        conf.put("2", map.get("database").toString());
        return mongoClient;
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoClient(),conf.get("0"));
    }

    @Bean
    public MongoTemplate mongoTemplate2(){
        return new MongoTemplate(mongoClient2(),conf.get("2"));
    }

    @Bean
    MongoTransactionManager mongoTransactionManager() {
        return new MongoTransactionManager(mongoTemplate().getMongoDbFactory());
    }

    private Map<String, Object> deal(String u){
        Map<String, Object> map = new HashMap<>();
        String[] split = u.split("@");
        String[] split1 = split[1].split(":");
        String host = split1[0];
        String port = split1[1].split("/")[0];
        String[] split2 = split[0].split(":");
        String username = split2[1].substring(2);
        String password = split2[2];
        map.put("host", host);
        map.put("port", port);
        map.put("database", split1[1].split("/")[1]);
        map.put("address", new ServerAddress(host, Integer.valueOf(port)));
        map.put("username", username);
        map.put("password", password);
        for(String key : map.keySet()){
            if(null == map.get(key) || "".equals(map.get(key))){
                throw new RuntimeException(key + "不能为空!");
            }
        }
        return map;
    }
}
