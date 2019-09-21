package ru.er_log.components;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils
{
    private static final String debugPrefix = "[ePG DEBUG]";
    private static final String errorPrefix = "[ePG ERROR]";

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

    public static void serialize(Object object, String path)
    {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        try
        {
            fileOutputStream = new FileOutputStream(path);
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
}
