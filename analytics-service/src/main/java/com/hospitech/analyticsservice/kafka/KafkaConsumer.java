package com.hospitech.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

// Adding this annotation so that it can be managed by Spring
@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    // This sendEvent fn in Kafka Producer was sending data of type byte array to the topic
    // and since we are getting the data from the topic, so the type will be same
    // First we will specify the topic which we want to listen to/consume events from
    // To do that we will use Spring Kafka Annotation (and with this annotation we can connect
    // kafka consumer to kafka topic), the group id tells the kafka broker, who the consumer is
    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        // Now the first thing we need to do is to convert this byte array in to java object
        // to get the properties that we want
        // This PatientEvent type is imported from source code generated after creating the
        // protobuf file (This is coming from the target folder)
        // parseFrom method can return an exception, so that needs handling via try catch block
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            // Here we can perform any business logic related to analytics service

            log.info("Received Patient Event: [PatientId={}, PatientName={}, PatientEmail={}]",
                    patientEvent.getPatientId(), patientEvent.getName(), patientEvent.getEmail());

        } catch (InvalidProtocolBufferException e) {
            log.error("Error Deserializing Event {}", e.getMessage());
        }
    }
}
