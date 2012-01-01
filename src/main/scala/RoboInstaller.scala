package com.mobilemagic.scalainstaller

import android.content.Context
import android.util.Log
import java.io._

object RoboInstaller {
  def makeWritable() {
    Log.d(TAG, "Make /system writable")
    sudo("mount -o remount,rw /system")
  }

  def makeReadOnly() {
    Log.d(TAG, "Make /system read-only")
    sudo("mount -o remount,ro /system")
  }
  
  def sudo(cmd: String) {
    try {
      val proc: Process = runtime.exec(Array[String]("su", "-c", cmd))
      val res: Int = proc.waitFor
      if (res != 0) throw new RuntimeException("Execution of cmd '"+cmd+"' failed with exit code "+res)
    }
    catch {
      case e: Exception => {
        throw new RuntimeException(e)
      }
    }
  }

  final val TAG: String = "RoboInstaller"
  val resources = Array(R.raw.scala_actors_291,
                        R.raw.scala_actors_291_desc,
                        R.raw.scala_collection_291,
                        R.raw.scala_collection_291_desc,
                        R.raw.scala_immutable_291,
                        R.raw.scala_immutable_291_desc,
                        R.raw.scala_library_291,
                        R.raw.scala_library_291_desc,
                        R.raw.scala_mutable_291,
                        R.raw.scala_mutable_291_desc)

  val runtime: Runtime = Runtime.getRuntime
}

class RoboInstaller(ctx: Context) {

  private def performOperationOnSystem(op: => Unit) {
    try {
          RoboInstaller.makeWritable()
          op
        }
        catch {
          case e: Exception => {
            throw new RuntimeException(e)
          }
        }
        finally {
          RoboInstaller.makeReadOnly()
        }    
  }
  def installScalaLibs() {
    performOperationOnSystem {
      installFiles()
      makeLinks()
    }
  }
  
  def uninstallScalaLibs() {
    performOperationOnSystem {
      removeLinks()
    }
  }

  private def installFiles() {
    for (resid <- RoboInstaller.resources) installFile(resid)
  }

  private def makeLinks() {
    for (resid <- RoboInstaller.resources) {
      val path: File = fileForResource(resid)
      if (path.getName.endsWith("_desc.xml")) RoboInstaller.sudo("ln -s "+path.getAbsolutePath+" /system/etc/permissions/"+path.getName)
    }
  }

  private def removeLinks() {
    for (resid <- RoboInstaller.resources) {
      val path: File = fileForResource(resid)
      if (path.getName.endsWith("_desc.xml")) RoboInstaller.sudo("rm /system/etc/permissions/"+path.getName)
    }
  }

  /**
   * Takes the resource with the given name and installs it into the files dir
   * @param resid
   */
  private def installFile(resid: Int) {
    val targetFile: File = fileForResource(resid)
    Log.i(RoboInstaller.TAG, "Extracting file to " + targetFile.getAbsolutePath)
    val fos: FileOutputStream = new FileOutputStream(targetFile)
    val is: InputStream = ctx.getResources.openRawResource(resid)
    val buffer: Array[Byte] = new Array[Byte](65000)
    while (is.available > 0) {
      val read: Int = is.read(buffer)
      fos.write(buffer, 0, read)
    }
    is.close()
    fos.close()
    RoboInstaller.sudo("chmod 666 "+targetFile.getAbsolutePath)
  }

  private def fileForResource(resid: Int): File = {
    var namePart: String = lastPart(ctx.getResources.getResourceName(resid))
    if (namePart.endsWith("_desc")) namePart = namePart + ".xml"
    else namePart = namePart + ".jar"
    return new File(ctx.getFilesDir, namePart)
  }

  private def lastPart(path: String): String = {
    return path.substring(path.lastIndexOf("/") + 1)
  }
}

