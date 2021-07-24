package com.grey.data

import com.grey.environment.LocalSettings

import java.io.{File, FileInputStream, FileOutputStream}
import java.util.zip.ZipInputStream


/**
 * De-archives '.zip' files
 */
class DataUnzip {

    private val localSettings = new LocalSettings()
    private val dataDirectory = localSettings.dataDirectory
    private val archiveDirectory = localSettings.archiveDirectory

    /**
     *
     * https://www.baeldung.com/java-compress-and-uncompress
     * @param archiveString: The name of a zip file
     */
    def dataUnzip(archiveString: String): Unit = {

        // Buffer
        val buffer = new Array[Byte](2048)

        // Create an archive input stream object
        val zipInputStream =  new ZipInputStream(new FileInputStream(archiveDirectory + archiveString))
        var zipEntry = zipInputStream.getNextEntry

        // Whilst a file exists in the archive object
        while (zipEntry != null) {

            // The next file in the archive
            val fileName = zipEntry.getName
            val fileObject = new File(dataDirectory + fileName)

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
        zipInputStream.closeEntry()
        zipInputStream.close()

    }

}
