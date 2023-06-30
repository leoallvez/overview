package br.com.deepbyte.overview.abtesting

import br.com.deepbyte.overview.util.IJsonFileReader
import io.github.leoallvez.firebase.RemoteSource
import io.mockk.every
import io.mockk.impl.annotations.MockK

open class LocalAndRemoteTest {

    @MockK(relaxed = true)
    protected lateinit var remoteSource: RemoteSource

    @MockK(relaxed = true)
    protected lateinit var jsonFileReader: IJsonFileReader

    protected fun everyLocalAndRemote(local: String, remote: String) {
        every { jsonFileReader.read(any()) } returns local
        every { remoteSource.getString(any()) } returns remote
    }

    companion object {
        const val EMPTY = ""
        const val INVALID_JSON = "{/x/}"
    }
}
