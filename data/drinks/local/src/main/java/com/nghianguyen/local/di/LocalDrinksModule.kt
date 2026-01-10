package com.nghianguyen.local.di

import android.content.Context
import com.nghianguyen.drinks.BeerLocalDataSource
import com.nghianguyen.drinks.CocktailLocalDataSource
import com.nghianguyen.drinks.ConsumedDrinksLocalDataSource
import com.nghianguyen.drinks.LiquorLocalDataSource
import com.nghianguyen.drinks.WineLocalDataSource
import com.nghianguyen.local.BeerLocalDataSourceImpl
import com.nghianguyen.local.ConsumedDrinksLocalDataSourceImpl
import com.nghianguyen.local.LiquorLocalDataSourceImpl
import com.nghianguyen.drinks.ShotLocalDataSource
import com.nghianguyen.local.CocktailLocalDataSourceImpl
import com.nghianguyen.local.ShotLocalDataSourceImpl
import com.nghianguyen.local.WineLocalDataSourceImpl
import com.nghianguyen.local.db.DrankDatabase
import com.nghianguyen.local.db.daos.BeerDao
import com.nghianguyen.local.db.daos.CocktailDao
import com.nghianguyen.local.db.daos.ConsumedDrinkDao
import com.nghianguyen.local.db.daos.LiquorDao
import com.nghianguyen.local.db.daos.ShotDao
import com.nghianguyen.local.db.daos.WineDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDrinksModule {

    @Provides
    @Singleton
    fun providesLocalConsumedDrinkDataSource(
        impl: ConsumedDrinksLocalDataSourceImpl
    ): ConsumedDrinksLocalDataSource = impl

    @Provides
    @Singleton
    fun providesLocalBeerDataSource(
        impl: BeerLocalDataSourceImpl
    ): BeerLocalDataSource = impl

    @Provides
    @Singleton
    fun providesLocalWineDataSource(
        impl: WineLocalDataSourceImpl
    ): WineLocalDataSource = impl

    @Provides
    @Singleton
    fun providesLocalLiquorDataSource(
        impl: LiquorLocalDataSourceImpl
    ): LiquorLocalDataSource = impl

    @Provides
    @Singleton
    fun providesLocalShotDataSource(
        impl: ShotLocalDataSourceImpl
    ): ShotLocalDataSource = impl

    @Provides
    @Singleton
    fun providesLocalCocktailDataSource(
        impl: CocktailLocalDataSourceImpl
    ): CocktailLocalDataSource = impl

    @Provides
    @Singleton
    fun providesConsumedDrinkDao(
        drankDatabase: DrankDatabase
    ): ConsumedDrinkDao = drankDatabase.consumedDrinkDao()

    @Provides
    @Singleton
    fun providesBeerDao(drankDatabase: DrankDatabase): BeerDao = drankDatabase.beerDao()


    @Provides
    @Singleton
    fun providesWineDao(drankDatabase: DrankDatabase): WineDao = drankDatabase.wineDao()


    @Provides
    @Singleton
    fun providesLiquorDao(drankDatabase: DrankDatabase): LiquorDao = drankDatabase.liquorDao()


    @Provides
    @Singleton
    fun providesShotDao(drankDatabase: DrankDatabase): ShotDao = drankDatabase.shotDao()

    @Provides
    @Singleton
    fun providesCocktailDao(drankDatabase: DrankDatabase): CocktailDao = drankDatabase.cocktailDao()

    @Provides
    @Singleton
    fun providesDrankDatabase(
        @ApplicationContext context: Context
    ): DrankDatabase = DrankDatabase.getInstance(context)
}