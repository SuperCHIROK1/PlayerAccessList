package me.superchirok1.playeraccesslist.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONData {

    private final File file;
    private PlayerData data;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JSONData(File dataFolder) {
        file = new File(dataFolder, "data.json");
        loadData();
    }

    public void loadData() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                data = new PlayerData();
                return;
            }
            FileReader fileReader = new FileReader(file);
            data = gson.fromJson(fileReader, PlayerData.class);
            fileReader.close();
            if (data == null)
                data = new PlayerData();
        } catch (IOException error) {
            error.printStackTrace();
            data = new PlayerData();
        }
    }

    public void saveData() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            gson.toJson(data, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public PlayerData getData() {
        return data;
    }
}
