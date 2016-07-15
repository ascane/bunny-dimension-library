package bunny.structure;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Multimap<K, V> {
	
	public int size();
	
	public boolean isEmpty();
	
	public boolean containsKey(Object key);
	
	public boolean containsValue(Object value);
	
	public boolean containsEntry(Object key, Object value);
	
	public List<V> get(K key);
	
	public boolean put(K key, V value);
	
	public boolean putAll(K key, Iterable<? extends V> values);
	
	public boolean putAll(Multimap<? extends K, ? extends V> multimap);
	
	public boolean remove(Object key, Object value);
	
	public List<V> removeAll(Object key);
	
	public List<V> replaceValues(K key, Iterable<? extends V> values);
	
	public void clear();
	
	public Set<K> keySet();
	
	public Multiset<K> keys();
	
	public Collection<V> values();
	
	public Collection<Map.Entry<K, V>> entries();
	
	public Map<K, List<V>> asMap();
	
}
