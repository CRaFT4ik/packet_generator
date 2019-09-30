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

package ru.er_log.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.*;
import org.pcap4j.util.MacAddress;
import ru.er_log.components.*;
import ru.er_log.utils.Utils;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static ru.er_log.utils.Log.err;
import static ru.er_log.utils.Log.out;

public class MainSettingsModel
{
    private ObservableList<eNetworkInterface> networkInterfaces;
    private ObservableList<eIPv4Config.tos.precedence> ipv4TosPreValues;
    private ObservableList<eConfig> configurationList;
    private eConfig defaultConfig;

    public MainSettingsModel()
    {
        configurationList = FXCollections.observableArrayList();
        networkInterfaces = FXCollections.observableArrayList();

        try
        {
            out("Adding interfaces:");
            for (PcapNetworkInterface networkInterface : Pcaps.findAllDevs())
            {
//                if (networkInterface.isRunning() && !networkInterface.isLoopBack())
//                {
                    out(networkInterface);
                    networkInterfaces.add(new eNetworkInterface(networkInterface));
//                }
            }
        } catch (PcapNativeException e)
        {
            e.printStackTrace();
        }

        ipv4TosPreValues = FXCollections.observableArrayList();
        ipv4TosPreValues.addAll(Arrays.asList(eIPv4Config.tos.precedence.values()));

        generateDefaultConfig();
    }

    private void generateDefaultConfig()
    {
        if (defaultConfig != null) return;

        eEthernetConfig ethernetConfig = new eEthernetConfig();
        eIPv4Config ipv4Config = new eIPv4Config();
        eTCPConfig tcpConfig = new eTCPConfig();
        eUDPConfig udpConfig = new eUDPConfig();
        eICMPConfig icmpConfig = new eICMPConfig();

        defaultConfig = new eConfig();
        defaultConfig.setEthernetConfig(ethernetConfig);
        defaultConfig.setIpv4Config(ipv4Config);
        defaultConfig.setTcpConfig(tcpConfig);
        defaultConfig.setUdpConfig(udpConfig);
        defaultConfig.setIcmpConfig(icmpConfig);
        defaultConfig.setSelectedType(eConfig.fourthLevelPackages.TCP);

        eIPv4Config.tos ipv4Tos = new eIPv4Config.tos(eIPv4Config.tos.precedence._000, false, false, false, false, false);
        eIPv4Config.flags ipv4Flags = new eIPv4Config.flags(false, true, false);
        ipv4Config
                .setVersion((short) 4)
                .setIhl((short) 5)
                .setTos(ipv4Tos)
                .setLength((int) eConfig.AUTO_VALUE)
                .setId(new Random().nextInt((0xFFFF + 0x1)))
                .setFlags(ipv4Flags)
                .setOffset((short) 0)
                .setTtl((short) 64)
                .setChecksum((int) eConfig.AUTO_VALUE)
                .setSrcIP("")
                .setDstIP("")
                //.setOptions(eConfig.AUTO_VALUE)
        ;

        eTCPConfig.reserved tcpReserved = new eTCPConfig.reserved(false, false, false, false, false, false);
        eTCPConfig.flags tcpFlags = new eTCPConfig.flags(false, false, false, false, true, false);
        tcpConfig
                .setSrcPort(37573)
                .setDstPort(80)
                .setSeqNum(new Random().nextInt((0xFFFF + 0x1)))
                .setAskNum(0)
                .setOffset((short) 5)
                .setReserved(tcpReserved)
                .setFlags(tcpFlags)
                .setWindowSize(65535)
                .setChecksum((int) eConfig.AUTO_VALUE)
                .setUrgent(0)
                //.setOptions(eConfig.AUTO_VALUE)
                .setData("")
        ;

        udpConfig
                .setSrcPort(37573)
                .setDstPort(80)
                .setLength((int) eConfig.AUTO_VALUE)
                .setChecksum((int) eConfig.AUTO_VALUE)
                .setData("")
        ;

        icmpConfig
                .setType((short) 8)
                .setCode((short) 0)
                .setChecksum((int) eConfig.AUTO_VALUE)
                .setId(new Random().nextInt((0xFFFF + 0x1)))
                .setSeqNum(0)
                .setData("")
        ;
    }

