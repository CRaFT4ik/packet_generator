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

public class eTCPConfig implements IConfig, Serializable
{
    private int srcPort;
    private int dstPort;
    private long seqNum;
    private long askNum;
    private short offset = (short) AUTO_VALUE;
    private reserved reserved;
    private flags flags;
    private int windowSize;
    private int checksum = (int) AUTO_VALUE;
    private int urgent;
    private long options;
    private String data;

    public eTCPConfig() {}

    public int getSrcPort()
    {
        return srcPort;
    }

    public eTCPConfig setSrcPort(int srcPort)
    {
        this.srcPort = srcPort;
        return this;
    }

    public int getDstPort()
    {
        return dstPort;
    }

    public eTCPConfig setDstPort(int dstPort)
    {
        this.dstPort = dstPort;
        return this;
    }

    public long getSeqNum()
    {
        return seqNum;
    }

    public eTCPConfig setSeqNum(long seqNum)
    {
        this.seqNum = seqNum;
        return this;
    }

    public long getAskNum()
    {
        return askNum;
    }

    public eTCPConfig setAskNum(long askNum)
    {
        this.askNum = askNum;
        return this;
    }

    public short getOffset()
    {
        return offset;
    }

    public eTCPConfig setOffset(short offset)
    {
        this.offset = offset;
        return this;
    }

    public boolean correctOffsetAuto()
    {
        return offset == (short) AUTO_VALUE;
    }

    public eTCPConfig.reserved getReserved()
    {
        return reserved;
    }

    public eTCPConfig setReserved(eTCPConfig.reserved reserved)
    {
        this.reserved = reserved;
        return this;
    }

    public eTCPConfig.flags getFlags()
    {
        return flags;
    }

    public eTCPConfig setFlags(eTCPConfig.flags flags)
    {
        this.flags = flags;
        return this;
    }

    public int getWindowSize()
    {
        return windowSize;
    }

    public eTCPConfig setWindowSize(int windowSize)
    {
        this.windowSize = windowSize;
        return this;
    }

    public int getChecksum()
    {
        return checksum;
    }

    public eTCPConfig setChecksum(int checksum)
    {
        this.checksum = checksum;
        return this;
    }

    public boolean correctChecksumAuto()
    {
        return checksum == (int) AUTO_VALUE;
    }

    public int getUrgent()
    {
        return urgent;
    }

    public eTCPConfig setUrgent(int urgent)
    {
        this.urgent = urgent;
        return this;
    }

    public long getOptions()
    {
        return options;
    }

    public eTCPConfig setOptions(long options)
    {
        this.options = options;
        return this;
    }

    public String getData()
    {
        return data;
    }

    public eTCPConfig setData(String data)
    {
        this.data = data;
        return this;
    }

    public static class reserved implements Serializable
    {
        private final boolean bit0;
        private final boolean bit1;
        private final boolean bit2;
        private final boolean bit3;
        private final boolean bit4;
        private final boolean bit5;

        public reserved(boolean bit_0, boolean bit_1, boolean bit_2, boolean bit_3, boolean bit_4, boolean bit_5)
        {
            this.bit0 = bit_0;
            this.bit1 = bit_1;
            this.bit2 = bit_2;
            this.bit3 = bit_3;
            this.bit4 = bit_4;
            this.bit5 = bit_5;
        }

        public byte toByte()
        {
            byte value = 0x0;

            value |= (isBit0() ? 1 : 0) << 5;
            value |= (isBit1() ? 1 : 0) << 4;
            value |= (isBit2() ? 1 : 0) << 3;
            value |= (isBit3() ? 1 : 0) << 2;
            value |= (isBit4() ? 1 : 0) << 1;
            value |= (isBit5() ? 1 : 0);

            return value;
        }

        public boolean isBit0()
        {
            return bit0;
        }

        public boolean isBit1()
        {
            return bit1;
        }

        public boolean isBit2()
        {
            return bit2;
        }

        public boolean isBit3()
        {
            return bit3;
        }

        public boolean isBit4()
        {
            return bit4;
        }

        public boolean isBit5()
        {
            return bit5;
        }
    }

    public static class flags implements Serializable
    {
        private final boolean u;
        private final boolean a;
        private final boolean p;
        private final boolean r;
        private final boolean s;
        private final boolean f;

        public flags(boolean U, boolean A, boolean P, boolean R, boolean S, boolean F)
        {
            u = U;
            a = A;
            p = P;
            r = R;
            s = S;
            f = F;
        }

        public boolean isU()
        {
            return u;
        }

        public boolean isA()
        {
            return a;
        }

        public boolean isP()
        {
            return p;
        }

        public boolean isR()
        {
            return r;
        }

        public boolean isS()
        {
            return s;
        }

        public boolean isF()
        {
            return f;
        }
    }
}
