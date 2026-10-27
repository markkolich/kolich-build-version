# kolich-build-version

The build number (short Git hash), timestamp, Maven version and Git branch of a running
application, as recorded by Maven when the application was built.

Builds with JDK 21+ but compiles with `--release 11`, so it runs on Java 11 and later.

## Usage

1. Add the dependency (published to `https://markkolich.github.io/repo`):

   ```xml
   <dependency>
       <groupId>com.kolich</groupId>
       <artifactId>kolich-build-version</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

2. Have the `buildnumber-maven-plugin` record the build, and filter `src/main/resources` so its
   values are written into the properties file:

   ```xml
   <!-- Only used by the buildnumber-maven-plugin to find the Git revision of the build. -->
   <scm>
       <connection>scm:git:file://${project.basedir}</connection>
   </scm>

   <build>
       <plugins>
           <plugin>
               <groupId>org.codehaus.mojo</groupId>
               <artifactId>buildnumber-maven-plugin</artifactId>
               <version>3.3.1</version>
               <executions>
                   <execution>
                       <id>create-buildnumber</id>
                       <phase>initialize</phase>
                       <goals>
                           <goal>create</goal>
                       </goals>
                       <configuration>
                           <doCheck>false</doCheck>
                           <doUpdate>false</doUpdate>
                           <!-- 7-characters to match Git's default "abbreviated commit hash" length. -->
                           <shortRevisionLength>7</shortRevisionLength>
                           <!-- An ISO-8601 compliant timestamp string (using SimpleDateFormat). -->
                           <timestampFormat>yyyy-MM-dd'T'HH:mm:ssXXX</timestampFormat>
                           <!-- Don't fail the build before the first commit exists. -->
                           <revisionOnScmFailure>unknown</revisionOnScmFailure>
                       </configuration>
                   </execution>
               </executions>
           </plugin>
       </plugins>

       <resources>
           <resource>
               <directory>src/main/resources</directory>
               <filtering>true</filtering>
           </resource>
       </resources>
   </build>
   ```

   Then add `src/main/resources/META-INF/build-version.properties`:

   ```properties
   buildNumber=${buildNumber}
   timestamp=${timestamp}
   version=${project.version}
   branch=${scmBranch}
   ```

3. Read it:

   ```java
   final BuildVersion buildVersion = BuildVersion.getInstance();
   buildVersion.getVersion();      // 1.0
   buildVersion.getBuildNumber();  // f596675
   ```

A property that's missing reads as `unknown`. If the file itself is missing,
`getInstance()` throws `IllegalStateException`.

## Build & Publish

```bash
mvn clean install    # tests, Checkstyle, PMD; installs into ~/.m2 and lays out dist/
```

`dist/kolich-build-version/<version>/` holds the JAR, sources JAR and POM with their `.md5` and
`.sha1` files. To publish, copy that directory into
`markkolich.github.io/repo/com/kolich/kolich-build-version/` and regenerate the indices.
