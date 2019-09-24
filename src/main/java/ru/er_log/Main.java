package ru.er_log;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.er_log.controllers.RootController;

public class Main extends Application
{
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

    public static void main(String[] args)
    {
        launch(args);
    }
}