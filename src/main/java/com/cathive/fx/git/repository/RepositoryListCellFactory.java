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

import org.eclipse.jgit.lib.Repository;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * 
 * @author Benjamin P. Jung
 */
public class RepositoryListCellFactory implements Callback<ListView<Repository>, ListCell<Repository>> {

    private boolean fullBranchDisplayed = false;

    @Override
    public ListCell<Repository> call(final ListView<Repository> param) {
        final RepositoryListCell listCell = new RepositoryListCell();
        listCell.setFullBranchDisplayed(this.fullBranchDisplayed);
        return listCell;
    }

    public void setFullBranchDisplayed(boolean fullBranchDisplayed) {
        this.fullBranchDisplayed = fullBranchDisplayed;
    }

    public boolean isFullBranchDisplayed() {
        return this.fullBranchDisplayed;
    }

}
