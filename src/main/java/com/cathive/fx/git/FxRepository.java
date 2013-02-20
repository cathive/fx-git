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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.events.ConfigChangedEvent;
import org.eclipse.jgit.events.ConfigChangedListener;
import org.eclipse.jgit.events.IndexChangedEvent;
import org.eclipse.jgit.events.IndexChangedListener;
import org.eclipse.jgit.events.ListenerHandle;
import org.eclipse.jgit.events.ListenerList;
import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.events.RefsChangedListener;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

/**
 * A wrapper around JGit's {@link Repository} class that offers a JavaFX-friendly
 * API.
 * 
 * @see Repository
 * @author Benjamin P. Jung
 */
public final class FxRepository implements ConfigChangedListener, IndexChangedListener, RefsChangedListener {

    private static final Logger LOGGER = Logger.getLogger(FxRepository.class.getName());

    /** The wrapped repository */
    private Repository repository;

    // Handles of the different listeners that have been attached to the wrapped repository.
    private ListenerHandle configChangedHandle;
    private ListenerHandle indexChangedHandle;
    private ListenerHandle refsChangedHandle;

    private final ReadOnlyObjectWrapper<Ref> headTargetRef = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyStringWrapper headTargetRefName = new ReadOnlyStringWrapper();

    private final ReadOnlyListWrapper<Ref> branches = new ReadOnlyListWrapper<Ref>(FXCollections.observableList(new ArrayList<Ref>()));
    private final ReadOnlyListWrapper<Ref> tags = new ReadOnlyListWrapper<Ref>(FXCollections.observableList(new ArrayList<Ref>()));

    /**
     * Creates a wrapper around the given {@link Repository}.
     * @param repository
     *     Repository to be wrapped. Must not be null and cannot be changed later on.
     */
    private FxRepository(final Repository repository) {

        super();

        this.repository = repository;
        this.headTargetRef.addListener(new ChangeListener<Ref>() {
            @Override
            public void changed(ObservableValue<? extends Ref> observable, Ref oldValue, Ref newValue) {
                headTargetRefName.set(newValue.getName());
            }
        });

        // Attache this class as a listener to the wrapped repository.
        final ListenerList listeners = repository.getListenerList();
        configChangedHandle = listeners.addConfigChangedListener(this);
        indexChangedHandle = listeners.addIndexChangedListener(this);
        refsChangedHandle = listeners.addRefsChangedListener(this);

        // Initialize the properties correctly.
        this.onConfigChanged();
        this.onIndexChanged();
        this.onRefsChanged();

    }

    @Override
    public void onConfigChanged(ConfigChangedEvent event) {
        System.out.println("Configuration has been changed!");
        onConfigChanged();
    }

    private void onConfigChanged() {
        
    }

    @Override
    public void onIndexChanged(IndexChangedEvent event) {
        System.out.println("Index has been changed!");
    }

    private void onIndexChanged() {

        final Git git = new Git(repository);

        try {
            final List<Ref> newBranches = git.branchList().call();
            LOGGER.info(String.format("Repository contains %d branches.", newBranches.size()));
            updateRefList(this.getBranches(), newBranches);
        } catch (final GitAPIException e) {
            throw new RuntimeException(e);
        }

        try {
            final List<Ref> newTags = git.tagList().call();
            LOGGER.info(String.format("Repository contains %d tags.", newTags.size()));
            updateRefList(this.getTags(), newTags);
        } catch (final GitAPIException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onRefsChanged(RefsChangedEvent event) {
        System.out.println("Refs have been changed!");
        onRefsChanged();
    }

    private void onRefsChanged() {
        try {
            final Ref headTarget = repository.getRef(Constants.HEAD).getTarget();
            LOGGER.info(String.format("HEAD points to '%s'.", headTarget == null ? "<null>" : headTarget.getName()));
            this.headTargetRef.set(headTarget);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Ref> getBranches() {
        return this.branches;
    }

    public ObservableList<Ref> getTags() {
        return this.tags;
    }

    public Repository getRepository() {
        return this.repository;
    }

    public Ref getHeadTargetRef() {
        return this.headTargetRef.get();
    }

    public ReadOnlyObjectProperty<Ref> headTargetRefProperty() {
        return this.headTargetRef.getReadOnlyProperty();
    }

    public String getHeadTargetRefName() {
        return this.headTargetRefName.get();
    }

    public ReadOnlyStringProperty headTargetRefNameProperty() {
        return this.headTargetRefName.getReadOnlyProperty();
    }

    /**
     * Returns a JavaFX friendly wrapped instance of this repository.
     * @param repository
     * @return
     */
    public static FxRepository get(final Repository repository) {
        // TODO Use Caching mechanism and avoid generation of
        //      multiple FxRepositories.
        return new FxRepository(repository);
    }

    private final void updateRefList(final List<Ref> currentRefs, final List<Ref> newRefs) {
        if (newRefs == null || newRefs.isEmpty()) {
            currentRefs.clear();
        } else if (currentRefs.isEmpty()) {
            currentRefs.addAll(newRefs);
        } else {
            final Iterator<Ref> it = currentRefs.iterator();
            while (it.hasNext()) {
                final Ref ref = it.next();
                if (!newRefs.contains(ref)) {
                    it.remove();
                }
            }
            for (final Ref ref: newRefs) {
                if (!currentRefs.contains(ref)) {
                    currentRefs.add(ref);
                }
            }
        }
    }

}
