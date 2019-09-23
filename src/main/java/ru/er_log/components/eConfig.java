package ru.er_log.components;

import ru.er_log.exceptions.NotFullConfigurationException;
import ru.er_log.utils.Utils;

import java.io.Serializable;

/*
 * Describes each field of packet model and a whole packet.
 */
public class eConfig implements IConfig, Serializable
{
    public enum fourthLevelPackages { TCP, UDP, ICMP }

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
        if (tcpConfig != null) stringBuilder.append("TCP ");
        if (udpConfig != null) stringBuilder.append("UDP ");
        if (icmpConfig != null) stringBuilder.append("ICMP ");

        return stringBuilder.toString();
    }

    /* Checks, if this config completely filled and can be used for. */
    @Override
    public void verify() throws NullPointerException
    {
        if (selectedType == null)
            throw new NullPointerException("Packet type not specified");

        IConfig selectedConfig;
        switch (selectedType)
        {
            case TCP:
                selectedConfig = tcpConfig;
                break;
            case UDP:
                selectedConfig = udpConfig;
                break;
            case ICMP:
                selectedConfig = icmpConfig;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedType);
        }

        selectedConfig.verify();
    }

    public fourthLevelPackages getSelectedType()
    {
        return selectedType;
    }

    public eConfig setSelectedType(fourthLevelPackages selectedTabId)
    {
        this.selectedType = selectedType;
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
