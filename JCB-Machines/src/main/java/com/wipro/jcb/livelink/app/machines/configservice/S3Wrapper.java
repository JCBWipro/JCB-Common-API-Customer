package com.wipro.jcb.livelink.app.machines.configservice;

//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLEncoder;
//import java.util.List;
//
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.GetObjectRequest;
//import com.amazonaws.services.s3.model.ListObjectsRequest;
//import com.amazonaws.services.s3.model.ObjectListing;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.PutObjectResult;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import com.amazonaws.services.s3.model.S3ObjectSummary;


@Service
public class S3Wrapper {
//	@Autowired
//	private AmazonS3 amazonS3Client;
//	@Value("${cloud.aws.s3.bucket}")
//	private String bucket;
//	
//	@Value("${cloud.aws.endpoint}")
//	private String s3Url;
//
//	public PutObjectResult upload(InputStream inputStream, String uploadKey) throws IOException {
//		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, inputStream, new ObjectMetadata());
//		PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);
//		IOUtils.closeQuietly(inputStream);
//		return putObjectResult;
//	}
//
//	public ResponseEntity<byte[]> download(String key) {
//		GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
//		S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
//		S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
//		byte[] bytes;
//		try {
//			bytes = IOUtils.toByteArray(objectInputStream);
//			String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");
//			HttpHeaders httpHeaders = new HttpHeaders();
//			httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//			httpHeaders.setContentLength(bytes.length);
//			httpHeaders.setContentDispositionFormData("attachment", fileName);
//			return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void delete(String key) {
//		amazonS3Client.deleteObject(bucket, key);
//	}
//
//	public List<S3ObjectSummary> list() {
//		ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest().withBucketName(bucket));
//		List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();
//		return s3ObjectSummaries;
//	}
//
//	public void uploadFileToS3Bucket(File file, boolean enablePublicReadAccess,String filepath) {
//		try {
//			
//			PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,filepath,file);
//			PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);
//		
//			
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	public void deleteInFolder(String key) {
//		try {
//			ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
//	                .withBucketName(bucket)
//	                .withPrefix(key);
//			
//	        ObjectListing objectListing = amazonS3Client.listObjects(listObjectsRequest);
//	        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
//	        	System.out.println("Testse "+objectSummary.getKey());
//				amazonS3Client.deleteObject(bucket, objectSummary.getKey());
//			}  
//	       }
//			catch(Exception e) {
//				e.printStackTrace();
//				
//			}
//		
//	}

}
