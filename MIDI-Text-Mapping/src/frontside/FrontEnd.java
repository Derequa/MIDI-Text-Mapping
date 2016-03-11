package frontside;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import gernerators.FNoise;
import gernerators.Generator;
import gernerators.KarplusStrong;
import gernerators.Markov;
import gernerators.TriangularDist;
import gernerators.TriangularDist.DistributionScheme;
import gernerators.properties.Property.PropertyType;
import mapping.Mapper;
import mapping.Settings;

public class FrontEnd extends JFrame implements ActionListener, ChangeListener {

	private static final long serialVersionUID = 4829661262223915297L;
	
	private Dimension consoleSize = new Dimension(320, 540);
	private Dimension cardSize = new Dimension(330, 160);
	
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
	private String str_lbl_map = "Selected Mapping Scheme:";
	private String str_btn_map = "Set Mapping Scheme";
	private String str_opt_fnoise = FNOISE + " Options";
	private String str_opt_karplus = KARPLUS + " Options";
	private String str_opt_triangular = TRIANGULAR + " Options";
	private String str_opt_markov = MARKOV + " Options";
	private String str_opt_none = "No Options";
	private String str_sub_opt_fnoise = "Number of Dice:";
	private String str_sub_opt_karplus_buf = "Buffer Size:";
	private String str_sub_opt_karplus_thres = "Reset Threshold:";
	private String str_sub_opt_triangular_left = "Left Slant";
	private String str_sub_opt_triangular_norm = "Normal";
	private String str_sub_opt_triangular_right = "Right Slant";
	private String str_sub_opt_markov_bal = "Balance Probabilities?";
	private String str_sub_opt_markov_gen = "Generate Undefined Transitions?";
	private String str_nosel_output = "output.mid";
	private String str_change_thres = "Beats Between Changes:";
	
	private JSlider sld_tempo = new JSlider(JSlider.HORIZONTAL, Settings.TEMPO_MIN, Settings.TEMPO_MAX, Settings.TEMPO_DEFAULT);
	private JSlider sld_change_macro = new JSlider(JSlider.HORIZONTAL, Settings.ORG_MIN, Settings.MACRO_ORG_MAX, Settings.MACRO_ORG_DEFAULT);
	private JSlider sld_change_micro = new JSlider(JSlider.HORIZONTAL, Settings.ORG_MIN, Settings.MICRO_ORG_MAX, Settings.MICRO_ORG_DEFAULT);
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
	
	private JButton btn_clear_console = new JButton("Clear Console");
	private JButton btn_select_mapper = new JButton(str_btn_map);
	private JButton btn_select_file = new JButton("Set Source File");
	private JButton btn_select_dir = new JButton("Set Source Folder");
	private JButton btn_select_output = new JButton("Set Output File");
	private JButton btn_generate = new JButton("Generate!");
	private JButton btn_markov_sel_file_macro = new JButton(str_btn_map);
	private JButton btn_markov_sel_file_micro = new JButton(str_btn_map);
	private JButton btn_markov_sel_file_dur = new JButton(str_btn_map);
	private JButton btn_markov_sel_file_vel = new JButton(str_btn_map);
	private JButton btn_markov_sel_file_spc = new JButton(str_btn_map);
	
	private JRadioButton btn_opt_triangular_left_macro = new JRadioButton();
	private JRadioButton btn_opt_triangular_left_micro = new JRadioButton();
	private JRadioButton btn_opt_triangular_left_dur = new JRadioButton();
	private JRadioButton btn_opt_triangular_left_vel = new JRadioButton();
	private JRadioButton btn_opt_triangular_left_spc = new JRadioButton();
	private JRadioButton btn_opt_triangular_norm_macro = new JRadioButton();
	private JRadioButton btn_opt_triangular_norm_micro = new JRadioButton();
	private JRadioButton btn_opt_triangular_norm_dur = new JRadioButton();
	private JRadioButton btn_opt_triangular_norm_vel = new JRadioButton();
	private JRadioButton btn_opt_triangular_norm_spc = new JRadioButton();
	private JRadioButton btn_opt_triangular_right_macro = new JRadioButton();
	private JRadioButton btn_opt_triangular_right_micro = new JRadioButton();
	private JRadioButton btn_opt_triangular_right_dur = new JRadioButton();
	private JRadioButton btn_opt_triangular_right_vel = new JRadioButton();
	private JRadioButton btn_opt_triangular_right_spc = new JRadioButton();
	
