package dev.alexengrig.plugin.maven.codecov;

import dev.alexengrig.plugin.maven.codecov.exception.CreateScriptDirectoryException;
import dev.alexengrig.plugin.maven.codecov.exception.NoScriptDirectoryException;
import dev.alexengrig.plugin.maven.codecov.exception.ScriptDirectoryException;
import dev.alexengrig.plugin.maven.codecov.service.FileDownloader;
import dev.alexengrig.plugin.maven.codecov.service.FileExecutor;
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

@Mojo(name = "coverage", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class CodecovCoverageMojo extends AbstractMojo {
    private final FileDownloader fileDownloader = new FileDownloader();
    private final FileExecutor fileExecutor = new FileExecutor(getLog()::info);

    @Parameter(name = "scriptUrl", defaultValue = "https://codecov.io/bash")
    private URL scriptUrl;
    @Parameter(name = "scriptDirectory", alias = "scriptDir", defaultValue = "./")
    private File scriptDirectory;
    @Parameter(name = "createScriptDirectoryIfNotExists", alias = "createScriptDirIfNotExists", defaultValue = "true")
    private boolean createScriptDirectoryIfNotExists;
    @Parameter(name = "scriptFilename", alias = "scriptName", defaultValue = "codecov.sh")
    private String scriptFilename;
    @Parameter(name = "projectToken", alias = "token", defaultValue = "${env.CODECOV_TOKEN}")
    private String projectToken;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Started.");
        requireScriptDirectoryExists();
        File file = new File(scriptDirectory, scriptFilename);
        getLog().info(String.format("Download file from %s to '%s'.", scriptUrl, file));
        fileDownloader.download(scriptUrl, file);
        getLog().info(String.format("Downloaded file: '%s'.", file));
        getLog().info(String.format("Execute file '%s'.", file));
        int exitCode = fileExecutor.execute(file, projectToken);
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
