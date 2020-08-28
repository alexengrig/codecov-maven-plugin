/*
 * Copyright 2020 Alexengrig Dev.
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

import dev.alexengrig.codecov.maven.exception.CreateScriptDirectoryException;
import dev.alexengrig.codecov.maven.exception.NoScriptDirectoryException;
import dev.alexengrig.codecov.maven.exception.ScriptDirectoryException;
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
    private final CommandExecutor commandExecutor = new CommandExecutor(getLog()::info);

    @Parameter(name = "scriptUrl", defaultValue = "https://codecov.io/bash")
    private URL scriptUrl;
    @Parameter(name = "scriptDirectory", alias = "scriptDir", defaultValue = "./")
    private File scriptDirectory;
    @Parameter(name = "createScriptDirectoryIfNotExists", alias = "createScriptDirIfNotExists", defaultValue = "true")
    private boolean createScriptDirectoryIfNotExists;
    @Parameter(name = "scriptFilename", alias = "scriptName", defaultValue = "codecov.sh")
    private String scriptFilename;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Started.");
        requireScriptDirectoryExists();
        File file = new File(scriptDirectory, scriptFilename);
        getLog().info(String.format("Downloading file from %s to '%s'.", scriptUrl, file));
        fileDownloader.download(scriptUrl, file);
        getLog().info(String.format("Downloaded file: '%s'.", file));
        getLog().info(String.format("Executing file '%s'.", file));
        String command = String.format("bash %s", file);
        int exitCode = commandExecutor.execute(command);
        getLog().info(String.format("Executed file '%s' with exit code: %d.", file, exitCode));
        getLog().info("Finished.");
    }

    private void requireScriptDirectoryExists() throws ScriptDirectoryException {
        if (!scriptDirectory.exists() && !createScriptDirectoryIfNotExists) {
            throw new NoScriptDirectoryException(scriptDirectory);
        }
        try {
            Files.createDirectories(scriptDirectory.toPath());
        } catch (IOException e) {
            throw new CreateScriptDirectoryException(scriptDirectory, e);
        }
    }
}
