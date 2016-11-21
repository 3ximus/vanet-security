package gui.html;

import gui.core.VanetGUI;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class VanetGUIHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new VanetGUI();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
