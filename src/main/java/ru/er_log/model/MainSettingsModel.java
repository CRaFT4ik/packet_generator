package ru.er_log.model;

import com.logicmonitor.macaddress.detector.MacAddressHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;
import ru.er_log.components.Utils;
import ru.er_log.components.eConfig;
import ru.er_log.components.eIPv4Config;
import ru.er_log.components.eNetworkInterface;
import ru.er_log.exceptions.NotFullConfigurationException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;

import static ru.er_log.components.Utils.logErr;

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
            for (PcapNetworkInterface networkInterface : Pcaps.findAllDevs())
                if (networkInterface.isRunning())
                    networkInterfaces.add(new eNetworkInterface(networkInterface));
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

    public String getExportFilename()
    {
        return "export_" + Utils.getTime("dd.MM.YY_HH:mm:ss") + ".epg";
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
}
