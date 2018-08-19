/*
 * Copyright (C) 2018 Codepunk, LLC
 * Author(s): Scott Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codepunk.doofenschmirtz.util.pluginator

/**
 * Manages and supplies plugins based on supplied criteria. This criteria may or may not change
 * over the lifecycle of the app; therefore it is good practice to always using the
 * [Pluginator.get] method each time you need to reference a plugin rather than saving the
 * returned value. Upon calling [Pluginator.get], Pluginator will automatically determine
 * determine if a new plugin needs to be created based on the given criteria.
 *
 * Plugins allow you to avoid multiple `if`/`else` or `switch` (or `when` in Kotlin) statements
 * to perform certain behaviors based on a set of criteria. This can range from logging information
 * only if running in a development environment, or performing a certain action only if a user is
 * logged in, showing extra controls if the user has admin rights, showing a "what's new" dialog
 * the first time the app is run after an upgrade, or showing a special screen if it is a user's
 * birthday.
 *
 * As an example, suppose you wanted to show an encouraging message when someone used the app on a
 * Monday. You could easily include an `if` statement to accomplish this. But imagine that you also
 * wanted to change the background color on Mondays, and add an extra button that was only visible
 * on Mondays, or show a different message on Friday, and so on. Soon your code would be littered
 * with `if` statements testing the day of the week.
 *
 * You could instead set up a Day-of-the-week Pluginator:
 *
 *     interface DayPlugin {
 *         fun showGreeting(context: Context)
 *     }
 *
 *     class BaseDayPlugin: DayPlugin {
 *         override fun showGreeting(context: Context) {
 *             // No action
 *         }
 *     }
 *
 *     class MondayPlugin: DayPlugin {
 *         override fun showGreeting(context: Context) {
 *             Toast.makeText(
 *                     context,
 *                     "Looks like someone has a case of the Mondays!",
 *                     Toast.LENGTH_LONG)
 *                     .show()
 *         }
 *     }
 *
 *     class DayPluginator: Pluginator<DayPlugin, Calendar>() {
 *         override fun isPluginStale(state: Calendar): Boolean {
 *             return activeState == null || getValue(activeState!!) != getValue(state)
 *         }
 *
 *         override fun newPlugin(state: Calendar): DayPlugin {
 *             return when (calendar.get(Calendar.DAY_OF_WEEK)) {
 *                 Calendar.MONDAY -> MondayPlugin()
 *                 else -> BaseDayPlugin()
 *             }
 *         }
 *
 *         private fun getValue(calendar: Calendar?): Int {
 *             return when (calendar?.get(Calendar.DAY_OF_WEEK)) {
 *                 Calendar.MONDAY -> Calendar.MONDAY
 *                 else -> -1
 *             }
 *         }
 *     }
 *
 * In the above example, we created a `DayPlugin` interface with a single method, `showGreeting`.
 * In the example mentioned previously, we could have also included a "`setBackgroundColor`" method,
 * and so on. Since the current date/time is constantly changing, we pass that along with each call
 * to [get]. Pluginator in turn calls [isPluginStale] and [newPlugin] as appropriate to
 * determine whether the existing plugin (if any) is "stale" and a new one needs to be created.
 *
 * To make use of this new plugin, we can code something like the following in an Activity:
 *
 *     override fun onStart() {
 *         super.onStart()
 *         DayPluginator().get(Calendar.getInstance()).showGreeting(this)
 *     }
 *
 * This is, of course, a rough example but it shows the power of using PluginManagers. Note that we
 * have avoided `if`/`else`/`switch`/`when` statements in our `MainActivity` class, which makes for
 * cleaner and more modular code. Another benefit is that if you are testing a new feature, that
 * feature can be implemented as a plugin. For example, if you wanted to show a "Hooray, it's
 * Friday!" message on Fridays, you could simply create another plugin and the code in onStart in
 * your activity wouldn't have to change at all. If at any time you want to remove that feature
 * entirely, it becomes much easier because it is completely contained in its own plugin class and
 * the calling class doesn't need to be modified, or even made aware that the plugin was removed.
 *
 * Any (non-null) class can be specified as a [Plugin] to be managed, therefore it can contain any
 * required functionality. The [get] method will return an appropriate instance of [Plugin] based
 * on the supplied [State]. Likewise, [State] can be any class, and will be passed to the
 * [isPluginStale] method to determine if a new instance of [Plugin] needs to be created and
 * stored as the [activePlugin].
 */
abstract class Pluginator<Plugin : Any, State>(
    private var pluginListener: PluginListener<Plugin, State>? = null
) {

    // region Properties

    /**
     * The currently-active plugin.
     */
    @SuppressWarnings("weakerAccess")
    protected lateinit var activePlugin: Plugin
        private set

    /**
     * The state used to create the currently-active plugin.
     */
    protected var activeState: State? = null
        private set

    // endregion Properties

    // region Methods

    /**
     * Returns a [Plugin] instance appropriate to the supplied [state].
     */
    fun get(state: State): Plugin {
        if (this::activePlugin.isInitialized) {
            if (isPluginStale(state)) {
                onDeactivatePlugin(activePlugin)
                pluginListener?.onDeactivatePlugin(activePlugin)
            } else {
                return activePlugin
            }
        }
        activePlugin = newPlugin(state)
        activeState = state
        onActivatePlugin(activePlugin, state)
        pluginListener?.onActivatePlugin(activePlugin, state)
        return activePlugin
    }

    /**
     * Determines whether the active plugin is "stale". That is, based on the supplied
     * [state], whether a new instance of [Plugin] needs to be created or not.
     */
    protected abstract fun isPluginStale(state: State): Boolean

    /**
     * Creates a new instance of [Plugin] based on the supplied [state].
     */
    protected abstract fun newPlugin(state: State): Plugin

    /**
     * Called when a [plugin] is activated (i.e. created), and the [state] that was used to
     * determine that a new instance of [Plugin] was necessary.
     */
    protected open fun onActivatePlugin(plugin: Plugin, state: State) {
    }

    /**
     * Called when a [plugin] is about to be deactivated (i.e. destroyed).
     */
    protected open fun onDeactivatePlugin(plugin: Plugin) {
    }

    // endregion Methods

    // region Nested classes

    /**
     * Interface that serves as a listener for when [Plugin]s are activated (i.e. created) and
     * deactivated (destroyed) based on a given [State].
     */
    interface PluginListener<Plugin, State> {

        // region Methods

        /**
         * Called when a [plugin] is activated (i.e. created), and the [state] that was used to
         * determine that a new instance of [Plugin] was necessary.
         */
        fun onActivatePlugin(plugin: Plugin, state: State?)

        /**
         * Called when a [plugin] is about to be deactivated (i.e. destroyed).
         */
        fun onDeactivatePlugin(plugin: Plugin)

        // endregion Methods
    }

    // endregion Nested classes
}
