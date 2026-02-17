// core/src/main/java/io/github/some_example_name/tests/Demo/TestWinScene.java
package io.github.some_example_name.tests.Demo;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import io.github.some_example_name.engine.io.IOManager;
import io.github.some_example_name.engine.io.OutputManager;
import io.github.some_example_name.engine.scene.AbstractScene;
import io.github.some_example_name.engine.scene.SceneManager;

public class TestWinScene extends AbstractScene {

    private BitmapFont font;

    @Override
    protected void onInitialise() {
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        font.setColor(Color.LIME);
    }

    @Override
    protected void onUpdate(float delta) {
        SceneManager sm = SceneManager.getInstance();
        if (IOManager.getInstance().getDynamicInput().isKeyJustPressed(Input.Keys.R)) {
            sm.unload("main");
            sm.load("main", new TestMainScene());
            sm.setActive("main");
        }
        if (IOManager.getInstance().getDynamicInput().isKeyJustPressed(Input.Keys.ENTER)) {
            sm.setActive("start");
        }
    }

    @Override
    public void render(float delta) {
        OutputManager output = IOManager.getInstance().getOutputManager();
        output.beginFrame();

        float cx = output.getWorldWidth() / 2f;
        drawCentered(output, "YOU WIN", cx, 370f);
        drawCentered(output, "SCORE: " + DemoRunStats.getLastScore(), cx, 320f);
        drawCentered(output, "SURVIVED: " + String.format("%.1fs", DemoRunStats.getLastSurvivalSeconds()), cx, 285f);
        drawCentered(output, "BEST SCORE: " + DemoRunStats.getBestScore(), cx, 250f);
        drawCentered(output, "R: RESTART   ENTER: START MENU", cx, 200f);

        output.endFrame();
    }

    private void drawCentered(OutputManager output, String text, float centerX, float y) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(output.getBatch(), layout, centerX - layout.width / 2f, y);
    }

    @Override
    protected void onDispose() {
        if (font != null) font.dispose();
    }
}
