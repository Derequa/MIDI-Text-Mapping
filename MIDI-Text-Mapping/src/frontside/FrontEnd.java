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
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gernerators.FNoise;
import gernerators.Generator.GeneratorType;
import gernerators.KarplusStrong;
import gernerators.properties.Property.PropertyType;

public class FrontEnd extends JFrame implements ActionListener, ChangeListener {

	private static final long serialVersionUID = 4829661262223915297L;
	
	private Dimension consoleSize = new Dimension(320, 400);
	private Dimension componentSize = new Dimension(330, 200);
	
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
	
	private String str_options = "File Input";
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
	private String str_sub_opt_fnoise = "Number of Dice:";
	private String str_sub_opt_karplus_buf = "Buffer Size:";
	private String str_sub_opt_karplus_thres = "Reset Threshold:";
	
	private JSlider sld_tempo = new JSlider(JSlider.HORIZONTAL, TEMPO_MIN, TEMPO_MAX, TEMPO_DEFAULT);
	private JSlider sld_opt_fnoise_macro = new JSlider(JSlider.HORIZONTAL, FNoise.MIN_DICE, FNoise.MAX_DICE, FNoise.DEFAULT_DICE);
	private JSlider sld_opt_fnoise_micro = new JSlider(JSlider.HORIZONTAL, FNoise.MIN_DICE, FNoise.MAX_DICE, FNoise.DEFAULT_DICE);
	private JSlider sld_opt_fnoise_dur = new JSlider(JSlider.HORIZONTAL, FNoise.MIN_DICE, FNoise.MAX_DICE, FNoise.DEFAULT_DICE);
	private JSlider sld_opt_fnoise_vel = new JSlider(JSlider.HORIZONTAL, FNoise.MIN_DICE, FNoise.MAX_DICE, FNoise.DEFAULT_DICE);
	private JSlider sld_opt_fnoise_spc = new JSlider(JSlider.HORIZONTAL, FNoise.MIN_DICE, FNoise.MAX_DICE, FNoise.DEFAULT_DICE);
	private JSlider sld_opt_karplus_buf_macro = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_BUFFER_SIZE, KarplusStrong.MAX_BUFFER_SIZE, KarplusStrong.DEFAULT_BUFFER_LENGTH);
	private JSlider sld_opt_karplus_buf_micro = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_BUFFER_SIZE, KarplusStrong.MAX_BUFFER_SIZE, KarplusStrong.DEFAULT_BUFFER_LENGTH);
	private JSlider sld_opt_karplus_buf_dur = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_BUFFER_SIZE, KarplusStrong.MAX_BUFFER_SIZE, KarplusStrong.DEFAULT_BUFFER_LENGTH);
	private JSlider sld_opt_karplus_buf_vel = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_BUFFER_SIZE, KarplusStrong.MAX_BUFFER_SIZE, KarplusStrong.DEFAULT_BUFFER_LENGTH);
	private JSlider sld_opt_karplus_buf_spc = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_BUFFER_SIZE, KarplusStrong.MAX_BUFFER_SIZE, KarplusStrong.DEFAULT_BUFFER_LENGTH);
	private JSlider sld_opt_karplus_thres_macro = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_THRESHOLD, KarplusStrong.MAX_THRESHOLD, KarplusStrong.DEFAULT_THRESHOLD);
	private JSlider sld_opt_karplus_thres_micro = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_THRESHOLD, KarplusStrong.MAX_THRESHOLD, KarplusStrong.DEFAULT_THRESHOLD);
	private JSlider sld_opt_karplus_thres_dur = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_THRESHOLD, KarplusStrong.MAX_THRESHOLD, KarplusStrong.DEFAULT_THRESHOLD);
	private JSlider sld_opt_karplus_thres_vel = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_THRESHOLD, KarplusStrong.MAX_THRESHOLD, KarplusStrong.DEFAULT_THRESHOLD);
	private JSlider sld_opt_karplus_thres_spc = new JSlider(JSlider.HORIZONTAL, KarplusStrong.MIN_THRESHOLD, KarplusStrong.MAX_THRESHOLD, KarplusStrong.DEFAULT_THRESHOLD);
	
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
	private JTextArea txt_selected_file = new JTextArea(str_nosel_file);
	private JTextArea txt_selected_dir = new JTextArea(str_nosel_dir);
	private JTextArea txt_selected_map = new JTextArea(str_nosel_map);
	private JTextArea txt_current_tempo = new JTextArea("" + sld_tempo.getValue());
	private JTextArea txt_opt_fnoise_macro = new JTextArea("" + sld_opt_fnoise_macro.getValue());
	private JTextArea txt_opt_fnoise_micro = new JTextArea("" + sld_opt_fnoise_micro.getValue());
	private JTextArea txt_opt_fnoise_dur = new JTextArea("" + sld_opt_fnoise_dur.getValue());
	private JTextArea txt_opt_fnoise_vel = new JTextArea("" + sld_opt_fnoise_vel.getValue());
	private JTextArea txt_opt_fnoise_spc = new JTextArea("" + sld_opt_fnoise_spc.getValue());
	private JTextArea txt_opt_karplus_buf_macro = new JTextArea("" + sld_opt_karplus_buf_macro.getValue());
	private JTextArea txt_opt_karplus_buf_micro = new JTextArea("" + sld_opt_karplus_buf_micro.getValue());
	private JTextArea txt_opt_karplus_buf_dur = new JTextArea("" + sld_opt_karplus_buf_dur.getValue());
	private JTextArea txt_opt_karplus_buf_vel = new JTextArea("" + sld_opt_karplus_buf_vel.getValue());
	private JTextArea txt_opt_karplus_buf_spc = new JTextArea("" + sld_opt_karplus_buf_spc.getValue());
	private JTextArea txt_opt_karplus_thres_macro = new JTextArea("" + sld_opt_karplus_thres_macro.getValue());
	private JTextArea txt_opt_karplus_thres_micro = new JTextArea("" + sld_opt_karplus_thres_micro.getValue());
	private JTextArea txt_opt_karplus_thres_dur = new JTextArea("" + sld_opt_karplus_thres_dur.getValue());
	private JTextArea txt_opt_karplus_thres_vel = new JTextArea("" + sld_opt_karplus_thres_vel.getValue());
	private JTextArea txt_opt_karplus_thres_spc = new JTextArea("" + sld_opt_karplus_thres_spc.getValue());
	
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
	
	private File toMap;
	private File mappingScheme;

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
		
		pnl_options.add(btn_select_file);
		pnl_options.add(lbl_selected_file);
		pnl_options.add(txt_selected_file);
		pnl_options.add(btn_select_dir);
		pnl_options.add(lbl_selected_dir);
		pnl_options.add(txt_selected_dir);
		pnl_options.add(btn_select_mapper);
		pnl_options.add(lbl_selected_map);
		pnl_options.add(txt_selected_map);
		pnl_options.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_options));
		c.add(pnl_options, BorderLayout.NORTH);
		
		JPanel genAndTemp = new JPanel(new GridLayout(0, 1));
		JPanel tempoPanel = new JPanel(new GridLayout(0, 1));
		
		tempoPanel.add(lbl_tempo);
		tempoPanel.add(txt_current_tempo);
		genAndTemp.add(tempoPanel);
		genAndTemp.add(sld_tempo);
		genAndTemp.add(btn_generate);
		
		JPanel combos = new JPanel(new GridLayout(0, 3));
		combos.add(setupCards(pnl_cards_org_macro, combo_org_macro, str_org_macro, PropertyType.MACRO_ORG));
		combos.add(setupCards(pnl_cards_org_micro, combo_org_micro, str_org_micro, PropertyType.MICRO_ORG));
		combos.add(setupCards(pnl_cards_dur, combo_dur, str_dur, PropertyType.DURATION));
		combos.add(setupCards(pnl_cards_vel, combo_vel, str_vel, PropertyType.VELOCITY));
		combos.add(setupCards(pnl_cards_spc, combo_spc, str_spc, PropertyType.SPACING));
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
	
	private JPanel setupCards(JPanel cardPanel, JComboBox<String> comboBox, String borderTitle, PropertyType mode){
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(comboBox, BorderLayout.NORTH);
		panel.add(cardPanel, BorderLayout.CENTER);
		comboBox.setSelectedIndex(0);
		cardPanel.add(assembleFNoiseCard(mode), FNOISE);
		cardPanel.add(assembleKarplusCard(mode), KARPLUS);
		cardPanel.add(assembleTriangularCard(mode), TRIANGULAR);
		cardPanel.add(assembleMarkovCard(mode), MARKOV);
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
		sld_tempo.addChangeListener(this);
		sld_opt_fnoise_macro.addChangeListener(this);
		sld_opt_fnoise_micro.addChangeListener(this);
		sld_opt_fnoise_dur.addChangeListener(this);
		sld_opt_fnoise_vel.addChangeListener(this);
		sld_opt_fnoise_spc.addChangeListener(this);
		sld_opt_karplus_buf_macro.addChangeListener(this);
		sld_opt_karplus_buf_micro.addChangeListener(this);
		sld_opt_karplus_buf_dur.addChangeListener(this);
		sld_opt_karplus_buf_vel.addChangeListener(this);
		sld_opt_karplus_buf_spc.addChangeListener(this);
		sld_opt_karplus_thres_macro.addChangeListener(this);
		sld_opt_karplus_thres_micro.addChangeListener(this);
		sld_opt_karplus_thres_dur.addChangeListener(this);
		sld_opt_karplus_thres_vel.addChangeListener(this);
		sld_opt_karplus_thres_spc.addChangeListener(this);
		
		sld_tempo.setPaintTicks(true);
		sld_tempo.setPaintLabels(true);
		sld_tempo.setMajorTickSpacing(50);
		sld_tempo.setMinorTickSpacing(10);
		txt_current_tempo.setEditable(false);
		txt_selected_file.setEditable(false);
		txt_selected_dir.setEditable(false);
		txt_selected_map.setEditable(false);
		txt_opt_fnoise_macro.setEditable(false);
		txt_opt_fnoise_micro.setEditable(false);
		txt_opt_fnoise_dur.setEditable(false);
		txt_opt_fnoise_vel.setEditable(false);
		txt_opt_fnoise_spc.setEditable(false);
		txt_opt_karplus_buf_macro.setEditable(false);
		txt_opt_karplus_buf_micro.setEditable(false);
		txt_opt_karplus_buf_dur.setEditable(false);
		txt_opt_karplus_buf_vel.setEditable(false);
		txt_opt_karplus_buf_spc.setEditable(false);
		txt_opt_karplus_thres_macro.setEditable(false);
		txt_opt_karplus_thres_micro.setEditable(false);
		txt_opt_karplus_thres_dur.setEditable(false);
		txt_opt_karplus_thres_vel.setEditable(false);
		txt_opt_karplus_thres_spc.setEditable(false);
		
		setupSlider(sld_opt_fnoise_macro, GeneratorType.FNOISE);
		setupSlider(sld_opt_fnoise_micro, GeneratorType.FNOISE);
		setupSlider(sld_opt_fnoise_dur, GeneratorType.FNOISE);
		setupSlider(sld_opt_fnoise_vel, GeneratorType.FNOISE);
		setupSlider(sld_opt_fnoise_spc, GeneratorType.FNOISE);
		setupSlider(sld_opt_karplus_buf_macro, GeneratorType.KARPLUS);
		setupSlider(sld_opt_karplus_buf_micro, GeneratorType.KARPLUS);
		setupSlider(sld_opt_karplus_buf_dur, GeneratorType.KARPLUS);
		setupSlider(sld_opt_karplus_buf_vel, GeneratorType.KARPLUS);
		setupSlider(sld_opt_karplus_buf_spc, GeneratorType.KARPLUS);
		setupSlider(sld_opt_karplus_thres_macro, GeneratorType.KARPLUS);
		setupSlider(sld_opt_karplus_thres_micro, GeneratorType.KARPLUS);
		setupSlider(sld_opt_karplus_thres_dur, GeneratorType.KARPLUS);
		setupSlider(sld_opt_karplus_thres_vel, GeneratorType.KARPLUS);
		setupSlider(sld_opt_karplus_thres_spc, GeneratorType.KARPLUS);
	}
	
	private void setupSlider(JSlider j, GeneratorType mode){
		j.setPaintLabels(true);
		j.setPaintTicks(true);
		switch(mode){
			case FNOISE:		j.setMajorTickSpacing(10);
								j.setMinorTickSpacing(3);
								break;
			case KARPLUS:		j.setMajorTickSpacing(15);
								j.setMinorTickSpacing(5);
								break;
			default:			break;
		}
	}
	
	
	private JPanel assembleFNoiseCard(PropertyType mode){
		JPanel build = new JPanel(new GridLayout(0, 1));
		JPanel info = new JPanel(new GridLayout(1, 0));
		info.add(new JLabel(str_sub_opt_fnoise));
		build.add(info);
		switch(mode){
			case MACRO_ORG:		info.add(txt_opt_fnoise_macro);
								build.add(sld_opt_fnoise_macro);
								break;
			case MICRO_ORG:		info.add(txt_opt_fnoise_micro);
								build.add(sld_opt_fnoise_micro);
								break;
			case DURATION:		info.add(txt_opt_fnoise_dur);
								build.add(sld_opt_fnoise_dur);
								break;
			case VELOCITY:		info.add(txt_opt_fnoise_vel);
								build.add(sld_opt_fnoise_vel);
								break;
			case SPACING:		info.add(txt_opt_fnoise_spc);
								build.add(sld_opt_fnoise_spc);
								break;
		}
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_fnoise));
		build.setPreferredSize(componentSize);
		return build;
	}
	
	private JPanel assembleKarplusCard(PropertyType mode){
		JPanel build = new JPanel(new GridLayout(0, 1));
		JPanel slider1 = new JPanel(new GridLayout(1, 0));
		JPanel slider2 = new JPanel(new GridLayout(1, 0));
		slider1.add(new JLabel(str_sub_opt_karplus_thres));
		slider2.add(new JLabel(str_sub_opt_karplus_buf));
		switch(mode){
			case MACRO_ORG:		slider1.add(txt_opt_karplus_thres_macro);
								slider2.add(txt_opt_karplus_buf_macro);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_macro);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_macro);
								break;
			case MICRO_ORG:		slider1.add(txt_opt_karplus_thres_micro);
								slider2.add(txt_opt_karplus_buf_micro);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_micro);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_micro);
								break;
			case DURATION:		slider1.add(txt_opt_karplus_thres_dur);
								slider2.add(txt_opt_karplus_buf_dur);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_dur);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_dur);
								break;
			case VELOCITY:		slider1.add(txt_opt_karplus_thres_vel);
								slider2.add(txt_opt_karplus_buf_vel);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_vel);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_vel);
								break;
			case SPACING:		slider1.add(txt_opt_karplus_thres_spc);
								slider2.add(txt_opt_karplus_buf_spc);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_spc);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_spc);
								break;
		}
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_karplus));
		build.setPreferredSize(componentSize);
		return build;
	}

	private JPanel assembleTriangularCard(PropertyType mode){
		JPanel build = new JPanel();
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_triangular));
		build.add(new JTextArea("TRIANGULAR OPTIONS IN PROGRESS..."));
		return build;
	}
	
	private JPanel assembleMarkovCard(PropertyType mode){
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
	
	private File selectFile(int mode){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(mode);
		int ret = fileChooser.showOpenDialog(this);
		if(ret == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		else 
			return null;
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
		else if(e.getSource().equals(btn_select_file)){
			File f = selectFile(JFileChooser.FILES_ONLY);
			if(f != null){
				toMap = f;
				txt_selected_file.setText(f.getName());
				txt_selected_dir.setText(str_nosel_dir);
			}
		}
		else if(e.getSource().equals(btn_select_dir)){
			File f = selectFile(JFileChooser.DIRECTORIES_ONLY);
			if(f != null){
				toMap = f;
				txt_selected_file.setText(str_nosel_file);
				txt_selected_dir.setText(f.getName());
			}
		}
		else if(e.getSource().equals(btn_select_mapper)){
			File f = selectFile(JFileChooser.FILES_ONLY);
			if(f != null){
				mappingScheme = f;
				txt_selected_map.setText(f.getName());
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(sld_tempo))
			txt_current_tempo.setText("" + sld_tempo.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_macro))
			txt_opt_fnoise_macro.setText("" + sld_opt_fnoise_macro.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_micro))
			txt_opt_fnoise_micro.setText("" + sld_opt_fnoise_micro.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_dur))
			txt_opt_fnoise_dur.setText("" + sld_opt_fnoise_dur.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_vel))
			txt_opt_fnoise_vel.setText("" + sld_opt_fnoise_vel.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_spc))
			txt_opt_fnoise_spc.setText("" + sld_opt_fnoise_spc.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_macro))
			txt_opt_karplus_buf_macro.setText("" + sld_opt_karplus_buf_macro.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_micro))
			txt_opt_karplus_buf_micro.setText("" + sld_opt_karplus_buf_micro.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_dur))
			txt_opt_karplus_buf_dur.setText("" + sld_opt_karplus_buf_dur.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_vel))
			txt_opt_karplus_buf_vel.setText("" + sld_opt_karplus_buf_vel.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_spc))
			txt_opt_karplus_buf_spc.setText("" + sld_opt_karplus_buf_spc.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_macro))
			txt_opt_karplus_thres_macro.setText("" + sld_opt_karplus_thres_macro.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_micro))
			txt_opt_karplus_thres_micro.setText("" + sld_opt_karplus_thres_micro.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_dur))
			txt_opt_karplus_thres_dur.setText("" + sld_opt_karplus_thres_dur.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_vel))
			txt_opt_karplus_thres_vel.setText("" + sld_opt_karplus_thres_vel.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_spc))
			txt_opt_karplus_thres_spc.setText("" + sld_opt_karplus_thres_spc.getValue());
	}

}
