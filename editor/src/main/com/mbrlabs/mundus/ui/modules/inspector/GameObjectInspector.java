/*
 * Copyright (c) 2016. See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mbrlabs.mundus.ui.modules.inspector;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.core.Inject;
import com.mbrlabs.mundus.core.Mundus;
import com.mbrlabs.mundus.core.project.ProjectManager;
import com.mbrlabs.mundus.scene3d.components.ModelComponent;
import com.mbrlabs.mundus.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.ui.modules.inspector.components.ComponentWidget;
import com.mbrlabs.mundus.ui.modules.inspector.components.IdentifierWidget;
import com.mbrlabs.mundus.ui.modules.inspector.components.ModelComponentWidget;
import com.mbrlabs.mundus.ui.modules.inspector.components.TransformWidget;
import com.mbrlabs.mundus.ui.modules.inspector.components.terrain.TerrainComponentWidget;

/**
 * @author Marcus Brummer
 * @version 13-10-2016
 */
public class GameObjectInspector extends VisTable {

    private IdentifierWidget identifierWidget;
    private TransformWidget transformWidget;
    private Array<ComponentWidget> componentWidgets;
    private VisTextButton addComponentBtn;
    private VisTable componentTable;

    private GameObject gameObject;

    @Inject
    private ProjectManager projectManager;

    public GameObjectInspector() {
        super();
        Mundus.inject(this);
        align(Align.top);

        identifierWidget = new IdentifierWidget();
        transformWidget = new TransformWidget();
        componentWidgets = new Array<>();
        addComponentBtn = new VisTextButton("Add Component");
        componentTable = new VisTable();

        add(identifierWidget).growX().pad(7).row();
        add(transformWidget).growX().pad(7).row();
        for (BaseInspectorWidget cw : componentWidgets) {
            componentTable.add(cw).row();
        }
        add(componentTable).growX().pad(7).row();
        add(addComponentBtn).expandX().fill().top().center().pad(10).row();
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;

        // build ui
        buildComponentWidgets();
        componentTable.clearChildren();
        for (ComponentWidget cw : componentWidgets) {
            componentTable.add(cw).grow().row();
        }

        // update
        updateGameObject();
    }

    public void updateGameObject() {
        if (gameObject != null) {
            identifierWidget.setValues(gameObject);
            transformWidget.setValues(gameObject);

            for (ComponentWidget cw : componentWidgets) {
                cw.setValues(gameObject);
            }
        }
    }

    private void buildComponentWidgets() {
        if (gameObject != null) {
            componentWidgets.clear();
            for (Component component : gameObject.getComponents()) {
                // model component widget
                if (component.getType() == Component.Type.MODEL) {
                    componentWidgets.add(new ModelComponentWidget((ModelComponent) component));
                    // terrainAsset component widget
                } else if (component.getType() == Component.Type.TERRAIN) {
                    componentWidgets.add(new TerrainComponentWidget((TerrainComponent) component));
                }
            }
        }
    }

}
