package com.grey.environment

import org.apache.commons.io.FileUtils

import java.io.File
import scala.util.Try
import scala.util.control.Exception

class Directories {

    // Reset
    def directoryReset(directoryName: String): Boolean = {

        // Delete
        val delete = directoryDelete(directoryName = directoryName)

        // Create
        if (delete) {
            directoryCreate(directoryName = directoryName)
        } else {
            // Superfluous because directoryDelete( ) will address all directoryDelete( ) errors
            false
        }

    }

    // Create
    def directoryCreate(directoryName: String): Boolean = {

        // Object
        val directoryObject = new File(directoryName)

        // Create
        val create: Try[Boolean] = Exception.allCatch.withTry(
            if (!directoryObject.exists()) {
                directoryObject.mkdir()
            } else {
                true
            }
        )

        // State
        if (create.isFailure){
            sys.error(create.failed.get.getMessage)
        } else {
            create.isSuccess
        }

    }

    // Delete
    def directoryDelete(directoryName: String): Boolean = {

        // Object
        val directoryObject = new File(directoryName)

        // Delete
        val delete: Try[Unit] = Exception.allCatch.withTry(
            if (directoryObject.exists()){
                FileUtils.deleteDirectory(directoryObject)
            }
        )

        // State
        if (delete.isFailure){
            sys.error(delete.failed.get.getMessage)
        } else {
            delete.isSuccess
        }

    }

}
