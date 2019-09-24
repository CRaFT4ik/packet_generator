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
