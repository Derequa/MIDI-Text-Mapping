package frontside;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;

public class FrontEnd extends JFrame implements ActionListener {

	private static final long serialVersionUID = 4829661262223915297L;
	
	private Dimension consoleSize = new Dimension(320, 400);
	
	public static final int TEMPO_MAX = 330;
	public static final int TEMPO_MIN = 30;
	public static final int TEMPO_DEFAULT = 130;
	
	private final String TITLE = "MIDI File Mapper";

	private final String FNOISE = "1/f Noise";
	private final String KARPLUS = "Karplus/Strong";
	private final String TRIANGULAR = "Triangular";
	private final String MARKOV = "Markov";
	private final String NONE = "None (Mapping Only)";
	
	private String[] org_strings = {FNOISE, KARPLUS, TRIANGULAR, MARKOV, NONE};
	private String[] std_strings = {FNOISE, KARPLUS, TRIANGULAR, MARKOV};
	
	private String str_options = "Actions";
	private String str_console = "Console Output";
	private String str_org_macro = "Compositional Method (High-level)";
	private String str_org_micro = "Compositional Method (Low-level)";
	private String str_dur = "Note Duration Generation Method";
	private String str_vel = "Note Velocity Generation Method";
	private String str_spc = "Note Spacing Generation Method";
	private String str_nosel_file = "No file selected";
	private String str_nosel_dir = "No directory selected";
	private String str_nosel_map = "No mapping scheme selected";
	private String str_opt_fnoise = FNOISE + " Options";
	private String str_opt_karplus = KARPLUS + " Options";
	private String str_opt_triangular = TRIANGULAR + " Options";
	private String str_opt_markov = MARKOV + " Options";
	private String str_opt_none = "No Options";
	
	private JSlider sld_tempo = new JSlider(JSlider.HORIZONTAL, TEMPO_MIN, TEMPO_MAX, TEMPO_DEFAULT);
	
	private JComboBox<String> combo_org_macro = new JComboBox<String>(org_strings);
	private JComboBox<String> combo_org_micro = new JComboBox<String>(org_strings);
	private JComboBox<String> combo_dur = new JComboBox<String>(std_strings);
	private JComboBox<String> combo_vel = new JComboBox<String>(std_strings);
	private JComboBox<String> combo_spc = new JComboBox<String>(std_strings);
	
	private JButton btn_select_mapper = new JButton("Selecting a Mapping Scheme");
	private JButton btn_select_file = new JButton("Choose a Source File");
	private JButton btn_select_dir = new JButton("Choose a Source Folder");
	private JButton btn_generate = new JButton("Generate!");
	
	protected JTextArea gui_console = new JTextArea(50, 50);
	private JTextArea selected_file = new JTextArea(str_nosel_file);
	private JTextArea selected_dir = new JTextArea(str_nosel_dir);
	private JTextArea selected_map = new JTextArea(str_nosel_map);
	
	private JLabel lbl_selected_file = new JLabel("Selected File:");
	private JLabel lbl_selected_dir = new JLabel("Selected Directory:");
	private JLabel lbl_selected_map = new JLabel("Selected Mapping Scheme:");
	private JLabel lbl_tempo = new JLabel("Tempo:");
	
	// Panels for options, combo boxes, and their associated card-layout panels
	private JPanel pnl_cards_org_macro = new JPanel(new CardLayout());
	private JPanel pnl_cards_org_micro = new JPanel(new CardLayout());
	private JPanel pnl_cards_dur = new JPanel(new CardLayout());
	private JPanel pnl_cards_vel = new JPanel(new CardLayout());
	private JPanel pnl_cards_spc = new JPanel(new CardLayout());
	private JPanel pnl_options = new JPanel(new GridLayout(3, 0));
	private JPanel pnl_console = new JPanel();

	public FrontEnd(){
		setupPanels();
		setupButtons();
		setSize(500, 500);
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void setupPanels(){
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		selected_file.setEditable(false);
		selected_dir.setEditable(false);
		pnl_options.add(btn_select_file);
		pnl_options.add(lbl_selected_file);
		pnl_options.add(selected_file);
		pnl_options.add(btn_select_dir);
		pnl_options.add(lbl_selected_dir);
		pnl_options.add(selected_dir);
		pnl_options.add(btn_select_mapper);
		pnl_options.add(lbl_selected_map);
		pnl_options.add(selected_map);
		pnl_options.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_options));
		c.add(pnl_options, BorderLayout.NORTH);
		
