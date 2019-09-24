package ru.er_log.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import ru.er_log.utils.Log;

import java.net.URL;
import java.util.ResourceBundle;

public class LogsController implements Initializable
{
    @FXML private TextArea fieldLogs;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Log.ILogObserver logObserver = new Log.ILogObserver()
        {
            @Override
            public void handleLog(String message)
            {
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(fieldLogs.getText());
                if (stringBuilder.length() != 0)
                    stringBuilder.append("\r\n");
                stringBuilder.append(message);

                fieldLogs.setText(stringBuilder.toString());
            }
        };
        Log.getInstance().attachLogOutObserver(logObserver);
        Log.getInstance().attachLogErrObserver(logObserver);
    }

    public TextArea getFieldLogs()
    {
        return fieldLogs;
    }
}
