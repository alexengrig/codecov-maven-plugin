package dev.alexengrig.codecov.maven.exception;

import org.apache.maven.plugin.MojoExecutionException;

public class CommandExecuteException extends MojoExecutionException {
    public CommandExecuteException() {
        super("Failed to execute command");
    }
}
