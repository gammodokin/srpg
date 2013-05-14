package srpg;

import game.GameObject;
import game.GameObjectManager;
import game.Key;
import game.Sprite;

import java.util.HashMap;

import srpg.map.StageMap;

import myutil.Queue;

public class SRPGGameObjectManager extends GameObjectManager {
	
	private HashMap<Integer, StageMap> maps;

	public SRPGGameObjectManager(Sprite sp, Queue<Key> keyQ, int width, int height, String codeBase) {
		super(sp, keyQ, width, height, codeBase);
		
		maps = new HashMap<Integer, StageMap>();
	}

	public void createMap(int id) {
		maps.put(id, addGO(StageMap.class, 0, 0));
	}
	/*
	public void createMapObject(int id, String name, int x, int y) {
		createMapObject(maps.get(id), name, x, y);
	}
	public GameObject createMapObject(StageMap map, String name, int x, int y) {
		return createMapObject(map, name, x, y, null);
	}
	public GameObject createMapObject(StageMap map, String name, int x, int y, GameObject outerGO) {
		return addGO(SRPGGameObjectManager.class, name, x, y, map, outerGO);
	}
	*/
	public <E extends GameObject> E createMapObject(int id, Class<E> go, int x, int y) {
		return createMapObject(maps.get(id), go, x, y);
	}
	public <E extends GameObject> E createMapObject(StageMap map, Class<E> go, int x, int y) {
		return createMapObject(map, go, x, y, null);
	}
	public <E extends GameObject> E createMapObject(StageMap map, Class<E> go, int x, int y, GameObject outerGO) {
		return addGO(SRPGGameObjectManager.class, go, x, y, map, outerGO);
	}
	
}