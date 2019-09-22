package ru.er_log.utils;

import com.logicmonitor.macaddress.detector.MacAddressHelper;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.util.MacAddress;
import ru.er_log.components.eNetworkInterface;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

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

    /*
     * TODO:
     *  - Works on Linux, untested on Windows, MAC, ... .
     *  - Searches in "Destination" only with "0.0.0.0" or "default" values, not by IP.
     */
    public static InetAddress getDefaultGateway(String ip) // ip is unused param.
    {
        try
        {
            Process result = Runtime.getRuntime().exec("netstat -rn");
            BufferedReader output = new BufferedReader(new InputStreamReader(result.getInputStream()));

            String line = output.readLine();
            if (line == null) return null;

            while (line != null)
            {
                if (line.startsWith("default") || line.startsWith("0.0.0.0"))
                    break;
                line = output.readLine();
            }

            assert line != null;
            StringTokenizer st = new StringTokenizer(line);
            st.nextToken();
            String gateway = st.nextToken();

            InetAddress inetAddress = null;
            try { inetAddress = Inet4Address.getByName(gateway); }
            catch (UnknownHostException e) { return null; }

            return inetAddress;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static MacAddress getMacAddressByIp(eNetworkInterface eNetworkInterface, String ip)
    {
        if (eNetworkInterface == null || ip == null) return null;

        InetAddress inetAddress = null;
        try { inetAddress = Inet4Address.getByName(ip); }
        catch (UnknownHostException e) { return null; }

        MacAddress macAddress = MacAddressHelper.getInstance().getMacAddress(inetAddress);
        if (macAddress != null) return macAddress;

        // If we can't find MAC for @ip, we trying to find MAC for gateway address.
        InetAddress gateway = getDefaultGateway(ip);
        macAddress = MacAddressHelper.getInstance().getMacAddress(gateway);
        return macAddress;
    }
}
