# Repository Unit Test Guide

This reference provides the standard pattern for creating unit tests for Repositories in the **Overview** project using **MockK**, **Kluent**, and **Kotlin Coroutines Test**.

## Testing Overview

Repository unit tests verify the coordination between data sources, correct error handling, and proper mapping between data and domain models.

### Example: `AppleRepositoryTest.kt`

```kotlin
class AppleRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var remoteDataSource: IAppleRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var localDataSource: IAppleLocalDataSource

    private lateinit var sut: GetById<Apple>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = AppleRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun `should return domain object and save locally when remote returns success`() = runTest {
        // Given
        val id = 1L
        val dataModel = fakeAppleDataModel(id)
        coEvery { remoteDataSource.getById(id) } returns DataResult.Success(dataModel)

        // When
        val result = sut.getById(id)

        // Then
        result shouldBeEqualTo dataModel.toDomain()
        coVerify(exactly = 1) { remoteDataSource.getById(id) }
        coVerify(exactly = 1) { localDataSource.save(any()) }
    }

    @Test
    fun `should fallback to local storage when remote returns error`() = runTest {
        // Given
        val id = 1L
        val localData = fakeAppleEntity(id)
        coEvery { remoteDataSource.getById(id) } returns DataResult.Error()
        coEvery { localDataSource.getById(id) } returns localData

        // When
        val result = sut.getById(id)

        // Then
        result shouldBeEqualTo localData.toDomain()
        coVerify(exactly = 1) { remoteDataSource.getById(id) }
        coVerify(exactly = 1) { localDataSource.getById(id) }
    }
}
```

### Key Requirements:

- **Library Usage:** Use **Kluent** for assertions (e.g., `shouldBeEqualTo`, `shouldBeNull`).
- **Mocking:** Use `@MockK(relaxed = true)` for data sources to avoid boilerplate for non-relevant calls.
- **SUT Naming:** Use `sut` (System Under Test) and type it with the interface (e.g., `GetById<Apple>`).
- **Coroutines:** Use `runTest` to wrap the test execution.
- **Mapping Verification:** Ensure that the result matches the mapped domain model.
- **Coordination Check:** Use `coVerify` to ensure data sources are called in the expected order and frequency.
- **Failures:** Always test how the repository behaves when a data source returns an error or empty result.
- **Location:** Place tests in `:data/src/test/java/...` mirroring the package structure of the implementation.
