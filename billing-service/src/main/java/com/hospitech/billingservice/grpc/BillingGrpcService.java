package com.hospitech.billingservice.grpc;

// This package was generated when we compiled this service after creating the proto file
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
                                     StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received {}", billingRequest.toString());

        // Business logic - e.g save to database, perform calculations etc
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("12231")
                .setStatus("ACTIVE")
                .build();

        // This is sending data via gRPC instead of REST API
        responseObserver.onNext(response);  // sends the response where intended
        responseObserver.onCompleted();     // closes the connection, marking it complete
    }
}
