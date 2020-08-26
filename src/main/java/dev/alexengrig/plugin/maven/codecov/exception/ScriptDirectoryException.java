package dev.alexengrig.plugin.maven.codecov.exception;

import org.apache.maven.plugin.MojoFailureException;

public class ScriptDirectoryException extends MojoFailureException {
    public ScriptDirectoryException(String message) {
        super(message);
    }

    public ScriptDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
