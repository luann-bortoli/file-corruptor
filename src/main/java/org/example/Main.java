package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
        VBox root = new VBox(15);
        root.setPadding(new Insets(10));

        Button chooseFileBtn = new Button("Choose file");
        chooseFileBtn.setOnAction(ev -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose file");

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) System.out.println(file.getAbsolutePath());

        });

        root.getChildren().addAll(
                chooseFileBtn
        );

        Scene scene = new Scene(root, 800, 500);

        stage.setTitle("file-corruptor");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args)
    {
        launch();
    }
}