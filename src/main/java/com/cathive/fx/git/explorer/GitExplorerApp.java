/*
 * Copyright (C) 2013 The Cat Hive Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cathive.fx.git.explorer;

import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.PerspectiveCameraBuilder;
import javafx.scene.SceneBuilder;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.StageStyle;

/**
 * 
 * @author Benjamin P. Jung
 */
public final class GitExplorerApp extends Application {

    private final Logger LOGGER = Logger.getLogger(GitExplorerApp.class.getName());

    private ResourceBundle resources;
    private GitExplorerPane gitPane = new GitExplorerPane();


    @Override
    public void init() throws Exception {
        super.init();
        final String resourceBundleName = getClass().getName();
        LOGGER.info(String.format("Loading resources from resource bundle: '%s.'", resourceBundleName));
        resources = ResourceBundle.getBundle(resourceBundleName);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        primaryStage.initStyle(StageStyle.DECORATED);
        StageBuilder.create()
            .resizable(true)
            .scene(SceneBuilder.create()
                .camera(PerspectiveCameraBuilder.create().build())
                .root(gitPane)
                .stylesheets("/com/cathive/fx/git/explorer/GitExplorerApp.css")
                .build())
            .title(resources.getString("app.title"))
            .applyTo(primaryStage);

        primaryStage.centerOnScreen();
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
