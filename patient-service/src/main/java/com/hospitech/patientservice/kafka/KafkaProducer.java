// This class will be responsible for producing the events into the Kafka topic

package com.hospitech.patientservice.kafka;

import com.hospitech.patientservice.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

// Adding this Service annotation means that spring will manage this class for us
@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    // First thing that we need in this class is the Kafka template
    // This is how we define our message type, and we use kafka template to send those messages
    // Here the String represents the "Key" of the message and byte[] represents the "Value" of msg
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    // We can either add the autowired annotation on the field directly or autowire it like this,
    // to use it in this file
    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // This message needs to be sent when the patient is created, so it will be used in the
    // PatientService class, in the create patient method
    public void sendEvent(Patient patient) {
        // This PatientEvent type is coming from the Code that got created when we defined and
        // compiled the proto file for this message
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                // The setType is used to define a particular event inside a topic
                // like this is for patient created, but there could be updated event as well
                // that will be sent to the same topic, but discretion is done via setting event type like this
                .setEventType("PATIENT_CREATED")
                .build();

        // Till this point everything is being set, except sending the event to the kafka topic
        try {
            kafkaTemplate.send("patient", event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending PatientCreated event: {}", event);
        }
    }
}
