/*
 * Copyright 2020-2022 Alexengrig Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.alexengrig.codecov.maven;

import dev.alexengrig.codecov.maven.domain.ScriptArguments;
import dev.alexengrig.codecov.maven.exception.*;
import dev.alexengrig.codecov.maven.service.CommandExecutor;
import dev.alexengrig.codecov.maven.service.FileDownloader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

@Mojo(name = "upload", defaultPhase = LifecyclePhase.TEST)
public class CodecovUploadMojo extends AbstractMojo {
    private final FileDownloader fileDownloader = new FileDownloader();
    private final CommandExecutor commandExecutor = new CommandExecutor(this::info);

    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;
    @Parameter(property = "url", defaultValue = "https://codecov.io/bash")
    private URL url;
    @Parameter(property = "directory", defaultValue = "./")
    private File directory;
    @Parameter(property = "createDirectoryIfNotExists", defaultValue = "true")
    private boolean createDirectoryIfNotExists;
    @Parameter(property = "filename", defaultValue = "codecov.sh")
    private String filename;
    @Parameter(property = "shell", defaultValue = "bash")
    private String shell;
    @Parameter(property = "scriptArguments")
    private ScriptArguments scriptArguments;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            info("Skip is enable.");
            return;
        }
        info("Started.");
        requireScriptDirectoryExists();
        runScript();
        info("Finished.");
    }

    private void requireScriptDirectoryExists() throws ScriptDirectoryException {
        if (!directory.exists() && !createDirectoryIfNotExists) {
            throw new NoScriptDirectoryException(directory);
        }
        try {
            Files.createDirectories(directory.toPath());
        } catch (IOException e) {
            throw new CreateScriptDirectoryException(directory, e);
        }
    }

    private void runScript() throws FileDownloadException, CommandExecuteException, ScriptFailureException {
        String command = (shell + " " + getFile() + " " + scriptArguments.inline()).trim();
        info("Executing command: '%s'.", command);
        int exitCode = executeCommand(command);
        info("Executed command '%s' with exit code: %d.", command, exitCode);
    }

    private File getFile() throws FileDownloadException {
        File file = new File(directory, filename);
        if (!file.exists()) {
            info("Downloading file from %s to '%s'.", url, file);
            fileDownloader.download(url, file);
            info("Downloaded file: '%s'.", file);
        }
        return file;
    }

    private int executeCommand(String command) throws CommandExecuteException, ScriptFailureException {
        int exitCode = commandExecutor.execute(command);
        if (exitCode != 0) {
            throw new ScriptFailureException(command, exitCode);
        }
        return exitCode;
    }

    protected void info(String message) {
        getLog().info(message);
    }

    protected void info(String format, Object... args) {
        getLog().info(String.format(format, args));
    }
}
