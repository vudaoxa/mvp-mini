/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package net.mfilm.di.modules

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import net.mfilm.di.ActContext
import net.mfilm.di.PerActivity
import net.mfilm.ui.about.AboutMvpPresenter
import net.mfilm.ui.about.AboutMvpView
import net.mfilm.ui.about.AboutPresenter
import net.mfilm.ui.categories.CategoriesMvpPresenter
import net.mfilm.ui.categories.CategoriesMvpView
import net.mfilm.ui.categories.CategoriesPresenter
import net.mfilm.ui.chapter_images.ChapterImagesMvpPresenter
import net.mfilm.ui.chapter_images.ChapterImagesMvpView
import net.mfilm.ui.chapter_images.ChapterImagesPresenter
import net.mfilm.ui.chapters.ChaptersMvpPresenter
import net.mfilm.ui.chapters.ChaptersMvpView
import net.mfilm.ui.chapters.ChaptersPresenter
import net.mfilm.ui.favorites.FavoritesMvpPresenter
import net.mfilm.ui.favorites.FavoritesMvpView
import net.mfilm.ui.favorites.FavoritesPresenter
import net.mfilm.ui.history.HistoryMvpPresenter
import net.mfilm.ui.history.HistoryMvpView
import net.mfilm.ui.history.HistoryPresenter
import net.mfilm.ui.main.MainMvpPresenter
import net.mfilm.ui.main.MainMvpView
import net.mfilm.ui.main.MainPresenter
import net.mfilm.ui.manga_info.MangaInfoMvpPresenter
import net.mfilm.ui.manga_info.MangaInfoMvpView
import net.mfilm.ui.manga_info.MangaInfoPresenter
import net.mfilm.ui.mangas.MangasMvpPresenter
import net.mfilm.ui.mangas.MangasMvpView
import net.mfilm.ui.mangas.MangasPresenter
import net.mfilm.ui.mangas.search.SearchHistoryMvpPresenter
import net.mfilm.ui.mangas.search.SearchHistoryPresenter
import net.mfilm.ui.search_history.SearchHistoryMvpView
import net.mfilm.ui.settings.SettingsMvpPresenter
import net.mfilm.ui.settings.SettingsMvpView
import net.mfilm.ui.settings.SettingsPresenter
import net.mfilm.ui.splash.SplashMvpPresenter
import net.mfilm.ui.splash.SplashMvpView
import net.mfilm.ui.splash.SplashPresenter

/**
 * Created by janisharali on 27/01/17.
 */

@Module
class ActModule(val mActivity: Activity) {

    @Provides
    @ActContext
    fun provideContext(): Context {
        return mActivity
    }

    @Provides
    fun provideActivity(): Activity {
        return mActivity
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @PerActivity
    fun provideSplashPresenter(presenter: SplashPresenter<SplashMvpView>): SplashMvpPresenter<SplashMvpView> {
        return presenter
    }

    @Provides
    fun provideAboutPresenter(presenter: AboutPresenter<AboutMvpView>): AboutMvpPresenter<AboutMvpView> {
        return presenter
    }

//    @Provides
//    @PerActivity
//    fun provideLoginPresenter(presenter: LoginPresenter<LoginMvpView>): LoginMvpPresenter<LoginMvpView> {
//        return presenter
//    }

    @Provides
    @PerActivity
    fun provideMainPresenter(presenter: MainPresenter<MainMvpView>): MainMvpPresenter<MainMvpView> {
        return presenter
    }

    @Provides
    fun provideHomePresenter(presenter: MangasPresenter<MangasMvpView>): MangasMvpPresenter<MangasMvpView> {
        return presenter
    }

    @Provides
    fun provideChaptersPresenter(presenter: ChaptersPresenter<ChaptersMvpView>): ChaptersMvpPresenter<ChaptersMvpView> {
        return presenter
    }

    @Provides
    fun provideChapterImagesPresenter(presenter: ChapterImagesPresenter<ChapterImagesMvpView>): ChapterImagesMvpPresenter<ChapterImagesMvpView> {
        return presenter
    }

    @Provides
    fun provideCategoriesPresenter(presenter: CategoriesPresenter<CategoriesMvpView>): CategoriesMvpPresenter<CategoriesMvpView> {
        return presenter
    }

    @Provides
    fun provideFavoritesPresenter(presenter: FavoritesPresenter<FavoritesMvpView>): FavoritesMvpPresenter<FavoritesMvpView> {
        return presenter
    }

    @Provides
    fun provideHistoryPresenter(presenter: HistoryPresenter<HistoryMvpView>): HistoryMvpPresenter<HistoryMvpView> {
        return presenter
    }

    @Provides
    fun provideSearchHistoryPresenter(presenter: SearchHistoryPresenter<SearchHistoryMvpView>): SearchHistoryMvpPresenter<SearchHistoryMvpView> {
        return presenter
    }
    @Provides
    fun provideMangaInfoPresenter(presenter: MangaInfoPresenter<MangaInfoMvpView>): MangaInfoMvpPresenter<MangaInfoMvpView> {
        return presenter
    }

    @Provides
    fun provideSettingsPresenter(presenter: SettingsPresenter<SettingsMvpView>): SettingsMvpPresenter<SettingsMvpView> {
        return presenter
    }
}
