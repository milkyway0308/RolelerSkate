/*
 * Copyright 2014 jamietech. All rights reserved.
 * https://github.com/jamietech/MinecraftServerPing
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */
package br.com.azalim.mcserverping;

import com.google.common.base.Charsets;

/**
 * Storage class for {@link MinecraftPing} options.
 */

public class MCPingOptions {


    private String hostname;


    private String charset = Charsets.UTF_8.displayName();


    private int port = 25565;


    private int timeout = 5000;

    public String getHostname() {
        return hostname;
    }

    public MCPingOptions setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public MCPingOptions setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public int getPort() {
        return port;
    }

    public MCPingOptions setPort(int port) {
        this.port = port;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public MCPingOptions setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}
