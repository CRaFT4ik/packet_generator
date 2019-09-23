package ru.er_log.components;

import ru.er_log.exceptions.NotFullConfigurationException;

import java.io.Serializable;

public class eEthernetConfig implements IConfig, Serializable
{
    private String srcMAC;
    private String dstMAC;

    public eEthernetConfig() {}

    @Override
    public void verify() throws NullPointerException
    {

    }

    public String getSrcMAC()
    {
        return srcMAC;
    }
    public eEthernetConfig setSrcMAC(String srcMAC)
    {
        this.srcMAC = srcMAC;
        return this;
    }
    public String getDstMAC()
    {
        return dstMAC;
    }
    public eEthernetConfig setDstMAC(String dstMAC)
    {
        this.dstMAC = dstMAC;
        return this;
    }
}
