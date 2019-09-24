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

public class eICMPConfig implements IConfig, Serializable
{
    private short type;
    private short code;
    private int checksum = (int) AUTO_VALUE;
    private int id;
    private int seqNum;
    private String data;

    public eICMPConfig() {}

    public short getType()
    {
        return type;
    }

    public eICMPConfig setType(short type)
    {
        this.type = type;
        return this;
    }

    public short getCode()
    {
        return code;
    }

    public eICMPConfig setCode(short code)
    {
        this.code = code;
        return this;
    }

    public int getChecksum()
    {
        return checksum;
    }

    public eICMPConfig setChecksum(int checksum)
    {
        this.checksum = checksum;
        return this;
    }

    public boolean correctChecksumAuto()
    {
        return checksum == (int) AUTO_VALUE;
    }

    public int getId()
    {
        return id;
    }

    public eICMPConfig setId(int id)
    {
        this.id = id;
        return this;
    }

    public int getSeqNum()
    {
        return seqNum;
    }

    public eICMPConfig setSeqNum(int seqNum)
    {
        this.seqNum = seqNum;
        return this;
    }

    public String getData()
    {
        return data;
    }

    public eICMPConfig setData(String data)
    {
        this.data = data;
        return this;
    }
}
