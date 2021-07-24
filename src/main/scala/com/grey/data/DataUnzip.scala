package com.grey.data

import java.io.{File, FileInputStream, FileOutputStream}
import java.util.zip.ZipInputStream
import scala.util.Try
import scala.util.control.Exception

/**
 * De-archives '.zip' files
 *
 * @param directory: The directory wherein the archive extracts will be saved
 */
class DataUnzip(directory: String) {

    /**
     *
     * @param archiveString: The name of a zip file; it must include the path to its location an its extension
     * @return Boolean -> Unzipped successfully?
     */
    def dataUnzip(archiveString: String): Boolean = {

        // Buffer
        val buffer = new Array[Byte](2048)

        // Create an archive input stream object
        val zipInputStream =  new ZipInputStream(new FileInputStream(archiveString))
        var zipEntry = zipInputStream.getNextEntry

        // Whilst a file exists in the archive object
        val dearchive: Try[Unit] = Exception.allCatch.withTry(

            while (zipEntry != null) {

                // The next file in the archive
                val fileName = zipEntry.getName
                val fileObject = new File(directory + fileName)

                // For cases whereby the archive has a directory structure
                if (!new File(fileObject.getParent).exists()) {
                    new File(fileObject.getParent).mkdirs()
                }

                // Herein, the file's contents are saved to disk
                val fileOutputStream = new FileOutputStream(fileObject)
                while (zipInputStream.read(buffer) > 0) {
                    fileOutputStream.write(buffer, 0, zipInputStream.read(buffer))
                }
                fileOutputStream.close()
                zipEntry = zipInputStream.getNextEntry

            }

        )
        zipInputStream.closeEntry()
        zipInputStream.close()

        // Status
        if (dearchive.isSuccess){
            dearchive.isSuccess
        } else {
            sys.error(dearchive.failed.get.getMessage)
        }

    }

}
