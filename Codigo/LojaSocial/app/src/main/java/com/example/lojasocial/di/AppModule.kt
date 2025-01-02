package com.example.lojasocial.di

import androidx.test.espresso.core.internal.deps.dagger.Module
import androidx.test.espresso.core.internal.deps.dagger.Provides
import com.example.lojasocial.data.remote.FirebaseApi
import com.example.lojasocial.data.repository.BeneficiaryRepositoryImpl
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Singleton

@Module
//@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase(database: FirebaseDatabase): FirebaseApi = FirebaseApi(database)

    @Provides
    @Singleton
    fun provideBeneficiaryRepository(dataSource: FirebaseApi): BeneficiaryRepository = BeneficiaryRepositoryImpl(dataSource)


}