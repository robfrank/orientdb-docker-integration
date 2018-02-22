package com.orientechologies.integration;

import org.junit.Test;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;

/**
 * Created by frank on 02/06/2017.
 */
public class OChrashIT {

  @Test
  public void crashServer() throws Exception {

    GenericContainer container =
        new GenericContainer("orientdb/orientdb-it:latest")
            .withEnv("ORIENTDB_ROOT_PASSWORD", "root")
            .withExposedPorts(2480, 2424)
            .waitingFor(Wait.forListeningPort());



    container.start();

    System.out.println("server started");

    Container.ExecResult result = container.execInContainer("ls");

    System.out.println("result.getStdout() = " + result.getStdout());

    System.out.println("container.isRunning() = " + container.isRunning());


  }
}
