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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.dhruvpjoshi.tappyplane.constants.TappyPlaneKeys;

public class TappyPlane extends ApplicationAdapter {
  private FPSLogger fpsLogger;
  private OrthographicCamera camera;
  private SpriteBatch batch;
  private Texture background;

  @Override
  public void create() {
    background = new Texture(TappyPlaneKeys.IMG_BACKGROUND);
    batch = new SpriteBatch();
    camera = new OrthographicCamera();
    camera.setToOrtho(false, TappyPlaneKeys.SCN_WIDTH, TappyPlaneKeys.SCN_HEIGHT);
    fpsLogger = new FPSLogger();
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
  public void updateScene() {}

  /** Draws everything on the screen. */
  public void drawScene() {
    camera.update();
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    batch.draw(background, 0, 0);
    batch.end();
  }

  @Override
  public void dispose() {
    background.dispose();
    batch.dispose();
  }
}
