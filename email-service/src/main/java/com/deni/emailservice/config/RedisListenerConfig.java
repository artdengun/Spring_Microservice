package com.deni.emailservice.config;

import com.deni.emailservice.service.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration // untuk menyimpan konfigurasi
@EnableRedisRepositories // agar bisa koneksi ke redis
public class RedisListenerConfig {
    // kita ambil database dari properties
    @Value("localhost") // default
    private String redisHost;
    @Value("6379") // default
    private Integer redisPort;
    @Value("${spring.redis.channel-name:emailSender}")
    private String redisChannelName;

    @Autowired
    private RedisMessageSubscriber redisMessageSubscriber;


    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig  =  new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public ChannelTopic channelTopic(){
        return  new ChannelTopic(redisChannelName);
    }

    @Bean
    public MessageListener messageListenerAdapter(){
        return new MessageListenerAdapter(redisMessageSubscriber);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListenerAdapter(), channelTopic());
        return container;
    }
}
