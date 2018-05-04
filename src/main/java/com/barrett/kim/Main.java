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
		Map<String, Integer> tags = Maps.newLinkedHashMap();

		StringBuilder artistLines = new StringBuilder();
		StringBuilder tagLines = new StringBuilder();

		TrackAutoPager.forEachTrack(user, apiKey, new TrackConsumer() {
			@Override
			public boolean consume(Track track) {
				if (untilDate.isAfter(track.getPlayedWhen())) {
					artistLines.append(lineRenderer.render(artists));
					artistLines.append('\n');
					artists.clear();

					tagLines.append(lineRenderer.render(tags));
					tagLines.append('\n');
					tags.clear();

					untilDate.previousWeek();
				}
				String trackArtist = track.getArtist();
				artists.compute(trackArtist, (key, existing) -> existing == null ? 1 : existing + 1);
				for (String artistTag : artistsTags.getTags(trackArtist)) {
					tags.compute(artistTag, (key, existing) -> (existing == null ? 0 : existing) + artists.get(trackArtist));
				}
				return untilDate.getWeekCount() <= 10;
			}
		});

		System.out.println("Top Artists:");
		System.out.println(artistLines);

		System.out.println("Top Tagss:");
		System.out.println(tagLines);

	}

}