package com.ctrip.search.mystest.sort;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;
/**
 * 
 * @author chenjk
 * @since 2014年7月22日
 *
 */
public class DistanceComparatorSource extends FieldComparatorSource {
	
	private int x;
	
	private int y;
	
	public DistanceComparatorSource(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public FieldComparator<?> newComparator(String fieldname, int numHits, int sortPos,
			boolean reversed) throws IOException {
		
		return new DistanceScoreDocLookupComparator(fieldname, numHits);
	}
	
	private class DistanceScoreDocLookupComparator extends FieldComparator{

		private int[] xDoc, yDoc;
		private float[] values;
		private float bottom;
		String fieldName;
		
		public DistanceScoreDocLookupComparator(String fieldname, int numHits) throws IOException{
			super();
			this.values = new float[numHits];
			this.fieldName = fieldname;
		}

		@Override
		public int compare(int arg0, int arg1) {
			if(values[arg0] < values[arg1]) return -1;
			if(values[arg0] > values[arg1]) return 1;
			return 0;
		}

		@Override
		public int compareBottom(int arg0) throws IOException {
			float docDistance = getDistance(arg0);
			if(bottom < docDistance) return -1;
			if(bottom > docDistance) return 1;
			return 0;
		}

		@Override
		public int compareDocToValue(int arg0, Object arg1) throws IOException {
			return 0;
		}

		@Override
		public void copy(int arg0, int arg1) throws IOException {
			values[arg0] = getDistance(arg1);
		}

		@Override
		public void setBottom(int arg0) {
			bottom = values[arg0];
		}

		@Override
		public FieldComparator setNextReader(AtomicReaderContext arg0)
				throws IOException {
			return this;
		}

		@Override
		public Object value(int arg0) {
			return new Float(values[arg0]);
		}
		
		private float getDistance(int doc){
			int deltax = xDoc[doc] - x;
			int deltay = yDoc[doc] - y;
			return (float)Math.sqrt(deltax * deltax + deltay * deltay);
		}
	}
}
