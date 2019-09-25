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

package ru.er_log;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.er_log.controllers.RootController;

public class Main
{
    public static void main(String[] args)
    {
         JavaFXMain.main(args);
    }

    public static class JavaFXMain extends Application
    {
        public static void main(String[] args)
        {
            launch(args);
        }

        @Override
        public void start(Stage stage) throws Exception
        {
            Font.loadFont("file:resources/ru/er_log/fonts/consolas.ttf", 12);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/er_log/views/scene.fxml"));
            Parent root = loader.load();

            RootController controller = (RootController) loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setTitle("ePacket Generator");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
    }
}