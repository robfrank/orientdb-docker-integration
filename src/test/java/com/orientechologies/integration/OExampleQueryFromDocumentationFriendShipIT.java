package com.orientechologies.integration;

import com.orientechnologies.orient.core.db.ODatabasePool;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by frank on 24/05/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OExampleQueryFromDocumentationFriendShipIT extends OSingleOrientDBServerBaseIT {

  @Before
  public void setUp() throws Exception {
    String serverUrl = "remote:" + container.getContainerIpAddress() + ":" + container.getMappedPort(2424);

    orientDB = new OrientDB(serverUrl, "root", "root", OrientDBConfig.defaultConfig());

    pool = new ODatabasePool(orientDB, "demodb", "admin", "admin");

  }

  @After
  public void tearDown() throws Exception {
    pool.close();
    orientDB.close();
  }


  @Test
  public void test_Friendship_Example_1() throws Exception {
    ODatabaseDocument db = pool.acquire();

    OResultSet resultSet = db.query(

        "MATCH {Class: Profiles, as: profile, where: (Name='Santo' AND Surname='OrientDB')}-HasFriend-{Class: Profiles, as: friend}  RETURN $pathelements"
    );

    assertThat(resultSet)
        .hasSize(20);

    resultSet.close();
    db.close();

  }
}
