package com.orientechologies.integration;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by frank on 17/05/2017.
 */
public class OSequenceAsAutoincrementFieldIT extends OSingleOrientDBServerBaseIT {

  @Before
  public void setUp() throws Exception {

    ODatabaseDocument db = orientDB.open(name.getMethodName(), "admin", "admin");

    String sql = Files.lines(Paths.get("./src/test/resources/sequence_id_test.osql"), Charset.forName("UTF-8"))
        .collect(Collectors.joining("\n"));

    db.execute("sql", sql);

    db.close();

  }

  @Test
  public void shouldInsertWithoutDuplicatesWithSQL() throws Exception {

    ODatabaseDocument db = pool.acquire();

    for (int i = 0; i < 1000; i++) {

      String name = "first +  last" + new Random().nextDouble();
      OResultSet resultSet = db.command("insert into User set name = ? ", name);

      resultSet.close();
    }
    db.close();
    verifyUniqueIds();
  }

  @Test
  public void shouldInsertWithoutDuplicatesWithMultiModel() throws Exception {

    ODatabaseDocument db = pool.acquire();
    for (int i = 0; i < 1000; i++) {

      //FIXME: this reload is here only to read the sequence value!!!!!
      db.getMetadata().reload();
      OVertex vertex = db.newVertex("User");
      vertex.setProperty("name", "first +  last" + new Random().nextDouble());
      db.save(vertex);

    }
    db.close();
    verifyUniqueIds();
  }

  private void verifyUniqueIds() {

    ODatabaseDocument db = pool.acquire();
    OResultSet vertices = db.query("select from User");
    List<Long> ids = new ArrayList<>();

    vertices.vertexStream()
        .forEach(v -> {
              Long id = v.getProperty("id");
              Assertions.assertThat(ids).doesNotContain(id);
              ids.add(id);

            }

        );

    db.close();
  }

}
