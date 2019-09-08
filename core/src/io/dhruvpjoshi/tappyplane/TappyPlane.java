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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dhruvpjoshi.tappyplane.constants.TappyPlaneKeys;

public class TappyPlane extends ApplicationAdapter {
  private Animation<TextureRegion> plane;
  private FPSLogger fpsLogger;
  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Texture background;
  private TextureRegion terrainAbove;
  private TextureRegion terrainBelow;
  private TextureAtlas atlas;
  private float terrainOffset;
  private float planeAnimTime;
  private Vector2 planeVelocity;
  private Vector2 planePosition;
  private Vector2 planeDefaultPosition;
  private Vector2 gravity;
  private static final Vector2 damping = new Vector2(0.99f, 0.99f);

  @Override
  public void create() {
    atlas = new TextureAtlas(Gdx.files.internal(TappyPlaneKeys.ATLAS));
    background = atlas.findRegion(TappyPlaneKeys.IMG_BACKGROUND).getTexture();

    terrainBelow = atlas.findRegion(TappyPlaneKeys.IMG_TERR_GRASS);
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
            atlas.findRegion(TappyPlaneKeys.IMG_PLANE_RED_1),
            atlas.findRegion(TappyPlaneKeys.IMG_PLANE_RED_2),
            atlas.findRegion(TappyPlaneKeys.IMG_PLANE_RED_3),
            atlas.findRegion(TappyPlaneKeys.IMG_PLANE_RED_2));
    plane.setPlayMode(Animation.PlayMode.LOOP);

    planeVelocity = new Vector2();
    planePosition = new Vector2();
    planeDefaultPosition = new Vector2();
    gravity = new Vector2();

    resetScene();
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
    planeVelocity.scl(damping);
    planeVelocity.add(gravity);
    planePosition.mulAdd(planeVelocity, deltaTime);
    planePosition.x = planeDefaultPosition.x;
    // seamless terrain illusion
    if (terrainOffset * -1 > terrainBelow.getRegionWidth()) {
      terrainOffset = 0;
    }
    if (terrainOffset > 0) {
      terrainOffset = -terrainBelow.getRegionWidth();
    }
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
    batch.draw(plane.getKeyFrame(planeAnimTime), planePosition.x, planePosition.y);
    batch.end();
  }

  private void resetScene() {
    terrainOffset = 0;
    planeAnimTime = 0;
    planeVelocity.set(0, 0);
    gravity.set(0, -2);
    planeDefaultPosition.set(400 - 88 / 2, 240 - 73 / 2);
    planePosition.set(planeDefaultPosition.x, planeDefaultPosition.y);
  }

  @Override
  public void dispose() {
    background.dispose();
    batch.dispose();
  }
}
