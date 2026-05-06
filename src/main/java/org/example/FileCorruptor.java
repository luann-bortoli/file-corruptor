package org.example;

import javafx.scene.control.Toggle;

import java.util.Random;

public class FileCorruptor
{
    public static void Corruptor(byte[] file, String intensity){

        double percent = 0;

        if (intensity.equals("Low Noise")) percent = 0.0005;
        if (intensity.equals("Data Drift")) percent = 0.005;
        if (intensity.equals("Byte Collapse")) percent = 0.05;

        if (percent == 0)
        {
            System.out.println("Intensity not set");
            return;
        }

            Random random = new Random();

            for (int i = 0; i < file.length * percent; i++){
                int index = random.nextInt(file.length);

                file[index] = (byte) random.nextInt(256);
            }
    }
}
