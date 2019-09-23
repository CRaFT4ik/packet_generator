package ru.er_log.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.util.MacAddress;
import ru.er_log.components.*;
import ru.er_log.utils.Utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import static ru.er_log.utils.Utils.log;

public class MainSettingsModel
{
    private ObservableList<eNetworkInterface> networkInterfaces;
    private ObservableList<eIPv4Config.tos.precedence> ipv4TosPreValues;
    private ObservableList<eConfig> configurationList;

    public MainSettingsModel()
    {
        configurationList = FXCollections.observableArrayList();
        networkInterfaces = FXCollections.observableArrayList();

        try
        {
            log("Adding interfaces:");
            for (PcapNetworkInterface networkInterface : Pcaps.findAllDevs())
            {
                if (networkInterface.isRunning() && !networkInterface.isLoopBack())
                {
                    log(networkInterface);
                    networkInterfaces.add(new eNetworkInterface(networkInterface));
                }
            }
        } catch (PcapNativeException e)
        {
            e.printStackTrace();
        }

        ipv4TosPreValues = FXCollections.observableArrayList();
        ipv4TosPreValues.addAll(Arrays.asList(eIPv4Config.tos.precedence.values()));

//        Thread t = new Thread()
//        {
//            @Override
//            public void run()
//            {
//                PcapHandle sendHandle = null;
//                try
//                {
//                    IcmpV4EchoPacket.Builder icmpBuilder = new IcmpV4EchoPacket.Builder();
//                    try
//                    {
//                        icmpBuilder
//                                .build();
//                    } catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//                    MacAddress srcMAC = MacAddress.getByName("AA-FF-AA-FF-AA-FF", "-");
//                    MacAddress dstMAC = MacAddress.getByName("BB-FF-AA-FF-AA-FF", "-");
//
//                    EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
//                    try
//                    {
//                        etherBuilder
//                                .srcAddr(srcMAC)
//                                .dstAddr(dstMAC)
//                                .type(EtherType.IPV4)
//                                .payloadBuilder(icmpBuilder)
//                                .paddingAtBuild(true)
//                                .build();
//                    } catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//                    PcapNetworkInterface nif = networkInterfaces.get(0).getNetworkInterface();
//
//                    if (nif == null)
//                    {
//                        logErr("nif is null!");
//                        return;
//                    }
//
//                    int snapLen = 65536;
//                    PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
//                    int timeout = 10;
//                    sendHandle = nif.openLive(snapLen, mode, timeout);
//                    for (int i = 0; i < 1; i++)
//                    {
//                        Packet p = etherBuilder.build();
//                        System.out.println(p);
//                        sendHandle.sendPacket(p);
//                        try
//                        {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e)
//                        {
//                            break;
//                        }
//                    }
//                } catch (PcapNativeException | NotOpenException e)
//                {
//                    e.printStackTrace();
//                } finally
//                {
//                    if (sendHandle != null && sendHandle.isOpen())
//                        sendHandle.close();
//                }
//            }
//        };
//
//        t.start();
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
                    File helpPDF = new File(getClass().getResource("/ru/er_log/help/SANS_tcpip.pdf").toURI());
                    Desktop.getDesktop().open(helpPDF);
                } catch (IOException | URISyntaxException ex)
                {
                    ex.printStackTrace();
                }
            }
        };

        thread.start();
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

    public void startSending(ArrayList<eConfig> configList, boolean isLoop, double delayMs) throws UnknownHostException
    {
        log("Start sending...");
        log("Total ", configList.size(), " packets", (isLoop) ? " in loop" : "", " with ", delayMs, " (ms) delay");

        ArrayList<EthernetPacket> packets = new ArrayList<>();
        for (eConfig config : configList)
        {
            Packet packet;
            IpNumber protocol;
            switch (config.getSelectedType())
            {
                case TCP:
                    packet = buildTcpPacket(config.getTcpConfig());
                    protocol = IpNumber.TCP;
                    break;
                case UDP:
                    packet = buildUdpPacket(config.getUdpConfig());
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
            EthernetPacket ethernetPacket = buildEthernetPacket(config.getEthernetConfig(), ipV4Packet.getPayload().getBuilder());

            packets.add(ethernetPacket);
        }
    }

    private IcmpV4CommonPacket buildIcmpV4Packet(eICMPConfig icmpConfig)
    {
        IcmpV4CommonPacket.Builder builder = new IcmpV4CommonPacket.Builder();
        // TODO

        return builder.build();
    }

    private UdpPacket buildUdpPacket(eUDPConfig udpConfig)
    {
        UdpPacket.Builder builder = new UdpPacket.Builder();
        // TODO

        return builder.build();
    }

    private TcpPacket buildTcpPacket(eTCPConfig tcpConfig)
    {
        TcpPacket.Builder builder = new TcpPacket.Builder();
        // TODO

        return builder.build();
    }

    private IpV4Packet buildIpV4Packet(eIPv4Config ipV4Config, Packet.Builder payload, IpNumber protocol) throws UnknownHostException
    {
        byte ihl = ((Short) ipV4Config.getIhl()).byteValue();
        IpVersion ipVersion = ipV4Config.getVersion() == 4 ? IpVersion.IPV4 : null;
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
                //.fragmentOffset()
                .ttl(ttl)
                .protocol(protocol)
                .correctChecksumAtBuild(ipV4Config.correctChecksumAuto())
                .headerChecksum(checksum)
                .srcAddr((Inet4Address) srcAddress)
                .dstAddr((Inet4Address) dstAddress)
                //.options()
                .paddingAtBuild(true)
                .payloadBuilder(payload);

        return builder.build();
    }

    private EthernetPacket buildEthernetPacket(eEthernetConfig ethernetConfig, Packet.Builder payload)
    {
        EthernetPacket.Builder builder = new EthernetPacket.Builder();
        builder
                .dstAddr(MacAddress.getByName(ethernetConfig.getDstMAC()))
                .srcAddr(MacAddress.getByName(ethernetConfig.getSrcMAC()))
                .type(EtherType.IPV4)
                .payloadBuilder(payload)
                .paddingAtBuild(true);

        return builder.build();
    }
}
