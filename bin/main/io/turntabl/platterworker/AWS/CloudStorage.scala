package io.turntabl.platterworker.AWS

import java.nio.file.Path

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.{ObjectListing, PutObjectResult}
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

object CloudStorage {
  private val s3client: AmazonS3 = connectionInstance
  private val bucketName = "platter-storage"

  def listObjects(): List[String] = {
    val objectListing: ObjectListing = s3client.listObjects(bucketName)
    import scala.collection.JavaConversions._
    val res = objectListing.getObjectSummaries map( x => x.getKey)
    res.toList
  }

  def contentOfObject(objectPath: String): String = {
    val s3object = s3client.getObject(bucketName, objectPath)
    val inputStream = s3object.getObjectContent
    scala.io.Source.fromInputStream(inputStream).mkString
  }

  def upload(timestamp: String, filename: String, path: Path): PutObjectResult = s3client.putObject( bucketName, s"${filename}${timestamp}.json",  path.toFile)

  private def connectionInstance = {
    // val credentials = new BasicAWSCredentials(System.getenv("AWS_KEY"), System.getenv("AWS_SECRET") )
    val credentials = new BasicAWSCredentials("AKIAJQPLSTRZCD3ZRBNQ", "mmnVkm/9OAcYtpVnMVf+bmqXs33oyXyFbUxfizmw" )

    val s3client = AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withRegion(Regions.EU_WEST_2) // eu-west-2
      .build()
    s3client
  }

}
