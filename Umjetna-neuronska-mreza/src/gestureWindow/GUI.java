package gestureWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import ANN.NeuralNetwork;
import Matrix.Matrix;



public class GUI extends JFrame {
	private List<Point> currentGesture = new ArrayList<>();
	private String state="";
	private List<String> finalGestures = new ArrayList<>();
	private List<JRadioButton> buttons = new ArrayList<>();
	private NeuralNetwork ANN;
	
	private JLabel f1;
	private JLabel f2;
	private JLabel f3;
	private JLabel f4;
	private JLabel f5;
	private JLabel label;
	String[] labels = new String[] {"alpha", "beta", "gamma", "delta", "epsilon"};
	
	public GUI() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500,400);
		initGUI();
	}
	
	public GUI(NeuralNetwork ANN) {
		this.ANN = ANN;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(400,400);
		initGUI();
	}

	private void initGUI() {		
		Container cp = getContentPane();
		cp.setLayout(null);
		cp.setLayout(new BorderLayout(5,5));
		
		Canvas canvas = new Canvas();
		cp.add(canvas, BorderLayout.CENTER);
		canvas.requestFocusInWindow();
		
		JToolBar toolBar = new JToolBar();
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentGesture.clear();
				canvas.repaint();
				f1.setText("0.0000");
				f2.setText("0.0000");
				f3.setText("0.0000");
				f4.setText("0.0000");
				f5.setText("0.0000");
				label.setText("");
			}
		});
		toolBar.add(clear);
		
		JButton saveGestureBtn = new JButton("Save gesture");
		saveGestureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.repaint();
				state =  getCurrentState(buttons);
				List<double[]> gesture = processGesture(currentGesture);
				String finalGesture = createStringFromGesture(gesture, state);
				finalGestures.add(finalGesture);
				currentGesture.clear();
				System.out.println(finalGestures.size());
			}
		});
		toolBar.add(saveGestureBtn);
		
		JButton saveToFile = new JButton("Save to file");
		saveToFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(GUI.this) != JFileChooser.APPROVE_OPTION) {
				  return;
				}
				String fileName = fileChooser.getSelectedFile().getPath();
				
				try {
					FileWriter fw = new FileWriter(new File(fileName));
					for(String s: finalGestures) {
						fw.write(s + "\n");
					}
					fw.flush();
					fw.close();
					JOptionPane.showMessageDialog(GUI.this, "Save successful", "INFO", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(IOException exc){
					JOptionPane.showMessageDialog(GUI.this, "Save failed" + exc, "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		toolBar.add(saveToFile);
		
		JButton loadANN = new JButton("Load ANN");
		loadANN.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(GUI.this) != JFileChooser.APPROVE_OPTION) {
				  return;
				}
				String fileName = fileChooser.getSelectedFile().getPath();
				ANN = new NeuralNetwork(fileName);
			}
		});
		toolBar.add(loadANN);
		
		
		JButton recognize = new JButton("Recognize");
		recognize.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<double[]> gesture = processGesture(currentGesture);
				double[] recognitionGesture = getFeatures(gesture);
				Matrix output = ANN.predict(recognitionGesture);
				setLabels(output);
			}

			
		});
		toolBar.add(recognize);
		
		ButtonGroup G = new ButtonGroup();
		JRadioButton alfa = new JRadioButton("alpha", true);
		JRadioButton beta = new JRadioButton("beta", false);
		JRadioButton gama = new JRadioButton("gamma", false);
		JRadioButton delta = new JRadioButton("delta", false);
		JRadioButton epsilon = new JRadioButton("epsilon", false);
		G.add(alfa);
		G.add(beta);
		G.add(gama);
		G.add(delta);
		G.add(epsilon);
		
		buttons.add(alfa);
		buttons.add(beta);
		buttons.add(gama);
		buttons.add(delta);
		buttons.add(epsilon);
		
		JPanel group = new JPanel();
		group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
		group.add(alfa);
		group.add(beta);
		group.add(gama);
		group.add(delta);
		group.add(epsilon);
		cp.add(group, BorderLayout.WEST);
		
		
		JPanel bottom = new JPanel();
		bottom.setPreferredSize(new Dimension(70,200));
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		
		f1 = new JLabel("0.0000");
		bottom.add(f1);
		f2 = new JLabel("0.0000");
		bottom.add(f2);
		f3 = new JLabel("0.0000");
		bottom.add(f3);
		f4 = new JLabel("0.0000");
		bottom.add(f4);
		f5 = new JLabel("0.0000");
		bottom.add(f5);
		JLabel clf = new JLabel("Klasa:");
		bottom.add(clf);
		label = new JLabel("None");
		bottom.add(label);
		
		cp.add(bottom, BorderLayout.EAST);
		
		cp.add(toolBar, BorderLayout.NORTH);

	}
	
	
	private void setLabels(Matrix output) {
		String s = String.format("%.4f", output.getData(0, 0));
		f1.setText(s);
		s = String.format("%.4f", output.getData(1, 0));
		f2.setText(s);
		s = String.format("%.4f", output.getData(2, 0));
		f3.setText(s);
		s = String.format("%.4f", output.getData(3, 0));
		f4.setText(s);
		s = String.format("%.4f", output.getData(4, 0));
		f5.setText(s);
		label.setText(getClass(output));
	}
	
	private String getClass(Matrix M) {
		int index=0; 
		for(int i=1; i<5; i++) {
			if(M.getData(i, 0) > M.getData(index, 0))
				index = i;
		}
		return labels[index];
	}

	private List<double []> processGesture(List<Point> currentGesture) {
		int sumaX = 0;
		int sumaY = 0;
		for(Point p: currentGesture) {
			sumaX += p.x;
			sumaY += p.y;
		}
		double avgX = sumaX / currentGesture.size();
		double avgY = sumaY / currentGesture.size();
		
		List<double[]> points = new ArrayList<>();
		for(Point p: currentGesture) {
			points.add(new double[] {p.x-avgX, p.y-avgY});
		}
		
		double minX = points.get(0)[0];
		double minY = points.get(0)[1];
		double maxX = points.get(0)[0];
		double maxY = points.get(0)[1];
		
		for(double[] p: points) {
			if(p[0] > maxX)
				maxX = p[0];
			else if(p[0] < minX)
				minX = p[0];
			
			if(p[1] > maxY)
				maxY = p[1];
			else if(p[1] < minY)
				minY = p[1];
		}
		
		double distX = maxX - minX;
		double distY = maxY - minY;
		double max = Math.max(distX, distY);
		
		for(int i=0; i<points.size(); i++) {
			double[] p = points.get(i);
			points.set(i, new double[] {p[0]/max, p[1]/max});
		}
		
		double D = 0;
		List<Double> distances = new ArrayList<>();
		
		for(int i=0; i<points.size()-1; i++) {
			double[] p1 = points.get(i);
			double[] p2 = points.get(i+1);
			double d = Math.sqrt(Math.pow(p1[0]-p2[0], 2) + Math.pow(p1[1]-p2[1], 2));
			D += d;
			distances.add(D);
		}
		
		List<double []> finalPoints = new ArrayList<>();
		finalPoints.add(points.get(0));
		
		int M = 30;
		for(int k=1; k<M; k++) {
			double d = k*D/(M-1);
			
			int position = 1;
			for(int i=1; i<distances.size(); i++) {
				if(d < distances.get(i)) {
					position = i;
					break;
				}
				else
					position = i;
			}
			
			double[] p1 = points.get(position-1);
			double[] p2 = points.get(position);
			double[] point = new double[] {(p1[1]+p2[1])/2 ,(p1[0]+p2[0])/2};
			finalPoints.add(point);
		}
		return finalPoints;
	}
	
	private String createStringFromGesture(List<double[]> gesture, String state) {
		StringBuilder sb = new StringBuilder();
		for(double[] p: gesture)
			sb.append(p[0]+", "+p[1]+", ");
		sb.append(state);
		return sb.toString();
	}
	
	
	private double[] getFeatures(List<double[]> gesture) {
		double[] example = new double[2*30];
		int i=0;
		for(double[] point: gesture) {
			example[i] = point[0];
			example[i+1] = point[1];
			i+=2;
		}
		return example;
	}
	
	
	private String getCurrentState(List<JRadioButton> buttons) {
		for(JRadioButton jb: buttons) {
			if(jb.isSelected()) {
				String text = jb.getText();
				if(text.equals("alpha"))
					return "1,0,0,0,0";
				else if(text.equals("beta"))
					return "0,1,0,0,0";
				else if(text.equals("gamma"))
					return "0,0,1,0,0";
				else if(text.equals("delta"))
					return "0,0,0,1,0";
				else if(text.equals("epsilon"))
					return "0,0,0,0,1";
			}
		}
		return null;
	}
	
	
	
	
	public class Canvas extends JComponent{
		
		public Canvas() {
			setFocusable(true);
			requestFocusInWindow();
			registerMouseListeners();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			
			g2d.setColor(Color.BLUE);
			
			for(int i=0; i<currentGesture.size()-2;i++) {
				g2d.drawLine(currentGesture.get(i).x, currentGesture.get(i).y,
						currentGesture.get(i+1).x, currentGesture.get(i+1).y);
			}
		}

		private void registerMouseListeners() {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					currentGesture.clear();
				}
				
				@Override
				public void mouseReleased(MouseEvent e) {
					
				}
			});
			
			addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					currentGesture.add(e.getPoint());
					repaint();
				}
			});
		}
	}
}
