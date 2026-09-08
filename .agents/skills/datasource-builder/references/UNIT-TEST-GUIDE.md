# Data Source Unit Test Guide

This reference provides the standard pattern for creating unit tests for Data Sources in the **Overview** project using **MockK**, **Kluent**, and **Kotlin Coroutines Test**.

## Testing Overview

Data Source unit tests verify that the raw data retrieval calls are correctly mapped to results (especially `DataResult` for remote) and that local interactions (DAOs) are correctly executed.

### Example: `AppleRemoteDataSourceTest.kt`

```kotlin
class AppleRemoteDataSourceTest {

    @MockK
    private lateinit var api: ApiService

    private lateinit var sut: IAppleRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = AppleRemoteDataSource(api)
    }

    @Test
    fun `getById should return Success when API returns success`() = runTest {
        // Given
        val id = 1L
        val body = AppleDataModel(id = id)
        val response = mockk<NetworkResponse.Success<AppleDataModel>>()
        every { response.body } returns body
        coEvery { api.getAppleById(id) } returns response

        // When
        val result = sut.getById(id)

        // Then
        assertTrue(result is DataResult.Success)
        (result as DataResult.Success).data shouldBeEqualTo body
        coVerify(exactly = 1) { api.getAppleById(id) }
    }
}
```

### Example: `AppleLocalDataSourceTest.kt`

```kotlin
class AppleLocalDataSourceTest {

    private val dao = mockk<AppleDao>(relaxed = true)
    private val sut = AppleLocalDataSource(dao)

    @Test
    fun `getById should return data from dao`() = runTest {
        // Given
        val id = 1L
        val expected = AppleDataModel(id = id)
        coEvery { dao.getById(id) } returns expected

        // When
        val result = sut.getById(id)

        // Then
        result shouldBeEqualTo expected
        coVerify(exactly = 1) { dao.getById(id) }
    }
}
```

### Key Requirements:

- **Mocking API:** For Remote Data Sources, use `mockk<NetworkResponse.Success<T>>()` to simulate Retrofit's `NetworkResponse`.
- **SUT Naming:** Use `sut` (System Under Test) and type it with the interface.
- **Coroutines:** Always use `runTest`.
- **Remote Results:** Assert that the result is an instance of `DataResult.Success` or `DataResult.Error`.
- **DAO Verification:** Use `coVerify` to ensure DAO methods were called with the expected parameters.
- **Location:** Place tests in `:data/src/test/java/...` mirroring the package structure.
