package com.parcial_unidad1.pagos_service.controller;

import java.util.Collections;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.amazonaws.services.logs.model.CreateLogStreamRequest;
import com.amazonaws.services.logs.model.InputLogEvent;
import com.amazonaws.services.logs.model.PutLogEventsRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CloudWatchService {
    private static final Logger logger = LoggerFactory.getLogger(CloudWatchService.class);

    public void enviarLog(String mensaje) {
        try {
            String endpoint = System.getenv("AWS_ENDPOINT") != null ? System.getenv("AWS_ENDPOINT") : "http://localhost:4566";
            AWSLogs awsLogs = AWSLogsClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("test", "test")))
                    .build();

            String streamName = "pago-" + UUID.randomUUID().toString().substring(0, 8);
            awsLogs.createLogStream(new CreateLogStreamRequest("pagos", streamName));

            InputLogEvent evento = new InputLogEvent()
                    .withMessage(mensaje)
                    .withTimestamp(System.currentTimeMillis());

            PutLogEventsRequest request = new PutLogEventsRequest()
                    .withLogGroupName("pagos")
                    .withLogStreamName(streamName)
                    .withLogEvents(Collections.singletonList(evento));

            awsLogs.putLogEvents(request);
        } catch (Exception e) {
            logger.error("Error al enviar log de pago a CloudWatch: {}", e.getMessage());
        }
    }
}