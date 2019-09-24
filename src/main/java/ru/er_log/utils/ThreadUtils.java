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

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Allows to create only one thread for each task. Unique id describes each task.
 */
public class ThreadUtils
{
    private ExecutorService executor;
    private HashMap<Integer, Worker> workers;
    private static ThreadUtils instance;

    private ThreadUtils()
    {
        executor = Executors.newCachedThreadPool();
        workers = new HashMap<>();
    }

    public static ThreadUtils getInstance()
    {
        if (instance == null)
            return instance = new ThreadUtils();
        else
            return instance;
    }

    public void terminate()
    {
        if (executor != null)
            executor.shutdown();

        workers.forEach((k, v) ->
        {
            v.cancel();
        });

        instance = null;
    }

    /**
     * @param task                  task (function)
     * @param uniqueTaskHashCode    unique hash for adding task (anyone can get this task with this hash)
     */
    public Worker addTask(Runnable task, int uniqueTaskHashCode)
    {
        if (workers.containsKey(uniqueTaskHashCode))
        {
            return workers.get(uniqueTaskHashCode);
        } else
        {
            Worker worker = new Worker(task);
            workers.put(uniqueTaskHashCode, worker);
            return worker;
        }
    }

    public Worker getTask(int uniqueTaskHashCode)
    {
        return workers.get(uniqueTaskHashCode);
    }

    public class Worker
    {
        Runnable task;
        Future<?> future;

        private Worker(Runnable task)
        {
            this.task = task;
        }

        public void work()
        {
            if (future != null && !future.isDone()) return;
            future = executor.submit(task);
        }

        public boolean cancel()
        {
            if (future == null) return false;
            else return future.cancel(true);
        }

        public boolean isDone()
        {
            if (future == null) return true;
            else return future.isDone();
        }

        public boolean isCanceled()
        {
            if (future == null) return false;
            else return future.isCancelled();
        }
    }
}
