package com.barrett.kim;

import java.util.Optional;

import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

public class TrackAutoPager {

	public static void forEachTrack(String user, String apiKey, TrackConsumer trackConsumer) {
		int totalPages;
		PaginatedResult<Track> tracks = null;
		do {
			Integer nextPage = Optional.ofNullable(tracks).map(t -> t.getPage() + 1).orElse(1);
			tracks = User.getRecentTracks(user, nextPage, 100, apiKey);

			for (Track track : tracks.getPageResults()) {
				if (!trackConsumer.consume(track)) {
					return;
				}
			}

			totalPages = tracks.getTotalPages();
		}
		while (tracks.getPage() < totalPages);
	}

	public static interface TrackConsumer {

		/**
		 * @return true if track consuming should continue.
		 */
		boolean consume(Track track);

	}

}
