/*
 * Copyright (c) 2015. See AUTHORS file.
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

package com.mbrlabs.mundus.tools;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mbrlabs.mundus.core.project.ProjectContext;
import com.mbrlabs.mundus.input.InputManager;
import com.mbrlabs.mundus.shader.Shaders;
import com.mbrlabs.mundus.terrain.brushes.SphereBrushTool;

/**
 * @author Marcus Brummer
 * @version 25-12-2015
 */
public class ToolManager extends InputAdapter implements Disposable {

    private static final int KEY_DEACTIVATE = Input.Keys.ESCAPE;

    private Tool activeTool;

    public Tool sphereBrushTool;
    public ModelPlacementTool modelPlacementTool;

    private ProjectContext projectContext;
    private InputManager inputManager;
    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private Shaders shaders;

    public ToolManager(InputManager inputManager, PerspectiveCamera cam, ProjectContext projectContext, ModelBatch modelBatch, Shaders shaders) {
        this.projectContext = projectContext;
        this.inputManager = inputManager;
        this.modelBatch = modelBatch;
        this.activeTool = null;
        this.shaders = shaders;
        this.cam = cam;

        sphereBrushTool = new SphereBrushTool(projectContext, cam, shaders.brushShader, modelBatch);
        modelPlacementTool = new ModelPlacementTool(projectContext, cam, shaders.entityShader, modelBatch);

        this.inputManager.addProcessor(this);
    }

    public void activateTool(Tool tool) {
        deactivateTool();
        activeTool = tool;
        inputManager.addProcessor(activeTool);
    }

    public void deactivateTool() {
        if(activeTool != null) {
            inputManager.removeProcessor(activeTool);
            activeTool = null;
        }
    }

    public void render() {
        if(activeTool != null) {
            activeTool.render();
        }
    }

    public void act() {
        if(activeTool != null) {
            activeTool.act();
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == KEY_DEACTIVATE) {
            deactivateTool();
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        sphereBrushTool.dispose();
    }

}
