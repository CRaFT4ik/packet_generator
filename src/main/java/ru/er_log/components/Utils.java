package ru.er_log.components;

import com.logicmonitor.macaddress.detector.MacAddressHelper;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.util.MacAddress;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Utils
{
    private static final String debugPrefix   = "  [ePG DEBUG]";
    private static final String errorPrefix   = "  [ePG ERROR]";
    private static final String warningPrefix = "[ePG WARNING]";

    public static void log(Object ... objects)
    {
        System.out.print(debugPrefix + " ");
        for (Object object : objects)
            System.out.print(object.toString());
        System.out.println();
    }

    public static void logErr(Object ... objects)
    {
        System.err.print(errorPrefix + " ");
        for (Object object : objects)
            System.err.print(object.toString());
        System.err.println();
    }

    public static void logWarn(Object ... objects)
    {
        System.out.print(warningPrefix + " ");
        for (Object object : objects)
            System.out.print(object.toString());
        System.out.println();
    }

    public static void serialize(Object object, String path)
    {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        try
        {
            File file = new File(path);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists())
                file.getParentFile().mkdirs();
            if (!file.createNewFile())
                throw new IOException("Can't write file " + path + "!");

            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.close();

            log("Serialization success!");
        } catch (IOException e)
        {
            e.printStackTrace();
            logErr("Serialization failed!");
        }
    }

    public static Object deserialize(String path)
    {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;

        try
        {
            fileInputStream = new FileInputStream(path);
            objectInputStream = new ObjectInputStream(fileInputStream);

            Object object = objectInputStream.readObject();
            objectInputStream.close();

            return object;
        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTime(String format)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(cal.getTime());
    }

    public static MacAddress getMacAddressByIp(eNetworkInterface eNetworkInterface, String ip)
    {
        if (eNetworkInterface == null || ip == null) return null;

        InetAddress inetAddress = null;
        try { inetAddress = Inet4Address.getByName(ip); }
        catch (UnknownHostException e) { return null; }

        MacAddress macAddress = MacAddressHelper.getInstance().getMacAddress(inetAddress);
        if (macAddress != null) return macAddress;

        PcapNetworkInterface networkInterface = eNetworkInterface.getNetworkInterface();

        logWarn(networkInterface.getLinkLayerAddresses());
        return (MacAddress) networkInterface.getLinkLayerAddresses().get(0);

////
////        for (PcapAddress pcapAddress : networkInterface.getAddresses())
////        {
////            InetAddress interfaceAddress = pcapAddress.getAddress();
////            if (interfaceAddress instanceof Inet4Address)
////            {
////
////                if (MacAddressHelper._isUnderSameSubNet(inetAddress, pcapAddress.getAddress(), pcapAddress.getNetmask()))
////                {
////                    return MacAddressHelper.getInstance().getMacAddress(inetAddress);
////                }
////            }
////        }
//
//        return null;
    }
}
