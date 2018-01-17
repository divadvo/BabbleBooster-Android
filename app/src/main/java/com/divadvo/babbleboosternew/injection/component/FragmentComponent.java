package com.divadvo.babbleboosternew.injection.component;

import dagger.Subcomponent;
import com.divadvo.babbleboosternew.injection.PerFragment;
import com.divadvo.babbleboosternew.injection.module.FragmentModule;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
}
