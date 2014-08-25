package cacheTest;

import java.util.HashMap;
import java.util.LinkedList;

public class Cache {
	private LinkedList<Record> list;
	private HashMap<Integer, Record> map;

	// maxSize가 0이면 캐시 개수 무제한
	private int maxSize = 0;
	
	Cache() {
		this.list = new LinkedList<>();
		this.map = new HashMap<>();
	}

	public Record lookUp(int id) {
		Record record = map.get(id);
		
		if(record == null) {
			return record;
		} else {
			list.remove(record);
			list.push(record);
			return record;
		}
	}

	public void store(Record record) {
		if (0 < maxSize && maxSize <= list.size()) {
			Record oldRecord = list.removeLast();
			map.remove(oldRecord.getId());
		}
		
		this.list.push(record);
		this.map.put(record.getId(), record);
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
}
