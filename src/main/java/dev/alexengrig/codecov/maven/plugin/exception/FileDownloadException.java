package dev.alexengrig.codecov.maven.plugin.exception;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.net.URL;

public class FileDownloadException extends MojoExecutionException {
    public FileDownloadException(URL url, File file, Exception cause) {
        super(String.format("Failed to download file from %s to '%s'", url, file), cause);
    }
}
