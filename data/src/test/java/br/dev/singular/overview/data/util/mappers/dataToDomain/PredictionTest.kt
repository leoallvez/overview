package br.dev.singular.overview.data.util.mappers.dataToDomain

import br.dev.singular.overview.data.model.PredictionDataModel
import br.dev.singular.overview.domain.model.Prediction
import org.junit.Assert.assertEquals
import org.junit.Test

class PredictionTest {

    @Test
    fun `PredictionDataModel toDomain should map affinity to available prediction`() {
        // arrange
        val dataModel = PredictionDataModel(affinityPercentage = 75)

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(Prediction.Available(affinityPercentage = 75), domainModel)
    }

    @Test
    fun `PredictionDataModel toDomain should map null affinity to error prediction`() {
        // arrange
        val dataModel = PredictionDataModel()

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(Prediction.Error, domainModel)
    }

    @Test
    fun `PredictionDataModel toDomain should limit affinity above the maximum`() {
        // arrange
        val dataModel = PredictionDataModel(affinityPercentage = 150)

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(Prediction.Available(affinityPercentage = 100), domainModel)
    }

    @Test
    fun `PredictionDataModel toDomain should limit affinity below the minimum`() {
        // arrange
        val dataModel = PredictionDataModel(affinityPercentage = -10)

        // act
        val domainModel = dataModel.toDomain()

        // assert
        assertEquals(Prediction.Available(affinityPercentage = 0), domainModel)
    }
}
