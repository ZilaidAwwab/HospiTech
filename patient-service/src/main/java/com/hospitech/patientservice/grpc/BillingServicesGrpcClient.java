// This class will act as a gRPC client, and will be able to communicate with the gRPC server

package com.hospitech.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServicesGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(BillingServicesGrpcClient.class);
    /**
     * We will declare the variable to hold the grpc client
     *
     * This is the nested class within the billing service grpc class that provides blocking
     * or synchronous client calls to grpc server that is running in the billing service
     *
     * This means that anytime we make calls to the billing service using this blocking stub
     * that the execution is going to wait for the response to come back from the billing service
     * before it continues (similar to REST calls, just that it is grpc)
     */
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    // localhost:9001/BillingService/CreatePatientAccount (In local Environment)
    // aws.grpc:23221/BillingService/CreatePatientAccount (In production Environment)
    public BillingServicesGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort
    ) {
        // This is just for indication
        log.info("Connecting to Billing Service GRPC at {}:{}", serverAddress, serverPort);

        // Here we are now doing the actual blocking stub work
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(serverAddress, serverPort).usePlaintext().build();
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public void createBillingAccount(String patientId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId).setName(name).setEmail(email).build();

        // blocking stub is our grpc client
        BillingResponse response = blockingStub.createBillingAccount(request);

        log.info("Received response from billing service via gRPC: {}", response);
    }
}
