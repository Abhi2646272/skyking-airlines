package com.skyking.flight_service.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class GenericMapper {

    // Simple types remain the same
    private static final Set<Class<?>> SIMPLE_TYPES = Set.of(
            String.class, Integer.class, int.class,
            Long.class, long.class,
            Double.class, double.class,
            Float.class, float.class,
            Boolean.class, boolean.class
            // Add other simple types like BigDecimal, Date, UUID if needed
    );

    // The public entry point remains the same
    public static <T> T map(Object source, Class<T> targetClass) {
        if (source == null) return null;
        try {
            if (isSimple(targetClass) && targetClass.isAssignableFrom(source.getClass())) {
                return targetClass.cast(source);
            }

            // --- FIX START: Safely instantiate the target object ---

            // 1. Get the No-Argument Constructor
            java.lang.reflect.Constructor<T> constructor = targetClass.getDeclaredConstructor();

            // 2. Make the constructor accessible, even if it is private/protected
            constructor.setAccessible(true);

            // 3. Create the new instance
            T target = constructor.newInstance();
            // --- FIX END ---

            deepCopy(source, target, new HashSet<>());
            return target;
        } catch (Exception e) {
            // IMPORTANT: Check the root cause! The original exception (e.getCause())
            // will tell you exactly what failed (e.g., NoSuchMethodException).
            System.err.println("Root Cause of Mapping Failure: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            throw new RuntimeException("Mapping failed for: " + targetClass.getSimpleName(), e);
        }
    }
    private static void deepCopy(Object source, Object target, Set<Object> visited) {
        if (source == null || target == null) return;
        if (visited.contains(source)) return; // avoid recursion
        visited.add(source);

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        for (Field targetField : targetClass.getDeclaredFields()) {
            try {
                // Find source field
                Field sourceField = null;
                try {
                    sourceField = sourceClass.getDeclaredField(targetField.getName());
                } catch (NoSuchFieldException ignore) {}

                if (sourceField == null) continue;

                sourceField.setAccessible(true);
                targetField.setAccessible(true);

                Object sourceValue = sourceField.get(source);
                if (sourceValue == null) continue;

                // 1. Simple types → direct copy
                if (isSimple(sourceField.getType())) {
                    targetField.set(target, sourceValue);
                    continue;
                }

                // --- FIX START: Collection Handling ---
                if (sourceValue instanceof Collection) {
                    Collection<?> sourceCollection = (Collection<?>) sourceValue;

                    // 1a. Determine the generic type of the elements inside the collection
                    Class<?> targetElementType;
                    try {
                        ParameterizedType pt = (ParameterizedType) targetField.getGenericType();
                        targetElementType = (Class<?>) pt.getActualTypeArguments()[0];
                    } catch (ClassCastException e) {
                        // Handle raw collections or non-parameterized types gracefully
                        System.err.println("Warning: Collection field '" + targetField.getName() + "' is not parameterized. Skipping deep mapping.");
                        continue;
                    }

                    // 1b. Create a new concrete target collection (using ArrayList or HashSet)
                    Collection<Object> targetCollection;
                    if (targetField.getType().isAssignableFrom(Set.class)) {
                        targetCollection = new HashSet<>();
                    } else if (targetField.getType().isAssignableFrom(List.class)) {
                        targetCollection = new ArrayList<>();
                    } else {
                        // Attempt to instantiate the specific collection type if it has a default constructor
                        targetCollection = (Collection<Object>) targetField.getType().getDeclaredConstructor().newInstance();
                    }

                    // 1c. Iterate and map elements
                    for (Object element : sourceCollection) {
                        if (isSimple(targetElementType)) {
                            targetCollection.add(element);
                        } else {
                            // Recursively map complex elements (e.g., Leg -> LegDto)
                            targetCollection.add(map(element, targetElementType));
                        }
                    }

                    targetField.set(target, targetCollection);
                    continue;
                }
                // --- FIX END ---

                // 2. Complex single objects (original logic)
                Class<?> targetFieldType = targetField.getType();
                Class<?> sourceFieldType = sourceField.getType();

                // Example: Airline -> AirlineDto
                if (!targetFieldType.equals(sourceFieldType)) {
                    // Conversion (e.g., Domain Model to DTO)
                    Object nestedTarget = map(sourceValue, targetFieldType);
                    targetField.set(target, nestedTarget);
                } else {
                    // Deep cloning (Source object type equals Target object type)
                    Object clonedNested = targetFieldType
                            .getDeclaredConstructor()
                            .newInstance();
                    deepCopy(sourceValue, clonedNested, visited);
                    targetField.set(target, clonedNested);
                }

            } catch (Exception e) {
                // Re-throw with improved context
                throw new RuntimeException("Error mapping field: " + targetField.getName() + " of type " + targetField.getType().getSimpleName(), e);
            }
        }
    }

    private static boolean isSimple(Class<?> type) {
        return SIMPLE_TYPES.contains(type) || type.isPrimitive();
    }
}