package paint2D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.json.simple.parser.ParseException;

public class GUI extends JFrame {

	JPanel Buttons, DrawingArea;
	JButton Line, Rect, Square, Triangle, Circ, Ellipse, RightTriangle, Delete, move, Resize, loadTriangle, Colors,
			undo, redo, saveXML, saveJson, loadXML, loadJson;
	private final ArrayList<Shape> selectedShape = new ArrayList<>();
	private Stack<ArrayList<Shape>> memory = new Stack();
	private Stack<ArrayList<Shape>> Undone = new Stack();
	private Constructor<?> triangleConstructor;
	private int x1, x2, x3, y1, y2, Functions = 1, indexOfSelectedElement;
	private int newCornerX, newMaxX, dX = 0, dY = 0, newCornerY, newMaxY;
	private int cornerX, cornerY, maxX, maxY;
	private boolean flag = false;
	private boolean selectedFlag = false;
	private boolean firstFlag = false;
	private String kind;
	private Color c = Color.black;
	private Color selsect = Color.BLUE , Temp;
	private int deltaX, dXTriangle, dYTriangle, deltaY;

	public GUI() {

		setTitle("My Painter");
		setSize(1650, 735);
		setVisible(true);
		setBackground(Color.white);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		DrawingArea = new JPanel();
		DrawingArea.setBackground(Color.white);
		DrawingArea.addMouseListener(Mouse);
		DrawingArea.addMouseMotionListener(MouseMotion);
		add(DrawingArea);

		Buttons = new JPanel();
		Buttons.setPreferredSize(new Dimension(150, 150));
		Buttons.setBackground(Color.black);

		Line = createButtons(Line, "Line", 1);
		Rect = createButtons(Rect, "Rect", 2);
		Square = createButtons(Square, "Square", 3);
		Triangle = createButtons(Triangle, "Tri", 4);
		Circ = createButtons(Circ, "Circle", 5);
		Ellipse = createButtons(Ellipse, "Ellipse", 6);
		RightTriangle = createButtons(RightTriangle, "RTri", 7);
		move = createButtons(move, "move", 8);
		Resize = createButtons(Resize, "Resize", 9);
		loadTriangle = createButtons(loadTriangle, "LoadTri", 10);
		Delete = createButtons(Delete, "delete", 11);
		Colors = createButtons(Colors, "Color", 12);
		undo = createButtons(undo, "undo", 13);
		redo = createButtons(redo, "redo", 14);
		saveXML = createButtons(saveXML, "SXML", 15);
		saveJson = createButtons(saveJson, "SJson", 16);
		loadXML = createButtons(loadXML, "LXML", 17);
		loadJson = createButtons(loadJson, "Ljson", 18);
		add(Buttons, BorderLayout.WEST);
		validate();
	}

