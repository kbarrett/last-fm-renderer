package com.barrett.kim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import com.barrett.kim.TrackAutoPager.TrackConsumer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import de.umass.lastfm.Track;

public class Main {

	public static void main(String[] args) throws IOException {
		Map<String, String> properties = Files.lines(Paths.get("api-key.txt"))
				.map(l -> l.split("=", 2))
				.collect(ImmutableMap.toImmutableMap(l -> l[0], l -> l[1]));

		String apiKey = properties.get("API_KEY");
		String user = properties.get("USER_NAME");

		ArtistsTags artistsTags = new ArtistsTags(apiKey);

		LineRenderer lineRenderer = new LineRenderer(100);

		UntilDate untilDate = new UntilDate(LocalDateTime.now());
		untilDate.previousWeek();
		Map<String, Integer> artists = Maps.newLinkedHashMap();

		StringBuilder lines = new StringBuilder();

		TrackAutoPager.forEachTrack(user, apiKey, new TrackConsumer() {
			@Override
			public boolean consume(Track track) {
				if (untilDate.isAfter(track.getPlayedWhen())) {
					lines.append(lineRenderer.render(artists));
					lines.append('\n');
					artists.clear();
					untilDate.previousWeek();
				}
				artists.compute(track.getArtist(), (key, existing) -> existing == null ? 1 : existing + 1);
				return untilDate.getWeekCount() <= 10;
			}
		});


		System.out.println(lines);
	}

}