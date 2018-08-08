package com.example.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

/**
 * Created by HurleyD on 08/08/2018.
 */
public interface KafkaInputSink {
    public static final String INPUT_CONFLUENT = "inputConfluent";

    public static final String INPUT_SCS = "inputSCS";

    @Input(INPUT_CONFLUENT)
    MessageChannel inputConfluentMessageChannel();

    @Input(INPUT_SCS)
    MessageChannel inputScsMessageChannel();

}
