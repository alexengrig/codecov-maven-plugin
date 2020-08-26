package dev.alexengrig.plugin.maven.codecov.exception;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

public class FileExecuteException extends MojoExecutionException {
    public FileExecuteException(File file, Exception cause) {
        super(String.format("Failed to execute file '%s'", file), cause);
    }
}
