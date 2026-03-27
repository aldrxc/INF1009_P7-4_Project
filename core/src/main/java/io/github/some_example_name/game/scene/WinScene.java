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

public class WinScene extends AbstractScene {

    private final SceneManager sceneManager;
    private final CellIOController ioController;
    private BitmapFont font;
    private String headerText;

    // -- NEW --
    private Texture bgTexture; 

    private Texture rTexture, escTexture, oTexture;

    // The rotating phrases for when the tumor wins
    private final String[] winPhrases = {
            "THE HOST FAILS",
            "ALL SYSTEMS COLLAPSE",
            "THERE IS NOTHING LEFT TO RESIST YOU."
    };

    public WinScene(SceneManager sceneManager, EngineServices services, CellIOController ioController) {
        super(services);
        if (sceneManager == null)
            throw new IllegalArgumentException("SceneManager cannot be null");
        this.sceneManager = sceneManager;
        this.ioController = ioController;
    }

    @Override
    protected void onInitialise() {
        font = new BitmapFont();
        font.getData().setScale(1.8f); // Slightly smaller to fit cleanly in the void
        font.setColor(new Color(0.9f, 0.7f, 1.0f, 1f)); // Light purple/pink to match the necrotic theme

        bgTexture = getServices().getAssets().getTexture("images/scenes/winscene.jpg");
        

        // Randomly select one phrase when the scene loads
        headerText = winPhrases[(int) (Math.random() * winPhrases.length)];

        rTexture = getServices().getAssets().getTexture("key-gui/settingKeys/r.png");
        escTexture = getServices().getAssets().getTexture("key-gui/settingKeys/escape.png");
        oTexture = getServices().getAssets().getTexture("key-gui/settingKeys/o.png");
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
        float cx = screenW / 2f;
        float cy = screenH / 2f;

        // ---------------------------------------------------------
        // 1. Draw the Background Image (Dimmed for readability!)
        // ---------------------------------------------------------
        SceneUiSupport.drawFullscreenBackground(output, bgTexture, screenW, screenH, new Color(0.4f, 0.4f, 0.4f, 1f));

        // ---------------------------------------------------------
        // 2. Draw Stats - Perfectly Centered
        // ---------------------------------------------------------
        float currentY = cy + 150f; // This is the mathematical center for this block size!
        float spacingMedium = 55f;
        float spacingSmall = 45f;
        float spacingPrompts = 55f;
        float spacingDonation = 50f;

        font.setColor(new Color(0.9f, 0.4f, 1.0f, 1f)); // Bright purple for the header
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

        // ---------------------------------------------------------
        // 3. Draw Input Prompts
        // ---------------------------------------------------------
        font.setColor(Color.YELLOW);
        float promptY = currentY - spacingPrompts;

        UIUtils.drawPromptCentered(output, font, rTexture, "[R] PLAY AGAIN", cx - 140f, promptY);
        UIUtils.drawPromptCentered(output, font, escTexture, "[ESC] MENU", cx + 140f, promptY);
        
        UIUtils.drawPromptCentered(output, font, oTexture, "[O] OPEN DONATION PAGE", cx, promptY - spacingDonation);

        output.endUi();
        output.endFrame();
    }

    @Override
    protected void onDispose() {
        if (font != null) font.dispose();
    }
}
