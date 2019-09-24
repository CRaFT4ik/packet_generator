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
