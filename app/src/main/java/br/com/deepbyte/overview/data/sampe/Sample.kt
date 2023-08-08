package br.com.deepbyte.overview.data.sampe

import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie

val slideMediaSample = listOf<Media>(
    Movie(title = "Barbie").also { movie ->
        movie.backdropPath = "/nHf61UzkfFno5X1ofIhugCPus2R.jpg"
    },
    Movie(title = "Guardiões da Galáxia: Vol. 3").also { movie ->
        movie.backdropPath = "/5YZbUmjbMa3ClvSW1Wj3D6XGolb.jpg"
    },
    Movie(title = "Oppenheimer").also { movie ->
        movie.backdropPath = "/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg"
    },
    Movie(title = "The Flash").also { movie ->
        movie.backdropPath = "/yF1eOkaYvwiORauRCPWznV9xVvi.jpg"
    },
    Movie(title = "Sobrenatural: A Porta Vermelha").also { movie ->
        movie.backdropPath = "/i2GVEvltEu3BXn5crBSxgKuTaca.jpg"
    }
)
