package game;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import myutil.Queue;
import myutil.Stack;

public class GameObjectManager {

	private Sprite sp;
	private Map<Integer, GameObject> gos;
	private Stack<UserIF> uifs;
	private List<Motionable> mos;
	private Comparator<Motionable> mosComp;
	
	public final int height;
	public final int width;
	final int FPS = 30;
	final int KEY_REPEAT_PS = 10;
	
	public final String codeBase;

	protected GameObjectManager(Sprite sp, Queue<Key> keyQ, int width, int height, String codeBase) {
		this.sp = sp;
		this.width = width;
		this.height = height;
		this.codeBase = codeBase;
		
		init();

		UserIF.setKeyQ(keyQ);
		
		mosComp = new Comparator<Motionable>() {

			public int compare(Motionable arg0, Motionable arg1) {
				return arg1.processPriority() - arg0.processPriority();
			}
			
		};
	}
	
	void init() {
		sp.init();
		gos = new HashMap<Integer, GameObject>();
		uifs = new Stack<UserIF>();
		mos = new LinkedList<Motionable>();
	}
	
	// 内部クラス生成の場合、goArgの最後の要素に外部クラスの参照を指定
	/*
	public GameObject addGO(String name, int x, int y, GameObject... goArg) {
		return addGO(GameObjectManager.class, name, x, y, goArg);
	}
	
	public GameObject addGO(Class<? extends GameObjectManager> gom, String name, int x, int y, GameObject... goArg) {
		try {
			return addGO(gom, (Class<? extends GameObject>)Class.forName(name.replace('.', '$')), x, y, goArg);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	public <T extends GameObject> T addGO(Class<T> goClass, int x, int y, GameObject... goArg) {
		return addGO(GameObjectManager.class, goClass, x, y, goArg);
	}
	
	public <T extends GameObject> T addGO(Class<? extends GameObjectManager> gom, Class<T> goClass, int x, int y, GameObject... goArg) {
		int plnNo = sp.newPln();
		
		GameObject outerGO = goArg == null || goArg.length == 0 ? null : goArg[goArg.length - 1];
		LinkedList<Class<?>> argClass = new LinkedList<Class<?>>();
		LinkedList<Object> initArgs = new LinkedList<Object>();

		argClass.add(gom);						initArgs.add(this);
		argClass.add(Sprite.class);				initArgs.add(sp);
		argClass.add(int.class);				initArgs.add(plnNo);
		argClass.add(int.class);				initArgs.add(x);
		argClass.add(int.class);				initArgs.add(y);
		
		if(goArg != null)
			for(int i = 0; i < goArg.length - 1; i++) {
					argClass.add(goArg[i].getClass());
					initArgs.add(goArg[i]);
				}
		
		if(outerGO != null) {
			//name = name.replace('.', '$');
			argClass.addFirst(outerGO.getClass());
			initArgs.addFirst(outerGO);
		}

		T go = null;
		try {
			//go = (GameObject) Class.forName(name)
			go = goClass.getConstructor(argClass.toArray(new Class[0]))
			.newInstance(initArgs.toArray(new Object[0]));
		} catch (InvocationTargetException e){ 
			e.getCause().printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gos.put(plnNo, go);
		
		return go;
	}
	
	public void delGO(int plnNo) {
		gos.remove(plnNo);
		sp.delPln(plnNo);
	}
	
	public boolean isPlayable() {
		return !uifs.isEmpty();
	}

	public void play() {
		//System.out.println("go : " + gos.size() + ", mo : " + mos.size() + ", uif : " + uifs.size());
		
		for(Motionable mo : mos)
			mo.motion();
		
		uifs.peek().action();
	}

	public void setUIF(UserIF uif) {
		uifs.push(uif);
	}

	public void removeUIF() {
		uifs.pop();
	}
	
	public void removeUIF(UserIF uif) {
		uifs.remove(uif);
	}
	
	public void addMotionable(Motionable m) {
		mos.add(m);
		Collections.sort(mos, mosComp);
		
//		System.out.println("add : " + m);
//		System.out.println("go : " + gos.size() + ", mo : " + mos.size() + ", uif : " + uifs.size());
	}
	
	public void removeMotionable(Motionable m) {
		mos.remove(m);
		
//		System.out.println("   remove : " + m);
//		System.out.println("go : " + gos.size() + ", mo : " + mos.size() + ", uif : " + uifs.size());
	}
}

