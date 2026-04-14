package com.mycompany.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen extends ScreenAdapter {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont smallFont;

    private float playerX;
    private float playerY;
    private final float playerSize = 40f;
    private final float speed = 260f;

    private float npcX;
    private float npcY;
    private final float npcSize = 40f;

    private boolean dialogueActive = false;
    private int currentNode = 0;

    private String visibleText = "";
    private int visibleCharCount = 0;
    private float typeTimer = 0f;
    private final float typeSpeed = 0.03f;
    private boolean lineFullyShown = false;

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(2.0f);

        smallFont = new BitmapFont();
        smallFont.getData().setScale(1.3f);

        resetPositions();
    }

    private void resetPositions() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        playerX = width * 0.15f;
        npcX = width * 0.70f;

        float groundY = height * 0.25f;
        playerY = groundY;
        npcY = groundY;
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.10f, 0.10f, 0.12f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        float groundY = height * 0.25f;

        float dialogueBoxX = width * 0.08f;
        float dialogueBoxY = height * 0.05f;
        float dialogueBoxWidth = width * 0.84f;
        float dialogueBoxHeight = height * 0.30f;

        float portraitSize = 70f;

        float leftPortraitX = dialogueBoxX + 25;
        float leftPortraitY = dialogueBoxY + 25;

        float rightPortraitX = dialogueBoxX + dialogueBoxWidth - portraitSize - 25;
        float rightPortraitY = dialogueBoxY + 25;

        playerY = groundY;
        npcY = groundY;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rect(0, groundY - 10, width, 4);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(playerX, playerY, playerSize, playerSize);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(npcX, npcY, npcSize, npcSize);

        if (dialogueActive) {
            shapeRenderer.setColor(0f, 0f, 0f, 0.90f);
            shapeRenderer.rect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight);

            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(leftPortraitX, leftPortraitY, portraitSize, portraitSize);

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(rightPortraitX, rightPortraitY, portraitSize, portraitSize);
        }

        shapeRenderer.end();

        batch.begin();

        if (!dialogueActive && isNearNpc()) {
            float talkTextX = npcX - 35f;
            float talkTextY = npcY + npcSize + 60f;
            smallFont.draw(batch, "Press E to talk", talkTextX, talkTextY);
        }

        if (!dialogueActive) {
            smallFont.draw(batch, "Move: A / D    Talk: E", 20, 40);
        }

        if (dialogueActive) {
            DialogueNode node = getNode(currentNode);

            float namesY = dialogueBoxY + dialogueBoxHeight - 20;
            font.draw(batch, "White", dialogueBoxX + 25, namesY);
            font.draw(batch, "Red", dialogueBoxX + dialogueBoxWidth - 90, namesY);

            drawWrappedText(node, dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight);

            if (node.choices == null) {
                if (lineFullyShown) {
                    smallFont.draw(batch, "Press SPACE to continue", dialogueBoxX + 130, dialogueBoxY + 30);
                } else {
                    smallFont.draw(batch, "Press SPACE to skip typing", dialogueBoxX + 130, dialogueBoxY + 30);
                }
            } else if (lineFullyShown) {
                float choiceStartY = dialogueBoxY + 95;
                smallFont.draw(batch, "1. " + node.choices[0], dialogueBoxX + 130, choiceStartY + 50);
                smallFont.draw(batch, "2. " + node.choices[1], dialogueBoxX + 130, choiceStartY + 25);
                smallFont.draw(batch, "3. " + node.choices[2], dialogueBoxX + 130, choiceStartY);
            }
        }

        batch.end();
    }

    private void drawWrappedText(DialogueNode node, float boxX, float boxY, float boxWidth, float boxHeight) {
        String fullLine = node.speaker + ": " + visibleText;

        float textX = boxX + 130;
        float textY = boxY + boxHeight - 70;
        float wrapWidth = boxWidth - 260;

        font.draw(batch, fullLine, textX, textY, wrapWidth, -1, true);
    }

    private void update(float delta) {
        if (!dialogueActive) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                playerX -= speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                playerX += speed * delta;
            }

            float maxX = Gdx.graphics.getWidth() - playerSize;
            if (playerX < 0) playerX = 0;
            if (playerX > maxX) playerX = maxX;

            if (isNearNpc() && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                dialogueActive = true;
                goToNode(0);
            }
        } else {
            updateTyping(delta);

            DialogueNode node = getNode(currentNode);

            if (!lineFullyShown) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    visibleCharCount = node.text.length();
                    visibleText = node.text;
                    lineFullyShown = true;
                }
                return;
            }

            if (node.choices == null) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    if (node.nextNode == -1) {
                        closeDialogue();
                    } else {
                        goToNode(node.nextNode);
                    }
                }
            } else {
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    goToNode(node.choiceNext[0]);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                    goToNode(node.choiceNext[1]);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                    goToNode(node.choiceNext[2]);
                }
            }
        }
    }

    private void updateTyping(float delta) {
        DialogueNode node = getNode(currentNode);

        if (lineFullyShown) return;

        typeTimer += delta;

        while (typeTimer >= typeSpeed && !lineFullyShown) {
            typeTimer -= typeSpeed;
            visibleCharCount++;

            if (visibleCharCount >= node.text.length()) {
                visibleCharCount = node.text.length();
                lineFullyShown = true;
            }

            visibleText = node.text.substring(0, visibleCharCount);
        }
    }

    private void goToNode(int nodeIndex) {
        currentNode = nodeIndex;
        visibleText = "";
        visibleCharCount = 0;
        typeTimer = 0f;
        lineFullyShown = false;
    }

    private void closeDialogue() {
        dialogueActive = false;
        visibleText = "";
        visibleCharCount = 0;
        lineFullyShown = false;
    }

    private boolean isNearNpc() {
        float playerCenter = playerX + playerSize / 2f;
        float npcCenter = npcX + npcSize / 2f;
        return Math.abs(playerCenter - npcCenter) < 90f;
    }

    @Override
    public void resize(int width, int height) {
        float maxX = width - playerSize;
        if (playerX > maxX) {
            playerX = maxX;
        }

        if (npcX > width - npcSize) {
            npcX = width * 0.70f;
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
        smallFont.dispose();
    }

    private DialogueNode getNode(int id) {
        switch (id) {
            case 0:
                return new DialogueNode("Red", "You finally came.", null, null, 1);
            case 1:
                return new DialogueNode("White", "I was not sure if I should.", null, null, 2);
            case 2:
                return new DialogueNode("Red", "Then answer me honestly.", null, null, 3);
            case 3:
                return new DialogueNode(
                    "Red",
                    "Why are you here?",
                    new String[]{"I wanted to see you.", "I have questions.", "I got lost."},
                    new int[]{4, 5, 6},
                    -1
                );
            case 4:
                return new DialogueNode("White", "I wanted to see you.", null, null, 7);
            case 5:
                return new DialogueNode("White", "I have questions.", null, null, 8);
            case 6:
                return new DialogueNode("White", "I got lost.", null, null, 9);
            case 7:
                return new DialogueNode("Red", "That is a dangerous reason to come this far.", null, null, 10);
            case 8:
                return new DialogueNode("Red", "Questions can be heavier than weapons.", null, null, 10);
            case 9:
                return new DialogueNode("Red", "That may be the most honest answer tonight.", null, null, 10);
            case 10:
                return new DialogueNode(
                    "Red",
                    "Do you want to stay a little longer?",
                    new String[]{"Yes.", "No.", "Only if you talk first."},
                    new int[]{11, 12, 13},
                    -1
                );
            case 11:
                return new DialogueNode("White", "Yes. I do.", null, null, 14);
            case 12:
                return new DialogueNode("White", "No. I should go.", null, null, 15);
            case 13:
                return new DialogueNode("White", "Only if you talk first.", null, null, 16);
            case 14:
                return new DialogueNode("Red", "Then stay. Just for a while.", null, null, -1);
            case 15:
                return new DialogueNode("Red", "Then go. But come back when you are certain.", null, null, -1);
            case 16:
                return new DialogueNode("Red", "Fair enough. I was waiting for you.", null, null, -1);
            default:
                return new DialogueNode("Red", "The dialogue is over.", null, null, -1);
        }
    }
}