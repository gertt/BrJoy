package com.browsejoy.games.app.view.activities.tv

// Step 1 - This interface defines the type of messages I want to communicate to my owner
interface ExoPlayerEventListener {
    // These methods are the different events and
    // need to pass relevant arguments related to the event triggered
    fun onPlaylistFinished(data: String)
}