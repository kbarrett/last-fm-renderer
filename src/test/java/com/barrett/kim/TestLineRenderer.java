package com.barrett.kim;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class TestLineRenderer {

	private final LineRenderer lineRenderer = new LineRenderer(10);

	@Test
	public void testMultipleCopies() throws Exception {
		assertEquals("aaaaabbbbb", lineRenderer.render(ImmutableMap.of("a", 1, "b", 1)));
		assertEquals("aaaaabbbbb", lineRenderer.render(ImmutableMap.of("a", 1, "b", 1)));
		assertEquals("aaaabbccdd", lineRenderer.render(ImmutableMap.of("a", 2, "b", 1, "c", 1, "d", 1)));
	}

	@Test
	public void testPadding() throws Exception {
		assertEquals("aaaaabbbc-", lineRenderer.render(ImmutableMap.of("a", 3, "b", 2, "c", 1)));
		assertEquals("aaaaaaaaa-", lineRenderer.render(ImmutableMap.of("a", 20, "b", 1)));
	}

	@Test
	public void testPartialCopies() throws Exception {
		assertEquals("abcdelmwx-", lineRenderer.render(ImmutableMap.of("abcdef", 2, "lmnop", 1, "wxyz", 1)));
		assertEquals("This short", lineRenderer.render(ImmutableMap.of("This is quite long", 1, "short", 1)));
	}

	@Test
	public void testLength() throws Exception {
		Map<String, Integer> input = ImmutableMap.of("a", 1, "b", 1);
		assertEquals("ab", new LineRenderer(2).render(input));
		assertEquals("ab-", new LineRenderer(3).render(input));
		assertEquals("aabb", new LineRenderer(4).render(input));
		assertEquals("aabb-", new LineRenderer(5).render(input));
	}

}
