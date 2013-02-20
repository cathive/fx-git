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

package com.cathive.fx.git.ref;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import com.cathive.fx.git.FxGit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuButtonBuilder;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleGroupBuilder;

/**
 * A MenuButton that can be used to switch between different Git Refs.
 * @see Repository
 * @see Ref
 * @author Benjamin P. Jung
 */
public class RefMenuButton extends MenuButton {

    private static final Logger LOGGER = Logger.getLogger(RefMenuButton.class.getName());

    private static final ResourceBundle resources = FxGit.getResources();
    private static final URL STYLESHEET = RefMenuButton.class.getResource("RefMenuButton.css");

    private final ToggleGroup toggleGroup = ToggleGroupBuilder.create().build();
    private final ObjectProperty<Repository> repository = new SimpleObjectProperty<>();
    private final Map<Ref, RefMenuItem> refMenuItems = new HashMap<>();
    private final ReadOnlyObjectWrapper<Ref> selectedRef = new ReadOnlyObjectWrapper<>();

    public RefMenuButton() {

        super();

        MenuButtonBuilder.create()
            .styleClass("git-ref-menu-button")
            .stylesheets(STYLESHEET.toExternalForm())
            .applyTo(this);

        this.toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                final Ref ref = newValue == null ? null : (Ref) newValue.getUserData();
                selectedRef.set(ref);
            }
        });

        this.selectedRef.addListener(new ChangeListener<Ref>() {
            @Override
            public void changed(ObservableValue<? extends Ref> observable, Ref oldValue, Ref newValue) {
                setText(newValue == null ? null: newValue.getName());
            }
        });

        this.repository.addListener(new ChangeListener<Repository>() {
            @Override
            public void changed(ObservableValue<? extends Repository> observable, Repository oldValue, Repository newValue) {

                // TODO Use an instance of FxRepository and bind to it's properties. 

                // Clears the current list of branches and tags.
                getItems().clear();
                refMenuItems.clear();

                // Clears the text property
                setText(null);

                final Git git = new Git(newValue);
                try {
                    final List<Ref> branches = git.branchList().call();
                    LOGGER.info(String.format("Repository contains %d branches.", branches.size()));
                    if (!branches.isEmpty()) {
                        getItems().add(new HeadingMenuItem(resources.getString("branches.title")));
                        for (final Ref branch: branches) {
                            getItems().add(createRefMenuItem(branch));
                        }
                    }
                } catch (final GitAPIException e) {
                    throw new RuntimeException(e);
                }

                final Map<String, Ref> tags = newValue.getTags();
                LOGGER.info(String.format("Repository contains %d tags.", tags.size()));
                if (!tags.isEmpty()) {
                    getItems().add(new HeadingMenuItem(resources.getString("tags.title")));
                    for (final Map.Entry<String, Ref> tagEntry: tags.entrySet()) {
                        getItems().add(createRefMenuItem(tagEntry.getValue()));
                    }
                }

            }

        });
    }

    public Repository getRepository() {
        return this.repository.get();
    }

    public void setRepository(final Repository repository) {
        this.repository.set(repository);
    }

    public ObjectProperty<Repository> repositoryProperty() {
        return this.repository;
    }

    public Ref getSelectedRef() {
        return this.selectedRef.get();
    }

    public void setSelectedRef(final Ref ref) {
        if (!this.refMenuItems.containsKey(ref)) {
            throw new IllegalArgumentException("Ref not found!");
        }
        toggleGroup.selectToggle(refMenuItems.get(ref));
    }

    public ReadOnlyObjectProperty<Ref> selectedRefProperty() {
        return selectedRef.getReadOnlyProperty();
    }

    /**
     * Helper method to wrap the passed <code>Ref</code> object
     * correctly into a <code>RefMenuItem</code>
     * @param ref
     *     The Git ref object to be wrapped.
     * @return
     *     A radio menu item that represents the passed Git ref object.
     */
    private RefMenuItem createRefMenuItem(final Ref ref) {
        final RefMenuItem item = new RefMenuItem(ref);
        item.setToggleGroup(toggleGroup);
        refMenuItems.put(ref, item);
        return item;
    }

    /**
     * A special separator item that contains the name of the "ref section"
     * that is about to follow, e.g. "branches" or "tags".
     * @author Benjamin P. Jung
     */
    private static class HeadingMenuItem extends SeparatorMenuItem {

        /** Graphical representation of this special separator menu item */
        private final Label label;

        HeadingMenuItem() {
            super();
            setHideOnClick(false);
            label = LabelBuilder.create()
                    .stylesheets(STYLESHEET.toExternalForm())
                    .build();
            label.textProperty().bind(this.textProperty());
            this.setContent(label);
        }

        HeadingMenuItem(final String text) {
            this();
            this.setText(text);
        }

    }

}
