package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.model.PredictionDataModel
import br.dev.singular.overview.data.model.PredictionInputDataModel
import br.dev.singular.overview.data.network.JevApiService
import br.dev.singular.overview.data.network.request.JevFavorites
import br.dev.singular.overview.data.network.request.JevMedia
import br.dev.singular.overview.data.network.request.JevQuestion
import br.dev.singular.overview.data.network.request.JevRequest
import br.dev.singular.overview.data.network.request.JevState
import br.dev.singular.overview.data.network.request.JevTitle
import br.dev.singular.overview.data.network.response.JevResponse
import javax.inject.Inject
import kotlin.math.roundToInt

interface IPredictionRemoteDataSource {
    suspend fun getByMedia(input: PredictionInputDataModel): DataResult<PredictionDataModel>
}

class PredictionRemoteDataSource @Inject constructor(
    private val api: JevApiService
) : IPredictionRemoteDataSource {

    override suspend fun getByMedia(
        input: PredictionInputDataModel
    ): DataResult<PredictionDataModel> {
        return when (val result = responseToResult(api.decide(input.toRequest()))) {
            is DataResult.Success -> DataResult.Success(
                PredictionDataModel(affinityPercentage = result.data.toPercentage())
            )
            is DataResult.Error -> DataResult.Error()
        }
    }

    private fun PredictionInputDataModel.toRequest() = JevRequest(
        model = BuildConfig.JEV_MODEL,
        state = JevState(
            media = JevMedia(originalTitle = title, type = type, genres = genres),
            favorites = JevFavorites(
                titles = favorites.map { JevTitle(originalTitle = it.title, type = it.type) },
                genres = favoriteGenres
            )
        ),
        questions = mapOf(AFFINITY to AFFINITY_QUESTION)
    )

    private fun JevResponse.toPercentage(): Int? {
        val score = answers[AFFINITY]?.score ?: return null
        return (score / AFFINITY_QUESTION.criteria.lastIndex * 100).roundToInt()
    }

    private companion object {
        const val AFFINITY = "affinity"
        val AFFINITY_QUESTION = JevQuestion(
            type = "score",
            instructions = "Based on the user's favorite titles and genres, " +
                "how much would the user enjoy the media?",
            criteria = listOf(
                "The user would not enjoy it at all",
                "The user would probably not enjoy it",
                "The user might enjoy it",
                "The user would probably enjoy it",
                "The user would certainly enjoy it"
            )
        )
    }
}
