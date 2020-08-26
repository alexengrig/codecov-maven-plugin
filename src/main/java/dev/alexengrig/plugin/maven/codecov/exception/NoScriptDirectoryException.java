package dev.alexengrig.plugin.maven.codecov.exception;

import java.io.File;

public class NoScriptDirectoryException extends ScriptDirectoryException {
    public NoScriptDirectoryException(File path) {
        super(String.format("Script directory does not exist: '%s'", path));
    }
}
