package br.dev.singular.overview.presentation.ui.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.GenreUiModel
import com.composables.icons.lucide.Baby
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Clapperboard
import com.composables.icons.lucide.Ghost
import com.composables.icons.lucide.HandFist
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.History
import com.composables.icons.lucide.Landmark
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mic
import com.composables.icons.lucide.Music
import com.composables.icons.lucide.Newspaper
import com.composables.icons.lucide.Rabbit
import com.composables.icons.lucide.Rocket
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.ShieldAlert
import com.composables.icons.lucide.Smile
import com.composables.icons.lucide.Sparkles
import com.composables.icons.lucide.Sun
import com.composables.icons.lucide.Sword
import com.composables.icons.lucide.Theater
import com.composables.icons.lucide.Tv
import com.composables.icons.lucide.Users
import com.composables.icons.lucide.Video
import com.composables.icons.lucide.Zap

@Composable
fun rememberCollapseScrollConnection(
    onCollapsedStateChange: (Boolean) -> Unit
) = remember {
    object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {

            val isCollapsed = consumed.y < 0
            onCollapsedStateChange(isCollapsed)

            return super.onPostScroll(consumed, available, source)
        }
    }
}

private enum class GenreType(
    val id: Long,
    @get:StringRes val labelRes: Int,
    val icon: ImageVector
) {
    ADVENTURE(12L, R.string.genre_adventure, Lucide.HandFist),
    FANTASY(14L, R.string.genre_fantasy, Lucide.Sparkles),
    ANIMATION(16L, R.string.genre_animation, Lucide.Rabbit),
    DRAMA(18L, R.string.genre_drama, Lucide.Theater),
    HORROR(27L, R.string.genre_horror, Lucide.Ghost),
    ACTION(28L, R.string.genre_action, Lucide.Zap),
    COMEDY(35L, R.string.genre_comedy, Lucide.Smile),
    HISTORY(36L, R.string.genre_history, Lucide.History),
    WESTERN(37L, R.string.genre_western, Lucide.Sun),
    THRILLER(53L, R.string.genre_thriller, Lucide.CircleAlert),
    CRIME(80L, R.string.genre_crime, Lucide.ShieldAlert),
    DOCUMENTARY(99L, R.string.genre_documentary, Lucide.Video),
    SCIENCE_FICTION(878L, R.string.genre_science_fiction, Lucide.Rocket),
    MYSTERY(9648L, R.string.genre_mystery, Lucide.Search),
    MUSIC(10402L, R.string.genre_music, Lucide.Music),
    ROMANCE(10749L, R.string.genre_romance, Lucide.Heart),
    FAMILY(10751L, R.string.genre_family, Lucide.Users),
    WAR(10752L, R.string.genre_war, Lucide.Sword),
    ACTION_ADVENTURE(10759L, R.string.genre_action_adventure, Lucide.HandFist),
    KIDS(10762L, R.string.genre_kids, Lucide.Baby),
    NEWS(10763L, R.string.genre_news, Lucide.Newspaper),
    REALITY(10764L, R.string.genre_reality, Lucide.Tv),
    SCI_FI_FANTASY(10765L, R.string.genre_sci_fi_fantasy, Lucide.Rocket),
    SOAP(10766L, R.string.genre_soap, Lucide.Tv),
    TALK(10767L, R.string.genre_talk, Lucide.Mic),
    WAR_POLITICS(10768L, R.string.genre_war_politics, Lucide.Landmark),
    TV_MOVIE(10770L, R.string.genre_tv_movie, Lucide.Tv),
    UNKNOWN(0L, 0, Lucide.Clapperboard);

    companion object {
        private val map = entries.associateBy { it.id }
        fun fromId(id: Long): GenreType = map[id] ?: UNKNOWN
    }
}

@Composable
fun GenreUiModel.localizedName(): String {
    val genre = remember(id) { GenreType.fromId(id) }
    if (genre.labelRes == 0) return name
    return stringResource(genre.labelRes).ifEmpty { name }
}

@Composable
fun GenreUiModel.getImageVector(): ImageVector {
    return remember(id) { GenreType.fromId(id).icon }
}
