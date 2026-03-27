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
import io.github.some_example_name.game.util.RunStats;
import io.github.some_example_name.game.util.SceneFlow;
import io.github.some_example_name.game.util.UIUtils;

public class LoseScene extends AbstractScene {

    private final SceneManager sceneManager;
    private final CellIOController ioController;
    private BitmapFont font;
    private String headerText;

    private Texture rTexture, escTexture, oTexture;
    private Texture thumbnailTexture;

    // The rotating phrases for when the tumor is defeated
    private final String[] losePhrases = {
            "THE HOST SURVIVES.",
            "VITAL SYSTEMS STABILISED",
            "YOU CAN NO LONGER GROW."
    };

    public LoseScene(SceneManager sceneManager, EngineServices services, CellIOController ioController) {
        super(services);
        if (sceneManager == null)
            throw new IllegalArgumentException("SceneManager cannot be null");
        this.sceneManager = sceneManager;
        this.ioController = ioController;
    }

    @Override
    protected void onInitialise() {
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        font.setColor(new Color(0.2f, 0.8f, 0.4f, 1f)); 
        thumbnailTexture = getServices().getAssets().getTexture("images/scenes/losescene.jpg");

        rTexture = getServices().getAssets().getTexture("key-gui/settingKeys/r.png");
        escTexture = getServices().getAssets().getTexture("key-gui/settingKeys/escape.png");
        oTexture = getServices().getAssets().getTexture("key-gui/settingKeys/o.png");

        headerText = losePhrases[(int) (Math.random() * losePhrases.length)];
    }

    @Override
    protected void onUpdate(float delta) {
        CellInputMapper mapper = ioController.getInputMapper();

        if (mapper.checkRestartAction()) {
            SceneFlow.restartGame(sceneManager, getServices(), ioController);
        } else if (mapper.checkMenuAction()) {
            SceneFlow.goToStart(sceneManager);
        } else if (mapper.checkDonateAction()) {
            ioController.getWebService().openDonationSiteInBrowser();
        }
    }

    @Override
    public void render(float delta, float interpolationAlpha) {
        OutputManager output = getServices().getOutputManager();
        output.beginFrame();
        output.beginUi();

        float screenW = output.getUiWidth();
        float screenH = output.getUiHeight();
        SceneUiSupport.drawFullscreenBackground(output, thumbnailTexture, screenW, screenH, Color.WHITE);

        float cx = screenW / 2f;
        float cy = screenH / 2f;

        float currentY = cy + 150f;
        float spacingMedium = 55f;
        float spacingSmall = 45f;
        float spacingPrompts = 55f;
        float spacingDonation = 50f;

        font.setColor(new Color(0.2f, 0.8f, 0.4f, 1f));
        SceneUiSupport.drawCentered(output, font, headerText, cx, currentY);
        
        currentY -= spacingMedium; 
        font.setColor(Color.WHITE);
        SceneUiSupport.drawDivider(output, font, cx, currentY);
        
        currentY -= spacingMedium; 
        SceneUiSupport.drawCentered(output, font, "SCORE: " + RunStats.getLastScore(), cx, currentY);
        
        currentY -= spacingSmall; 
        SceneUiSupport.drawCentered(output, font, "CELLS INFECTED: " + RunStats.getLastInfectedCells(), cx, currentY);
        
        currentY -= spacingSmall; 
        SceneUiSupport.drawCentered(output,
                font,
                "FINAL SPREAD: " + Math.round(RunStats.getLastSpreadPercent()) + "%   LEVEL: " + RunStats.getLastLevel(),
                cx,
                currentY);
        
        currentY -= spacingMedium; 
        SceneUiSupport.drawDivider(output, font, cx, currentY);

        currentY -= spacingSmall;
        SceneUiSupport.drawCentered(output,
                font,
                "TIME: " + String.format("%.1fs   BEST SCORE: %d", RunStats.getLastSurvivalSeconds(), RunStats.getBestScore()),
                cx,
                currentY);
        
        float promptY = currentY - spacingPrompts;
        UIUtils.drawPromptCentered(output, font, rTexture, "[R] PLAY AGAIN", cx - 140f, promptY);
        UIUtils.drawPromptCentered(output, font, escTexture, "[ESC] MENU", cx + 140f, promptY);
        
        UIUtils.drawPromptCentered(output, font, oTexture, "[O] OPEN DONATION PAGE", cx, promptY - spacingDonation);

        output.endUi();
        output.endFrame();
    }

    @Override
    protected void onDispose() {
        if (font != null)
            font.dispose();
    }
}
