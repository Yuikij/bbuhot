package com.bbuhot.server.service;

import com.bbuhot.server.config.Configuration;
import com.bbuhot.server.config.Flags;
import com.bbuhot.server.entity.EntityModule;
import com.bbuhot.server.persistence.TestPersistenceModule;
import com.bbuhot.server.util.TestMessageUtil;
import dagger.Component;

import javax.inject.Singleton;

@Component(
    modules = {
      TestPersistenceModule.class,
      EntityModule.class,
    })
@Singleton
abstract class TestServiceComponent {

  static TestServiceComponent getInstance() {
    Configuration configuration =
            new TestMessageUtil("")
                    .getResourcesAsMessage(
                            Configuration.getDefaultInstance(),
                            "com/bbuhot/server/service/configuration_for_test.json");
    Flags.initializeWithConfiguration(configuration);
    return DaggerTestServiceComponent.create();
  }

  abstract void inject(AdminGameServicesTest adminGameServicesTest);

    abstract void inject(BetUpdatingServiceTest betUpdatingServiceTest);

    abstract void inject(GameListingServiceTest gameListingServiceTest);
}
