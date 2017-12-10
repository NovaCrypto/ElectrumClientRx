/*
 *  ElectrumClientRx
 *  Copyright (C) 2017 Alan Evans, NovaCrypto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *  Original source: https://github.com/NovaCrypto/ElectrumClientRx
 *  You can contact the authors via github issues.
 */

package io.github.novacrypto

import com.google.gson.Gson
import io.github.novacrypto.electrum.Command
import org.junit.Test

import java.io.IOException

import org.junit.Assert.assertEquals

class ServerStubTests {

    internal inner class Response(private val id: Int, private val result: String) {

        override fun toString(): String {
            return Gson().toJson(this)
        }
    }

    @Test
    @Throws(IOException::class)
    fun canSetupAResponse() {
        val serverStub = ServerStub()
                .`when`(
                        { c -> c.method == "a" }
                ) { c -> Response(c.id, "a.response") }
        serverStub.input.println(Command.create(123, "a"))
        assertEquals(Response(123, "a.response").toString(), serverStub.outputBufferedReader.readLine())
    }

    @Test
    @Throws(IOException::class)
    fun canSetupAResponseWithStringDirect() {
        val serverStub = ServerStub()
                .`when`(
                        { c -> c.method == "a" }
                ) { c -> "{\"id\":1,\"method\":\"blockchain.numblocks.subscribe\",\"params\":[]}" }
        serverStub.input.println(Command.create(123, "a"))
        assertEquals("{\"id\":1,\"method\":\"blockchain.numblocks.subscribe\",\"params\":[]}",
                serverStub.outputBufferedReader.readLine())
    }

    @Test
    @Throws(IOException::class)
    fun canSetupTwoResponses() {
        val serverStub = ServerStub()
                .`when`(
                        { c -> c.id == 1 }
                ) { c -> Response(c.id, "x") }.`when`(
                { c -> c.id == 2 }
        ) { c -> Response(c.id, "y") }
        serverStub.input.println(Command.create(2, "a"))
        serverStub.input.println(Command.create(1, "a"))
        assertEquals(Response(2, "y").toString(), serverStub.outputBufferedReader.readLine())
        assertEquals(Response(1, "x").toString(), serverStub.outputBufferedReader.readLine())
    }

    @Test
    @Throws(IOException::class)
    fun canPutSomethingOnWireDirectly() {
        val serverStub = ServerStub()
        serverStub.printlnOnOutput(Response(123, "a.response"))
        assertEquals(Response(123, "a.response").toString(), serverStub.outputBufferedReader.readLine())
    }
}