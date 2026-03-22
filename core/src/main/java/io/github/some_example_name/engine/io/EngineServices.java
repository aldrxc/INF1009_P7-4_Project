package io.github.some_example_name.engine.io;

public final class EngineServices {
    private final AudioOutput audio;
    private final DynamicInput input;
    private final OutputManager output;

    public EngineServices(AudioOutput audio, DynamicInput input, OutputManager output) {
        if (audio == null || input == null || output == null) {
            throw new IllegalArgumentException("Engine services cannot contain null components.");
        }
        this.audio = audio;
        this.input = input;
        this.output = output;
    }

    public AudioOutput getAudio() {
        return audio;
    }

    public DynamicInput getInput() {
        return input;
    }

    public OutputManager getOutputManager() {
        return output;
    }
}
