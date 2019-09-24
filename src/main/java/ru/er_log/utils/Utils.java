package ru.er_log.utils;

import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.util.MacAddress;
import ru.er_log.components.eNetworkInterface;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Utils
{

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

            Log.out("Serialization success!");
        } catch (IOException e)
        {
            e.printStackTrace();
            Log.err("Serialization failed!");
        }
    }

    public static Object deserialize(String path)
    {
        return deserialize(new File(path));
    }

    public static Object deserialize(File file)
    {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;

        try
        {
            fileInputStream = new FileInputStream(file);
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
        if (gateway == null) return null;

        PcapNetworkInterface networkInterface = eNetworkInterface.getNetworkInterface();
        for (PcapAddress pcapAddress : networkInterface.getAddresses())
            if (pcapAddress.getAddress() instanceof Inet4Address)
                if (MacAddressHelper._isUnderSameSubNet(gateway, pcapAddress.getAddress(), pcapAddress.getNetmask()))
                {
                    macAddress = MacAddressHelper.getInstance().getMacAddress(gateway);
                    return macAddress;
                }

        return null;
    }

    public static String bytesToHex(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        String separator = "-";
        for (byte b : bytes)
            sb.append(String.format("%02x", b)).append(separator);
        if (sb.length() > 0) sb.deleteCharAt(sb.lastIndexOf(separator));
        return sb.toString().toLowerCase();
    }
}
