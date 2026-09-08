# Mapper Unit Test Guide

This reference provides the standard pattern for creating unit tests for Mappers in the **Overview** project.

## Testing Overview

Mapper unit tests verify that every field of the source model is correctly transformed into the target model, ensuring no data loss or incorrect formatting.

### Example: `AppleMapperTest.kt`

```kotlin
class AppleMapperTest {

    @Test
    fun `toDomain should map all fields correctly`() {
        // Given
        val dataModel = AppleDataModel(id = 1, rawDescription = "Test")

        // When
        val result = dataModel.toDomain()

        // Then
        result.id shouldBeEqualTo 1
        result.description shouldBeEqualTo "Test"
    }

    @Test
    fun `toUi should format display label and build image URL`() {
        // Given
        val domain = Apple(id = 1, description = "Test")

        // When
        val result = domain.toUi()

        // Then
        result.id shouldBeEqualTo 1
        result.displayLabel shouldBeEqualTo "Apple: Test"
        result.imageURL.shouldNotBeEmpty()
    }
}
```

### Key Requirements:

- **Exhaustive Mapping:** Test that all properties are mapped.
- **Null Safety:** If fields are nullable, test the behavior when they are null (e.g., default values).
- **List Mapping:** Always include a test for mapping a `List` of objects.
- **Location:** Place tests in the corresponding `test` directory of the module, mirroring the implementation package.
