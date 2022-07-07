package com.example.demo;

import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class IntegrationTestRunner {

    private final String configFile;
    private final String testRunRunnerCliCommand;
    private final String workDirectory;
    static Map<String, String> ENV_MAP = System.getenv();

    private static final String INTEGRATION_TEST_HOME = IntegrationTestRunner.class.getResource("/integration-test").getPath();
    private static final String DEFAULT_WORK_DIRECTORY = deduceDefaultWorkDirectory();

    public static String HTTP_RUNNER_PATH = Paths.get(INTEGRATION_TEST_HOME, "httprunner").toString();
    public static String DEFAULT_CONFIG_FILE =
            Paths.get(HTTP_RUNNER_PATH, ENV_MAP.getOrDefault("INTEGRATION_TEST_CONFIG_FILE", ".integration.yaml")).toString();

    public IntegrationTestRunner(String configFile, String testRunRunnerCliCommand, String workingDirectory) {
        this.configFile = configFile;
        this.testRunRunnerCliCommand = testRunRunnerCliCommand;
        this.workDirectory = workingDirectory;
    }


    public IntegrationTestRunner() {
        this(
                DEFAULT_CONFIG_FILE,
                deduceTestRunnerCliCommand(),
                DEFAULT_WORK_DIRECTORY
        );
    }


    private static String deduceTestRunnerCliCommand() {
        Path pyBin = Paths.get(DEFAULT_WORK_DIRECTORY, "venv", "bin", "python");
        if (pyBin.toFile().isFile()) {
            return "venv/bin/python -m testrunner.cli";
        } else {
            return "python3 -m testrunner.cli";
        }
    }

    private static String deduceDefaultWorkDirectory() {
        Path path = Paths.get(INTEGRATION_TEST_HOME, "runner-cli");
        if (path.toFile().exists()) {
            return path.toString();
        } else {
            return ".";
        }
    }


    public void afterAll() throws Exception {
        String cmd = String.format(
                "%s destroy-env " +
                        "--config_file %s",
                this.testRunRunnerCliCommand,
                this.configFile
        );
        runProcess(cmd, this.workDirectory);
    }

    public void beforeAll() throws Exception {
        String cmd = String.format(
                "%s init-env --config_file %s",
                this.testRunRunnerCliCommand,
                this.configFile
        );
        runProcess(cmd, this.workDirectory);
    }

    private static void runProcess(String cmd, String workingDirectory) throws InterruptedException, IOException {
        Map<String, String> env = new HashMap<>(System.getenv());
        if (!System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            env.put("PATH", env.get("PATH") + ":" + "/usr/local/bin");
        }
        List<String> envArray = new ArrayList<>(env.size());
        for (String key : env.keySet()) {
            envArray.add(key + "=" + env.get(key));
        }
        Process process = Runtime.getRuntime().exec(
                cmd,
                envArray.toArray(new String[]{}),
                new File(workingDirectory)
        );
        int ret = process.waitFor();
        System.out.println(StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8));
        System.err.println(StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8));
        assertEquals(0, ret);
    }

    public void runSingleTest(String httpRunnerTestFile) throws Exception {
        String cmd = String.format(
                "%s just-run %s --config_file %s",
                this.testRunRunnerCliCommand,
                httpRunnerTestFile,
                this.configFile
        );
        runProcess(cmd, this.workDirectory);
    }

    public void runDirectoryTest(String dir) throws Exception {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            throw new FileNotFoundException(dir);
        }
        File[] files = dirFile.listFiles();
        for (File file : Objects.requireNonNull(files)) {
            if (file.isDirectory()) {
                runDirectoryTest(file.getPath());
            } else {
                runSingleTest(file.getPath());
            }
        }
    }

    public void runTests() throws Exception {
        String dir;
        dir = System.getenv("INTEGRATION_TEST_DIR");
        if (dir == null) {
            dir = Paths.get(HTTP_RUNNER_PATH, "/testcases").toString();
        }
        runDirectoryTest(dir);
    }


}
