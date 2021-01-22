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

import java.util.List;

/**
 * References: http://wiki.vg/Server_List_Ping
 * https://gist.github.com/thinkofdeath/6927216
 */

public class MCPingResponse {

    /**
     * @return the MOTD
     */
    private Description description;

    /**
     * @return @{link Players}
     */
    private Players players;

    /**
     * @return @{link Version}
     */
    private Version version;

    /**
     * @return Base64 encoded favicon image
     */
    private String favicon;

    /**
     * @return Ping in ms.
     */

    private long ping;

    public long getPing() {
        return ping;
    }

    public MCPingResponse setPing(long ping) {
        this.ping = ping;
        return this;
    }

    public Description getDescription() {
        return description;
    }

    public MCPingResponse setDescription(Description description) {
        this.description = description;
        return this;
    }

    public Players getPlayers() {
        return players;
    }

    public MCPingResponse setPlayers(Players players) {
        this.players = players;
        return this;
    }

    public Version getVersion() {
        return version;
    }

    public MCPingResponse setVersion(Version version) {
        this.version = version;
        return this;
    }

    public String getFavicon() {
        return favicon;
    }

    public MCPingResponse setFavicon(String favicon) {
        this.favicon = favicon;
        return this;
    }

    public class Description {

        /**
         * @return Server description text
         */
        private String text;

        public String getStrippedText() {
            return MCPingUtil.stripColors(this.text);
        }

        public String getText() {
            return text;
        }
    }


    public class Players {

        /**
         * @return Maximum player count
         */
        private int max;

        /**
         * @return Online player count
         */
        private int online;

        /**
         * @return List of some players (if any) specified by server
         */
        private List<Player> sample;

        public int getMax() {
            return max;
        }

        public Players setMax(int max) {
            this.max = max;
            return this;
        }

        public int getOnline() {
            return online;
        }

        public Players setOnline(int online) {
            this.online = online;
            return this;
        }

        public List<Player> getSample() {
            return sample;
        }

        public Players setSample(List<Player> sample) {
            this.sample = sample;
            return this;
        }
    }

    public class Player {

        /**
         * @return Name of player
         */
        private String name;

        /**
         * @return Unknown
         */
        private String id;

        public String getName() {
            return name;
        }

        public Player setName(String name) {
            this.name = name;
            return this;
        }

        public String getId() {
            return id;
        }

        public Player setId(String id) {
            this.id = id;
            return this;
        }
    }


    public class Version {

        /**
         * @return Version name (ex: 13w41a)
         */
        private String name;
        /**
         * @return Protocol version
         */
        private int protocol;

        public String getName() {
            return name;
        }

        public Version setName(String name) {
            this.name = name;
            return this;
        }

        public int getProtocol() {
            return protocol;
        }

        public Version setProtocol(int protocol) {
            this.protocol = protocol;
            return this;
        }
    }

}
