package game;


import javax.swing.JApplet;

import srpg.SRPG;


public class RunnableApplet extends JApplet {
	
	RunnableCanvas rc;
	
	public void init() {
		rc = new SRPG(getSize().width, getSize().height, this, getCodeBase());
		getContentPane().add(rc);
	}
	
	public void start() {
		rc.start();
	}
	
	public void stop() {
		rc.stop();
	}
}
