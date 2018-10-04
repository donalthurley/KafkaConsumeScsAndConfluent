import org.apache.avro.generic.GenericData
@Grapes([
        @GrabConfig(systemClassLoader=true),
        @Grab(group='org.apache.kafka', module='kafka-clients', version='2.0.0'),
        @Grab(group='org.apache.avro', module='avro', version='1.8.2'),
        @Grab(group='io.confluent', module='kafka-avro-serializer', version='3.1.1')
])


import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.Decoder
import org.apache.avro.io.DecoderFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.avro.Schema

def Properties consumerProps = new Properties()
def SCHEMA_REGISTRY_URL = "schema.registry.url"
Schema schema = new Schema.Parser().parse(new File("..\\..\\main\\resources\\avro\\InputSCS.avsc"));

consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, '192.168.99.100:9092')
consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, '192.168.99.100:9092')
consumerProps.put(SCHEMA_REGISTRY_URL, "http://192.168.99.100:8081");

consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 'org.apache.kafka.common.serialization.StringDeserializer')
consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 'org.apache.kafka.common.serialization.ByteArrayDeserializer')
//consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer");
//consumerProps.put("specific.avro.reader", true);
consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "1")

consumer = new KafkaConsumer(consumerProps)

consumer.subscribe(["input-scs-destination"])

println "polling"
this.consumer.poll(0);

println "seek to beginning"
this.consumer.seekToBeginning(consumer.assignment())

while (true) {
    ConsumerRecords<String, String> records = consumer.poll(500)
    for (ConsumerRecord<String, String> record : records) {
        println "offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}"
        GenericRecord genericRecord = byteArrayToDatum(schema, record.value())
        println "orgRefId = " + genericRecord.get("orgRefId")
    }
}

static GenericRecord byteArrayToDatum(Schema schema, byte[] byteData) {
    GenericDatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);
    ByteArrayInputStream byteArrayInputStream = null;
    try {
        byteArrayInputStream = new ByteArrayInputStream(byteData);
        Decoder decoder = DecoderFactory.get().binaryDecoder(byteArrayInputStream, null);
        return reader.read(null, decoder);
    } catch (IOException e) {
        return null;
    } finally {
        try {
            byteArrayInputStream.close();
        } catch (IOException e) {

        }
    }
}