import org.apache.avro.generic.GenericRecord
@Grapes([
        @GrabConfig(systemClassLoader=true),
        @Grab(group='org.apache.kafka', module='kafka-clients', version='2.0.0'),
        @Grab(group='org.apache.avro', module='avro', version='1.8.2'),
        @Grab(group='io.confluent', module='kafka-avro-serializer', version='3.1.1')
])

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.GenericData
import org.apache.avro.io.DatumReader
import org.apache.avro.io.Decoder
import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.Encoder
import org.apache.avro.io.EncoderFactory
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.avro.Schema

import java.text.SimpleDateFormat

KafkaProducer producer

def Properties producerProps = new Properties()
def SCHEMA_REGISTRY_URL = "schema.registry.url"

producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, '192.168.99.100:9092')
producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, '192.168.99.100:9092')

// Use these for Confluent produced events
//producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 'io.confluent.kafka.serializers.KafkaAvroSerializer')
//producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 'io.confluent.kafka.serializers.KafkaAvroSerializer')

// Use these for spring cloud stream produced events
producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 'org.apache.kafka.common.serialization.StringSerializer')
producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 'org.apache.kafka.common.serialization.ByteArraySerializer')
producerProps.put(SCHEMA_REGISTRY_URL, "http://192.168.99.100:8081");

producer = new KafkaProducer(producerProps)

def messageSender = { String topic, byte[] message ->
    println "Message : " + message
    println "Topic : " + topic
    String key = new Random().nextLong()
    producer.send(
            new ProducerRecord<String, byte[]>(topic, key, message),
            { RecordMetadata metadata, Exception e -> println "The offset of the record we just sent is: ${metadata.offset()}"
            } as Callback
    )
}

Schema schema = new Schema.Parser().parse(new File("..\\..\\main\\resources\\avro\\InputSCS.avsc"))

String kafkaTopic = "input-scs-destination"

sendGenericRecordMessage(schema, messageSender, kafkaTopic)

println "message sent"

producer.close()

void sendGenericRecordMessage(schema, messageSender, kafkaTopic) {
    GenericRecord genericRecord = new GenericData.Record(schema);
    genericRecord.put("scsName", "john")
    genericRecord.put("scsDescription", "john is here")
    println "sending genericRecord: " + genericRecord
    messageSender(kafkaTopic, datumToByteArray(schema, genericRecord))
}

static byte[] jsonAvroStringToByteArray(String json, Schema schema) throws IOException {
    DatumReader<Object> reader = new GenericDatumReader<>(schema)
    GenericDatumWriter<Object> writer = new GenericDatumWriter<>(schema)
    ByteArrayOutputStream output = new ByteArrayOutputStream()
    Decoder decoder = DecoderFactory.get().jsonDecoder(schema, json)
    Encoder encoder = EncoderFactory.get().binaryEncoder(output, null)
    Object datum = reader.read(null, decoder)
    writer.write(datum, encoder)
    encoder.flush()
    return output.toByteArray()
}

static byte[] datumToByteArray(Schema schema, GenericRecord datum) throws IOException {
    GenericDatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
        Encoder e = EncoderFactory.get().binaryEncoder(os, null);
        writer.write(datum, e);
        e.flush();
        byte[] byteData = os.toByteArray();
        return byteData;
    } finally {
        os.close();
    }
}

