// Generated by Dagger (https://dagger.dev).
package io.github.leoallvez.take.ui.mediadetails;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.leoallvez.take.abtest.AbTest;
import io.github.leoallvez.take.data.repository.MediaDetailsRepository;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("io.github.leoallvez.take.di.AbDisplayAds")
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MediaDetailsViewModel_Factory implements Factory<MediaDetailsViewModel> {
  private final Provider<AbTest<Boolean>> _experimentProvider;

  private final Provider<MediaDetailsRepository> _repositoryProvider;

  public MediaDetailsViewModel_Factory(Provider<AbTest<Boolean>> _experimentProvider,
      Provider<MediaDetailsRepository> _repositoryProvider) {
    this._experimentProvider = _experimentProvider;
    this._repositoryProvider = _repositoryProvider;
  }

  @Override
  public MediaDetailsViewModel get() {
    return newInstance(_experimentProvider.get(), _repositoryProvider.get());
  }

  public static MediaDetailsViewModel_Factory create(Provider<AbTest<Boolean>> _experimentProvider,
      Provider<MediaDetailsRepository> _repositoryProvider) {
    return new MediaDetailsViewModel_Factory(_experimentProvider, _repositoryProvider);
  }

  public static MediaDetailsViewModel newInstance(AbTest<Boolean> _experiment,
      MediaDetailsRepository _repository) {
    return new MediaDetailsViewModel(_experiment, _repository);
  }
}