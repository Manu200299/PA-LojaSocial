//package com.example.lojasocial.di
//
//
//import androidx.test.espresso.core.internal.deps.dagger.Module
//import androidx.test.espresso.core.internal.deps.dagger.Provides
//import com.example.lojasocial.data.remote.api.FirebaseApi
//import com.example.lojasocial.data.repository.VolunteerRepositoryImpl
//import com.example.lojasocial.domain.repository.VolunteerRepository
//import com.example.lojasocial.domain.use_case.LoginVolunteerUseCase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//import javax.inject.Singleton
//
//@Module
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
//
//    @Provides
//    @Singleton
//    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()
//
//    @Provides
//    @Singleton
//    fun provideFirebaseApi(auth: FirebaseAuth, database: FirebaseDatabase): FirebaseApi {
//        return FirebaseApi(auth, database)
//    }
//
//    @Provides
//    @Singleton
//    fun provideVolunteerRepository(firebaseApi: FirebaseApi): VolunteerRepository {
//        return VolunteerRepositoryImpl(firebaseApi)
//    }
//
//    @Provides
//    @Singleton
//    fun provideRegisterVolunteerUseCase(repository: VolunteerRepository): RegisterVolunteerUseCase {
//        return RegisterVolunteerUseCase(repository)
//    }
//
//    @Provides
//    @Singleton
//    fun provideLoginVolunteerUseCase(repository: VolunteerRepository): LoginVolunteerUseCase {
//        return LoginVolunteerUseCase(repository)
//    }
//}
