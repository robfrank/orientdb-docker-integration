package com.orientechologies.integration;

import com.orientechnologies.orient.core.db.ODatabasePool;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import org.junit.*;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;

/**
 * Created by frank on 17/05/2017.
 */
public abstract class OSingleOrientDBServerBaseIT {

  private static final Logger LOGGER = LoggerFactory.getLogger(OSingleOrientDBServerBaseIT.class);

  @ClassRule
  public static GenericContainer container =
      new GenericContainer(new ImageFromDockerfile("orientdb/orietdb-it")
          .withFileFromPath("Dockerfile", Paths.get("./docker/Dockerfile"))
      )
          .withEnv("ORIENTDB_ROOT_PASSWORD", "root")
          .withExposedPorts(2480, 2424)
          .waitingFor(Wait.forListeningPort());

  @Rule
  public TestName name = new TestName();

  protected OrientDB      orientDB;
  protected ODatabasePool pool;

  @BeforeClass
  public static void beforeClass() throws Exception {
    System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOGGER);
    container.followOutput(logConsumer);
  }

  @Before
  public void setupOrientDBAndPool() throws Exception {

    String dbName = name.getMethodName();

    String serverUrl = "remote:" + container.getContainerIpAddress() + ":" + container.getMappedPort(2424);

    orientDB = new OrientDB(serverUrl, "root", "root", OrientDBConfig.defaultConfig());

    if (orientDB.exists(dbName))
      orientDB.drop(dbName);
    orientDB.createIfNotExists(dbName, ODatabaseType.PLOCAL);

    pool = new ODatabasePool(orientDB, dbName, "admin", "admin");
  }

  @After
  public void tearDown() throws Exception {
    pool.close();
    orientDB.close();

  }

}
