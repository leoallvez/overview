package br.dev.singular.overview.presentation.ui.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
    val icon: ImageVector,
    val color: Color
) {
    ADVENTURE(12L, R.string.genre_adventure, Lucide.HandFist, Color(0xFFFF8A65)),
    FANTASY(14L, R.string.genre_fantasy, Lucide.Sparkles, Color(0xFFCE93D8)),
    ANIMATION(16L, R.string.genre_animation, Lucide.Rabbit, Color(0xFFFFEB3B)),
    DRAMA(18L, R.string.genre_drama, Lucide.Theater, Color(0xFF90CAF9)),
    HORROR(27L, R.string.genre_horror, Lucide.Ghost, Color(0xFF7EB2C2)),
    ACTION(28L, R.string.genre_action, Lucide.Zap, Color(0xFFFF7043)),
    COMEDY(35L, R.string.genre_comedy, Lucide.Smile, Color(0xFFF06292)),
    HISTORY(36L, R.string.genre_history, Lucide.History, Color(0xFFFFCC80)),
    WESTERN(37L, R.string.genre_western, Lucide.Sun, Color(0xFFFFC107)),
    THRILLER(53L, R.string.genre_thriller, Lucide.CircleAlert, Color(0xFF9FA8DA)),
    CRIME(80L, R.string.genre_crime, Lucide.ShieldAlert, Color(0xFFF54927)),
    DOCUMENTARY(99L, R.string.genre_documentary, Lucide.Video, Color(0xFFC963F8)),
    SCIENCE_FICTION(878L, R.string.genre_science_fiction, Lucide.Rocket, Color(0xFF80CBC4)),
    MYSTERY(9648L, R.string.genre_mystery, Lucide.Search, Color(0xFFB39DDB)),
    MUSIC(10402L, R.string.genre_music, Lucide.Music, Color(0xFFE6EE9C)),
    ROMANCE(10749L, R.string.genre_romance, Lucide.Heart, Color(0xFFF48FB1)),
    FAMILY(10751L, R.string.genre_family, Lucide.Users, Color(0xFFA5D6A7)),
    WAR(10752L, R.string.genre_war, Lucide.Sword, Color(0xFFC5E1A5)),
    ACTION_ADVENTURE(10759L, R.string.genre_action_adventure, Lucide.HandFist, Color(0xFFFF7043)),
    KIDS(10762L, R.string.genre_kids, Lucide.Baby, Color(0xFF81D4FA)),
    NEWS(10763L, R.string.genre_news, Lucide.Newspaper, Color(0xFF4A90E2)),
    REALITY(10764L, R.string.genre_reality, Lucide.Tv, Color(0xFFDCE775)),
    SCI_FI_FANTASY(10765L, R.string.genre_sci_fi_fantasy, Lucide.Rocket, Color(0xFFB39DDB)),
    SOAP(10766L, R.string.genre_soap, Lucide.Tv, Color(0xFFE1BEE7)),
    TALK(10767L, R.string.genre_talk, Lucide.Mic, Color(0xFFFFAB91)),
    WAR_POLITICS(10768L, R.string.genre_war_politics, Lucide.Landmark, Color(0xFF9FA8DA)),
    TV_MOVIE(10770L, R.string.genre_tv_movie, Lucide.Tv, Color(0xFFCFCF0A)),
    UNKNOWN(0L, 0, Lucide.Clapperboard, Color.White);

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

@Composable
fun GenreUiModel.getColor(): Color {
    return remember(id) { GenreType.fromId(id).color }
}