	private JCheckBox bx_debug = new JCheckBox("View Debug Messages");
	private JCheckBox bx_opt_markov_bal_macro = new JCheckBox(str_sub_opt_markov_bal);
	private JCheckBox bx_opt_markov_bal_micro = new JCheckBox(str_sub_opt_markov_bal);
	private JCheckBox bx_opt_markov_bal_dur = new JCheckBox(str_sub_opt_markov_bal);
	private JCheckBox bx_opt_markov_bal_vel = new JCheckBox(str_sub_opt_markov_bal);
	private JCheckBox bx_opt_markov_bal_spc = new JCheckBox(str_sub_opt_markov_bal);
	private JCheckBox bx_opt_markov_gen_macro = new JCheckBox(str_sub_opt_markov_gen);
	private JCheckBox bx_opt_markov_gen_micro = new JCheckBox(str_sub_opt_markov_gen);
	private JCheckBox bx_opt_markov_gen_dur = new JCheckBox(str_sub_opt_markov_gen);
	private JCheckBox bx_opt_markov_gen_vel = new JCheckBox(str_sub_opt_markov_gen);
	private JCheckBox bx_opt_markov_gen_spc = new JCheckBox(str_sub_opt_markov_gen);
	
	protected JTextArea gui_console = new JTextArea(50, 50);
	
	private JTextField txt_max_length = new JTextField();
	
	private JLabel lbl_selected_file = new JLabel("Selected File:");
	private JLabel lbl_selected_dir = new JLabel("Selected Directory:");
	private JLabel lbl_selected_map = new JLabel("Selected Mapping Scheme:");
	private JLabel lbl_selected_output = new JLabel("Output File:");
	private JLabel lbl_tempo = new JLabel("Tempo:");
	private JLabel lbl_change_macro = new JLabel(str_change_thres);
	private JLabel lbl_change_micro = new JLabel(str_change_thres);
	private JLabel lbl_change_macro_val = new JLabel("" + Settings.MACRO_ORG_DEFAULT);
	private JLabel lbl_change_micro_val = new JLabel("" + Settings.MICRO_ORG_DEFAULT);
	private JLabel lbl_selected_file_name = new JLabel(str_nosel_file);
	private JLabel lbl_selected_dir_name = new JLabel(str_nosel_dir);
	private JLabel lbl_selected_map_name = new JLabel(str_nosel_map);
	private JLabel lbl_selected_output_name = new JLabel(str_nosel_output);
	private JLabel lbl_current_tempo = new JLabel("" + sld_tempo.getValue());
	private JLabel lbl_opt_fnoise_macro = new JLabel("" + sld_opt_fnoise_macro.getValue());
	private JLabel lbl_opt_fnoise_micro = new JLabel("" + sld_opt_fnoise_micro.getValue());
	private JLabel lbl_opt_fnoise_dur = new JLabel("" + sld_opt_fnoise_dur.getValue());
	private JLabel lbl_opt_fnoise_vel = new JLabel("" + sld_opt_fnoise_vel.getValue());
	private JLabel lbl_opt_fnoise_spc = new JLabel("" + sld_opt_fnoise_spc.getValue());
	private JLabel lbl_opt_karplus_buf_macro = new JLabel("" + sld_opt_karplus_buf_macro.getValue());
	private JLabel lbl_opt_karplus_buf_micro = new JLabel("" + sld_opt_karplus_buf_micro.getValue());
	private JLabel lbl_opt_karplus_buf_dur = new JLabel("" + sld_opt_karplus_buf_dur.getValue());
	private JLabel lbl_opt_karplus_buf_vel = new JLabel("" + sld_opt_karplus_buf_vel.getValue());
	private JLabel lbl_opt_karplus_buf_spc = new JLabel("" + sld_opt_karplus_buf_spc.getValue());
	private JLabel lbl_opt_karplus_thres_macro = new JLabel("" + sld_opt_karplus_thres_macro.getValue());
	private JLabel lbl_opt_karplus_thres_micro = new JLabel("" + sld_opt_karplus_thres_micro.getValue());
	private JLabel lbl_opt_karplus_thres_dur = new JLabel("" + sld_opt_karplus_thres_dur.getValue());
	private JLabel lbl_opt_karplus_thres_vel = new JLabel("" + sld_opt_karplus_thres_vel.getValue());
	private JLabel lbl_opt_karplus_thres_spc = new JLabel("" + sld_opt_karplus_thres_spc.getValue());
	private JLabel lbl_opt_markov_sel_file_macro = new JLabel(str_lbl_map);
	private JLabel lbl_opt_markov_sel_file_micro = new JLabel(str_lbl_map);
	private JLabel lbl_opt_markov_sel_file_dur = new JLabel(str_lbl_map);
	private JLabel lbl_opt_markov_sel_file_vel = new JLabel(str_lbl_map);
	private JLabel lbl_opt_markov_sel_file_spc = new JLabel(str_lbl_map);
	private JLabel lbl_opt_markov_sel_file_name_macro = new JLabel(str_nosel_file);
	private JLabel lbl_opt_markov_sel_file_name_micro = new JLabel(str_nosel_file);
	private JLabel lbl_opt_markov_sel_file_name_dur = new JLabel(str_nosel_file);
	private JLabel lbl_opt_markov_sel_file_name_vel = new JLabel(str_nosel_file);
	private JLabel lbl_opt_markov_sel_file_name_spc = new JLabel(str_nosel_file);
	private JLabel lbl_max_length = new JLabel("Max Length (Minutes):");
	
