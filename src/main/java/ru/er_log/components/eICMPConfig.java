package ru.er_log.components;

import java.io.Serializable;

public class eICMPConfig implements IConfig, Serializable
{
    private short type;
    private short code;
    private int checksum;
    private int id;
    private int seqNum;
    private String data;

    public eICMPConfig() {}

    @Override
    public void verify() throws NullPointerException
    {

    }

    public short getType() { return type; }
    public eICMPConfig setType(short type){ this.type = type; return this; }
    public short getCode() { return code; }
    public eICMPConfig setCode(short code) { this.code = code; return this; }
    public int getChecksum() { return checksum; }
    public eICMPConfig setChecksum(int checksum) { this.checksum = checksum; return this; }
    public int getId() { return id; }
    public eICMPConfig setId(int id) { this.id = id; return this; }
    public int getSeqNum() { return seqNum; }
    public eICMPConfig setSeqNum(int seqNum) { this.seqNum = seqNum; return this; }
    public String getData() { return data; }
    public eICMPConfig setData(String data) { this.data = data; return this; }
}
