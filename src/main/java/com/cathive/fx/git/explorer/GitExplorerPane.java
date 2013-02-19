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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.cathive.fx.git.ref.RefMenuButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.DirectoryChooserBuilder;

/**
 * 
 * @author Benjamin P. Jung
 */
public final class GitExplorerPane extends BorderPane {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private ListView<Repository> localRepositoriesListView;
    @FXML private RefMenuButton refMenuButton;

    public GitExplorerPane() {

        super();

        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(this);
        fxmlLoader.setResources(ResourceBundle.getBundle(GitExplorerApp.class.getName()));
        fxmlLoader.setRoot(this);
        fxmlLoader.setLocation(getClass().getResource("GitExplorerPane.fxml"));
        try {
            fxmlLoader.load();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void initialize() throws Exception {
    }

    /**
     * This method is called when the user intents to create a new repository
     * or load an existing repository.
     * @throws IOException 
     */
    @FXML
    void onNewRepository() throws IOException {
        final DirectoryChooser dc = DirectoryChooserBuilder.create()
                .title(resources.getString("repository.new.directoryChooser.title"))
                .build();
        final File dir = dc.showDialog(getScene().getWindow());
        if (dir != null) {
            final Repository repo = new FileRepositoryBuilder() //
                .setWorkTree(dir)
                .findGitDir() // scan up the file system tree
                .readEnvironment() // scan environment GIT_* variables
                .build();
            this.localRepositoriesListView.getItems().add(repo);
        }
    }

}
