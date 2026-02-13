package io.github.some_example_name.engine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

/**
 * AudioOutput - handles sound effects (sfx) and music
 * key feature: caching / lazy loading
 * loading audio from disk is slow
 * so only load once, store a map, and reuse next time
 */
public class AudioOutput implements Disposable {

    // cache to store loaded sounds so we dont reload them
    private Map<String, Sound> soundEffects;
    private Music backgroundMusic;
    private float volume = 1.0f;

    public void initialize() {
        soundEffects = new HashMap<>();
    }

    /**
     * plays a sound effect
     * 
     * @param fileName path to file (e.g. "jump.wav")
     */
    public void playSound(String fileName) {
        // check if this sound is already loaded
        if (!soundEffects.containsKey(fileName)) {
            // if no, check if file exists on disk
            if (Gdx.files.internal(fileName).exists()) {
                // load and save to map to cache it
                Sound s = Gdx.audio.newSound(Gdx.files.internal(fileName));
                soundEffects.put(fileName, s);
            } else {
                // avoid crashing if file is missing
                return;
            }
        }
        // play sound from cache
        soundEffects.get(fileName).play(volume);
    }

    @Override
    public void dispose() {
        // loop through every sound in cache and dispose it
        if (soundEffects != null) {
            for (Sound s : soundEffects.values()) {
                s.dispose();
            }
            soundEffects.clear();
        }
        // dispose music separately - its streamed, not cached usually
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
    }
}