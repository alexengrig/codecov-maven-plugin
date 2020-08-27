package dev.alexengrig.plugin.maven.codecov.exception;

import org.apache.maven.plugin.MojoExecutionException;

public class CommandExecuteException extends MojoExecutionException {
    public CommandExecuteException() {
        super("Failed to execute command");
    }
}
