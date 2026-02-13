package io.github.some_example_name.engine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

public class AudioOutput implements Disposable {

    private Map<String, Sound> soundEffects;
    private Music backgroundMusic;
    private float volume = 1.0f;

    public void initialize() {
        soundEffects = new HashMap<>();
    }

    public void playSound(String fileName) {
        if (!soundEffects.containsKey(fileName)) {
            if (Gdx.files.internal(fileName).exists()) {
                Sound s = Gdx.audio.newSound(Gdx.files.internal(fileName));
                soundEffects.put(fileName, s);
            } else {
                return;
            }
        }
        soundEffects.get(fileName).play(volume);
    }

    @Override
    public void dispose() {
        if (soundEffects != null) {
            for (Sound s : soundEffects.values())
                s.dispose();
            soundEffects.clear();
        }
        if (backgroundMusic != null)
            backgroundMusic.dispose();
    }
}