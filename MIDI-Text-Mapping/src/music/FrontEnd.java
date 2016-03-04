package music;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class FrontEnd extends JFrame implements ActionListener {

	private static final long serialVersionUID = 4829661262223915297L;
	
	public static final int TEMPO_MAX = 30;
	public static final int TEMPO_MIN = 330;
	public static final int TEMPO_DEFAULT = 130;

	private String[] combo_Org_Strings = {"1/f Noise", "Triangular", "Markov", "None (Mapping Only)"};
	private String[] combo_Dur_Strings = {"1/f Noise", "Karplus/Strong", "Triangular", "Markov"};
	private String[] combo_Vel_Strings = {"1/f Noise", "Karplus/Strong", "Triangular", "Markov"};
	private String[] combo_Spc_Strings = {"1/f Noise", "Karplus/Strong", "Triangular", "Markov"};
	
	private JSlider sld_tempo = new JSlider(JSlider.HORIZONTAL, TEMPO_MIN, TEMPO_MAX, TEMPO_DEFAULT);
	
	private JComboBox<String> combo_Org_Macro = new JComboBox<String>(combo_Org_Strings);
	private JComboBox<String> combo_Org_Micro = new JComboBox<String>(combo_Org_Strings);
	private JComboBox<String> combo_Dur = new JComboBox<String>(combo_Dur_Strings);
	private JComboBox<String> combo_Vel = new JComboBox<String>(combo_Vel_Strings);
	private JComboBox<String> combo_Spc = new JComboBox<String>(combo_Spc_Strings);
	
	private JLabel lbl_Org = new JLabel("Compositional Method");
	private JLabel lbl_Dur = new JLabel("Note Duration Generation Method");
	private JLabel lbl_Vel = new JLabel("Note Velocity Generation Method");
	private JLabel lbl_Spc = new JLabel("Note Spacing Generation Method");
	
	private JButton btn_select_Mapper = new JButton("Selecting a Mapping Scheme");
	private JButton btn_select_File = new JButton("Choose a Source File");
	private JButton btn_select_Directory = new JButton("Choose a Source Folder");
	private JButton btn_Generate = new JButton("Generate!");
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
