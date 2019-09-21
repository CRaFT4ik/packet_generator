package ru.er_log.components;

import java.io.Serializable;

public class eIPv4Config implements Serializable
{
    private short version;
    private short ihl;
    private tos tos;
    private int length;
    private int id;
    private flags flags;
    private short ttl;
    private int checksum;
    private String srcIP;
    private String dstIP;
    private long options;

    public eIPv4Config() { }

    public short getVersion()
    {
        return version;
    }
    public eIPv4Config setVersion(short version)
    {
        this.version = version;
        return this;
    }
    public short getIhl()
    {
        return ihl;
    }
    public eIPv4Config setIhl(short ihl)
    {
        this.ihl = ihl;
        return this;
    }
    public eIPv4Config.tos getTos()
    {
        return tos;
    }
    public eIPv4Config setTos(eIPv4Config.tos tos)
    {
        this.tos = tos;
        return this;
    }
    public int getLength()
    {
        return length;
    }
    public eIPv4Config setLength(int length)
    {
        this.length = length;
        return this;
    }
    public int getId()
    {
        return id;
    }
    public eIPv4Config setId(int id)
    {
        this.id = id;
        return this;
    }
    public eIPv4Config.flags getFlags()
    {
        return flags;
    }
    public eIPv4Config setFlags(eIPv4Config.flags flags)
    {
        this.flags = flags;
        return this;
    }
    public short getTtl()
    {
        return ttl;
    }
    public eIPv4Config setTtl(short ttl)
    {
        this.ttl = ttl;
        return this;
    }
    public int getChecksum()
    {
        return checksum;
    }
    public eIPv4Config setChecksum(int checksum)
    {
        this.checksum = checksum;
        return this;
    }
    public String getSrcIP()
    {
        return srcIP;
    }
    public eIPv4Config setSrcIP(String srcIP)
    {
        this.srcIP = srcIP;
        return this;
    }
    public String getDstIP()
    {
        return dstIP;
    }
    public eIPv4Config setDstIP(String dstIP)
    {
        this.dstIP = dstIP;
        return this;
    }
    public long getOptions()
    {
        return options;
    }
    public eIPv4Config setOptions(long options) { this.options = options; return this; }

    public static class tos implements Serializable
    {
        private final precedence Pre;
        private final boolean D, T, R, C, x;

        public tos(precedence Pre, boolean D, boolean T, boolean R, boolean C, boolean x)
        {
            this.Pre = Pre;
            this.D = D;
            this.T = T;
            this.R = R;
            this.C = C;
            this.x = x;
        }

        public precedence getPre()
        {
            return Pre;
        }
        public boolean isD()
        {
            return D;
        }
        public boolean isT()
        {
            return T;
        }
        public boolean isR()
        {
            return R;
        }
        public boolean isC()
        {
            return C;
        }
        public boolean isX()
        {
            return x;
        }

        public enum precedence
        {
            _000((short) 0, "000 - routine"),
            _001((short) 1, "001 - priority"),
            _010((short) 2, "010 - immediate"),
            _011((short) 3, "011 - flash"),
            _100((short) 4, "100 - flash override"),
            _101((short) 5, "101 - critical"),
            _110((short) 6, "110 - internetwork control"),
            _111((short) 7, "111 - network control");

            private short value;
            private String description;

            precedence(short value, String description)
            {
                this.value = value;
                this.description = description;
            }

            public short getValue() { return value; }

            @Override
            public String toString()
            {
                return description;
            }
        }
    }

    public static class flags implements Serializable
    {
        private final boolean x, D, M;

        public flags(boolean x, boolean D, boolean M)
        {
            this.x = x;
            this.D = D;
            this.M = M;
        }

        public boolean isX()
        {
            return x;
        }

        public boolean isD()
        {
            return D;
        }

        public boolean isM()
        {
            return M;
        }
    }
}