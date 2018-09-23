package at.lber.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Connection {

    private static AmazonS3 connection;

    private static AWSCredentials credentials;

    private static AwsClientBuilder.EndpointConfiguration epc = new AwsClientBuilder.EndpointConfiguration("http://localhost:9444/s3", "eu-central-1");

    public static AmazonS3 getConnection() throws NoConnectionException {
        if(connection == null){
            if(credentials != null) {
                connection = AmazonS3ClientBuilder
                        .standard()
                        .withEndpointConfiguration(epc)
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withPathStyleAccessEnabled(true)
                        .build();
            } else {
                throw new NoConnectionException("No credentials entered!");
            }
        }
        return connection;
    }

    public static void setCredentials(String accessKey, String secret){
        credentials = new BasicAWSCredentials(
                accessKey, secret
        );
    }

}
