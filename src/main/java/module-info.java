module ru.er_log
{
    requires javafx.fxml;
    requires javafx.controls;
    requires org.pcap4j.core;
    requires java.desktop;

    exports ru.er_log;
    exports ru.er_log.components;
    opens ru.er_log.controllers;
    exports ru.er_log.models;
    exports ru.er_log.ui;
    exports ru.er_log.utils;
}