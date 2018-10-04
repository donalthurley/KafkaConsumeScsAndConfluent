package com.example.kafka;

import com.example.kafka.avro.InputConfluent;
import com.example.kafka.avro.InputSCS;
import com.example.kafka.avro.OutputConfluent;
import com.example.kafka.avro.OutputSCS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    KafkaSourceProducer kafkaSourceProducer;

    @StreamListener(KafkaInputSink.INPUT_CONFLUENT)
    public void processInputConfluent(InputConfluent inputConfluent) {
        log.info("Consumed Confluent kafka event: " + inputConfluent);

        OutputConfluent outputConfluent = OutputConfluent.newBuilder()
                .setConfluentName(inputConfluent.getConfluentName())
                .setConfluentDescription(inputConfluent.getConfluentDescription())
                .build();

        kafkaSourceProducer.produceConfluentMessage(outputConfluent);

        log.info("Consumed Confluent kafka event: " + outputConfluent);
    }

    @StreamListener(KafkaInputSink.INPUT_SCS)
    public void processInputScs(InputSCS inputSCS) {
        log.info("Consumed SCS kafka event: " + inputSCS);

        OutputSCS outputSCS = OutputSCS.newBuilder()
                .setScsName(inputSCS.getScsName())
                .setScsDescription(inputSCS.getScsDescription())
                .build();

        kafkaSourceProducer.produceScsMessage(outputSCS);

        log.info("Consumed SCS kafka event: " + inputSCS);
    }

}
