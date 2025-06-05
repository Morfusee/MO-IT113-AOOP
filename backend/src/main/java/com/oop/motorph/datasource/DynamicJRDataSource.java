package com.oop.motorph.datasource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DynamicJRDataSource implements JRDataSource {

    private final Iterator<?> iterator;
    private Object currentRecord;

    public DynamicJRDataSource(List<?> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data list cannot be null.");
        }
        this.iterator = data.iterator();
    }

    @Override
    public boolean next() throws JRException {
        if (iterator.hasNext()) {
            currentRecord = iterator.next();
            return true;
        }
        currentRecord = null;
        return false;
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        String fieldName = field.getName();
        Object value = null; // Initialize value

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
                            // Try direct accessor (e.g., myField())
                            method = currentObject.getClass().getMethod(part);
                        } catch (NoSuchMethodException e) {
                            // Try traditional getter (e.g., getMyField())
                            String getterMethodName = "get" + part.substring(0, 1).toUpperCase() + part.substring(1);
                            method = currentObject.getClass().getMethod(getterMethodName);
                        }
                        currentObject = method.invoke(currentObject);
                    } catch (NoSuchMethodException e) {
                        // If no getter found for this part, the field doesn't exist at this level
                        System.err.println("Warning: No such field or getter method for '" + part + "' in "
                                + currentObject.getClass().getName() + " (while processing '" + fieldName + "')");
                        currentObject = null; // Mark as null to stop further processing
                        break;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new JRException("Error accessing field part '" + part + "' using reflection for field '"
                                + fieldName + "': " + e.getMessage(), e);
                    }
                }

                // If this is the last part, set the value
                if (i == fieldParts.length - 1) {
                    value = currentObject;
                }
            }
        } catch (JRException e) {
            throw e; // Re-throw JRException from within the loop
        } catch (Exception e) {
            // Catch any other unexpected exceptions and wrap them
            throw new JRException(
                    "Error in DynamicJRDataSource.getFieldValue for field '" + fieldName + "': " + e.getMessage(), e);
        }

        return value;
    }
}