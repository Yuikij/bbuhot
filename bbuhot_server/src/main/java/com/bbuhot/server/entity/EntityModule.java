package com.bbuhot.server.entity;

import com.google.common.collect.Sets;
import dagger.Module;
import dagger.Provides;
import java.util.Set;
import javax.inject.Singleton;

@Module
public class EntityModule {

  public static final Set<Class<?>> annotatedClasses =
      Sets.newHashSet(
          // Keep alphabetical order.
          GameEntity.BetEntity.class, GameEntity.class, UserEntity.class);

  @Provides
  @Singleton
  Set<Class<?>> annotatedClasses() {
    return annotatedClasses;
  }
}
