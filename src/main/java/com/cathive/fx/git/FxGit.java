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

package com.cathive.fx.git;

import java.util.ResourceBundle;

import org.eclipse.jgit.events.ConfigChangedEvent;
import org.eclipse.jgit.events.ConfigChangedListener;
import org.eclipse.jgit.events.IndexChangedEvent;
import org.eclipse.jgit.events.IndexChangedListener;
import org.eclipse.jgit.events.ListenerHandle;
import org.eclipse.jgit.events.ListenerList;
import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.events.RefsChangedListener;
import org.eclipse.jgit.lib.Repository;

/**
 * This class offers a bunch of static helper methods.
 * @author Benjamin P. Jung
 */
public final class FxGit {

    private FxGit() {
        // No instance of this class should ever be needed.
        super();
    }

    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(FxGit.class.getName());
    public static ResourceBundle getResources() {
        return RESOURCES;
    }

    private static final ListenerHandle CONFIG_CHANGED_HANDLE;
    static {
        final ListenerList listeners = Repository.getGlobalListenerList();
        CONFIG_CHANGED_HANDLE = listeners.addConfigChangedListener(new ConfigChangedListener() {
            @Override
            public void onConfigChanged(ConfigChangedEvent event) {
                System.out.println("Configuration has been changed!");
            }
        });
    }

    private static final ListenerHandle INDEX_CHANGED_HANDLE;
    static {
        final ListenerList listeners = Repository.getGlobalListenerList();
        INDEX_CHANGED_HANDLE = listeners.addIndexChangedListener(new IndexChangedListener() {
            @Override
            public void onIndexChanged(IndexChangedEvent event) {
                System.out.println("Index has been changed!");
            }
        });
    }

    private static final ListenerHandle REFS_CHANGED_HANDLE;
    static {
        final ListenerList listeners = Repository.getGlobalListenerList();
        REFS_CHANGED_HANDLE = listeners.addRefsChangedListener(new RefsChangedListener() {
            @Override
            public void onRefsChanged(RefsChangedEvent event) {
                System.out.println("Refs have been changed!");
            }
        });
    }

}