	private JButton createButtons(JButton button, String value, final int iterator) {

		button = new JButton(value);
		button.setPreferredSize(new Dimension(60, 60));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Functions = iterator;
				if (Functions == 10) {
					classLoader();
				} else if (Functions == 11) {
					delete();
				} else if (Functions == 12) {
					c = JColorChooser.showDialog(null, "choose your color", c);
				} else if (Functions == 13) {
					undo();
				} else if (Functions == 14) {
					redo();
				} else if (Functions == 15) {
					SaveXML();
					Functions = 0;
				} else if (Functions == 16) {
					try {
						SaveJson();
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					Functions = 0;
				}
			}
		});
		Buttons.add(button);
		return button;
	}

	private void classLoader() {
		final int length = 6;
		JFileChooser chooseFile = new JFileChooser();
		chooseFile.setDialogTitle("Class Loader");
		chooseFile.setApproveButtonText("choose");
		chooseFile.setAcceptAllFileFilterUsed(false);
		chooseFile.addChoosableFileFilter(new FileNameExtensionFilter("CLASS (*.class)", "class"));
		if (chooseFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File choosedFile = chooseFile.getSelectedFile();
			try {
				String className = choosedFile.getName();
				className = className.substring(0, className.length() - length);
				String path = choosedFile.getAbsolutePath();
				path = path.replace("\\SecondProject", "");
				File directoryFile = new File(path);
				URL url = directoryFile.toURI().toURL();
				URL[] urls = new URL[] { url };
				@SuppressWarnings("resource")
				ClassLoader load = new URLClassLoader(urls);
				Class<?> loadedClass = load.loadClass("SecondProject." + className);
				if (Shape.class.isAssignableFrom(loadedClass)) {
					triangleConstructor = loadedClass.getConstructors()[0];
					Triangle.setVisible(true);
					RightTriangle.setVisible(true);
				} else {

					System.out.println("Error");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	MouseListener Mouse = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			x1 = x2 = x3 = e.getX() + 100;
			y1 = y2 = e.getY() + 40;
			selectedFlag = false;
			selection();
			if (Functions == 8 || Functions == 9 && selectedFlag == true) {
				if ((!memory.empty() ? memory.peek().get(indexOfSelectedElement).contain(x1, y1) : false)) {
					if (!firstFlag) {
						ArrayList<Shape> temp = new ArrayList<>();
						for (int i = 0; i < (!memory.empty() ? memory.peek().size() : 0); i++) {
							temp.add(memory.peek().get(i));
						}
						Temp = temp.get(indexOfSelectedElement).Getcolor();
						temp.remove(indexOfSelectedElement);
						memory.push(temp);
					}
				} else {
					Functions = 0;
				}
			}
			if (selectedFlag) {
				repaint();
			}
		}

		public void mouseReleased(MouseEvent e) {

			Shape shape;
			x2 = e.getX() + 100;
			y2 = e.getY() + 40;

			if (x2 > x1) {
				x3 = x1 - (x2 - x1);
			} else if (x2 < x1) {
				x3 = x1 + (x1 - x2);
			} else {
				x3 = x1;
			}

			flag = true;
			if (Functions == 9 || Functions == 8) {
				selection();
				if (selectedShape.size() > 0) {
					selectedShape.clear();
				}
				if (Functions == 9) {
					if (!kind.equals("Triangle")) {
						shape = checkAndAdd(kind, cornerX, cornerY, x2, y2);
						shape.color(Temp);
					} else {
						shape = checkAndAdd(kind, dXTriangle, dYTriangle, x2, y2);
						shape.color(Temp);
					}
				} else {
					shape = checkAndAdd(kind, newCornerX, newCornerY, newMaxX, newMaxY);
					shape.color(Temp);
				}
				memory.peek().add(shape);
				indexOfSelectedElement = memory.peek().size() - 1;
				selectedFlag = false;
				Functions = 0;
			}
			repaint();

			firstFlag = false;

		}

	};
	MouseMotionListener MouseMotion = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent e) {

			x2 = e.getX() + 100;
			y2 = e.getY() + 40;

			if (x2 > x1) {
				x3 = x1 - (x2 - x1);
			} else if (x2 < x1) {
				x3 = x1 + (x1 - x2);
			} else {
				x3 = x1;
			}

			if ((Functions == 9 || Functions == 8)) {
				selection();
				firstFlag = true;
			}
			repaint();
		}

	};

	private void selection() {
		selectedShape.clear();
		Shape temp;
		if (selectedFlag) {
			if (Functions == 9) {
				newCornerX = Math.min(cornerX, x2);
				newMaxX = Math.max(cornerX, x2);
				if (kind.equals("Square") || kind.equals("Circle")) {
					if (y2 > cornerY) {
						y2 = cornerY + (newMaxX - newCornerX);
					} else {
						y2 = cornerY - (newMaxX - newCornerX);
					}
				}
				makeSelectedShape(cornerX, cornerY, x2, y2);
			} else if (Functions == 8) {
				newCornerX = x2 - dX;
				newCornerY = y2 - dY;
				newMaxX = newCornerX + maxX - cornerX;
				newMaxY = newCornerY + maxY - cornerY;
				makeSelectedShape(newCornerX, newCornerY, newMaxX, newMaxY);
			}

		} else {
			for (int i = (!memory.empty() ? memory.peek().size() - 1 : -1); i > -1; i--) {
				temp = memory.peek().get(i);
				if (temp.contain(x2, y2)) {
					indexOfSelectedElement = i;
					i = -1;
					cornerX = temp.getcornerX();
					cornerY = temp.getCornerY();
					maxX = temp.getMaxX();
					maxY = temp.getMaxY();
					dXTriangle = temp.GetX1();
					dYTriangle = temp.GetY1();
					deltaX = dXTriangle - cornerX;
					deltaY = dYTriangle - cornerY;
					dX = x1 - cornerX;
					dY = y1 - cornerY;
					kind = temp.getType();
					makeSelectedShape(cornerX, cornerY, maxX, maxY);
					selectedFlag = true;
				} else {
					selectedFlag = false;
				}
			}
		}
	}

	private void delete() {
		if (selectedFlag) {
			selectedShape.clear();
			ArrayList<Shape> temp = new ArrayList<>();
			for (int i = 0; i < (!memory.empty() ? memory.peek().size() : 0); i++) {
				temp.add(memory.peek().get(i));
			}
			temp.remove(indexOfSelectedElement);
			memory.push(temp);
		}
		selectedFlag = false;
	}

	private void makeSelectedShape(int cornerX, int cornerY, int maxX, int maxY) {
		Shape temp;
		int centerX, centerY;
		if (kind.equals("Triangle") && Functions == 9) {
			if (maxX > dXTriangle) {
				cornerX = dXTriangle - (maxX - dXTriangle);
			} else {
				cornerX = dXTriangle + (dXTriangle - maxX);
			}
		}
		centerX = ((maxX - cornerX) / 2) + cornerX;
		centerY = ((maxY - cornerY) / 2) + cornerY;
		Shape rec1 = new Rectangle(cornerX - 5, cornerY - 5, cornerX + 5, cornerY + 5);
		Shape rec2 = new Rectangle(maxX - 5, maxY - 5, maxX + 5, maxY + 5);
		Shape rec3 = new Rectangle(cornerX - 5, maxY - 5, cornerX + 5, maxY + 5);
		Shape rec4 = new Rectangle(maxX - 5, cornerY - 5, maxX + 5, cornerY + 5);
		Shape rec5 = new Rectangle(centerX - 5, centerY - 5, centerX + 5, centerY + 5);
		Shape rec6 = new Rectangle(cornerX, cornerY, maxX, maxY);
		selectedShape.add(rec1);
		selectedShape.add(rec2);
		selectedShape.add(rec3);
		selectedShape.add(rec4);
		selectedShape.add(rec5);
		selectedShape.add(rec6);
		if (kind.equals("Triangle") || kind.equals("RTriangle") && Functions == 9) {
			temp = checkAndAdd(kind, dXTriangle, dYTriangle, maxX, maxY);
		} else {
			temp = checkAndAdd(kind, cornerX, cornerY, maxX, maxY);
		}
		selectedShape.add(temp);

	}

	private Shape checkAndAdd(String type, int firstX, int firstY, int secondX, int secondY) {
		Shape shape = null;
		int newX1, height, width, newX2, newX3, newY1, newY2, newY3;
		int thirdX = 0;
		if (type.equals("Rectangle")) {
			shape = new Rectangle(firstX, firstY, secondX, secondY);
		}else if(type.equals("Line")){
			shape = new Line(firstX, firstY, secondX, secondY);
		}else if (type.equals("Square")) {
			shape = new Square(firstX, firstY, secondX, secondY);
		} else if (type.equals("Ellipse")) {
			shape = new Ellipse(firstX, firstY, secondX, secondY);
		} else if (type.equals("Circle")) {
			shape = new Circle(firstX, firstY, secondX, secondY);
		} else if (type.equals("RTriangle")) {
			try {
				shape = (Shape) triangleConstructor.newInstance(firstX, firstY, secondX, secondY, firstX, secondY);
			} catch (Exception e) {
				System.out.println("Error");
			}
		} else if (type.equals("Triangle")) {
			if (Functions == 9) {
				if (secondX > firstX) {
					thirdX = secondX - 2 * (secondX - firstX);
				} else {
					thirdX = secondX + 2 * (firstX - secondX);
				}
				try {
					shape = (Shape) triangleConstructor.newInstance(firstX, firstY, secondX, secondY, thirdX, secondY);
				} catch (Exception e) {
					System.out.println("Error");
				}
			} else if (Functions == 8) {

				newX1 = firstX + deltaX;
				newX2 =secondX;
				newX3 = firstX;
				newY1 = firstY + deltaY;
				if (cornerY==dYTriangle) {
					newY2 = secondY;
				} else {
					newY2 = firstY;
				}
				newY3 = newY2;

				try {
					shape = (Shape) triangleConstructor.newInstance(newX1, newY1, newX2, newY2, newX3, newY3);
				} catch (Exception e) {
					System.out.println("Error");
				}
			}

		}
		return shape;
	}

	public void paint(Graphics s) {
		super.paint(s);
		Shape temp;

		if (Functions == 1 && selectedFlag == false) {
			selectedShape.clear();
			Shape line = new Line(x1, y1, x2, y2);
			register(line, s);
		} else if (Functions == 2 && selectedFlag == false) {
			selectedShape.clear();
			Shape rectangle = new Rectangle(x1, y1, x2, y2);
			register(rectangle, s);
		} else if (Functions == 3 && selectedFlag == false) {
			selectedShape.clear();
			Shape square = new Square(x1, y1, x2, y2);
			register(square, s);
		} else if (Functions == 4 && selectedFlag == false) {
			selectedShape.clear();
			Shape triangle = null;
			try {
				triangle = (Shape) triangleConstructor.newInstance(x1, y1, x2, y2, x3, y2);
				register(triangle, s);
			} catch (Exception e) {
				System.out.println("Error");
			}
		} else if (Functions == 5 && selectedFlag == false) {
			selectedShape.clear();
			Shape circle = new Circle(x1, y1, x2, y2);
			register(circle, s);
		} else if (Functions == 6 && selectedFlag == false) {
			selectedShape.clear();
			Shape ellipse = new Ellipse(x1, y1, x2, y2);
			register(ellipse, s);
		} else if (Functions == 7 && selectedFlag == false) {
			selectedShape.clear();
			Shape triangle = null;
			try {
				triangle = (Shape) triangleConstructor.newInstance(x1, y1, x2, y2, x1, y2);
				register(triangle, s);
			} catch (Exception e) {
				System.out.println("Error");
			}
		} else if (Functions == 17) {
			LoadXML();
			Functions = 0;
		} else if (Functions == 18) {
			try {
				LoadJson();
				Functions = 0;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (!memory.isEmpty()) {
			for (Shape counter : memory.peek()) {
				s.setColor(counter.Getcolor());
				counter.paint((Graphics2D) s);
			}
			if (selectedShape.size() > 0) {
				for (Shape counter2 : selectedShape) {
					s.setColor(selsect);
					counter2.paint((Graphics2D) s);
				}
			}
		}
	}

	public void register(Shape shape, Graphics s) {
		shape.color(c);
		s.setColor(c);
		shape.paint((Graphics2D) s);
		if (flag == true) {
			ArrayList<Shape> temp = new ArrayList<>();
			for (int i = 0; i < (!memory.empty() ? memory.peek().size() : 0); i++) {
				temp.add(memory.peek().get(i));
			}
			temp.add(shape);
			memory.push(temp);
			Undone.clear();
			flag = false;
		}
	}

	private void undo() {
		if (!memory.isEmpty()) {
			Undone.push(memory.pop());
		}
		Functions = 0;
	}

	private void redo() {
		if (!Undone.isEmpty()) {
			memory.push(Undone.pop());
		}
		Functions = 0;
	}

	public void SaveXML() {

		JFileChooser chooser = new JFileChooser();
		Document XML = new Document();
		Element Array = new Element("ArrayList");
		XML.setRootElement(Array);
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			for (int i = 0; i < memory.peek().size(); i++) {
				Element shape = new Element("Shape");
				Element type = new Element("Type");
				type.addContent(memory.peek().get(i).getType());
				Element X1 = new Element("X1");
				X1.addContent(String.valueOf(memory.peek().get(i).GetX1()));
				Element Y1 = new Element("Y1");
				Y1.addContent(String.valueOf(memory.peek().get(i).GetY1()));
				Element X2 = new Element("X2");
				X2.addContent(String.valueOf(memory.peek().get(i).GetX2()));
				Element Y2 = new Element("Y2");
				Y2.addContent(String.valueOf(memory.peek().get(i).GetY2()));
				Element color = new Element("Color");
				color.addContent(Integer.toString(memory.peek().get(i).Getcolor().getRGB()));
				if (memory.peek().get(i).getType() == "Triangle") {
					Element X3 = new Element("X3");
					X3.addContent(String.valueOf(memory.peek().get(i).GetX3()));
					Element Y3 = new Element("Y3");
					Y3.addContent(String.valueOf(memory.peek().get(i).GetY3()));
					shape.addContent(X3);
					shape.addContent(Y3);
				}

				shape.addContent(type);
				shape.addContent(X1);
				shape.addContent(Y1);
				shape.addContent(X2);
				shape.addContent(Y2);
				shape.addContent(color);
				Array.addContent(shape);

			}
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			try {
				xmlOutput.output(XML, new FileWriter(chooser.getSelectedFile()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void LoadXML() {
		JFileChooser chooser = new JFileChooser();
		SAXBuilder builder = new SAXBuilder();
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File f = new File(chooser.getSelectedFile().getPath());
			try {
				Document document = (Document) builder.build(f);
				Element root = document.getRootElement();
				ArrayList<Shape> l = new ArrayList<>();
				List shapes = root.getChildren("Shape");
				for (int i = 0; i < shapes.size(); i++) {
					Element shape = (Element) shapes.get(i);
					switch (shape.getChildText("Type")) {
					case "Line":
						Line n = new Line(Integer.parseInt(shape.getChildText("X1")),
								Integer.parseInt(shape.getChildText("Y1")), Integer.parseInt(shape.getChildText("X2")),
								Integer.parseInt(shape.getChildText("Y2")));
						Color c = new Color(Integer.parseInt(shape.getChildText("Color")));
						n.color(c);
						l.add(n);
						break;
					case "Rectangle":
						Rectangle m = new Rectangle(Integer.parseInt(shape.getChildText("X1")),
								Integer.parseInt(shape.getChildText("Y1")), Integer.parseInt(shape.getChildText("X2")),
								Integer.parseInt(shape.getChildText("Y2")));
						Color a = new Color(Integer.parseInt(shape.getChildText("Color")));
						m.color(a);
						l.add(m);
						break;
					case "Square":
						Square p = new Square(Integer.parseInt(shape.getChildText("X1")),
								Integer.parseInt(shape.getChildText("Y1")), Integer.parseInt(shape.getChildText("X2")),
								Integer.parseInt(shape.getChildText("Y2")));
						Color b = new Color(Integer.parseInt(shape.getChildText("Color")));
						p.color(b);
						l.add(p);
						break;
					case "Ellipse":
						Ellipse q = new Ellipse(Integer.parseInt(shape.getChildText("X1")),
								Integer.parseInt(shape.getChildText("Y1")), Integer.parseInt(shape.getChildText("X2")),
								Integer.parseInt(shape.getChildText("Y2")));
						Color d = new Color(Integer.parseInt(shape.getChildText("Color")));
						q.color(d);
						l.add(q);
						break;
					case "Circle":
						Circle r = new Circle(Integer.parseInt(shape.getChildText("X1")),
								Integer.parseInt(shape.getChildText("Y1")), Integer.parseInt(shape.getChildText("X2")),
								Integer.parseInt(shape.getChildText("Y2")));
						Color e = new Color(Integer.parseInt(shape.getChildText("Color")));
						r.color(e);
						l.add(r);
						break;
					case "Triangle":
						Triangle s = new Triangle(Integer.parseInt(shape.getChildText("X1")),
								Integer.parseInt(shape.getChildText("Y1")), Integer.parseInt(shape.getChildText("X2")),
								Integer.parseInt(shape.getChildText("Y2")), Integer.parseInt(shape.getChildText("X3")),
								Integer.parseInt(shape.getChildText("Y3")));
						Color g = new Color(Integer.parseInt(shape.getChildText("Color")));
						s.color(g);
						l.add(s);
						break;
					}
					memory.push(l);
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void SaveJson() throws JSONException {
		JFileChooser choose = new JFileChooser();
		if (choose.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter File = new FileWriter(choose.getSelectedFile());
				JSONObject obj = new JSONObject();
				JSONArray list = new JSONArray();
				for (int i = 0; i < memory.peek().size(); i++) {
					JSONObject shape = new JSONObject();
					shape.put("Type", memory.peek().get(i).getType());
					shape.put("X1", Integer.toString(memory.peek().get(i).GetX1()));
					shape.put("Y1", Integer.toString(memory.peek().get(i).GetY1()));
					shape.put("X2", Integer.toString(memory.peek().get(i).GetX2()));
					shape.put("Y2", Integer.toString(memory.peek().get(i).GetY2()));
					if (memory.peek().get(i).getType() == "Triangle") {
						shape.put("X3", Integer.toString(memory.peek().get(i).GetX3()));
						shape.put("Y3", Integer.toString(memory.peek().get(i).GetY3()));
					}
					shape.put("Color", Integer.toString(memory.peek().get(i).Getcolor().getRGB()));
					list.add(shape);
				}
				obj.put("Shapes", list);
				File.write(obj.toString());
				File.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void LoadJson() throws JSONException {
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			JSONParser parser = new JSONParser();
			try {
				JSONObject obj = (JSONObject) parser.parse(new FileReader(chooser.getSelectedFile().getPath()));
				JSONArray shapes = (JSONArray) obj.get("Shapes");
				ArrayList<Shape> list = new ArrayList<>();
				for (int i = 0; i < shapes.size(); i++) {
					JSONObject Shape = (JSONObject) shapes.get(i);
					switch ((String) Shape.get("Type")) {
					case "Line":
						Line a = new Line(Integer.valueOf((String) Shape.get("X1")),
								Integer.valueOf((String) Shape.get("Y1")), Integer.valueOf((String) Shape.get("X2")),
								Integer.valueOf((String) Shape.get("Y2")));
						Color z = new Color(Integer.valueOf((String) Shape.get("Color")));
						a.color(z);
						list.add(a);
						break;
					case "Rectangle":
						Rectangle b = new Rectangle(Integer.valueOf((String) Shape.get("X1")),
								Integer.valueOf((String) Shape.get("Y1")), Integer.valueOf((String) Shape.get("X2")),
								Integer.valueOf((String) Shape.get("Y2")));
						Color y = new Color(Integer.valueOf((String) Shape.get("Color")));
						b.color(y);
						list.add(b);
						break;
					case "Square":
						Square c = new Square(Integer.valueOf((String) Shape.get("X1")),
								Integer.valueOf((String) Shape.get("Y1")), Integer.valueOf((String) Shape.get("X2")),
								Integer.valueOf((String) Shape.get("Y2")));
						Color x = new Color(Integer.valueOf((String) Shape.get("Color")));
						c.color(x);
						list.add(c);
						break;
					case "Ellipse":
						Ellipse d = new Ellipse(Integer.valueOf((String) Shape.get("X1")),
								Integer.valueOf((String) Shape.get("Y1")), Integer.valueOf((String) Shape.get("X2")),
								Integer.valueOf((String) Shape.get("Y2")));
						Color w = new Color(Integer.valueOf((String) Shape.get("Color")));
						d.color(w);
						list.add(d);
						break;
					case "Circle":
						Circle e = new Circle(Integer.valueOf((String) Shape.get("X1")),
								Integer.valueOf((String) Shape.get("Y1")), Integer.valueOf((String) Shape.get("X2")),
								Integer.valueOf((String) Shape.get("Y2")));
						Color v = new Color(Integer.valueOf((String) Shape.get("Color")));
						e.color(v);
						list.add(e);
						break;
					case "Triangle":
						Triangle f = new Triangle(Integer.valueOf((String) Shape.get("X1")),
								Integer.valueOf((String) Shape.get("Y1")), Integer.valueOf((String) Shape.get("X2")),
								Integer.valueOf((String) Shape.get("Y2")), Integer.valueOf((String) Shape.get("X3")),
								Integer.valueOf((String) Shape.get("Y3")));
						Color u = new Color(Integer.valueOf((String) Shape.get("Color")));
						f.color(u);
						list.add(f);
					}
				}
				memory.push(list);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		GUI t = new GUI();
	}

}
