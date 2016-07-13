package bunny.structure;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Multimap<K, V> {
	
	int size();
	
	boolean isEmpty();
	
	boolean containsKey(Object key);
	
	boolean containsValue(Object value);
	
	boolean containsEntry(Object key, Object value);
	
	boolean put(K key, V value);
	
	boolean remove(Object key, Object value);
	
	boolean putAll(K key, Iterable<? extends V> values);
	
	boolean putAll(Multimap<? extends K, ? extends V> multimap);
	
	Collection<V> replaceValues(K key, Iterable<? extends V> values);
	
	Collection<V> removeAll(Object key);
	
	void clear();
	
	Collection<V> get(K key);
	
	Set<K> keySet();
	
	Multiset<K> keys();
	
	Collection<V> values();
	
	Collection<Map.Entry<K, V>> entries();
	
	Map<K, Collection<V>> asMap();
	
}
