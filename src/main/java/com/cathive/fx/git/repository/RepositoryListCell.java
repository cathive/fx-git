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

package com.cathive.fx.git.repository;

import java.io.IOException;

import org.eclipse.jgit.lib.Repository;

import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBoxBuilder;

/**
 * 
 * @author Benjamin P. Jung
 */
public class RepositoryListCell extends ListCell<Repository> {

    private boolean fullBranchDisplayed = false;

    @Override
    protected void updateItem(final Repository item, final boolean empty) {

        super.updateItem(item, empty);

        if (item != null) {
            
            setText(null);

            String branchText = null;
            String repositoryNameText = null;

            repositoryNameText = item.isBare() ? item.getDirectory().getAbsolutePath()
                                               : item.getWorkTree().getAbsolutePath();

            try {
                branchText = fullBranchDisplayed ? item.getFullBranch() : item.getBranch();
            } catch (final IOException e) {
                branchText = "<<Error>>"; // TODO Implement better error handling!
            }

            setGraphic(VBoxBuilder.create()
                .children(
                    LabelBuilder.create()
                            .text(repositoryNameText)
                            .build(),
                    LabelBuilder.create()
                            .text(branchText)
                            .build())
                .build());

        }

    }

    public void setFullBranchDisplayed(boolean fullBranchDisplayed) {
        this.fullBranchDisplayed = fullBranchDisplayed;
    }

    public boolean isFullBranchDisplayed() {
        return this.fullBranchDisplayed;
    }

}
