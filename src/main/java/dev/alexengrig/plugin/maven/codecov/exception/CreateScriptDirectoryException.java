package dev.alexengrig.plugin.maven.codecov.exception;

import java.io.File;

public class CreateScriptDirectoryException extends ScriptDirectoryException {
    public CreateScriptDirectoryException(File path, Exception cause) {
        super(String.format("Failed to create script directory: '%s'", path), cause);
    }
}
