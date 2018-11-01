package com.example.kafka;

import com.example.kafka.avro.OutputConfluent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by HurleyD on 31/10/2018.
 */
@Slf4j
@RestController
@Api(tags = "Kafka Test APIs")
public class KafkaController {

    @Autowired
    KafkaSourceProducer kafkaSourceProducer;

    @ApiOperation(value = "Output confluent message",
            notes = "Testing avro schema name and name space's organization."
    )
    @RequestMapping(value = {"/hello"}, method = RequestMethod.GET)
    ResponseEntity<String> helloWorld() {
        log.info("calling helloWorld");

        return new ResponseEntity<String>("hello world", HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Output confluent message",
            notes = "Testing avro schema name and name space's organization."
    )
    @RequestMapping(value = {"/outputConfluent"}, method = RequestMethod.POST)
    ResponseEntity<String> outputConfluentMessage(@RequestBody SampleData sampleData) {
        log.info("calling outputConfluentMessage : " + sampleData);

        OutputConfluent outputConfluent = OutputConfluent.newBuilder()
                .setConfluentName(sampleData.getName())
                .setConfluentDescription(sampleData.getDesc())
                .build();
        kafkaSourceProducer.produceConfluentMessage(outputConfluent);

        return new ResponseEntity<String>(sampleData.toString(), HttpStatus.ACCEPTED);
    }

}
