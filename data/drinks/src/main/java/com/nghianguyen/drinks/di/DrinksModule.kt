package com.nghianguyen.drinks.di

import com.nghianguyen.drinks.BeerRepositoryImpl
import com.nghianguyen.drinks.CocktailRepositoryImpl
import com.nghianguyen.drinks.ConsumedDrinkRepositoryImpl
import com.nghianguyen.drinks.LiquorRepositoryImpl
import com.nghianguyen.drinks.ShotRepositoryImpl
import com.nghianguyen.drinks.WineRepositoryImpl
import com.nghianguyen.drinks.repository.BeerRepository
import com.nghianguyen.drinks.repository.CocktailRepository
import com.nghianguyen.drinks.repository.ConsumedDrinkRepository
import com.nghianguyen.drinks.repository.LiquorRepository
import com.nghianguyen.drinks.repository.ShotRepository
import com.nghianguyen.drinks.repository.WineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DrinksModule {

    @Provides
    @Singleton
    fun providesConsumedDrinkRepository(
        impl: ConsumedDrinkRepositoryImpl
    ): ConsumedDrinkRepository = impl

    @Provides
    @Singleton
    fun provideBeerRepository(impl: BeerRepositoryImpl): BeerRepository = impl

    @Provides
    @Singleton
    fun provideWineRepository(impl: WineRepositoryImpl): WineRepository = impl


    @Provides
    @Singleton
    fun provideLiquorRepository(impl: LiquorRepositoryImpl): LiquorRepository = impl

    @Provides
    @Singleton
    fun provideShotRepository(impl: ShotRepositoryImpl): ShotRepository = impl

    @Provides
    @Singleton
    fun provideCocktailRepository(impl: CocktailRepositoryImpl): CocktailRepository = impl
}