package com.example.movieapps.domain.di

import android.content.Context
import androidx.room.Room
import com.example.movieapps.domain.MovieImdbRepository
import com.example.movieapps.domain.service.MovieImdbApi
import com.example.movieapps.domain.storage.MovieListDao
import com.example.movieapps.domain.storage.MovieListDatabase
import com.example.movieapps.domain.storage.UserDatabasePassphrase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MovieImdbApi {
        return retrofit.create(MovieImdbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserDatabasePassphrase(@ApplicationContext context: Context) =
        UserDatabasePassphrase(context)

    @Provides
    @Singleton
    fun provideSupportFactory(userDatabasePassphrase: UserDatabasePassphrase) =
        SupportFactory(userDatabasePassphrase.getPassphrase())

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        supportFactory: SupportFactory
    ): MovieListDatabase {

        return Room.databaseBuilder(
            context,
            MovieListDatabase::class.java,
            "movielist_database"
        )
//            .openHelperFactory(supportFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideHighScoreDao(database: MovieListDatabase): MovieListDao {
        return database.movieListDao()
    }

    @Provides
    @Singleton
    fun provideMovieImdbRepository(
        movieImdbApi: MovieImdbApi,
        movieListDao: MovieListDao
    ): MovieImdbRepository {
        return MovieImdbRepository(movieImdbApi, movieListDao)
    }
}
