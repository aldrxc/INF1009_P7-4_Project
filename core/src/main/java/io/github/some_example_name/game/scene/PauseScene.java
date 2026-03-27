package io.github.some_example_name.game.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.some_example_name.engine.io.EngineServices;
import io.github.some_example_name.engine.io.OutputManager;
import io.github.some_example_name.engine.scene.AbstractScene;
import io.github.some_example_name.engine.scene.SceneManager;
import io.github.some_example_name.game.io.CellIOController;
import io.github.some_example_name.game.io.CellInputMapper;
import io.github.some_example_name.game.util.SceneFlow;
import io.github.some_example_name.game.util.UIUtils;

public class PauseScene extends AbstractScene {

    private final SceneManager sceneManager;
    private final CellIOController ioController;
    private BitmapFont font;

    // Load the sleeping monster!
    private Texture bgTexture; 

    private Texture pTexture, rTexture, escTexture;
    private Texture wTexture, aTexture, sTexture, dTexture;
    private Texture upTexture, leftTexture, downTexture, rightTexture;
    private Texture shiftTexture;

    public PauseScene(SceneManager sceneManager, EngineServices services, CellIOController ioController) {
        super(services);
        if (sceneManager == null)
            throw new IllegalArgumentException("SceneManager cannot be null");
        this.sceneManager = sceneManager;
        this.ioController = ioController;
    }

    @Override
    protected void onInitialise() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        bgTexture = getServices().getAssets().getTexture("images/scenes/pausescene.jpg");

        pTexture = getServices().getAssets().getTexture("key-gui/settingKeys/p.png");
        rTexture = getServices().getAssets().getTexture("key-gui/settingKeys/r.png");
        escTexture = getServices().getAssets().getTexture("key-gui/settingKeys/escape.png");

        wTexture = getServices().getAssets().getTexture("key-gui/movement/w.png");
        aTexture = getServices().getAssets().getTexture("key-gui/movement/a.png");
        sTexture = getServices().getAssets().getTexture("key-gui/movement/s.png");
        dTexture = getServices().getAssets().getTexture("key-gui/movement/d.png");

        upTexture = getServices().getAssets().getTexture("key-gui/movement/arrow_up.png");
        leftTexture = getServices().getAssets().getTexture("key-gui/movement/arrow_left.png");
        downTexture = getServices().getAssets().getTexture("key-gui/movement/arrow_down.png");
        rightTexture = getServices().getAssets().getTexture("key-gui/movement/arrow_right.png");

        shiftTexture = getServices().getAssets().getTexture("key-gui/movement/shift.png");
    }

    @Override
    protected void onUpdate(float delta) {
        CellInputMapper mapper = ioController.getInputMapper();
        
        if (mapper.checkPauseAction()) {
            sceneManager.setActive("game");
        } else if (mapper.checkRestartAction()) {
            SceneFlow.restartGame(sceneManager, getServices(), ioController);
        } else if (mapper.checkMenuAction()) {
            SceneFlow.goToStart(sceneManager);
        }
    }

    @Override
    public void render(float delta, float interpolationAlpha) {
        OutputManager output = getServices().getOutputManager();
        output.beginFrame();
        output.beginUi();

        float screenW = output.getUiWidth();
        float screenH = output.getUiHeight();
        float cx = screenW / 2f;
        float cy = screenH / 2f;

        // 1. Draw the sleeping tumor full screen
        SceneUiSupport.drawFullscreenBackground(output, bgTexture, screenW, screenH, new Color(0.5f, 0.5f, 0.5f, 1f));

        // 2. PAUSE MENU
        font.getData().setScale(2.0f);
        font.setColor(Color.YELLOW);
        SceneUiSupport.drawCentered(output, font, "GAME PAUSED", cx, cy + 120f);

        font.getData().setScale(1.2f);
        font.setColor(Color.WHITE);
        SceneUiSupport.drawDivider(output, font, cx, cy + 70f);

        float menuStartX = cx - 110f; 
        UIUtils.drawPromptLeftAligned(output, font, pTexture, "[P] RESUME", menuStartX, cy + 10f);
        UIUtils.drawPromptLeftAligned(output, font, rTexture, "[R] RESTART", menuStartX, cy - 40f);
        UIUtils.drawPromptLeftAligned(output, font, escTexture, "[ESC] MAIN MENU", menuStartX, cy - 90f);
        
        SceneUiSupport.drawDivider(output, font, cx, cy - 130f);

        // 3. CONTROLS
        font.getData().setScale(1.0f);
        font.setColor(Color.LIGHT_GRAY);
        
        SceneUiSupport.drawMovementAndActionLegend(output, font,
                wTexture, aTexture, sTexture, dTexture,
                upTexture, leftTexture, downTexture, rightTexture,
                shiftTexture, pTexture, "Resume", "[P]", screenW);

        output.endUi();
        output.endFrame();
    }

    @Override
    protected void onDispose() {
        if (font != null) font.dispose();
    }
}
