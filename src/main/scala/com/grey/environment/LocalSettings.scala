package com.grey.environment

import java.nio.file.Paths


class LocalSettings {

    // The operating system
    val operatingSystem: String = System.getProperty("os.name").toUpperCase()
    val operatingSystemWindows: Boolean = operatingSystem.startsWith("WINDOWS")

    // Local characteristics
    val projectDirectory: String = System.getProperty("user.dir")
    val localSeparator: String = System.getProperty("file.separator")

    // Local data directories
    val warehouseDirectory: String = Paths.get(projectDirectory, "warehouse").toString + localSeparator
    val resourcesDirectory: String = Paths.get(projectDirectory, "src", "main", "resources", "").toString
    val dataDirectory: String = Paths.get(projectDirectory, "data", "").toString
    val archiveDirectory = Paths.get(projectDirectory, "archive").toString

}
