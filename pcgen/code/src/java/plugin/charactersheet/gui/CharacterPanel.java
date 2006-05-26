/*
 * CharacterPanel.java
 *
 * Created on February 27, 2004, 1:03 PM
 */

package plugin.charactersheet.gui;

import gmgen.gui.ScrollablePanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;

import pcgen.core.Globals;
import pcgen.core.NoteItem;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.gui.panes.FlippingSplitPane;
import plugin.charactersheet.CharacterSheetPlugin;

/**
 *
 * @author  ddjone3
 */
public class CharacterPanel extends FlippingSplitPane {
	private PlayerCharacter pc;
	private Page1Panel page1;
	private Page2Panel page2;
	private ScrollablePanel page1layout;
	private ScrollablePanel page2layout;
	private List spellPaneList;
	private NotesPanel notesPanel;
	private SelectPanel selectPanel;
	private FlippingSplitPane rightSidePane;
	private Properties pcProperties;
	private int serial = 0;
	private boolean readyForRefresh = false;
	private boolean systemRefresh = true;

	/** BLUE = 0 */
	public static final int BLUE = 0;
	/** LIGHTBLUE = 1 */
	public static final int LIGHTBLUE = 1;
	/** GREEN = 2 */
	public static final int GREEN = 2;
	/** LIGHTGREEN = 3 */
	public static final int LIGHTGREEN = 3;
	/** RED = 4 */
	public static final int RED = 4;
	/** LIGHTRED = 5 */
	public static final int LIGHTRED = 5;
	/** YELLOW = 6 */
	public static final int YELLOW = 6;
	/** LIGHTYELLOW = 7 */
	public static final int LIGHTYELLOW = 7;
	/** GREY = 8 */
	public static final int GREY = 8;
	/** LIGHTGREY = 9 */
	public static final int LIGHTGREY = 9;

	private javax.swing.JTabbedPane mainTabs;

	/** black */
	public static Color black = new Color(0, 0, 0);
	/** darkGrey */
	public static Color darkGrey = new Color(102, 102, 102);
	/** lightGrey */
	public static Color lightGrey = new Color(204, 204, 204);
	/** white */
	public static Color white = new Color(255, 255, 255);
	/** border */
	public static Color border = new Color(85, 85, 136);
	/**header */
	public static Color header = new Color(153, 153, 187);
	/** bodyDark */
	public static Color bodyDark = new Color(170, 170, 204);
	/** bodyMedDark */
	public static Color bodyMedDark = new Color(187, 187, 238);
	/** bodyMedLight */
	public static Color bodyMedLight = new Color(204, 204, 238);
	/** bodyLight */
	public static Color bodyLight = new Color(238, 238, 255);

	/** Creates new form CharacterPanel1 */
	public CharacterPanel() {
		initComponents();
		initPrefs();
		setLocalColor();
	}

