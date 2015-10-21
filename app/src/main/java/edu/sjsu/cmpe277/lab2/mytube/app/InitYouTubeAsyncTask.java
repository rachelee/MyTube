///*
// * Copyright (c) 2012 Google Inc.
// *
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
// * in compliance with the License. You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software distributed under the License
// * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// * or implied. See the License for the specific language governing permissions and limitations under
// * the License.
// */
//
//package edu.sjsu.cmpe277.lab2.mytube.app;
//
//import android.util.Log;
//import com.google.api.services.youtube.YouTube;
//import com.google.api.services.youtube.model.Playlist;
//import com.google.api.services.youtube.model.PlaylistSnippet;
//import com.google.api.services.youtube.model.PlaylistStatus;
//
//import java.io.IOException;
//import java.util.Calendar;
//
///**
// * Asynchronously load the tasks.
// *
// * @author Yaniv Inbar
// */
//class InitYouTubeAsyncTask extends CommonAsyncTask {
//
//
//    InitYouTubeAsyncTask(LoginActivity loginActivity) {
//        super(loginActivity);
//    }
//
//    Playlist playlistInserted;
//
//    @Override
//    protected void doInBackground() throws IOException {
//        // This code constructs the playlist resource that is being inserted.
//        // It defines the playlist's title, description, and privacy status.
//        PlaylistSnippet playlistSnippet = new PlaylistSnippet();
//        playlistSnippet.setTitle("Test Playlist " + Calendar.getInstance().getTime());
//        playlistSnippet.setDescription("A private playlist created with the YouTube API v3");
//        PlaylistStatus playlistStatus = new PlaylistStatus();
//        playlistStatus.setPrivacyStatus("private");
//
//        Playlist youTubePlaylist = new Playlist();
//        youTubePlaylist.setSnippet(playlistSnippet);
//        youTubePlaylist.setStatus(playlistStatus);
//
//        // Call the API to insert the new playlist. In the API call, the first
//        // argument identifies the resource parts that the API response should
//        // contain, and the second argument is the playlist being inserted.
//        YouTube.Playlists.Insert playlistInsertCommand =
//                client.playlists().insert("snippet,status", youTubePlaylist);
//
//        playlistInserted = playlistInsertCommand.execute();
//    }
//
//    @Override
//    protected final void onPostExecute(Boolean success) {
//        activity.newPlaylistId = playlistInserted.getSnippet().getChannelId();
//        Log.d("new list id:", activity.newPlaylistId);
//    }
//
//
//    static void run(LoginActivity loginActivity) {
//        new InitYouTubeAsyncTask(loginActivity).execute();
//    }
//}
