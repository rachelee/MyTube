package edu.sjsu.cmpe277.lab2.mytube.app;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class YouTubeService {

    public static String token;

    public static String favorPlaylistId;

    private YouTube youtube;

    private GoogleCredential credential;

    public YouTubeService() {
        if (credential == null) {
            credential = new GoogleCredential().setAccessToken(token);
        }
        if (youtube == null) {
            youtube = new YouTube.Builder(AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName("MyTube").build();
        }
    }

    public List<VideoItem> search(String keyword) {
        try {
            YouTube.Search.List searchCommand = youtube.search().list("id,snippet");
            searchCommand.setType("video");
            searchCommand.setFields("items(id/videoId,snippet/title,snippet/publishedAt,snippet/thumbnails/default/url)");
            searchCommand.setMaxResults((long) 15);
            searchCommand.setRegionCode("US");
            searchCommand.setQ(keyword);

            SearchListResponse response = searchCommand.execute();
            List<SearchResult> results = response.getItems();
            List<VideoItem> items = new ArrayList<VideoItem>();

            for (SearchResult result : results) {
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                //TODO: Replace mock number of views
                item.setNumOfViews(new Random().nextInt(10000000));
                item.setPublishDate(result.getSnippet().getPublishedAt().toString());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setVideoIdId(result.getId().getVideoId());
                items.add(item);
            }

            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String favorListInsert(String title) {
        PlaylistSnippet playlistSnippet = new PlaylistSnippet();
        playlistSnippet.setTitle(title);
        playlistSnippet.setDescription("A private playlist created with the YouTube API v3");
        PlaylistStatus playlistStatus = new PlaylistStatus();
        playlistStatus.setPrivacyStatus("private");
        Playlist youTubePlaylist = new Playlist();
        youTubePlaylist.setSnippet(playlistSnippet);
        youTubePlaylist.setStatus(playlistStatus);

        try {
            YouTube.Playlists.Insert playlistInsertCommand =
                    youtube.playlists().insert("snippet,status", youTubePlaylist);

            Playlist playlistInserted = playlistInsertCommand.execute();
            return playlistInserted.getId();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOrCreateFavorList() {
        try {
            YouTube.Playlists.List playlistListCommand;
            playlistListCommand = youtube.playlists().list("snippet,contentDetails");
            playlistListCommand.setMine(true);
            PlaylistListResponse playlistListResponse;
            playlistListResponse = playlistListCommand.execute();
            List<Playlist> playlists = playlistListResponse.getItems();
            Playlist favorPlaylist = null;
            for (Playlist playlist : playlists) {
                if (playlist.getSnippet().getTitle().equals("SJSU-CMPE-277")) {
                    favorPlaylist = playlist;
                    favorPlaylistId = favorPlaylist.getId();
                    return favorPlaylistId;
                }
            }
            if (favorPlaylist == null) {
                favorPlaylistId = favorListInsert("SJSU-CMPE-277");
                return favorPlaylistId;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean favorListItemInsert(String videoId) {
        ResourceId resourceId = new ResourceId();
        resourceId.setKind("youtube#video");
        resourceId.setVideoId(videoId);

        PlaylistItemSnippet playlistItemSnippet = new PlaylistItemSnippet();
        playlistItemSnippet.setPlaylistId(favorPlaylistId);
        playlistItemSnippet.setResourceId(resourceId);

        PlaylistItem playlistItem = new PlaylistItem();
        playlistItem.setSnippet(playlistItemSnippet);

        PlaylistItem returnedPlaylistItem = null;
        try {
            YouTube.PlaylistItems.Insert playlistItemsInsertCommand =
                    youtube.playlistItems().insert("snippet", playlistItem);
            returnedPlaylistItem = playlistItemsInsertCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnedPlaylistItem != null;
    }

    public boolean favorListItemDelete(String favorPlaylistItemId) {
        try {
            YouTube.PlaylistItems.Delete playlistItemsDeleteCommand = youtube.playlistItems().delete(favorPlaylistItemId);
            playlistItemsDeleteCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<VideoItem> getFavorListVideos() {
        try {
            YouTube.PlaylistItems.List playlistItemCommand =
                    youtube.playlistItems().list("snippet");
            playlistItemCommand.setPlaylistId(favorPlaylistId);
            playlistItemCommand.setMaxResults((long) 20);
            PlaylistItemListResponse playlistItemListResponse = playlistItemCommand.execute();

            List<PlaylistItem> results = playlistItemListResponse.getItems();
            List<VideoItem> items = new ArrayList<VideoItem>();

            for (PlaylistItem result : results) {
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setPublishDate(result.getSnippet().getPublishedAt().toString());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setVideoIdId(result.getSnippet().getResourceId().getVideoId());
                item.setFavorPlaylistItemId(result.getId());
                items.add(item);
            }

            return items;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}