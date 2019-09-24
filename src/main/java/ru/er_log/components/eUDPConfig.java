/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 + Copyright (c) 2019, Eldar Timraleev (aka CRaFT4ik).
 +
 + Licensed under the Apache License, Version 2.0 (the "License");
 + you may not use this file except in compliance with the License.
 + You may obtain a copy of the License at
 +
 +     http://www.apache.org/licenses/LICENSE-2.0
 +
 + Unless required by applicable law or agreed to in writing, software
 + distributed under the License is distributed on an "AS IS" BASIS,
 + WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 + See the License for the specific language governing permissions and
 + limitations under the License.
 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

package ru.er_log.components;

import java.io.Serializable;

public class eUDPConfig implements IConfig, Serializable
{
    private int srcPort;
    private int dstPort;
    private int length = (int) AUTO_VALUE;
    private int checksum = (int) AUTO_VALUE;
    private String data;

    public eUDPConfig() {}

    public int getSrcPort()
    {
        return srcPort;
    }

    public eUDPConfig setSrcPort(int srcPort)
    {
        this.srcPort = srcPort;
        return this;
    }

    public int getDstPort()
    {
        return dstPort;
    }

    public eUDPConfig setDstPort(int dstPort)
    {
        this.dstPort = dstPort;
        return this;
    }

    public int getLength()
    {
        return length;
    }

    public eUDPConfig setLength(int length)
    {
        this.length = length;
        return this;
    }

    public boolean correctLengthAuto()
    {
        return length == (int) AUTO_VALUE;
    }

    public int getChecksum()
    {
        return checksum;
    }

    public eUDPConfig setChecksum(int checksum)
    {
        this.checksum = checksum;
        return this;
    }

    public boolean correctChecksumAuto()
    {
        return checksum == (int) AUTO_VALUE;
    }

    public String getData()
    {
        return data;
    }

    public eUDPConfig setData(String data)
    {
        this.data = data;
        return this;
    }
}
