package com.javahelp.backend.data;

/**
 * Interface specifying generic CRUD operations for the provided type.
 * <p></p>
 * The provided type must have some unique, immutable piece of information that acts as an ID or
 * primary key. This piece of information makes up the second generic type argument to this
 * interface.
 * <p></p>
 * For instance, consider a {@code User} entity with a Base-64 {@link String} ID. A gateway to
 * fetch entities such as this from a DynamoDBDatabase could have a class declaration that
 * looks something like this:
 * <p></p>
 * {@code class DynamoUserGateway implements ICRUDGateway<User, String>}
 *
 * @param <T> type to specify CRUD operations for
 * @param <S> id/primary key type for CRUD type
 */
public interface ICRUDGateway<T, S> {

    /**
     * Creates a new object, and mutates its ID field to reflect the newly created object's ID.
     * @param object The object to create in the database
     * @return The created object, with the mutated ID
     */
    T create(T object);

    /**
     *
     * @param id ID of the object to get
     * @return the object with the specified ID
     */
    T read(S id);

    /**
     * Updates the specified object to match the provided object
     * @param object object to update, will update based on the ID field of this object
     */
    void update(T object);

    /**
     * Deletes the object with the specified ID
     * @param id ID of the object to delete
     */
    void delete(S id);

}