	// Panels for options, combo boxes, and their associated card-layout panels
	private JPanel pnl_cards_org_macro = new JPanel(new CardLayout());
	private JPanel pnl_cards_org_micro = new JPanel(new CardLayout());
	private JPanel pnl_cards_dur = new JPanel(new CardLayout());
	private JPanel pnl_cards_vel = new JPanel(new CardLayout());
	private JPanel pnl_cards_spc = new JPanel(new CardLayout());
	private JPanel pnl_options = new JPanel(new GridLayout(0, 3));
	private JPanel pnl_console = new JPanel(new BorderLayout());
	
	private File toMap;
	private File mappingScheme;
	private File outputFile;
	private File markov_map_macro;
	private File markov_map_micro;
	private File markov_map_dur;
	private File markov_map_vel;
	private File markov_map_spc;

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
		pnl_options.add(lbl_selected_file_name);
		pnl_options.add(btn_select_dir);
		pnl_options.add(lbl_selected_dir);
		pnl_options.add(lbl_selected_dir_name);
		pnl_options.add(btn_select_mapper);
		pnl_options.add(lbl_selected_map);
		pnl_options.add(lbl_selected_map_name);
		pnl_options.add(btn_select_output);
		pnl_options.add(lbl_selected_output);
		pnl_options.add(lbl_selected_output_name);
		pnl_options.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_options));
		c.add(pnl_options, BorderLayout.NORTH);
		
		JPanel genAndTemp = new JPanel(new GridLayout(0, 1));
		JPanel tempoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel lengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		lengthPanel.add(lbl_max_length);
		lengthPanel.add(txt_max_length);
		tempoPanel.add(lbl_tempo);
		tempoPanel.add(lbl_current_tempo);
		genAndTemp.add(lengthPanel);
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
		consolePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		consolePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		consolePane.setPreferredSize(consoleSize);
		pnl_console.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_console));
		pnl_console.add(consolePane, BorderLayout.CENTER);
		JPanel consoleOptions = new JPanel(new GridLayout(1, 0));
		consoleOptions.add(bx_debug);
		consoleOptions.add(btn_clear_console);
		pnl_console.add(consoleOptions, BorderLayout.SOUTH);
		c.add(pnl_console, BorderLayout.WEST);
	}
	
	private JPanel setupCards(JPanel cardPanel, JComboBox<String> comboBox, String borderTitle, PropertyType mode){
		JPanel panel = new JPanel(new BorderLayout());
		if(mode == PropertyType.MACRO_ORG){
			JPanel sliderCombo = new JPanel(new GridLayout(0, 1));
			JPanel labels = new JPanel(new FlowLayout(FlowLayout.LEFT));
			sliderCombo.add(comboBox);
			labels.add(lbl_change_macro);
			labels.add(lbl_change_macro_val);
			sliderCombo.add(labels);
			sliderCombo.add(sld_change_macro);
			sliderCombo.setPreferredSize(new Dimension(330, 115));
			panel.add(sliderCombo, BorderLayout.NORTH);
		}
		else if(mode == PropertyType.MICRO_ORG){
			JPanel sliderCombo = new JPanel(new GridLayout(0, 1));
			JPanel labels = new JPanel(new FlowLayout(FlowLayout.LEFT));
			sliderCombo.add(comboBox);
			labels.add(lbl_change_micro);
			labels.add(lbl_change_micro_val);
			sliderCombo.add(labels);
			sliderCombo.add(sld_change_micro);
			sliderCombo.setPreferredSize(new Dimension(330, 115));
			panel.add(sliderCombo, BorderLayout.NORTH);
		}
		else
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
		btn_select_output.addActionListener(this);
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
		sld_change_macro.addChangeListener(this);
		sld_change_micro.addChangeListener(this);
		sld_opt_karplus_buf_dur.addChangeListener(this);
		sld_opt_karplus_buf_vel.addChangeListener(this);
		sld_opt_karplus_buf_spc.addChangeListener(this);
		sld_opt_karplus_thres_macro.addChangeListener(this);
		sld_opt_karplus_thres_micro.addChangeListener(this);
		sld_opt_karplus_thres_dur.addChangeListener(this);
		sld_opt_karplus_thres_vel.addChangeListener(this);
		sld_opt_karplus_thres_spc.addChangeListener(this);
		btn_opt_triangular_left_macro.addActionListener(this);
		btn_opt_triangular_left_micro.addActionListener(this);
		btn_opt_triangular_left_dur.addActionListener(this);
		btn_opt_triangular_left_vel.addActionListener(this);
		btn_opt_triangular_left_spc.addActionListener(this);
		btn_opt_triangular_norm_macro.addActionListener(this);
		btn_opt_triangular_norm_micro.addActionListener(this);
		btn_opt_triangular_norm_dur.addActionListener(this);
		btn_opt_triangular_norm_vel.addActionListener(this);
		btn_opt_triangular_norm_spc.addActionListener(this);
		btn_opt_triangular_right_macro.addActionListener(this);
		btn_opt_triangular_right_micro.addActionListener(this);
		btn_opt_triangular_right_dur.addActionListener(this);
		btn_opt_triangular_right_vel.addActionListener(this);
		btn_opt_triangular_right_spc.addActionListener(this);
		btn_markov_sel_file_macro.addActionListener(this);
		btn_markov_sel_file_micro.addActionListener(this);
		btn_markov_sel_file_dur.addActionListener(this);
		btn_markov_sel_file_vel.addActionListener(this);
		btn_markov_sel_file_spc.addActionListener(this);
		btn_clear_console.addActionListener(this);
		
		sld_change_macro.setPaintLabels(true);
		sld_change_macro.setPaintTicks(true);
		sld_change_macro.setMajorTickSpacing(16);
		sld_change_macro.setMinorTickSpacing(4);
		sld_change_micro.setPaintLabels(true);
		sld_change_micro.setPaintTicks(true);
		sld_change_micro.setMajorTickSpacing(4);
		sld_change_micro.setMinorTickSpacing(1);
		sld_tempo.setPaintTicks(true);
		sld_tempo.setPaintLabels(true);
		sld_tempo.setMajorTickSpacing(50);
		sld_tempo.setMinorTickSpacing(10);
		gui_console.setEditable(false);
		txt_max_length.setPreferredSize(new Dimension(50, 20));
		txt_max_length.setText("5");
		
		setupSlider(sld_opt_fnoise_macro, 0);
		setupSlider(sld_opt_fnoise_micro, 0);
		setupSlider(sld_opt_fnoise_dur, 0);
		setupSlider(sld_opt_fnoise_vel, 0);
		setupSlider(sld_opt_fnoise_spc, 0);
		setupSlider(sld_opt_karplus_buf_macro, 1);
		setupSlider(sld_opt_karplus_buf_micro, 1);
		setupSlider(sld_opt_karplus_buf_dur, 1);
		setupSlider(sld_opt_karplus_buf_vel, 1);
		setupSlider(sld_opt_karplus_buf_spc, 1);
		setupSlider(sld_opt_karplus_thres_macro, 2);
		setupSlider(sld_opt_karplus_thres_micro, 2);
		setupSlider(sld_opt_karplus_thres_dur, 2);
		setupSlider(sld_opt_karplus_thres_vel, 2);
		setupSlider(sld_opt_karplus_thres_spc, 2);
		
		btn_generate.setEnabled(toMap != null);
	}
	
	private void setupSlider(JSlider j, int opt){
		j.setPaintLabels(true);
		j.setPaintTicks(true);
		switch(opt){
			case 0:				j.setMajorTickSpacing(10);
								j.setMinorTickSpacing(3);
								break;
			case 1:				j.setMajorTickSpacing(5);
								j.setMinorTickSpacing(1);
								break;
			case 2:				j.setMajorTickSpacing(15);
								j.setMinorTickSpacing(5);
								break;
			default:			break;
		}
	}
	
	
	private JPanel assembleFNoiseCard(PropertyType mode){
		JPanel build = new JPanel(new GridLayout(0, 1));
		JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT));
		info.add(new JLabel(str_sub_opt_fnoise));
		build.add(info);
		switch(mode){
			case MACRO_ORG:		info.add(lbl_opt_fnoise_macro);
								build.add(sld_opt_fnoise_macro);
								break;
			case MICRO_ORG:		info.add(lbl_opt_fnoise_micro);
								build.add(sld_opt_fnoise_micro);
								break;
			case DURATION:		info.add(lbl_opt_fnoise_dur);
								build.add(sld_opt_fnoise_dur);
								break;
			case VELOCITY:		info.add(lbl_opt_fnoise_vel);
								build.add(sld_opt_fnoise_vel);
								break;
			case SPACING:		info.add(lbl_opt_fnoise_spc);
								build.add(sld_opt_fnoise_spc);
								break;
		}
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_fnoise));
		build.setPreferredSize(cardSize);
		return build;
	}
	
	private JPanel assembleKarplusCard(PropertyType mode){
		JPanel build = new JPanel(new GridLayout(0, 1));
		JPanel slider1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel slider2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		slider1.add(new JLabel(str_sub_opt_karplus_thres));
		slider2.add(new JLabel(str_sub_opt_karplus_buf));
		switch(mode){
			case MACRO_ORG:		slider1.add(lbl_opt_karplus_thres_macro);
								slider2.add(lbl_opt_karplus_buf_macro);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_macro);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_macro);
								break;
			case MICRO_ORG:		slider1.add(lbl_opt_karplus_thres_micro);
								slider2.add(lbl_opt_karplus_buf_micro);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_micro);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_micro);
								break;
			case DURATION:		slider1.add(lbl_opt_karplus_thres_dur);
								slider2.add(lbl_opt_karplus_buf_dur);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_dur);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_dur);
								break;
			case VELOCITY:		slider1.add(lbl_opt_karplus_thres_vel);
								slider2.add(lbl_opt_karplus_buf_vel);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_vel);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_vel);
								break;
			case SPACING:		slider1.add(lbl_opt_karplus_thres_spc);
								slider2.add(lbl_opt_karplus_buf_spc);
								build.add(slider1);
								build.add(sld_opt_karplus_thres_spc);
								build.add(slider2);
								build.add(sld_opt_karplus_buf_spc);
								break;
		}
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_karplus));
		build.setPreferredSize(cardSize);
		return build;
	}

	private JPanel assembleTriangularCard(PropertyType mode){
		JPanel build = new JPanel(new GridLayout(0, 2));
		switch(mode){
			case MACRO_ORG:		build.add(btn_opt_triangular_left_macro);
								build.add(new JLabel(str_sub_opt_triangular_left));
								build.add(btn_opt_triangular_norm_macro);
								build.add(new JLabel(str_sub_opt_triangular_norm));
								build.add(btn_opt_triangular_right_macro);
								build.add(new JLabel(str_sub_opt_triangular_right));
								break;
			case MICRO_ORG:		build.add(btn_opt_triangular_left_micro);
								build.add(new JLabel(str_sub_opt_triangular_left));
								build.add(btn_opt_triangular_norm_micro);
								build.add(new JLabel(str_sub_opt_triangular_norm));
								build.add(btn_opt_triangular_right_micro);
								build.add(new JLabel(str_sub_opt_triangular_right));
								break;
			case DURATION:		build.add(btn_opt_triangular_left_dur);
								build.add(new JLabel(str_sub_opt_triangular_left));
								build.add(btn_opt_triangular_norm_dur);
								build.add(new JLabel(str_sub_opt_triangular_norm));
								build.add(btn_opt_triangular_right_dur);
								build.add(new JLabel(str_sub_opt_triangular_right));
								break;
			case VELOCITY:		build.add(btn_opt_triangular_left_vel);
								build.add(new JLabel(str_sub_opt_triangular_left));
								build.add(btn_opt_triangular_norm_vel);
								build.add(new JLabel(str_sub_opt_triangular_norm));
								build.add(btn_opt_triangular_right_vel);
								build.add(new JLabel(str_sub_opt_triangular_right));
								break;
			case SPACING:		build.add(btn_opt_triangular_left_spc);
								build.add(new JLabel(str_sub_opt_triangular_left));
								build.add(btn_opt_triangular_norm_spc);
								build.add(new JLabel(str_sub_opt_triangular_norm));
								build.add(btn_opt_triangular_right_spc);
								build.add(new JLabel(str_sub_opt_triangular_right));
								break;
		}
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_triangular));
		build.setPreferredSize(cardSize);
		return build;
	}
	
	private JPanel assembleMarkovCard(PropertyType mode){
		JPanel build = new JPanel(new GridLayout(0, 1));
		JPanel labels = new JPanel(new GridLayout(1, 0));
		build.add(labels);
		switch(mode){
			case MACRO_ORG:		build.add(btn_markov_sel_file_macro);
								labels.add(lbl_opt_markov_sel_file_macro);
								labels.add(lbl_opt_markov_sel_file_name_macro);
								build.add(bx_opt_markov_bal_macro);
								build.add(bx_opt_markov_gen_macro);
								break;
			case MICRO_ORG:		build.add(btn_markov_sel_file_micro);
								labels.add(lbl_opt_markov_sel_file_micro);
								labels.add(lbl_opt_markov_sel_file_name_micro);
								build.add(bx_opt_markov_bal_micro);
								build.add(bx_opt_markov_gen_micro);
								break;
			case DURATION:		build.add(btn_markov_sel_file_dur);
								labels.add(lbl_opt_markov_sel_file_dur);
								labels.add(lbl_opt_markov_sel_file_name_dur);
								build.add(bx_opt_markov_bal_dur);
								build.add(bx_opt_markov_gen_dur);
								break;
			case VELOCITY:		build.add(btn_markov_sel_file_vel);
								labels.add(lbl_opt_markov_sel_file_vel);
								labels.add(lbl_opt_markov_sel_file_name_vel);
								build.add(bx_opt_markov_bal_vel);
								build.add(bx_opt_markov_gen_vel);
								break;
			case SPACING:		build.add(btn_markov_sel_file_spc);
								labels.add(lbl_opt_markov_sel_file_spc);
								labels.add(lbl_opt_markov_sel_file_name_spc);
								build.add(bx_opt_markov_bal_spc);
								build.add(bx_opt_markov_gen_spc);
								break;
		}
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_markov));
		build.setPreferredSize(cardSize);
		return build;
	}
	
	private JPanel assembleNoneCard(){
		JPanel build = new JPanel();
		build.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), str_opt_none));
		build.add(new JLabel("No Options Available"));
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
				lbl_selected_file_name.setText(f.getName());
				lbl_selected_dir_name.setText(str_nosel_dir);
			}
			btn_generate.setEnabled(toMap != null);
		}
		else if(e.getSource().equals(btn_select_dir)){
			File f = selectFile(JFileChooser.DIRECTORIES_ONLY);
			if(f != null){
				toMap = f;
				lbl_selected_file_name.setText(str_nosel_file);
				lbl_selected_dir_name.setText(f.getName());
			}
			btn_generate.setEnabled(toMap != null);
		}
		else if(e.getSource().equals(btn_select_mapper)){
			File f = selectFile(JFileChooser.FILES_ONLY);
			if(f != null){
				mappingScheme = f;
				lbl_selected_map_name.setText(f.getName());
			}
		}
		else if(e.getSource().equals(btn_select_output)){
			File f = selectFile(JFileChooser.FILES_ONLY);
			if(f != null){
				outputFile = f;
				lbl_selected_output_name.setText(f.getName());
			}
		}
		else if(e.getSource().equals(btn_markov_sel_file_macro)){
			File f = selectFile(JFileChooser.FILES_ONLY);
			if(f != null){
				markov_map_macro = f;
				lbl_opt_markov_sel_file_name_macro.setText(f.getName());
			}
		}
		else if(e.getSource().equals(btn_markov_sel_file_micro)){
			File f = selectFile(JFileChooser.FILES_ONLY);
			if(f != null){
				markov_map_micro = f;
				lbl_opt_markov_sel_file_name_micro.setText(f.getName());
			}
		}
		else if(e.getSource().equals(btn_markov_sel_file_dur)){
			File f = selectFile(JFileChooser.FILES_ONLY);
			if(f != null){
				markov_map_dur = f;
				lbl_opt_markov_sel_file_name_dur.setText(f.getName());
			}
		}
		else if(e.getSource().equals(btn_markov_sel_file_vel)){
			File f = selectFile(JFileChooser.FILES_ONLY);
			if(f != null){
				markov_map_vel = f;
				lbl_opt_markov_sel_file_name_vel.setText(f.getName());
			}
		}
		else if(e.getSource().equals(btn_markov_sel_file_spc)){
			File f = selectFile(JFileChooser.FILES_ONLY);
			if(f != null){
				markov_map_spc = f;
				lbl_opt_markov_sel_file_name_spc.setText(f.getName());
			}
		}
		else if(e.getSource().equals(btn_opt_triangular_left_macro))
			setSelectedButtonSet(btn_opt_triangular_left_macro, btn_opt_triangular_norm_macro, btn_opt_triangular_right_macro);
		else if(e.getSource().equals(btn_opt_triangular_norm_macro))
			setSelectedButtonSet(btn_opt_triangular_norm_macro, btn_opt_triangular_left_macro, btn_opt_triangular_right_macro);
		else if(e.getSource().equals(btn_opt_triangular_right_macro))
			setSelectedButtonSet(btn_opt_triangular_right_macro, btn_opt_triangular_left_macro, btn_opt_triangular_norm_macro);
		else if(e.getSource().equals(btn_opt_triangular_left_micro))
			setSelectedButtonSet(btn_opt_triangular_left_micro, btn_opt_triangular_norm_micro, btn_opt_triangular_right_micro);
		else if(e.getSource().equals(btn_opt_triangular_norm_micro))
			setSelectedButtonSet(btn_opt_triangular_norm_micro, btn_opt_triangular_left_micro, btn_opt_triangular_right_micro);
		else if(e.getSource().equals(btn_opt_triangular_right_micro))
			setSelectedButtonSet(btn_opt_triangular_right_micro, btn_opt_triangular_left_micro, btn_opt_triangular_norm_micro);
		else if(e.getSource().equals(btn_opt_triangular_left_dur))
			setSelectedButtonSet(btn_opt_triangular_left_dur, btn_opt_triangular_norm_dur, btn_opt_triangular_right_dur);
		else if(e.getSource().equals(btn_opt_triangular_norm_dur))
			setSelectedButtonSet(btn_opt_triangular_norm_dur, btn_opt_triangular_left_dur, btn_opt_triangular_right_dur);
		else if(e.getSource().equals(btn_opt_triangular_right_dur))
			setSelectedButtonSet(btn_opt_triangular_right_dur, btn_opt_triangular_left_dur, btn_opt_triangular_norm_dur);
		else if(e.getSource().equals(btn_opt_triangular_left_vel))
			setSelectedButtonSet(btn_opt_triangular_left_vel, btn_opt_triangular_norm_vel, btn_opt_triangular_right_vel);
		else if(e.getSource().equals(btn_opt_triangular_norm_vel))
			setSelectedButtonSet(btn_opt_triangular_norm_vel, btn_opt_triangular_left_vel, btn_opt_triangular_right_vel);
		else if(e.getSource().equals(btn_opt_triangular_right_vel))
			setSelectedButtonSet(btn_opt_triangular_right_vel, btn_opt_triangular_left_vel, btn_opt_triangular_norm_vel);
		else if(e.getSource().equals(btn_opt_triangular_left_spc))
			setSelectedButtonSet(btn_opt_triangular_left_spc, btn_opt_triangular_norm_spc, btn_opt_triangular_right_spc);
		else if(e.getSource().equals(btn_opt_triangular_norm_spc))
			setSelectedButtonSet(btn_opt_triangular_norm_spc, btn_opt_triangular_left_spc, btn_opt_triangular_right_spc);
		else if(e.getSource().equals(btn_opt_triangular_right_spc))
			setSelectedButtonSet(btn_opt_triangular_right_spc, btn_opt_triangular_left_spc, btn_opt_triangular_norm_spc);
		else if(e.getSource().equals(btn_generate) && btn_generate.isEnabled())
			runMapper();
		else if(e.getSource().equals(btn_clear_console))
			gui_console.setText("");
	}
	
	private void runMapper(){
		Settings s = new Settings();
		Settings.console = new PrintStream(new JTextOutputStream(gui_console));
		Settings.debug = bx_debug.isSelected();
		try{
			s.setFileToMap(toMap);
		} catch (Exception e){
			Settings.fail("UNABLE TO SET SOURCE FILE/FILES!");
		}
		s.mappingScheme = mappingScheme;
		if(outputFile != null)
			s.outputFile = outputFile;
		else
			s.outputFile = new File(str_nosel_output);
		s.setTempo(sld_tempo.getValue());
		float length = -1;
		try {
			length = Float.parseFloat(txt_max_length.getText());
		} catch (Exception e) {
			Settings.fail("UNABLE TO READ MAX LENGTH! USING DEFAULT: UNLIMITED LENGTH SET");
		}
		s.setMacroThreshold(sld_change_macro.getValue());
		s.setMicroThreshold(sld_change_micro.getValue());
		s.setLength(length);
		s.setMacroOrg(makeGenerator(combo_org_macro, PropertyType.MACRO_ORG));
		s.setMicroOrg(makeGenerator(combo_org_micro, PropertyType.MICRO_ORG));
		s.setDuration(makeGenerator(combo_dur, PropertyType.DURATION));
		s.setVelocity(makeGenerator(combo_vel, PropertyType.VELOCITY));
		s.setSpacing(makeGenerator(combo_spc, PropertyType.SPACING));
		Mapper m = new Mapper(s);
		m.importMappingScheme();
		m.mapSetFile();
		m.organize();
		m.writeToFile();
	}
	
	private Generator makeGenerator(JComboBox<String> box, PropertyType mode){
		switch((String) box.getSelectedItem()){
			case FNOISE:		return makeFNoiseGenerator(mode);
			case KARPLUS:		return makeKarplusGenerator(mode);
			case TRIANGULAR:	return makeTriangularGenerator(mode);
			case MARKOV:		return makeMarkovGenerator(mode);
			default: 			return null;
		}
	}
	
	private FNoise makeFNoiseGenerator(PropertyType mode){
		switch(mode){
			case MACRO_ORG:		return new FNoise(sld_opt_fnoise_macro.getValue(), mode);
			case MICRO_ORG:		return new FNoise(sld_opt_fnoise_micro.getValue(), mode);
			case DURATION:		return new FNoise(sld_opt_fnoise_dur.getValue(), mode);
			case VELOCITY:		return new FNoise(sld_opt_fnoise_vel.getValue(), mode);
			case SPACING:		return new FNoise(sld_opt_fnoise_spc.getValue(), mode);
			default:			return null;
		}
	}
	
	private KarplusStrong makeKarplusGenerator(PropertyType mode){
		switch(mode){
			case MACRO_ORG:		return new KarplusStrong(sld_opt_karplus_buf_macro.getValue(), sld_opt_karplus_thres_macro.getValue(), mode);
			case MICRO_ORG:		return new KarplusStrong(sld_opt_karplus_buf_micro.getValue(), sld_opt_karplus_thres_micro.getValue(), mode);
			case DURATION:		return new KarplusStrong(sld_opt_karplus_buf_dur.getValue(), sld_opt_karplus_thres_dur.getValue(), mode);
			case VELOCITY:		return new KarplusStrong(sld_opt_karplus_buf_vel.getValue(), sld_opt_karplus_thres_vel.getValue(), mode);
			case SPACING:		return new KarplusStrong(sld_opt_karplus_buf_spc.getValue(), sld_opt_karplus_thres_spc.getValue(), mode);
			default:			return null;
		}
	}
	
	private TriangularDist makeTriangularGenerator(PropertyType mode){
		DistributionScheme scheme = null;
		switch(mode){
			case MACRO_ORG:		if(btn_opt_triangular_left_macro.isSelected())
									scheme =  DistributionScheme.LEFT_SLANT;
								else if(this.btn_opt_triangular_norm_macro.isSelected())
									scheme =  DistributionScheme.NORMAL;
								else
									scheme =  DistributionScheme.RIGHT_SLANT;
								break;
	
			case MICRO_ORG:		if(btn_opt_triangular_left_micro.isSelected())
									scheme =  DistributionScheme.LEFT_SLANT;
								else if(this.btn_opt_triangular_norm_micro.isSelected())
									scheme =  DistributionScheme.NORMAL;
								else
									scheme =  DistributionScheme.RIGHT_SLANT;
								break;
	
			case DURATION:		if(btn_opt_triangular_left_dur.isSelected())
									scheme =  DistributionScheme.LEFT_SLANT;
								else if(this.btn_opt_triangular_norm_dur.isSelected())
									scheme =  DistributionScheme.NORMAL;
								else
									scheme =  DistributionScheme.RIGHT_SLANT;
								break;
			
			case VELOCITY:		if(btn_opt_triangular_left_vel.isSelected())
									scheme =  DistributionScheme.LEFT_SLANT;
								else if(this.btn_opt_triangular_norm_vel.isSelected())
									scheme =  DistributionScheme.NORMAL;
								else
									scheme =  DistributionScheme.RIGHT_SLANT;
								break;
	
			case SPACING:		if(btn_opt_triangular_left_macro.isSelected())
									scheme =  DistributionScheme.LEFT_SLANT;
								else if(this.btn_opt_triangular_norm_macro.isSelected())
									scheme =  DistributionScheme.NORMAL;
								else
									scheme =  DistributionScheme.RIGHT_SLANT;
								break;
		}
		return new TriangularDist(scheme, mode);
	}
	
	private Markov makeMarkovGenerator(PropertyType mode){
		switch(mode){
			case MACRO_ORG:		return new Markov(markov_map_macro, bx_opt_markov_gen_macro.isSelected(), bx_opt_markov_bal_macro.isSelected(), mode);
			case MICRO_ORG:		return new Markov(markov_map_micro, bx_opt_markov_gen_micro.isSelected(), bx_opt_markov_bal_micro.isSelected(), mode);
			case DURATION:		return new Markov(markov_map_dur, bx_opt_markov_gen_dur.isSelected(), bx_opt_markov_bal_dur.isSelected(), mode);
			case VELOCITY:		return new Markov(markov_map_vel, bx_opt_markov_gen_vel.isSelected(), bx_opt_markov_bal_vel.isSelected(), mode);
			case SPACING:		return new Markov(markov_map_spc, bx_opt_markov_gen_spc.isSelected(), bx_opt_markov_bal_spc.isSelected(), mode);
			default:			return null;
		}
	}
	
	private void setSelectedButtonSet(JRadioButton selected, JRadioButton unselected0, JRadioButton unselected1){
		selected.setSelected(true);
		unselected0.setSelected(false);
		unselected1.setSelected(false);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(sld_tempo))
			lbl_current_tempo.setText("" + sld_tempo.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_macro))
			lbl_opt_fnoise_macro.setText("" + sld_opt_fnoise_macro.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_micro))
			lbl_opt_fnoise_micro.setText("" + sld_opt_fnoise_micro.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_dur))
			lbl_opt_fnoise_dur.setText("" + sld_opt_fnoise_dur.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_vel))
			lbl_opt_fnoise_vel.setText("" + sld_opt_fnoise_vel.getValue());
		else if(e.getSource().equals(sld_opt_fnoise_spc))
			lbl_opt_fnoise_spc.setText("" + sld_opt_fnoise_spc.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_macro))
			lbl_opt_karplus_buf_macro.setText("" + sld_opt_karplus_buf_macro.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_micro))
			lbl_opt_karplus_buf_micro.setText("" + sld_opt_karplus_buf_micro.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_dur))
			lbl_opt_karplus_buf_dur.setText("" + sld_opt_karplus_buf_dur.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_vel))
			lbl_opt_karplus_buf_vel.setText("" + sld_opt_karplus_buf_vel.getValue());
		else if(e.getSource().equals(sld_opt_karplus_buf_spc))
			lbl_opt_karplus_buf_spc.setText("" + sld_opt_karplus_buf_spc.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_macro))
			lbl_opt_karplus_thres_macro.setText("" + sld_opt_karplus_thres_macro.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_micro))
			lbl_opt_karplus_thres_micro.setText("" + sld_opt_karplus_thres_micro.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_dur))
			lbl_opt_karplus_thres_dur.setText("" + sld_opt_karplus_thres_dur.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_vel))
			lbl_opt_karplus_thres_vel.setText("" + sld_opt_karplus_thres_vel.getValue());
		else if(e.getSource().equals(sld_opt_karplus_thres_spc))
			lbl_opt_karplus_thres_spc.setText("" + sld_opt_karplus_thres_spc.getValue());
		else if(e.getSource().equals(sld_change_macro))
			lbl_change_macro_val.setText("" + sld_change_macro.getValue());
		else if(e.getSource().equals(sld_change_micro))
			lbl_change_micro_val.setText("" + sld_change_micro.getValue());
	}

	
	public static void main(String[] args){
		@SuppressWarnings("unused")
		FrontEnd f = new FrontEnd();
	}
}