	/**
	 * Get the view
	 * @return JComponent
	 */
	public JComponent getView()
	{
		return this;
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {
		mainTabs = new javax.swing.JTabbedPane();

		setOneTouchExpandable(true);
		// SwingConstants.LEFT is equivalent to JTabbedPane.LEFT but more
		// 'correct' in a Java coding context (it is a static reference)
		mainTabs.setTabPlacement(javax.swing.SwingConstants.LEFT);
		mainTabs.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				mainTabsStateChanged();
			}
		});

		setLeftComponent(mainTabs);
		rightSidePane=new FlippingSplitPane(JSplitPane.VERTICAL_SPLIT);
		setRightComponent(rightSidePane);
		setResizeWeight(0.1);
	}

	private void mainTabsStateChanged() {
		refresh();
	}

	private void initPrefs() {
		setDividerLocation(SettingsHandler.getGMGenOption(CharacterSheetPlugin.LOG_NAME + ".DividerLocation", 2000));
	}

	/**
	 * Flush the preferences
	 */
	public void flushPrefs() {
		SettingsHandler.setGMGenOption(CharacterSheetPlugin.LOG_NAME + ".DividerLocation", getDividerLocation());
		if(notesPanel != null) {
			notesPanel.flushPrefs();
		}
	}

	/**
	 * Set the PC
	 * @param pc
	 */
	public void setPc(PlayerCharacter pc) {
		if(this.pc != pc) {
			this.pc = pc;
			serial = 0;
			generateTabs(true);
			readyForRefresh = true;
			refresh();
		}
	}

	/**
	 * Generate the tabs
	 * @param removeall
	 */
	public synchronized void generateTabs(boolean removeall) {
		if(removeall) {
			mainTabs.removeAll();
			if(page1 == null) {
				page1 = new Page1Panel();
				page1layout = new ScrollablePanel(20);
			}
			if(page2 == null) {
				page2 = new Page2Panel();
				page2layout = new ScrollablePanel(20);
			}

			if(rightSidePane == null){
				rightSidePane = new FlippingSplitPane(JSplitPane.VERTICAL_SPLIT);
			}
			if(notesPanel == null) {
				notesPanel = new NotesPanel();
			}
			if(selectPanel == null) {
				selectPanel = new SelectPanel(this);
			}
			if(spellPaneList != null) {
				for(int i = 0; i < spellPaneList.size(); i++) {
					SpellPage spellPage = (SpellPage)spellPaneList.get(i);
					spellPage.destruct();
				}
				spellPaneList.clear();
			}
			else {
				spellPaneList = new ArrayList();
			}

			pcProperties = new Properties();

			Globals.setCurrentPC(pc);
			populatePcProperties();

			/* For Testing
			test = new TestSheet(pcProperties);
			test.setPc(pc);
			testlayout = new ScrollablePanel(20);
			testlayout.add(test);
			mainTabs.add("Test", new JScrollPane(test));*/

			notesPanel.setPc(pc, pcProperties);
			try
			{
				selectPanel.setPc(pc);
			}
			catch (Exception e)
			{
				//do nothing?
			}
			setRightComponent(rightSidePane);
			rightSidePane.setTopComponent(notesPanel);
			rightSidePane.setBottomComponent(selectPanel);
			page1.setPc(pc, pcProperties);
			page1layout.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 5));
			page1layout.add(page1);
			mainTabs.add("Page 1", new JScrollPane(page1layout));
			((JScrollPane)mainTabs.getSelectedComponent()).getViewport().setViewPosition(new Point(0,0));

			page2.setPc(pc, pcProperties);
			page2layout.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 5));
			page2layout.add(page2);
			mainTabs.add("Page 2", new JScrollPane(page2layout));
		}
		List spellClassList = pc.getSpellClassList();
		for(int i = 0; i < spellClassList.size(); i++) {
			Object object = spellClassList.get(i);
			if (object instanceof PCClass) {
				PCClass pcclass = (PCClass) object;
				String className = pcclass.getDisplayClassName();
				int highestSpellLevel = pcclass.getHighestLevelSpell(pc);
				for(int j = 0; j <= highestSpellLevel; j++) {
					pcclass.getCastForLevel(pcclass.getLevel(), j, Globals.getDefaultSpellBook(), pc);
					pcclass.getKnownForLevel(pcclass.getLevel(), j, pc);

					StringBuffer sb = new StringBuffer();
					sb.append(className).append(' ').append(j);
					String title = sb.toString();
					int spellList = pcclass.getSpellSupport().getCharacterSpell(null, Globals.getDefaultSpellBook(), j).size();
					if(mainTabs.indexOfTab(title) == -1) {
						if(spellList > 0) {
							//TODO: Potentially, leveling could get these things out of order. Fix that
							SpellPage spellPage = new SpellPage(20);
							spellPage.setPc(pc, pcProperties, pcclass, j);
							mainTabs.add(title, new JScrollPane(spellPage));
							spellPaneList.add(spellPage);
							spellPage.refresh();
						}
					}
					else if (spellList == 0)
					{
						mainTabs.remove(mainTabs.indexOfTab(title));
					}
				}
			}
		}
		String title = pc.getRace().getName();
		if(pc.getRace().getSpellSupport().getCharacterSpellCount() > 0 && mainTabs.indexOfTab(title) == -1) {
			SpellPage spellPage = new SpellPage(20);
			spellPage.setPc(pc, pcProperties);
			mainTabs.add(title, new JScrollPane(spellPage));
			spellPaneList.add(spellPage);
			spellPage.refresh();
		}
	}

	/**
	 * Set the system refresh
	 * @param systemRefresh
	 */
	public void setSystemRefresh(boolean systemRefresh) {
		this.systemRefresh = systemRefresh;
	}

	/**
	 * Refresh
	 */
	public void refresh() {
		flushPrefs();
		if(readyForRefresh) {
			new Refresher().start();
		}
	}

	/**
	 * force the refresh
	 */
	public void forceRefresh()
	{
		new Refresher().start();
	}

	/**
	 * Get the PC
	 * @return PlayerCharacter
	 */
	public PlayerCharacter getPc() {
		return pc;
	}

	private void populatePcProperties() {
		List notesList = pc.getNotesList();
		for(int i = 0; i < notesList.size(); i++) {
			NoteItem note = (NoteItem)notesList.get(i);
			if(note.getName().equals("Hidden")) {
				try {
					pcProperties.load(new ByteArrayInputStream(note.getValue().getBytes()));
				}
				catch(IOException e) {
					//This is never going to happen, cause no actual io is taking place, besides,
					//if it did, we couldn't do anything about it anyway
					System.out.println("The impossible happened");
				}
				return;
			}
		}
	}

	/**
	 * Update the properties
	 */
	public void updateProperties() {
		if(page1 != null) {
			page1.updateProperties();
			page2.updateProperties();
			for(int i = 0; i < spellPaneList.size(); i++) {
				SpellPage spellPage = (SpellPage)spellPaneList.get(i);
				spellPage.updateProperties();
			}
			notesPanel.updateProperties();
		}
	}

	/**
	 * Save
	 */
	public void save() {
		updateProperties();
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			pcProperties.store(os, "Character Sheet Plugin Properties");
			NoteItem hiddenNote = null;
			int newNodeId = 0;
			List notesList = pc.getNotesList();
			for(int i = 0; i < notesList.size(); i++) {
				NoteItem testNote = (NoteItem)notesList.get(i);
				if (testNote.getId() > newNodeId) {
					newNodeId = testNote.getId();
				}
				if(testNote.getName().equals("Hidden")) {
					hiddenNote = testNote;
				}
			}

			//Add notes item for properties
			if(hiddenNote != null) {
				hiddenNote.setValue(os.toString());
			}
			else {
				newNodeId++;
				hiddenNote = new NoteItem(newNodeId, -1, "Hidden", os.toString());
				pc.addNotesItem(hiddenNote);
			}
		}
		catch(IOException e) {
			//This is never going to happen, cause no actual io is taking place, besides, if it did, we couldn't do anything
			//about it anyway
		}
	}

	/**
	 * Set color
	 */
	public void setColor() {
		setLocalColor();
		if(page1 != null) {
			page1.setColor();
			page2.setColor();
			for(int i = 0; i < spellPaneList.size(); i++) {
				SpellPage spellPage = (SpellPage)spellPaneList.get(i);
				spellPage.setColor();
			}
		}
	}

	/**
	 * Set local color
	 */
	public void setLocalColor() {
		if(page1layout != null) {
			page1layout.setBackground(white);
			page2layout.setBackground(white);
		}
	}

	/**
	 * Set blue color scheme
	 */
	public static void setColorBlue() {
		//Blue
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(85, 85, 136);   //555588
		header       = new Color(153, 153, 187); //9999BB
		bodyDark     = new Color(170, 170, 204); //AAAACC
		bodyMedDark  = new Color(187, 187, 238); //BBBBEE
		bodyMedLight = new Color(204, 204, 238); //CCCCEE
		bodyLight    = new Color(238, 238, 255); //EEEEFF
	}

	/**
	 * Set light blue color scheme
	 */
	public static void setColorLightBlue() {
		//Light Blue
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(85, 85, 136);   //555588
		header       = new Color(187, 187, 238); //BBBBEE
		bodyDark     = new Color(187, 187, 238); //BBBBEE
		bodyMedDark  = new Color(204, 204, 255); //CCCCFF
		bodyMedLight = new Color(238, 238, 255); //EEEEFF
		bodyLight    = new Color(255, 255, 255); //FFFFFF
	}

	/**
	 * Set green color scheme
	 */
	public static void setColorGreen() {
		//Green
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(85, 136, 85);   //558855
		header       = new Color(153, 187, 153); //99BB99
		bodyDark     = new Color(170, 204, 170); //AACCAA
		bodyMedDark  = new Color(187, 238, 187); //BBEEBB
		bodyMedLight = new Color(204, 238, 204); //CCEECC
		bodyLight    = new Color(238, 255, 238); //EEFFEE
	}

	/**
	 * Set light green color scheme
	 */
	public static void setColorLightGreen() {
		//Light Green
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(0, 136, 0);     //008800
		header       = new Color(68, 204, 68);   //44CC44
		bodyDark     = new Color(68, 204, 68);   //44CC44
		bodyMedDark  = new Color(204, 255, 204); //CCFFCC
		bodyMedLight = new Color(238, 255, 238); //EEFFEE
		bodyLight    = new Color(255, 255, 255); //FFFFFF
	}

	/**
	 * Set red color scheme
	 */
	public static void setColorRed() {
		//Red
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(163, 85, 85);   //885555
		header       = new Color(187, 153, 153); //BB9999
		bodyDark     = new Color(204, 170, 170); //CCAAAA
		bodyMedDark  = new Color(238, 187, 187); //EEBBBB
		bodyMedLight = new Color(238, 204, 204); //FFCCCC
		bodyLight    = new Color(255, 238, 238); //FFEEEE
	}

	/**
	 * Set light red color scheme
	 */
	public static void setColorLightRed() {
		//Light Red
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(163, 0, 0);     //880000
		header       = new Color(204, 0, 0);     //CC0000
		bodyDark     = new Color(204, 0, 0);     //CC0000
		bodyMedDark  = new Color(255, 204, 204); //FFCCCC
		bodyMedLight = new Color(255, 238, 238); //FFEEEE
		bodyLight    = new Color(255, 255, 255); //FFFFFF
	}

	/**
	 * Set light yellow color scheme
	 */
	public static void setColorYellow() {
		//Yellow
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(136, 136, 85);  //888855
		header       = new Color(187, 187, 153); //BBBB99
		bodyDark     = new Color(204, 204, 170); //CCCCAA
		bodyMedDark  = new Color(221, 221, 187); //DDDDBB
		bodyMedLight = new Color(238, 238, 204); //EEEECC
		bodyLight    = new Color(255, 255, 221); //FFFFDD
	}

	/**
	 * Set light yellow color scheme
	 */
	public static void setColorLightYellow() {
		//Light Yellow
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(136, 136, 85);   //888800
		header       = new Color(238, 238, 187);  //888800
		bodyDark     = new Color(238, 238, 187); //CCCCAA
		bodyMedDark  = new Color(255, 255, 204); //DDDDBB
		bodyMedLight = new Color(255, 255, 238); //FFFFEE
		bodyLight    = new Color(255, 255, 255); //FFFFFF
	}

	/**
	 * Set light grey color scheme
	 */
	public static void setColorGrey() {
		//Grey
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(0, 0, 0);       //000000
		header       = new Color(153, 153, 153); //999999
		bodyDark     = new Color(204, 204, 204); //CCCCCC
		bodyMedDark  = new Color(204, 204, 204); //CCCCCC
		bodyMedLight = new Color(204, 204, 204); //CCCCCC
		bodyLight    = new Color(255, 255, 255); //FFFFFF
	}

	/**
	 * Set light grey color scheme
	 */
	public static void setColorLightGrey() {
		//Light Grey
		black        = new Color(0, 0, 0);       //000000
		darkGrey     = new Color(102, 102, 102); //666666
		lightGrey    = new Color(204, 204, 204); //CCCCCC
		white        = new Color(255, 255, 255); //FFFFFF
		border       = new Color(0, 0, 0);       //000000
		header       = new Color(255, 255, 255); //FFFFFF
		bodyDark     = new Color(255, 255, 255); //FFFFFF
		bodyMedDark  = new Color(204, 204, 204); //CCCCCC
		bodyMedLight = new Color(204, 204, 204); //CCCCCC
		bodyLight    = new Color(255, 255, 255); //FFFFFF
	}

	private class Refresher extends Thread {

		public void run() {
			refresh();
		}

		private void refresh() {
			if(systemRefresh) {
				Globals.setCurrentPC(pc);
				updateProperties();
				if(mainTabs.getTabCount() > 0 && mainTabs.getSelectedIndex() > -1) {
					String currentTitle = mainTabs.getTitleAt(mainTabs.getSelectedIndex());
					JViewport viewPort = ((JScrollPane)mainTabs.getSelectedComponent()).getViewport();
					ScrollablePanel sp = (ScrollablePanel)viewPort.getView();
					Component comp = sp.getComponent(0);

					viewPort.setViewPosition(new Point(0,0));
					if(currentTitle.equals("Page 1")) {
						page1.refresh();
					}
					else if(currentTitle.equals("Page 2")) {
						page2.refresh();
					}
					else if(comp instanceof ClassSpellLevelPane) {
						ClassSpellLevelPane spellPage = (ClassSpellLevelPane)comp;
						spellPage.refresh();
					}
				}
				if(serial < pc.getSerial()) {
					generateTabs(false);
					notesPanel.refresh();
					selectPanel.refresh();
					serial = pc.getSerial();
				}
			}
			initPrefs();
			mainTabs.repaint();
		}
	}
}
