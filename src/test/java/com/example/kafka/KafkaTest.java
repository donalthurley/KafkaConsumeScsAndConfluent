package com.example.kafka;

import com.example.kafka.avro.InputConfluent;
import com.example.kafka.avro.InputSCS;
import com.example.kafka.avro.OutputConfluent;
import com.example.kafka.avro.OutputSCS;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KafkaApplication.class)
@Import(SchemaRegistryStubbedTestConfiguration.class)
public class KafkaTest {

    @Autowired
    private KafkaOutputSource kafkaOutputSource;

    @Autowired
    private KafkaInputSink kafkaInputSink;

    @Autowired
    private MessageCollector messageCollector;

    @Test
    public void testInputScs() throws Exception {
        InputSCS inputSCS = InputSCS.newBuilder()
                .setScsName("testScsName")
                .setScsDescription("testScsDescription")
                .build();
        Message<InputSCS> inputSscMessage = new GenericMessage<InputSCS>(inputSCS);
        kafkaInputSink.inputScsMessageChannel().send(inputSscMessage);

//        Message<Object> received = (Message<Object>) messageCollector.forChannel(kafkaOutputSource.outputScsMessageChannel()).poll();
//        assertNotNull(received);
//
//        OutputSCS outputSCS = (OutputSCS) getGenericRecord(received, OutputSCS.getClassSchema());
//        assertEquals("testScsName", outputSCS.getScsName().toString());
//        assertEquals("testScsDescription", outputSCS.getScsDescription().toString());
    }

    @Test
    public void testInputConfluent() throws Exception {
        InputConfluent inputConfluent = InputConfluent.newBuilder()
                .setConfluentName("testConfluentName")
                .setConfluentDescription("testConfluentDescription")
                .build();
        Message<InputConfluent> inputConfluentMessage = new GenericMessage<InputConfluent>(inputConfluent);
        kafkaInputSink.inputConfluentMessageChannel().send(inputConfluentMessage);

//        Message<Object> received = (Message<Object>) messageCollector.forChannel(kafkaOutputSource.outputConfluentMessageChannel()).poll();
//        assertNotNull(received);
//
//        OutputConfluent outputConfluent = (OutputConfluent) getGenericRecord(received, OutputConfluent.getClassSchema());
//        assertEquals("testConfluentName", outputConfluent.getConfluentName().toString());
//        assertEquals("testConfluentDescription", outputConfluent.getConfluentDescription().toString());
    }

    private GenericRecord getGenericRecord(Message<Object> received, Schema schema) {
        GenericRecord genericRecord = null;
        try {
            byte[] received_message = (byte[]) received.getPayload();
            System.out.println(received_message);
            DatumReader<GenericRecord> reader = new SpecificDatumReader<GenericRecord>(schema);
            Decoder decoder = DecoderFactory.get().binaryDecoder(received_message, null);
            genericRecord = reader.read(null, decoder);
            System.out.println("Message received : " + genericRecord);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return genericRecord;
    }

}
