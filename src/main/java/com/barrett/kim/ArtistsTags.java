package com.barrett.kim;

import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.umass.lastfm.Artist;

public class ArtistsTags {

	private LoadingCache<String, Collection<String>> cache;

	public ArtistsTags(String apiKey) {
		cache = CacheBuilder.newBuilder().build(new CacheLoader<String, Collection<String>>(){
			@Override
			public Collection<String> load(String artist) throws Exception {
				return Artist.getTopTags(artist, apiKey).stream().map(t -> t.getName()).collect(Collectors.toList());
			}
		});
	}

	public Collection<String> getTags(String artist) {
		return cache.getUnchecked(artist);
	}

}
