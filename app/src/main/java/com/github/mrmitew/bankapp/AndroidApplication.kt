package com.github.mrmitew.bankapp

import android.app.Application

/**
 * Created by Stefan Mitev on 4-5-19.
 */


/**
 * A dummy "banking" app that aims to demonstrate a clean software architecture,
 * utilizing the latest of what the Android Architecture components has to offer,
 * Kotlin Coroutines and other modern libraries.
 *
 * User can login (then app will create and encrypt a database); get a list with
 * his/her bank accounts; navigate into history of account transactions; deposit/withdraw to/from
 * savings account;
 *
 * Architecture Components, used by the app:
 * - Navigation Component
 * - ViewModel
 * - LiveData
 * - Room (+ SQLCipher)
 * - Android KTX
 * - LifeCycles
 *
 * The application follows the "clean" architecture and it has been layered by feature.
 *
 * Features:
 * - main
 * - login
 * - users
 * - transactions
 * - storage
 * -- cache
 * -- database
 * - backend
 * - auth
 * - accounts
 * - common
 *
 * It is really easy now to extract every feature into a separate module.
 * We can extract common VOs, DTOs, Entities, Repository interfaces into a common module.
 * Then when we test, we could even provide a special module that contains a fake implementation of the given module.
 * Say we want to mock "Accounts" feature. We can make gradle depend on "fake-accounts"
 * module and not "accounts" module. "fake-accounts" module can implement the same interfaces
 * and expose the public APIs under the same package structure as the actual "accounts" module.
 *
 * In order to navigate between screens when all features are separeted into different modules,
 * we can (as of Google I/O 2019) navigate using URIs, instead of IDs. See: https://youtu.be/JFGq0asqSuA?t=1421
 * Every module will have its own graph, which will be included into the main one in the app module.
 * I just didn't have enough time to do it. However, you can take a look at the `feature/modularization` branch
 * to get an idea.
 *
 * The general flow of the app is:
 * View talks to a ViewModel, then ViewModel executes a [UseCase].
 * The result is brought back to the view either via stream of [LiveData] or
 * by resuming a coroutine that was started by the View within a lifecycle scope.
 *
 * Use cases talk to gateways via interfaces. They can be completely unit tested since they don't
 * have any android related dependencies. Those that depend on LiveData, they can still be unit tested.
 * Gateways for the most of the cases are repositories that represent wrappers around local and remote data sources.
 * Use cases contain the entire business logic for the given scenario (use case). All gateways are "dumb" and do not do any side effects.
 * Glancing through the use cases will give every developer a good understanding of what a particular feature (or
 * the entire app), does.
 *
 * The app is using Koin as a dependency injection framework. Every feature declares its own module
 * that is used for the construction of the entire dependency graph.
 *
 * NOTE:
 * - When user logs in, please use the hard-coded pin (0000). The user pin isn't propagated to the
 * sql open helper factory, just because it was easier to do. However, the database will indeed be created
 * and encrypted when you login the first time. To make it so the app respects user's input, I'll have to
 * change the way the dependency graph is created in regards to the database. I can use assisted injection.
 * We should never ever put secrets in the app, especially hardcode in plain text. I know! :)
 * - Navigation drawer items do not do anything useful..besides the "Log out" which makes the user back
 * out of the app.
 */
class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyInjection.with(this)
    }
}

