package com.oop.motorph.datasource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * A custom JRDataSource implementation that allows dynamic retrieval of field
 * values
 * from a list of objects (POJOs, Records, or Maps) using reflection.
 * It supports nested properties accessed via dot notation (e.g.,
 * "employee.firstName").
 */
public class DynamicJRDataSource implements JRDataSource {

    private final Iterator<?> iterator;
    private Object currentRecord;

    /**
     * Constructs a DynamicJRDataSource with the given list of data.
     *
     * @param data The list of objects to be used as the data source for the report.
     * @throws IllegalArgumentException if the data list is null.
     */
    public DynamicJRDataSource(List<?> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data list cannot be null.");
        }
        this.iterator = data.iterator();
    }

    /**
     * Advances the data source to the next record.
     *
     * @return true if there is a next record, false otherwise.
     * @throws JRException if an error occurs while advancing the data source.
     */
    @Override
    public boolean next() throws JRException {
        if (iterator.hasNext()) {
            currentRecord = iterator.next();
            return true;
        }
        currentRecord = null; // No more records, set currentRecord to null
        return false;
    }

    /**
     * Retrieves the value of the specified field from the current record.
     * Supports accessing fields directly (for Maps) or via getter methods/accessor
     * methods (for Objects/Records).
     * Handles nested properties by splitting the field name by dots.
     *
     * @param field The JRField whose value is to be retrieved.
     * @return The value of the field.
     * @throws JRException if an error occurs during field value retrieval (e.g.,
     *                     reflection issues).
     */
    @Override
    public Object getFieldValue(JRField field) throws JRException {
        String fieldName = field.getName();
        Object value = null;

        if (currentRecord == null) {
            return null; // No current record to get a field from
        }

        // Split field name by dots to handle nested properties
        String[] fieldParts = fieldName.split("\\.");
        Object currentObject = currentRecord;

        try {
            for (int i = 0; i < fieldParts.length; i++) {
                String part = fieldParts[i];

                if (currentObject == null) {
                    // If any intermediate object is null, we can't go deeper
                    value = null;
                    break;
                }

                if (currentObject instanceof Map) {
                    // If the current object is a Map, directly get the value by key
                    currentObject = ((Map<?, ?>) currentObject).get(part);
                } else {
                    // Handle Object (DTO/Record) based data using Reflection
                    try {
                        Method method;
                        try {
                            // Try direct accessor (e.g., myField() for Java Records or direct field access)
                            method = currentObject.getClass().getMethod(part);
                        } catch (NoSuchMethodException e) {
                            // Try traditional getter (e.g., getMyField())
                            String getterMethodName = "get" + part.substring(0, 1).toUpperCase() + part.substring(1);
                            method = currentObject.getClass().getMethod(getterMethodName);
                        }
                        currentObject = method.invoke(currentObject);
                    } catch (NoSuchMethodException e) {
                        // If no getter/accessor found for this part, the field doesn't exist at this
                        // level
                        System.err.println("Warning: No such field or getter method for '" + part + "' in "
                                + currentObject.getClass().getName() + " (while processing '" + fieldName + "')");
                        currentObject = null; // Mark as null to stop further processing
                        break;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new JRException("Error accessing field part '" + part + "' using reflection for field '"
                                + fieldName + "': " + e.getMessage(), e);
                    }
                }

                // If this is the last part, set the final value
                if (i == fieldParts.length - 1) {
                    value = currentObject;
                }
            }
        } catch (JRException e) {
            throw e; // Re-throw JRException from within the loop
        } catch (Exception e) {
            // Catch any other unexpected exceptions and wrap them in a JRException
            throw new JRException(
                    "Error in DynamicJRDataSource.getFieldValue for field '" + fieldName + "': " + e.getMessage(), e);
        }

        return value;
    }
}