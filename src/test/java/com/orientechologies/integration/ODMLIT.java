package com.orientechologies.integration;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.IntStream;

/**
 * Created by frank on 24/05/2017.
 */
public class ODMLIT extends OSingleOrientDBServerWithDatabasePerTestMethodBaseIT {

  @Test
  public void testCreateSchema() throws Exception {

    ODatabaseDocument db = pool.acquire();

    OClass personClass = db.createVertexClass("Person");

    personClass.createProperty("id", OType.INTEGER);
    personClass.createProperty("name", OType.STRING);
    personClass.createProperty("surname", OType.STRING);
    personClass.createProperty("birthDay", OType.DATETIME);

    IntStream.range(0, 10)
        .forEach(id -> {
              db.begin();
              OVertex aPerson = db.newVertex(personClass);

              aPerson.setProperty("id", id);
              aPerson.setProperty("name", "nameOf" + id);
              aPerson.setProperty("surname", "surnameOf" + id);
              aPerson.setProperty("birthDay", Date.from(LocalDate.of(2003, 4, 1).plusDays(id).atStartOfDay().toInstant(
                  ZoneOffset.UTC)).getTime());

              aPerson.save();
              db.commit();
            }

        );

    OResultSet resultSet = db.query("SELECT FROM Person order by birthDay DESC ");

    resultSet.vertexStream()

        .forEach(p -> System.out.println(p.toJSON()));

    resultSet.close();
    db.close();
  }
}

