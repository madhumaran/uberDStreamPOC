package config

import com.typesafe.config.ConfigFactory


/**
  * Created by madhu on 10/8/17.
  */
//single instance of class
//static class
object  Settings {
  //load instance of configuration library,load(parameter)--> load particular file
  private val config= ConfigFactory.load()
  object WebLogGen{
    //get application section name "clickstream"
    private val weblogGen=config.getConfig("clickstream")

    lazy val filePath = weblogGen.getString("file_path")

    lazy val destinationFilePath = weblogGen.getString("destination_file_path")

    lazy val checkpointDirectory = weblogGen.getString("checkpointDirectory")

  }
}
