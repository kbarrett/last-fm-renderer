package com.barrett.kim;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LineRenderer {

	private int lineLength;

	public LineRenderer(int lineLength) {
		this.lineLength = lineLength;
	}

	public String render(Map<String, Integer> valuesToCounts) {
		int totalCount = valuesToCounts.values().stream().mapToInt(i -> i).sum();
		List<String> sortedValues = valuesToCounts.keySet().stream()
				.sorted((a, b) -> Integer.compare(valuesToCounts.get(b), valuesToCounts.get(a)))
				.collect(Collectors.toList());

		StringBuilder line = new StringBuilder();
		for (String value : sortedValues) {
			int count = valuesToCounts.get(value);
			double decimal = count / (double) totalCount;

			int segmentSize = (int) (lineLength * decimal);
			if (segmentSize == 0) {
				break;
			}

			for (int i = 0; i < (int) (segmentSize / (double) value.length()); ++i) {
				line.append(value);
			}
			line.append(value.substring(0, (int) (segmentSize % (double) value.length())));
		}

		while (line.length() < lineLength) {
			line.append("-");
		}

		return line.toString();
	}
}
