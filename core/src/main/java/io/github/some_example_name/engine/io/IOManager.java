package io.github.some_example_name.engine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

public class IOManager implements Disposable {

    // Encapsulation: Singleton instance
    private static IOManager instance;

    // Encapsulation: Composition of sub-managers
    private AudioOutput audio;
    private DynamicInput dynamicInput;
    private OutputManager outputManager;

    private IOManager() {
        audio = new AudioOutput();
        dynamicInput = new DynamicInput();
        outputManager = new OutputManager();
    }

    public static IOManager getInstance() {
        if (instance == null) {
            instance = new IOManager();
        }
        return instance;
    }

    // Abstraction: Simple public API to start the engine
    public void init() {
        // Explicit Initialization Order
        audio.initialize();
        outputManager.initialize();
        dynamicInput.initialize();

        // Polymorphism/Interface: LibGDX expects an InputProcessor interface
        Gdx.input.setInputProcessor(dynamicInput);
    }

    // Encapsulation: Getters provide access without allowing replacement
    public AudioOutput getAudio() {
        return audio;
    }

    public DynamicInput getDynamicInput() {
        return dynamicInput;
    }

    public OutputManager getOutputManager() {
        return outputManager;
    }

    @Override
    public void dispose() {
        if (audio != null)
            audio.dispose();
        if (outputManager != null)
            outputManager.dispose();
        if (dynamicInput != null)
            dynamicInput.dispose();

        Gdx.input.setInputProcessor(null);
    }
}