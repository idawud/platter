package io.turntabl.platterworker.AWS

import java.nio.file.Path

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

object CloudStorage {
  def upload(path: Path) = {
    val s3client: AmazonS3 = connectionInstance
    val bucketName = "platter-storage"
    s3client.putObject( bucketName, path.toString,  path.toFile)
  }

  private def connectionInstance = {
    val credentials = new BasicAWSCredentials("AKIAIJNSDYBK2CQFVL2A", "AqBMVTCbWlUSdUs2iIxnJEQ8EgUaXe0o5/D3LIXb")
    val s3client = AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withRegion(Regions.EU_WEST_2) // eu-west-2
      .build()
    s3client
  }

}
