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

package ru.er_log.utils;

import java.util.ArrayList;

public class Log
{
    private static final String debugPrefix   = "[DEBUG]";
    private static final String errorPrefix   = "[ERROR]";
    private static final String warningPrefix = " [WARN]";
    private static final String timeFormat    = "HH:mm:ss";

    private static Log singleton;
    private static ArrayList<ILogObserver> outObserversList = new ArrayList<>();
    private static ArrayList<ILogObserver> errObserversList = new ArrayList<>();

    public static Log getInstance()
    {
        if (singleton == null)
            singleton = new Log();

        return singleton;
    }

    public static void out(Object ... objects)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[").append(Utils.getTime(timeFormat)).append("] ");
        stringBuilder.append(debugPrefix).append(" ");
        for (Object object : objects)
            stringBuilder.append(object.toString());

        System.out.println(stringBuilder.toString());
        fireLogOut(stringBuilder.toString());
    }

    public static void err(Object ... objects)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[").append(Utils.getTime(timeFormat)).append("] ");
        stringBuilder.append(errorPrefix).append(" ");
        for (Object object : objects)
            stringBuilder.append(object.toString());

        System.err.println(stringBuilder.toString());
        fireLogErr(stringBuilder.toString());
    }

    public static void warn(Object ... objects)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[").append(Utils.getTime(timeFormat)).append("] ");
        stringBuilder.append(warningPrefix).append(" ");
        for (Object object : objects)
            stringBuilder.append(object.toString());

        System.out.println(stringBuilder.toString());
        fireLogOut(stringBuilder.toString());
    }

    public void attachLogOutObserver(ILogObserver observer)
    {
        outObserversList.add(observer);
    }

    public void attachLogErrObserver(ILogObserver observer)
    {
        errObserversList.add(observer);
    }

    public void removeLogObserver(ILogObserver observer)
    {
        outObserversList.remove(observer);
        errObserversList.remove(observer);
    }

    private static void fireLogOut(String message)
    {
        for (ILogObserver iLogObserver : outObserversList)
            java.awt.EventQueue.invokeLater(new HandleLogRunner(iLogObserver, message));
    }

    private static void fireLogErr(String message)
    {
        for (ILogObserver iLogObserver : errObserversList)
            java.awt.EventQueue.invokeLater(new HandleLogRunner(iLogObserver, message));
    }

    public interface ILogObserver
    {
        public void handleLog(String message);
    }

    private static class HandleLogRunner implements Runnable
    {
        private String message;
        private ILogObserver target;

        HandleLogRunner(ILogObserver target, String message)
        {
            this.message = message;
            this.target = target;
        }

        @Override
        public void run()
        {
            target.handleLog(message);
        }
    }
}
