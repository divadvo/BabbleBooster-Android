package com.divadvo.babbleboosternew.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import com.divadvo.babbleboosternew.common.injection.module.ApplicationTestModule;
import com.divadvo.babbleboosternew.injection.component.AppComponent;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends AppComponent {
}
