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

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioMenuItem;

import org.eclipse.jgit.lib.Ref;

/**
 * A special radio menu item to help you select your current tag or branch.
 * @author Benjamin P. Jung
 */
public final class RefMenuItem extends RadioMenuItem {

    private ReadOnlyObjectWrapper<Ref> ref = new ReadOnlyObjectWrapper<>();
    
    public RefMenuItem() {
        super(null);
        ref.addListener(new ChangeListener<Ref>() {
            @Override
            public void changed(ObservableValue<? extends Ref> observable, Ref oldValue, Ref newValue) {
                setUserData(newValue);
                setText(newValue.getName());
            }
        });
    }

    public RefMenuItem(final Ref ref) {
        this();
        this.ref.set(ref);
    }

    public Ref getRef() {
        return this.ref.get();
    }
    public ReadOnlyObjectProperty<Ref> refProperty() {
        return this.ref.getReadOnlyProperty();
    }

}