    public eConfig getDefaultConfig()
    {
        return this.defaultConfig;
    }

    public String getExportFormat()
    {
        return "epg";
    }

    public String getExportFilename()
    {
        return "export_" + Utils.getTime("dd.MM.YY_HH:mm:ss") + "." + getExportFormat();
    }

    public void addConfigurationToList(eConfig config)
    {
        configurationList.add(config);
    }

    public void removeConfigurationFromList(eConfig config)
    {
        configurationList.removeAll(config);
    }

    public void showHelpFile()
    {
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    String filename = "SANS_tcpip.pdf";
                    InputStream inputStream = getClass().getResourceAsStream("/ru/er_log/help/" + filename);
                    File helpPDF = new File(filename);
                    out("Creating new HELP file: " + helpPDF.getAbsolutePath());
                    copyInputStreamToFile(inputStream, helpPDF);
                    Desktop.getDesktop().open(helpPDF);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        };

        thread.start();
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException
    {
        try (FileOutputStream outputStream = new FileOutputStream(file))
        {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1)
                outputStream.write(bytes, 0, read);
        }
    }

    public ObservableList<eNetworkInterface> getNetworkInterfaces()
    {
        return networkInterfaces;
    }

    public ObservableList<eIPv4Config.tos.precedence> getIpv4TosPreValues()
    {
        return ipv4TosPreValues;
    }

    public ObservableList<eConfig> getConfigurationList()
    {
        return configurationList;
    }

    public void startSending(eNetworkInterface networkInterface, ArrayList<eConfig> configList, boolean isLoop, long delayMs)
            throws UnknownHostException, InterruptedException, PcapNativeException, NotOpenException
    {
        if (configList.isEmpty())
        {
            err("Sending list not specified");
            return;
        }

        out("Start sending...");
        out("Total ", configList.size(), " packets", (isLoop) ? " in loop" : "", " with ", delayMs, " (ms) delay");

        ArrayList<EthernetPacket> packets = new ArrayList<>();
        for (eConfig config : configList)
        {
            Packet packet;
            IpNumber protocol;
            switch (config.getSelectedType())
            {
                case TCP:
                    packet = buildTcpPacket(config.getTcpConfig(), config.getIpv4Config());
                    protocol = IpNumber.TCP;
                    break;
                case UDP:
                    packet = buildUdpPacket(config.getUdpConfig(), config.getIpv4Config());
                    protocol = IpNumber.UDP;
                    break;
                case ICMP:
                    packet = buildIcmpV4Packet(config.getIcmpConfig());
                    protocol = IpNumber.ICMPV4;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + config.getSelectedType());
            }

            IpV4Packet ipV4Packet = buildIpV4Packet(config.getIpv4Config(), packet.getBuilder(), protocol);
            EthernetPacket ethernetPacket = buildEthernetPacket(config.getEthernetConfig(), ipV4Packet.getBuilder());

            packets.add(ethernetPacket);
        }

        if (packets.isEmpty()) return;

        PcapHandle sendHandle = null;
        try
        {
            final int SNAPLEN = 65536; // [bytes]
            final int READ_TIMEOUT = 10; // [ms]
            sendHandle = networkInterface.getNetworkInterface().openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);

            do
            {
                for (EthernetPacket packet : packets)
                {
                    sendHandle.sendPacket(packet);
                    Thread.sleep(delayMs);
                }
            } while (isLoop);
        } finally
        {
            if (sendHandle != null && sendHandle.isOpen())
                sendHandle.close();
        }
    }

    private IcmpV4CommonPacket buildIcmpV4Packet(eICMPConfig icmpConfig)
    {
        IcmpV4Type type = IcmpV4Type.getInstance((byte) icmpConfig.getType());
        IcmpV4Code code = IcmpV4Code.getInstance(type.value(), (byte) icmpConfig.getCode());
        short checksum = (short) icmpConfig.getChecksum();
        short identifier = (short) icmpConfig.getId();
        short sequence = (short) icmpConfig.getSeqNum();

        Packet.Builder specBuilder;

        if (0 == type.compareTo(IcmpV4Type.ECHO))
        {
            specBuilder = new IcmpV4EchoPacket.Builder();
            IcmpV4EchoPacket.Builder builder = (IcmpV4EchoPacket.Builder) specBuilder;
            builder
                    .identifier(identifier)
                    .sequenceNumber(sequence)
            ;
        } else if (0 == type.compareTo(IcmpV4Type.ECHO_REPLY))
        {
            specBuilder = new IcmpV4EchoReplyPacket.Builder();
            IcmpV4EchoReplyPacket.Builder builder = (IcmpV4EchoReplyPacket.Builder) specBuilder;
            builder
                    .identifier(identifier)
                    .sequenceNumber(sequence)
            ;
        } else
        {
            throw new IllegalStateException("Unknown ICMPv4 type: " + type.toString());
        }

        UnknownPacket.Builder payload = new UnknownPacket.Builder();
        payload.rawData(icmpConfig.getData().getBytes());
        specBuilder
                .payloadBuilder(payload)
        ;

        IcmpV4CommonPacket.Builder builder = new IcmpV4CommonPacket.Builder();
        builder
                .type(type)
                .code(code)
                .correctChecksumAtBuild(icmpConfig.correctChecksumAuto())
                .checksum(checksum)
                .payloadBuilder(specBuilder)
        ;

        return builder.build();
    }

    private UdpPacket buildUdpPacket(eUDPConfig udpConfig, eIPv4Config ipV4Config) throws UnknownHostException
    {
        UdpPort srcPort = new UdpPort((short) udpConfig.getSrcPort(), "ePacket Generator src. UDP port");
        UdpPort dstPort = new UdpPort((short) udpConfig.getDstPort(), "ePacket Generator dst. UDP port");
        short length = (short) udpConfig.getLength();
        short checksum = (short) udpConfig.getChecksum();
        InetAddress srcAddress = Inet4Address.getByName(ipV4Config.getSrcIP());
        InetAddress dstAddress = Inet4Address.getByName(ipV4Config.getDstIP());

        UnknownPacket.Builder payload = new UnknownPacket.Builder();
        payload.rawData(udpConfig.getData().getBytes());

        UdpPacket.Builder builder = new UdpPacket.Builder();
        builder
                .srcPort(srcPort)
                .dstPort(dstPort)
                .correctLengthAtBuild(udpConfig.correctLengthAuto())
                .length(length)
                .correctChecksumAtBuild(udpConfig.correctChecksumAuto())
                .checksum(checksum)
                .srcAddr(srcAddress) // For checksum calculation.
                .dstAddr(dstAddress) // Same.
                .payloadBuilder(payload)
        ;

        return builder.build();
    }

    private TcpPacket buildTcpPacket(eTCPConfig tcpConfig, eIPv4Config ipV4Config) throws UnknownHostException
    {
        InetAddress srcAddress = Inet4Address.getByName(ipV4Config.getSrcIP());
        InetAddress dstAddress = Inet4Address.getByName(ipV4Config.getDstIP());
        TcpPort srcPort = new TcpPort((short) tcpConfig.getSrcPort(), "ePacket Generator src. TCP port");
        TcpPort dstPort = new TcpPort((short) tcpConfig.getDstPort(), "ePacket Generator dst. TCP port");
        int seqNumber = (int) tcpConfig.getSeqNum();
        int ackNumber = (int) tcpConfig.getAskNum();
        byte dataOffset = (byte) tcpConfig.getOffset();
        byte reserved = tcpConfig.getReserved().toByte();
        short window = (short) tcpConfig.getWindowSize();
        short checksum = (short) tcpConfig.getChecksum();
        short urgent = (short) tcpConfig.getUrgent();

        UnknownPacket.Builder payload = new UnknownPacket.Builder();
        payload.rawData(tcpConfig.getData().getBytes());

        TcpPacket.Builder builder = new TcpPacket.Builder();
        builder
                .srcPort(srcPort)
                .dstPort(dstPort)
                .sequenceNumber(seqNumber)
                .acknowledgmentNumber(ackNumber)
                .correctLengthAtBuild(tcpConfig.correctOffsetAuto())
                .dataOffset(dataOffset)
                .reserved(reserved)
                .urg(tcpConfig.getFlags().isU())
                .ack(tcpConfig.getFlags().isA())
                .psh(tcpConfig.getFlags().isP())
                .rst(tcpConfig.getFlags().isR())
                .syn(tcpConfig.getFlags().isS())
                .fin(tcpConfig.getFlags().isF())
                .window(window)
                .correctChecksumAtBuild(tcpConfig.correctChecksumAuto())
                .checksum(checksum)
                .srcAddr(srcAddress) // For checksum calculation.
                .dstAddr(dstAddress)
                .urgentPointer(urgent)
                //.options()
                .paddingAtBuild(true)
                .payloadBuilder(payload)
        ;

        return builder.build();
    }

    private IpV4Packet buildIpV4Packet(eIPv4Config ipV4Config, Packet.Builder payload, IpNumber protocol) throws UnknownHostException
    {
        byte ihl = ((Short) ipV4Config.getIhl()).byteValue();
        IpVersion ipVersion = IpVersion.IPV4; // ipV4Config.getVersion() == 4 ? IpVersion.IPV4 : null;
        IpV4Packet.IpV4Tos tos = IpV4Rfc791Tos.newInstance(ipV4Config.getTos().toByte());
        short totalLength = (short) ipV4Config.getLength();
        short id = (short) ipV4Config.getId();
        byte ttl = (byte) ipV4Config.getTtl();
        short checksum = (short) ipV4Config.getChecksum();
        InetAddress srcAddress = Inet4Address.getByName(ipV4Config.getSrcIP());
        InetAddress dstAddress = Inet4Address.getByName(ipV4Config.getDstIP());

        IpV4Packet.Builder builder = new IpV4Packet.Builder();
        builder
                .version(ipVersion)
                .ihl(ihl)
                .tos(tos)
                .correctLengthAtBuild(ipV4Config.correctLengthAuto())
                .totalLength(totalLength)
                .identification(id)
                .reservedFlag(ipV4Config.getFlags().isX())
                .dontFragmentFlag(ipV4Config.getFlags().isD())
                .moreFragmentFlag(ipV4Config.getFlags().isM())
                .fragmentOffset(ipV4Config.getOffset())
                .ttl(ttl)
                .protocol(protocol)
                .correctChecksumAtBuild(ipV4Config.correctChecksumAuto())
                .headerChecksum(checksum)
                .srcAddr((Inet4Address) srcAddress)
                .dstAddr((Inet4Address) dstAddress)
                //.options()
                .paddingAtBuild(true)
                .payloadBuilder(payload)
        ;

//        out("IPv4 header: ", Utils.bytesToHex(builder.build().getHeader().getRawData()));
        return builder.build();
    }

    private EthernetPacket buildEthernetPacket(eEthernetConfig ethernetConfig, Packet.Builder payload)
    {
        EthernetPacket.Builder builder = new EthernetPacket.Builder();
        builder
                .dstAddr(MacAddress.getByName(ethernetConfig.getDstMAC()))
                .srcAddr(MacAddress.getByName(ethernetConfig.getSrcMAC()))
                .type(EtherType.IPV4)
                .paddingAtBuild(true)
                .payloadBuilder(payload)
        ;

        return builder.build();
    }
}
