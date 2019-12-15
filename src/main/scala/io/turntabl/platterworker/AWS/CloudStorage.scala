package io.turntabl.platterworker.AWS

import java.nio.charset.StandardCharsets
import java.nio.file.Path

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.{ObjectListing, PutObjectResult}
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

import scala.collection.mutable

object CloudStorage {
  private val s3client: AmazonS3 = connectionInstance
  private val bucketName = "platter-storage"

  def listObjects(): List[String] = {
    val objectListing: ObjectListing = s3client.listObjects(bucketName)
    import scala.collection.JavaConversions._
    val res = objectListing.getObjectSummaries map( x => x.getKey)
    res.toList
  }

  def contentofObject(objectPath: String): String = {
    val s3object = s3client.getObject(bucketName, objectPath)
    val inputStream = s3object.getObjectContent
    val content: Array[Byte] = inputStream.readAllBytes()
    val str = new String(content, StandardCharsets.UTF_8)
    str
  }

  def upload(timestamp: String, filename: String, path: Path): PutObjectResult = s3client.putObject( bucketName, s"${filename}${timestamp}.json",  path.toFile)

  private def connectionInstance = {
    val credentials = new BasicAWSCredentials("AKIAJQPLSTRZCD3ZRBNQ", "mmnVkm/9OAcYtpVnMVf+bmqXs33oyXyFbUxfizmw" )
    val s3client = AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withRegion(Regions.EU_WEST_2) // eu-west-2
      .build()
    s3client
  }

}
