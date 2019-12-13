package io.turntabl.platterworker.AWS

import java.nio.file.Path

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.PutObjectResult
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

object CloudStorage {
  private val s3client: AmazonS3 = connectionInstance
  private val bucketName = "platter-storage"


  def upload(timestamp: String, filename: String, path: Path): PutObjectResult = s3client.putObject( bucketName, s"${filename}${timestamp}.json",  path.toFile)

  private def connectionInstance = {
    val credentials = new BasicAWSCredentials("key", "secte" )
    val s3client = AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withRegion(Regions.EU_WEST_2) // eu-west-2
      .build()
    s3client
  }

}
