package io.github.leoallvez.take.data.source.tvshow

import io.github.leoallvez.take.data.db.dao.TvShowDao
import io.github.leoallvez.take.data.model.TvShow
import javax.inject.Inject

class TvShowLocalDataSource @Inject constructor(
    private val dao: TvShowDao
) {

    suspend fun save(vararg entities: TvShow) = dao.insert(*entities)

}
