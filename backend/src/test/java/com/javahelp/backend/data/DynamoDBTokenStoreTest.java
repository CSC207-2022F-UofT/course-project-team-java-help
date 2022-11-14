package com.javahelp.backend.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import com.amazonaws.regions.Regions;
import com.javahelp.model.token.Token;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link DynamoDBTokenStore}
 */
public class DynamoDBTokenStoreTest {

    String tableName = "javahelpBackendTokens";

    Regions regions = Regions.US_EAST_1;

    DynamoDBTokenStore db = new DynamoDBTokenStore(tableName, regions);

    @Test
    public void testCRUD() {
        Token created = new Token(Duration.ofDays(30), "Test Token", "test");
        String originalId = created.getToken();
        db.create(created);

        // check local id not equal to new id
        assertNotEquals(originalId, created.getToken());

        Token read = db.read(created.getToken());

        // check that read matches write
        assertEquals(created.getToken(), read.getToken());
        assertEquals(created.getTag(), read.getTag());
        assertEquals(created.getUserID(), read.getUserID());
        // can only guarantee equivalence to nearest millisecond
        assertEquals(created.getExpiryDate().toEpochMilli(), read.getExpiryDate().toEpochMilli());
        assertEquals(created.getIssuedDate().toEpochMilli(), read.getIssuedDate().toEpochMilli());

        read.setTag("Mutated Tag");

        db.update(read);

        Token newRead = db.read(read.getToken());

        // check that mutated tag matches
        assertEquals(read.getTag(), newRead.getTag());

        db.delete(newRead.getToken());

        // check token was deleted
        Token deleted = db.read(newRead.getToken());

        assertNull(deleted);
    }
}