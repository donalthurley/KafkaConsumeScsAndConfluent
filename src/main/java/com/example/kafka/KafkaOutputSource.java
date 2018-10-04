package com.example.kafka;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created by HurleyD on 08/08/2018.
 */
public interface KafkaOutputSource {
    public static final String OUTPUT_SCS = "outputSCS";

    public static final String OUTPUT_CONFLUENT = "outputConfluent";

    @Output(OUTPUT_SCS)
    MessageChannel outputScsMessageChannel();

    @Output(OUTPUT_CONFLUENT)
    MessageChannel outputConfluentMessageChannel();

}
