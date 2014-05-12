package com.aerospike.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AsSet implements IAsEntity{
	private AsNameSpace parent;
	private String name;
	protected Map<String, NameValuePair> values;

	public AsSet(AsNameSpace parent, String info){
		this.parent = parent;
		setInfo(info);
	}
	public Object getParent() {
		return this.parent;
	}
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof AsSet) &&
				(obj.toString().equals(toString())));
	}
	@Override
	public String getName() {
		return toString();
	}
	@Override
	public boolean hasChildren() {
		return (values != null && values.size() > 0);
	}
	@Override
	public Object[] getChildren() {
		return getValues().toArray();
	}

	public void setInfo(String info){
		//ns_name=test:set_name=demo:n_objects=1:set-stop-write-count=0:set-evict-hwm-count=0:set-enable-xdr=use-default:set-delete=false
		if (!info.isEmpty()){
			String[] parts = info.split(":");
			if (values == null){
				values = new HashMap<String, NameValuePair>();
			} 
			
			for (String part : parts){
				String[] kv = part.split("=");
				String key = kv[0];
				String value = kv[1];
				NameValuePair storedValue = values.get(key);
				if (storedValue == null){
					storedValue = new NameValuePair(this, key, value);
					values.put(key, storedValue);
				} else {
					storedValue.value = value;
				}
			}
			this.name = (String) values.get("set_name").value;
		}
	}

	public void mergeSetInfo(String info){
		//ns_name=test:set_name=demo:n_objects=1:set-stop-write-count=0:set-evict-hwm-count=0:set-enable-xdr=use-default:set-delete=false
		if (!info.isEmpty()){
			String[] parts = info.split(":");
			if (values == null){
				values = new HashMap<String, NameValuePair>();
			} 
			
			for (String part : parts){
				String[] kv = part.split("=");
				String key = kv[0];
				String value = kv[1];
				NameValuePair storedValue = values.get(key);
				if (storedValue == null){
					storedValue = new NameValuePair(this, key, value);
					values.put(key, storedValue);
				} else {
					try{
						Long newValue = Long.parseLong(value);
						Long oldValue = Long.parseLong(storedValue.value.toString());
						storedValue.value = (oldValue + newValue);
					} catch (NumberFormatException e){
						storedValue.value = value;
					}
				}
			}
			this.name = (String) values.get("set_name").value;
		}
	}
	public void setValues(Map<String, NameValuePair> newValues){
		this.values = newValues;
	}
	
	public List<NameValuePair> getValues(){
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		Set<String> keys = this.values.keySet();
		for (String key : keys){
			NameValuePair nvp = this.values.get(key);
			result.add(nvp);
		}
		return result;
	}
	
}
