package com.example.kafka;

import com.example.kafka.avro.InputConfluent;
import com.example.kafka.avro.InputSCS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

/**
 * Created by HurleyD on 08/08/2018.
 */
@EnableBinding(KafkaInputSink.class)
@EnableSchemaRegistryClient
@Slf4j
public class KafkaSinkConsumer {

    @StreamListener(KafkaInputSink.INPUT_CONFLUENT)
    public void processInputConfluent(InputConfluent input) {
        log.info("Consumed Confluent kafka event: " + input);
    }

    @StreamListener(KafkaInputSink.INPUT_SCS)
    public void processInputScs(InputSCS input) {
        log.info("Consumed SCS kafka event: " + input);
    }

}
