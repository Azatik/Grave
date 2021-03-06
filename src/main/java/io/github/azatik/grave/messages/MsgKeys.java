/*
 * This file is part of Grave, licensed under the MIT License (MIT).
 *
 * Copyright (c) Azatik <http://azatik.github.io>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.azatik.grave.messages;

import io.github.azatik.grave.configuration.ConfigManagerMsg;
import java.util.HashMap;
import java.util.Map;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class MsgKeys {

    public static final String graveLocationKey = "graveLocation";
    public static final String helpMsgInGraveCmdKey = "helpMsgInGraveCmd";
    public static Map<String, String> messagesMap = new HashMap<>();

    public MsgKeys() {
        ConfigManagerMsg fileMsg = new ConfigManagerMsg();
        CommentedConfigurationNode config = fileMsg.getConfig();
        messagesMap.put(graveLocationKey, config.getNode("Grave", graveLocationKey).getString());
        messagesMap.put(helpMsgInGraveCmdKey, config.getNode("Grave", helpMsgInGraveCmdKey).getString());
    }
}