		JPanel genAndTemp = new JPanel(new GridLayout(0, 1));
		JPanel tempoPanel = new JPanel(new GridLayout(0, 1));
		tempoPanel.add(lbl_tempo);
		tempoPanel.add(sld_tempo);
		genAndTemp.add(tempoPanel);
		genAndTemp.add(btn_generate);
		
		JPanel combos = new JPanel(new GridLayout(0, 3));
		combos.add(setupCards(pnl_cards_org_macro, combo_org_macro, str_org_macro));
		combos.add(setupCards(pnl_cards_org_micro, combo_org_micro, str_org_micro));
		combos.add(setupCards(pnl_cards_dur, combo_dur, str_dur));
		combos.add(setupCards(pnl_cards_vel, combo_vel, str_vel));
		combos.add(setupCards(pnl_cards_spc, combo_spc, str_spc));
		combos.add(genAndTemp);
		c.add(combos, BorderLayout.CENTER);
		
		
		JScrollPane consolePane = new JScrollPane(gui_console);
		gui_console.setEditable(false);
		consolePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		consolePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		consolePane.setPreferredSize(consoleSize);
		pnl_console.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_console));
		pnl_console.add(consolePane);
		c.add(pnl_console, BorderLayout.WEST);
	}
	
	private JPanel setupCards(JPanel cardPanel, JComboBox comboBox, String borderTitle){
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(comboBox, BorderLayout.NORTH);
		panel.add(cardPanel, BorderLayout.CENTER);
		comboBox.setSelectedIndex(0);
		cardPanel.add(assembleFNoiseCard(), FNOISE);
		cardPanel.add(assembleKarplusCard(), KARPLUS);
		cardPanel.add(assembleTriangularCard(), TRIANGULAR);
		cardPanel.add(assembleMarkovCard(), MARKOV);
		cardPanel.add(assembleNoneCard(), NONE);
		CardLayout layout = (CardLayout) cardPanel.getLayout();
		layout.show(cardPanel, (String) comboBox.getSelectedItem());
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), borderTitle));
		return panel;
	}
	
	private void setupButtons(){
		btn_select_file.addActionListener(this);
		btn_select_dir.addActionListener(this);
		btn_generate.addActionListener(this);
		btn_select_mapper.addActionListener(this);
		combo_org_macro.addActionListener(this);
		combo_org_micro.addActionListener(this);
		combo_dur.addActionListener(this);
		combo_vel.addActionListener(this);
		combo_spc.addActionListener(this);
	}
	
	private JPanel assembleFNoiseCard(){
		JPanel build = new JPanel();
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_fnoise));
		build.add(new JTextArea("FNOISE OPTIONS IN PROGRESS..."));
		return build;
	}
	
	private JPanel assembleKarplusCard(){
		JPanel build = new JPanel();
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_karplus));
		build.add(new JTextArea("KARPLUS OPTIONS IN PROGRESS..."));
		return build;
	}

	private JPanel assembleTriangularCard(){
		JPanel build = new JPanel();
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_triangular));
		build.add(new JTextArea("TRIANGULAR OPTIONS IN PROGRESS..."));
		return build;
	}
	
	private JPanel assembleMarkovCard(){
		JPanel build = new JPanel();
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_markov));
		build.add(new JTextArea("MARKOV OPTIONS IN PROGRESS..."));
		return build;
	}
	
	private JPanel assembleNoneCard(){
		JPanel build = new JPanel();
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_none));
		build.add(new JTextArea("NONE OPTIONS IN PROGRESS..."));
		return build;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(combo_org_macro))
			((CardLayout) pnl_cards_org_macro.getLayout()).show(pnl_cards_org_macro, (String) combo_org_macro.getSelectedItem());
		else if(e.getSource().equals(combo_org_micro))
			((CardLayout) pnl_cards_org_micro.getLayout()).show(pnl_cards_org_micro, (String) combo_org_micro.getSelectedItem());
		else if(e.getSource().equals(combo_dur))
			((CardLayout) pnl_cards_dur.getLayout()).show(pnl_cards_dur, (String) combo_dur.getSelectedItem());
		else if(e.getSource().equals(combo_vel))
			((CardLayout) pnl_cards_vel.getLayout()).show(pnl_cards_vel, (String) combo_vel.getSelectedItem());
		else if(e.getSource().equals(combo_spc))
			((CardLayout) pnl_cards_spc.getLayout()).show(pnl_cards_spc, (String) combo_spc.getSelectedItem());
	}

}
