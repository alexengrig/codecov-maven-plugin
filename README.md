# codecov-maven-plugin [![Maven Central](https://img.shields.io/maven-central/v/dev.alexengrig/codecov-maven-plugin/1.0)](https://search.maven.org/artifact/dev.alexengrig/codecov-maven-plugin/1.0/maven-plugin) [![JavaDoc](https://javadoc.io/badge2/dev.alexengrig/codecov-maven-plugin/javadoc.svg)](https://javadoc.io/doc/dev.alexengrig/codecov-maven-plugin) [![GitHub](https://img.shields.io/github/license/alexengrig/codecov-maven-plugin)](LICENSE)

[![travis-ci](https://travis-ci.com/alexengrig/codecov-maven-plugin.svg?branch=master)](https://travis-ci.com/alexengrig/codecov-maven-plugin)
[![codecov](https://codecov.io/gh/alexengrig/codecov-maven-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/alexengrig/codecov-maven-plugin)

Upload reports to [Codecov](https://codecov.io/) based on [bash script](https://github.com/codecov/codecov-bash).

## Get Started

Add this code (using JaCoCo Plugin for generate report) to your `pom.xml`:

```xml
<build>
    <plugins>
        <!-- JaCoCo Plugin -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.5</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <!-- Codecov Plugin -->
        <plugin>
            <groupId>dev.alexengrig</groupId>
            <artifactId>codecov-maven-plugin</artifactId>
            <version>1.0</version>
            <executions>
                <execution>
                    <id>upload</id>
                    <phase>test</phase>
                    <goals>
                        <goal>upload</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Add `CODECOV_TOKEN` with your 
[token](https://docs.codecov.io/docs/about-the-codecov-bash-uploader#upload-token)
of Codecov project to
[environment variables](https://docs.travis-ci.com/user/environment-variables/#defining-variables-in-repository-settings)
in your TravisCI project repository.

## License

This project is [licensed](LICENSE) under [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
