package ru.er_log.components;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/*
 * Describes each field of packet model and a whole packet.
 */
public class eConfig implements Serializable
{
    public static final int AUTO_VALUE_INTEGER = Integer.MIN_VALUE;

    private eEthernetConfig ethernetConfig = null;
    private eIPv4Config ipv4Config = null;
    private eTCPConfig tcpConfig = null;
    private eUDPConfig udpConfig = null;
    private eICMPConfig icmpConfig = null;

    private int selectedTabId = 0;

    private final String createTime;

    public eConfig()
    {
        this.createTime = Utils.getTime("dd.MM.YY HH:mm:ss");
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(createTime).append(": ");

        if (ethernetConfig != null) stringBuilder.append("Eth ");
        if (ipv4Config != null) stringBuilder.append("IPv4 ");
        if (tcpConfig != null) stringBuilder.append("TCP ");
        if (udpConfig != null) stringBuilder.append("UDP ");
        if (icmpConfig != null) stringBuilder.append("ICMP ");

        return stringBuilder.toString();
    }

    public int getSelectedTabId()
    {
        return selectedTabId;
    }

    public eConfig setSelectedTabId(int selectedTabId)
    {
        this.selectedTabId = selectedTabId;
        return this;
    }

    public eEthernetConfig getEthernetConfig()
    {
        return ethernetConfig;
    }

    public eConfig setEthernetConfig(eEthernetConfig ethernetConfig)
    {
        this.ethernetConfig = ethernetConfig;
        return this;
    }

    public eIPv4Config getIpv4Config()
    {
        return ipv4Config;
    }

    public eConfig setIpv4Config(eIPv4Config ipv4Config)
    {
        this.ipv4Config = ipv4Config;
        return this;
    }

    public eTCPConfig getTcpConfig()
    {
        return tcpConfig;
    }

    public eConfig setTcpConfig(eTCPConfig tcpConfig)
    {
        this.tcpConfig = tcpConfig;
        return this;
    }

    public eUDPConfig getUdpConfig()
    {
        return udpConfig;
    }

    public eConfig setUdpConfig(eUDPConfig udpConfig)
    {
        this.udpConfig = udpConfig;
        return this;
    }

    public eICMPConfig getIcmpConfig()
    {
        return icmpConfig;
    }

    public eConfig setIcmpConfig(eICMPConfig icmpConfig)
    {
        this.icmpConfig = icmpConfig;
        return this;
    }
}
