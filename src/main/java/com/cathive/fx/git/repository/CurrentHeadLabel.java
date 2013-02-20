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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

import org.eclipse.jgit.lib.Repository;

import com.cathive.fx.git.FxRepository;

/**
 * 
 * @author Benjamin P. Jung
 */
public class CurrentHeadLabel extends Label {

    private final ObjectProperty<Repository> repository = new SimpleObjectProperty<>();

    public CurrentHeadLabel() {
        super();
        repository.addListener(new ChangeListener<Repository>() {
            @Override
            public void changed(ObservableValue<? extends Repository> observable, Repository oldValue, Repository newValue) {
                textProperty().unbind();
                if (newValue != null) {
                    final FxRepository fxRepo = FxRepository.get(newValue);
                    textProperty().bind(fxRepo.headTargetRefNameProperty());
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

}
