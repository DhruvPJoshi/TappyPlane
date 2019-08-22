/**
 * Copyright 2019 Dhruv Joshi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dhruvpjoshi.tappyplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dhruvpjoshi.tappyplane.constants.TappyPlaneKeys;

public class TappyPlane extends ApplicationAdapter {
  private Animation<TextureRegion> plane;
  private FPSLogger fpsLogger;
  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Texture background;
  private TextureRegion terrainAbove;
  private TextureRegion terrainBelow;
  private float terrainOffset;
  private float planeAnimTime;

  @Override
  public void create() {
    background = new Texture(TappyPlaneKeys.IMG_BACKGROUND);

    terrainBelow = new TextureRegion(new Texture(TappyPlaneKeys.IMG_TERR_GRASS));
    terrainAbove = new TextureRegion(terrainBelow);
    terrainAbove.flip(true, true);
    terrainOffset = 0.0f;

    batch = new SpriteBatch();
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    // maintain aspect ratio
    camera = new OrthographicCamera(30, 30 * (screenWidth / screenHeight));
    camera.setToOrtho(false, TappyPlaneKeys.SCN_WIDTH, TappyPlaneKeys.SCN_HEIGHT);
    fpsLogger = new FPSLogger();

    plane =
        new Animation(
            0.05f,
            new TextureRegion(new Texture(TappyPlaneKeys.IMG_PLANE_RED_1)),
            new TextureRegion(new Texture(TappyPlaneKeys.IMG_PLANE_RED_2)),
            new TextureRegion(new Texture(TappyPlaneKeys.IMG_PLANE_RED_3)),
            new TextureRegion(new Texture(TappyPlaneKeys.IMG_PLANE_RED_2)));
    plane.setPlayMode(Animation.PlayMode.LOOP);
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(1, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    fpsLogger.log();
    updateScene();
    drawScene();
  }

  /** Updates the properties of items in the scene. This is where the game logic is applied. */
  public void updateScene() {
    float deltaTime = Gdx.graphics.getDeltaTime();
    terrainOffset -= 200 * deltaTime;
    planeAnimTime += deltaTime;
  }

  /** Draws everything on the screen. */
  public void drawScene() {
    camera.update();
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    batch.disableBlending(); // we don't need blending for background texture
    batch.draw(background, 0, 0);
    batch.enableBlending();
    batch.draw(terrainBelow, terrainOffset, 0);
    batch.draw(terrainBelow, terrainOffset + terrainBelow.getRegionWidth(), 0);
    batch.draw(
        terrainAbove, terrainOffset, TappyPlaneKeys.SCN_HEIGHT - terrainAbove.getRegionHeight());
    batch.draw(
        terrainAbove,
        terrainOffset + terrainAbove.getRegionWidth(),
        TappyPlaneKeys.SCN_HEIGHT - terrainAbove.getRegionHeight());
    batch.draw(plane.getKeyFrame(planeAnimTime), 350, 200);
    batch.end();
  }

  @Override
  public void dispose() {
    background.dispose();
    batch.dispose();
  }
}
