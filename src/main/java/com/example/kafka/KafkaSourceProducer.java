package com.example.kafka;

import com.example.kafka.avro.OutputConfluent;
import com.example.kafka.avro.OutputSCS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Created by HurleyD on 08/08/2018.
 */
@Slf4j
@EnableBinding(KafkaOutputSource.class)
@EnableSchemaRegistryClient
public class KafkaSourceProducer {

    @Autowired
    private KafkaOutputSource kafkaOutputSource;

    public boolean produceConfluentMessage(OutputConfluent outputConfluent) {
        log.info("Producing Kafka message: " + outputConfluent.toString());

        try {
            kafkaOutputSource.outputConfluentMessageChannel()
                    .send(MessageBuilder.withPayload(outputConfluent)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, outputConfluent.getConfluentName())
                    .build());
        } catch (Exception e) {
            log.error("Failed to produce message: " + e);
            return false;
        }
        return true;
    }

    public boolean produceScsMessage(OutputSCS outputSCS) {
        log.info("Producing Kafka message: " + outputSCS.toString());

        try {
            kafkaOutputSource.outputScsMessageChannel()
                    .send(MessageBuilder.withPayload(outputSCS)
                            .setHeader(KafkaHeaders.MESSAGE_KEY, outputSCS.getScsName())
                            .build());
        } catch (Exception e) {
            log.error("Failed to produce message: " + e);
            return false;
        }
        return true;
    }

}
