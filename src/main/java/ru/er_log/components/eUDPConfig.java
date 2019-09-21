package ru.er_log.components;

import java.io.Serializable;

public class eUDPConfig implements Serializable
{
    private int srcPort;
    private int dstPort;
    private int length;
    private int checksum;
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
    public int getChecksum()
    {
        return checksum;
    }
    public eUDPConfig setChecksum(int checksum)
    {
        this.checksum = checksum;
        return this;
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
