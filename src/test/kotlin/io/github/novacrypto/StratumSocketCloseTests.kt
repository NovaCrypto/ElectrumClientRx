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

import io.github.novacrypto.electrum.Command
import org.junit.Rule
import org.junit.Test

class StratumSocketCloseTests {

    @get:Rule
    @Suppress("unused")
    val ioSchedulerTrampoline = ioSchedulerTrampoline()

    @Test
    fun `when close, stream completes`() {
        val socket = givenSocket(serverStub { })
        val sendRx = socket
                .sendRx(Command.create(1, "No response expected"))
        socket.close()
        sendRx.test().assertComplete()
    }

}