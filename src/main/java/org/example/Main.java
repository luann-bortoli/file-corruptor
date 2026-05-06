package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
        final File[] file = {null};

        VBox root = new VBox(15);
        root.setPadding(new Insets(15));

        HBox radioContainer = new HBox();

        Label title = new Label("File corruptor");
        title.setStyle("-fx-font-size: 24px");

        Label selectedFile = new Label(">>");

        ToggleGroup levelGroup = new ToggleGroup();

        Button corruptFileBtn = new Button("Corrupt");
        corruptFileBtn.setDisable(true);
        corruptFileBtn.setOnAction(ev -> {

            if (file[0] == null) return;

            try
            {
                byte[] fileData = Files.readAllBytes(file[0].toPath());

                RadioButton selected = (RadioButton) levelGroup.getSelectedToggle();

                if (selected != null) {
                    FileCorruptor.Corruptor(fileData, selected.getText());

                    FileChooser saveChooser = new FileChooser();
                    saveChooser.setTitle("Save corrupted copy");

                    saveChooser.setInitialFileName("corrupted_" + file[0].getName());

                    File outputFile = saveChooser.showSaveDialog(stage);

                    if (outputFile == null) return;

                    Files.write(outputFile.toPath(), fileData);

                    System.out.println("Chegou aqui");
                }

            }
            catch ( IOException e )
            {
                System.out.println(e.getMessage());
            }
        });

        RadioButton lvl1 = new RadioButton("Low Noise");
        RadioButton lvl2 = new RadioButton("Data Drift");
        RadioButton lvl3 = new RadioButton("Byte Collapse");

        lvl1.setToggleGroup(levelGroup);
        lvl1.setSelected(true);

        lvl2.setToggleGroup(levelGroup);

        lvl3.setToggleGroup(levelGroup);


        Button chooseFileBtn = new Button("Choose file");
        chooseFileBtn.setOnAction(ev -> {

            System.out.println(levelGroup.getUserData());

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose file");

            File choosedFile = fileChooser.showOpenDialog(stage);

            if (choosedFile != null) {

                file[0] = choosedFile;

                selectedFile.setText(">> " + choosedFile.getName());
                corruptFileBtn.setDisable(false);
            }

        });

        root.getChildren().addAll(
                title,
                chooseFileBtn,
                selectedFile,
                radioContainer,
                corruptFileBtn
        );

        radioContainer.getChildren().addAll(
            lvl1, lvl2, lvl3
        );

        radioContainer.setMargin(lvl2, new Insets(0, 24, 0, 24));

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