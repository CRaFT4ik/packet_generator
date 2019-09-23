package ru.er_log.components;

import ru.er_log.exceptions.NotFullConfigurationException;

public interface IConfig
{
    long AUTO_VALUE = 0xFFFFFFFFFFFFFFFFL; // Uses for detect empty fields of optional values.

    void verify() throws NullPointerException;
}
