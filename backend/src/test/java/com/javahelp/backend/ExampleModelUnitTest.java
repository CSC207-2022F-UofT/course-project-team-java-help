package com.javahelp.backend;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.ExampleEntity;

import org.junit.Test;

/**
 * Example unit test that uses the model from inside the backend module
 */
public class ExampleModelUnitTest {

    @Test
    public void exampleEntity_works() {
        assertEquals("model in backend", new ExampleEntity("model in backend").name);
    }

}
