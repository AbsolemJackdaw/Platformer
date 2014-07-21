package game.content.save;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataList {

	private final JSONArray array;

	public DataList(){
		array = new JSONArray();
	}
	
	public DataList(JSONArray array){
		this.array = array;
	}


	@SuppressWarnings("unchecked")
	public void write(DataTag tag){

		array.add(tag.getData());

	}
	
	public JSONArray data(){
		return array;
	}
	
	
	
	public DataTag readArray(int i){
		if(i > array.size()){
			System.out.println("Index " + i + " is out of bounds ! No DataTag found in the DataList");
			return null;
		}
		
		JSONObject obj = (JSONObject)array.get(i);
		DataTag tag = new DataTag(obj);
		
		return tag;
	}

}
