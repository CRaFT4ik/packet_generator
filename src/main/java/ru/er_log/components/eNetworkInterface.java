package ru.er_log.components;

import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.util.LinkLayerAddress;

import java.util.ArrayList;
import java.util.List;

public final class eNetworkInterface
{
    private PcapNetworkInterface pcapNetworkInterface;

    public eNetworkInterface(PcapNetworkInterface pcapNetworkInterface)
    {
        this.pcapNetworkInterface = pcapNetworkInterface;
    }

    public PcapNetworkInterface getNetworkInterface()
    {
        return pcapNetworkInterface;
    }

    @Override
    public String toString()
    {
        String name = pcapNetworkInterface.getName();
        String description = pcapNetworkInterface.getDescription();
        List<PcapAddress> addresses = pcapNetworkInterface.getAddresses();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(name).append("]");
        if (description != null) stringBuilder.append(" ").append(description).append(" ");
        for (PcapAddress linkLayerAddress : addresses)
            stringBuilder.append(" :: ").append(linkLayerAddress.getAddress().getHostAddress());

        return stringBuilder.toString();
    }
}
