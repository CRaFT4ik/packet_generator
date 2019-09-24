package ru.er_log.components;

import ru.er_log.utils.Utils;

import java.io.Serializable;

/*
 * Describes each field of packet model and a whole packet.
 */
public class eConfig implements IConfig, Serializable
{
    public enum fourthLevelPackages implements Serializable
    { TCP, UDP, ICMP }

    private eEthernetConfig ethernetConfig;
    private eIPv4Config ipv4Config;
    private eTCPConfig tcpConfig;
    private eUDPConfig udpConfig;
    private eICMPConfig icmpConfig;
    private fourthLevelPackages selectedType;

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

        if (tcpConfig != null && selectedType == fourthLevelPackages.TCP) stringBuilder.append("TCP ");
        else if (udpConfig != null && selectedType == fourthLevelPackages.UDP) stringBuilder.append("UDP ");
        else if (icmpConfig != null && selectedType == fourthLevelPackages.ICMP) stringBuilder.append("ICMP ");

        return stringBuilder.toString();
    }

    public fourthLevelPackages getSelectedType()
    {
        return selectedType;
    }

    public eConfig setSelectedType(fourthLevelPackages selectedTabId)
    {
        this.selectedType = selectedTabId;
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
