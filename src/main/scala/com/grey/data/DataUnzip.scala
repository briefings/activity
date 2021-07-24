package com.grey.data

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.file.Paths
import java.util.zip.{ZipEntry, ZipInputStream}

/**
 * De-archives '.zip' files
 * https://www.baeldung.com/java-compress-and-uncompress
 *
 * @param directory: The directory wherein the archive extracts will be saved
 */
class DataUnzip(directory: String) {

    /**
     *
     * @param archiveString: The name of a zip file; it must include the path to its location an its extension
     * @return Boolean -> Unzipped successfully?
     */
    def dataUnzip(archiveString: String): Unit = {

        // Buffer
        val buffer: Array[Byte] = new Array[Byte](4096)

        // Create an archive input stream object
        val zipInputStream: ZipInputStream =  new ZipInputStream(new FileInputStream(archiveString))
        var zipEntry: ZipEntry = zipInputStream.getNextEntry

        // Whilst a file exists in the archive object
        while (zipEntry != null) {

            // The next file or directory in the archive
            val pathString = zipEntry.getName
            
            // Hence
            if (zipEntry.isDirectory | zipEntry.toString.split("/").length == 1){
                val fileObject = new File(Paths.get(directory, pathString).toString)
                fileObject.mkdirs()
            }
            else {
                
                val fileObject = new File(Paths.get(directory, pathString + ".csv").toString)
                
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
            }

            // Next entry
            zipEntry = zipInputStream.getNextEntry

        }

        // Close
        zipInputStream.closeEntry()
        zipInputStream.close()

    }

}
