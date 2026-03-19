package io.github.some_example_name.game.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class CellDataManager {
    private final String saveFilePath = "saves/cell_run.json";
    private Json json;

    public CellDataManager() {
        this.json = new Json();
    }

    public Object loadOrganLayout(String organId) {
        // TODO: return a parsed grid layout for EntityManager to generate Walls
        System.out.println("Loading layout for organ: " + organId);
        return new Object();
    }

    // THE COMMENTED OUT CODE BELOW IS A SAMPLE OF HOW THE ACTUAL
    // loadOrganLayout() METHOD WILL BE LIKE:

    // public int[][] loadOrganLayout(String organId) {
    // // 1 - point to file in assets folder
    // // Gdx.files.internal automatically looks inside "assets" folder
    // // so just provide path inside it
    // String filePath = "levels/" + organId + ".txt";
    // FileHandle file = Gdx.files.internal(filePath);

    // // safety check: does file actually exist?
    // if (!file.exists()) {
    // System.err.println("Error: Could not find level file at " + filePath);
    // // fallback: return a tiny default room with walls so game doesnt crash
    // return new int[][] {
    // { 1, 1, 1 },
    // { 1, 0, 1 },
    // { 1, 1, 1 }
    // };
    // }

    // // 2 - read entire text file into a single String
    // String text = file.readString();

    // // split text into separate rows based on newlines (\n) presses
    // String[] lines = text.trim().split("\\r?\\n");

    // // find out how tall and wide grid needs to be
    // int rows = lines.length;
    // int cols = lines[0].trim().split("\\s+").length; // look at first row to get
    // width

    // // create empty grid
    // int[][] grid = new int[rows][cols];

    // // 3 - loop through every line and piece of text to fill grid
    // for (int r = 0; r < rows; r++) {
    // // split current row into individual numbers based on spaces
    // String[] numbers = lines[r].trim().split("\\s+");

    // for (int c = 0; c < cols; c++) {
    // try {
    // // convert text "1" or "0" into an actual integer number
    // grid[r][c] = Integer.parseInt(numbers[c]);
    // } catch (NumberFormatException e) {
    // // if theres a typo in text file (like a letter instead of a number)
    // // default to 0
    // System.err.println("Typo in layout file at row " + r + " col " + c);
    // grid[r][c] = 0;
    // }
    // }
    // }

    // System.out.println("Successfully loaded layout for organ: " + organId);
    // return grid;
    // }

    public CancerStageConfig loadStageStats(int stage) {
        // hardcoded balancing values as requested, could also be read from a json file
        switch (stage) {
            case 1:
                return new CancerStageConfig(0.0f, false, false);
            case 2:
                return new CancerStageConfig(0.25f, false, false);
            case 3:
                return new CancerStageConfig(0.50f, true, false);
            case 4:
                return new CancerStageConfig(0.75f, true, true);
            default:
                return new CancerStageConfig(0.0f, false, false);
        }
    }

    public void saveGame(CellGameState state) {
        try {
            FileHandle file = Gdx.files.local(saveFilePath);
            file.writeString(json.prettyPrint(state), false);
            System.out.println("Game saved successfully to " + saveFilePath);
        } catch (Exception e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public CellGameState loadGame() {
        try {
            FileHandle file = Gdx.files.local(saveFilePath);
            if (file.exists()) {
                return json.fromJson(CellGameState.class, file.readString());
            }
        } catch (Exception e) {
            System.err.println("Failed to load game, starting new run: " + e.getMessage());
        }
        return new CellGameState("Lungs", 1, 1, 0.0f); // default new game state
    }
}