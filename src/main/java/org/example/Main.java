package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main extends Application
{
    private boolean isLoading = false;

    private void setLoading(boolean loading,
                            Button chooseFileBtn,
                            Button corruptFileBtn,
                            ToggleGroup levelGroup,
                            ProgressIndicator progress,
                            Label statusLabel)
    {
        isLoading = loading;

        chooseFileBtn.setDisable(loading);
        corruptFileBtn.setDisable(loading);

        if (levelGroup.getToggles() != null) {
            for (Toggle toggle : levelGroup.getToggles()) {
                ((RadioButton) toggle).setDisable(loading);
            }
        }

        progress.setVisible(loading);

        if (loading) {
            statusLabel.setText("Status: Corrupting file...");
            statusLabel.setTextFill(Color.ORANGE);
        }
    }

    @Override
    public void start(Stage stage)
    {
        final File[] file = {null};

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #0f172a;");

        Label title = new Label("File Corruptor");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Choose a file, select the corruption intensity and generate a corrupted copy.");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #cbd5e1;");

        VBox header = new VBox(5, title, subtitle);

        VBox fileCard = new VBox(12);
        fileCard.setPadding(new Insets(18));
        fileCard.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 14;");
        fileCard.setEffect(new DropShadow(12, Color.rgb(0, 0, 0, 0.4)));

        Label fileTitle = new Label("Selected File");
        fileTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label selectedFile = new Label("No file selected");
        selectedFile.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        Button chooseFileBtn = new Button("Choose file");
        chooseFileBtn.setStyle("""
                -fx-background-color: #2563eb;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 10 18;
                """);

        chooseFileBtn.setOnAction(ev -> {
            if (isLoading) return;

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose file");

            File choosedFile = fileChooser.showOpenDialog(stage);

            if (choosedFile != null) {
                file[0] = choosedFile;
                selectedFile.setText(choosedFile.getName());
                selectedFile.setStyle("-fx-font-size: 13px; -fx-text-fill: #22c55e;");

            }
        });

        fileCard.getChildren().addAll(fileTitle, selectedFile, chooseFileBtn);

        VBox levelCard = new VBox(12);
        levelCard.setPadding(new Insets(18));
        levelCard.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 14;");
        levelCard.setEffect(new DropShadow(12, Color.rgb(0, 0, 0, 0.4)));

        Label levelTitle = new Label("Corruption Intensity");
        levelTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        ToggleGroup levelGroup = new ToggleGroup();

        RadioButton lvl1 = new RadioButton("Low Noise");
        RadioButton lvl2 = new RadioButton("Data Drift");
        RadioButton lvl3 = new RadioButton("Byte Collapse");

        lvl1.setToggleGroup(levelGroup);
        lvl2.setToggleGroup(levelGroup);
        lvl3.setToggleGroup(levelGroup);

        lvl1.setSelected(true);

        lvl1.setStyle("-fx-text-fill: white;");
        lvl2.setStyle("-fx-text-fill: white;");
        lvl3.setStyle("-fx-text-fill: white;");

        VBox radioContainer = new VBox(8, lvl1, lvl2, lvl3);
        radioContainer.setPadding(new Insets(5, 0, 0, 0));

        levelCard.getChildren().addAll(levelTitle, radioContainer);

        HBox actionBar = new HBox(15);
        actionBar.setAlignment(Pos.CENTER_LEFT);

        Button corruptFileBtn = new Button("Corrupt & Save Copy");
        corruptFileBtn.setDisable(true);
        corruptFileBtn.setStyle("""
                -fx-background-color: #dc2626;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 12 22;
                """);

        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setPrefSize(28, 28);

        Label statusLabel = new Label("Status: Ready");
        statusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #cbd5e1;");

        actionBar.getChildren().addAll(corruptFileBtn, progress, statusLabel);

        chooseFileBtn.setOnAction(ev -> {
            if (isLoading) return;

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose file");

            File choosedFile = fileChooser.showOpenDialog(stage);

            if (choosedFile != null) {
                file[0] = choosedFile;
                selectedFile.setText(choosedFile.getName());
                selectedFile.setStyle("-fx-font-size: 13px; -fx-text-fill: #22c55e;");
                corruptFileBtn.setDisable(false);

                statusLabel.setText("Status: File loaded");
                statusLabel.setTextFill(Color.LIGHTGREEN);
            }
        });

        corruptFileBtn.setOnAction(ev -> {
            if (file[0] == null) return;

            RadioButton selected = (RadioButton) levelGroup.getSelectedToggle();
            if (selected == null) return;

            setLoading(true, chooseFileBtn, corruptFileBtn, levelGroup, progress, statusLabel);

            try {
                byte[] fileData = Files.readAllBytes(file[0].toPath());

                FileCorruptor.Corruptor(fileData, selected.getText());

                FileChooser saveChooser = new FileChooser();
                saveChooser.setTitle("Save corrupted copy");
                saveChooser.setInitialFileName("corrupted_" + file[0].getName());

                File outputFile = saveChooser.showSaveDialog(stage);

                if (outputFile == null) {
                    statusLabel.setText("Status: Cancelled");
                    statusLabel.setTextFill(Color.GRAY);
                    setLoading(false, chooseFileBtn, corruptFileBtn, levelGroup, progress, statusLabel);
                    return;
                }

                Files.write(outputFile.toPath(), fileData);

                statusLabel.setText("Status: Saved successfully!");
                statusLabel.setTextFill(Color.LIGHTGREEN);

            } catch (IOException e) {
                statusLabel.setText("Status: Error - " + e.getMessage());
                statusLabel.setTextFill(Color.RED);
            }

            setLoading(false, chooseFileBtn, corruptFileBtn, levelGroup, progress, statusLabel);
        });

        HBox cards = new HBox(20, fileCard, levelCard);
        cards.setAlignment(Pos.TOP_LEFT);

        HBox.setHgrow(fileCard, Priority.ALWAYS);
        HBox.setHgrow(levelCard, Priority.ALWAYS);

        fileCard.setPrefWidth(350);
        levelCard.setPrefWidth(350);

        root.getChildren().addAll(header, cards, actionBar);

        Scene scene = new Scene(root, 820, 520);

        stage.setTitle("file-corruptor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}