package es.studium.PruebaFTP;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import javax.swing.JLabel;
import java.awt.SystemColor;

public class clienteFTPBasico extends JFrame 
{
	private static final long serialVersionUID = 1L;

	//Labels
	private final JLabel lblServidor = new JLabel("Servidor FTP :");
	private final JLabel lblUsuario = new JLabel("Usuario :");
	private final JLabel lblDirectorioRaiz = new JLabel("Directorio Raiz :");
	private final JLabel lblDirectorioActual = new JLabel("Directorio Actual :");
	private final JLabel lblFicheroSeleccionado = new JLabel("Fichero Seleccionado :");
	private final JLabel lblFicheros = new JLabel("Ficheros:");
	private final JLabel lblDirectorios = new JLabel("Directorios:");
	private final JLabel lblDatosDeInteres = new JLabel("Datos de  Inter\u00E9s :");

	//TexFields
	static JTextField txtServidor = new JTextField();
	static JTextField txtUsuario = new JTextField();
	static JTextField txtDirectorioRaiz = new JTextField();
	private static JTextField txtFicheroSeleccionado = new JTextField();
	private static JTextField txtDirectorioActual = new JTextField();

	// Botones
	JButton botonSubirFichero = new JButton("Subir fichero");
	JButton botonDescargarFichero = new JButton("Descargar fichero");
	JButton botonEliminarFichero = new JButton("Eliminar fichero");
	JButton botonRenombrarFichero = new JButton("Renombrar Fichero");
	JButton botonCrearDirectorio = new JButton("Crear Directorio");
	JButton botonEliminarDirectorio = new JButton("Eliminar Directorio");
	JButton botonRenombrarDirectorio = new JButton("Renombrar Directorio");
	JButton botonSalir = new JButton("Salir");


	// Lista para los datos del directorio
	static JList<String> ListaStringArchivos = new JList<String>();


	// Contenedor
	private final Container contenedor = getContentPane();


	//Datos del servidor FTP - Servidor local
	static FTPClient cliente = new FTPClient();// cliente FTP
	String servidor = "127.0.0.1";
	String user = "Ale";
	String pasw = "studium2020;";
	boolean login;
	static String direcInicial = "/";

	//Para saber el directorio y fichero seleccionado
	static String direcSelec = direcInicial;
	static String ficheroSelec = "";


	public static void main(String[] args) throws IOException 
	{
		new clienteFTPBasico();
	} // final del main

	public clienteFTPBasico() throws IOException
	{
		super("CLIENTE BÁSICO FTP");
		
		//MODELO
		
		//Para ver los comandos que se originan
		cliente.addProtocolCommandListener(new PrintCommandListener(new PrintWriter (System.out)));
		cliente.connect(servidor);

		//Conexión al servidor
		cliente.enterLocalPassiveMode();
		login = cliente.login(user, pasw);

		//Se establece el directorio de trabajo actual
		cliente.changeWorkingDirectory(direcInicial);

		//Obteniendo ficheros y directorios del directorio actual
		FTPFile[] files = cliente.listFiles();
		llenarLista(files,direcInicial);
		
		
		//VISTA
		
		//Contruyendo txt
		txtFicheroSeleccionado.setBounds(364, 263, 183, 19);
		txtServidor.setBounds(364, 68, 183, 19);
		txtServidor.setText("127.0.0.1");
		txtUsuario.setBounds(364, 113, 183, 19);
		txtUsuario.setText("Ale");
		txtDirectorioRaiz.setBounds(364, 161, 183, 19);
		txtDirectorioRaiz.setText("/");
		txtDirectorioActual.setBounds(364, 213, 183, 19);
		getContentPane().setLayout(null);
		contenedor.add(txtServidor);
		contenedor.add(txtUsuario);
		contenedor.add(txtDirectorioRaiz);
		contenedor.add(txtFicheroSeleccionado);
		contenedor.add(txtDirectorioActual);


		//Contruyendo Botones
		botonSubirFichero.setBounds(17, 463, 150, 21);
		contenedor.add(botonSubirFichero);
		botonCrearDirectorio.setBounds(364, 342, 183, 21);
		contenedor.add(botonCrearDirectorio);
		botonEliminarDirectorio.setBounds(364, 404, 183, 21);
		contenedor.add(botonEliminarDirectorio);
		botonDescargarFichero.setBounds(182, 463, 148, 21);
		contenedor.add(botonDescargarFichero);
		botonEliminarFichero.setBounds(182, 494, 148, 21);
		contenedor.add(botonEliminarFichero);
		botonSalir.setBounds(430, 520, 99, 21);
		contenedor.add(botonSalir);
		botonRenombrarFichero.setBounds(17, 494, 150, 21);
		contenedor.add(botonRenombrarFichero);
		botonRenombrarDirectorio.setBounds(364, 373, 183, 21);
		contenedor.add(botonRenombrarDirectorio);

		//Construyendo Labels
		lblFicheros.setForeground(SystemColor.textHighlight);
		lblFicheros.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFicheros.setBounds(19, 434, 127, 19);
		contenedor.add(lblFicheros);

		lblDirectorios.setForeground(SystemColor.textHighlight);
		lblDirectorios.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDirectorios.setBounds(364, 313, 127, 19);
		contenedor.add(lblDirectorios);

		lblDatosDeInteres.setForeground(SystemColor.textHighlight);
		lblDatosDeInteres.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDatosDeInteres.setBounds(364, 24, 127, 19);
		contenedor.add(lblDatosDeInteres);

		lblServidor.setBounds(364, 45, 183, 13);
		contenedor.add(lblServidor);

		lblUsuario.setBounds(364, 97, 183, 13);
		contenedor.add(lblUsuario);

		lblDirectorioRaiz.setBounds(364, 138, 183, 13);
		contenedor.add(lblDirectorioRaiz);

		lblDirectorioActual.setBounds(364, 190, 183, 13);
		contenedor.add(lblDirectorioActual);

		lblFicheroSeleccionado.setBounds(364, 240, 183, 13);
		contenedor.add(lblFicheroSeleccionado);

		//Preparación de la lista
		//Se configura el tipo de selección para que solo se pueda
		//Seleccionar un elemento de la lista
		ListaStringArchivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//Barra de desplazamiento para la lista
		JScrollPane barraDesplazamiento = new JScrollPane(ListaStringArchivos);
		barraDesplazamiento.setPreferredSize(new Dimension(335,420));
		barraDesplazamiento.setBounds(new Rectangle(19, 5, 335, 420));
		contenedor.add(barraDesplazamiento);

		//Atributos del Frame.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(571,605);
		setVisible(true);
		setResizable(false);

		//Acciones al pulsar en la lista o en los botones
		ListaStringArchivos.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 ) {
					
				    if(ListaStringArchivos.getSelectedValue().toString().contains("(DIR)"))
				    {
				    	//String directorio = direcSelec;
				    	//directorio = directorio + "/" + ListaStringArchivos.getSelectedValue().toString();
				    	direcSelec=ListaStringArchivos.getSelectedValue().toString().trim();//directorio;
				    	try {
				    		//Directorio de trabajo actual
							cliente.changeWorkingDirectory(direcSelec);
							FTPFile[] ff2 = null;
							//Obtener ficheros del directorio actual
							ff2 = cliente.listFiles();
							//Llenar la lista
							llenarLista(ff2, direcSelec);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				    	finally {
				    		
				    	}
				    }
				  }

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}



		});

		ListaStringArchivos.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent lse)
			{
				// TODO Auto-generated method stub
				String fic = "";
				if (lse.getValueIsAdjusting()) 
				{
					ficheroSelec ="";
					//Elemento que se ha seleccionado de la lista
					fic =ListaStringArchivos.getSelectedValue().toString();

					//Se trata de un fichero
					ficheroSelec = fic;
					txtFicheroSeleccionado.setText( ficheroSelec);
					

					//Nos quedamos con el nocmbre
					txtDirectorioActual.setText(direcSelec);
				}
			}
		});

		botonSalir.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					cliente.disconnect();
				}
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});

		botonCrearDirectorio.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String nombreCarpeta = JOptionPane.showInputDialog(null, "Introduce el nombre del directorio","carpeta");
				if (!(nombreCarpeta==null)) 
				{
					String directorio = direcSelec;
					if (!direcSelec.equals("/"))
						directorio = directorio + "/";
					//Nombre del directorio a crear
					directorio += nombreCarpeta.trim(); 
					//Quita blancos a derecha y a izquierda
					try 
					{
						if (cliente.makeDirectory(directorio))
						{
							String m = nombreCarpeta.trim()+ " => Se ha creado correctamente ...";
							JOptionPane.showMessageDialog(null, m);
							txtFicheroSeleccionado.setText(m);
							//Directorio de trabajo actual
							cliente.changeWorkingDirectory(direcSelec);
							FTPFile[] ff2 = null;
							//Obtener ficheros del directorio actual
							ff2 = cliente.listFiles();
							//Llenar la lista
							llenarLista(ff2, direcSelec);
						}
						else
							JOptionPane.showMessageDialog(null, nombreCarpeta.trim() + " => No se ha podido crear ...");
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				} // Final del if
			}
		}); // final del botón CreaDir

		botonEliminarDirectorio.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String nombreCarpeta = JOptionPane.showInputDialog(null,"Introduce el nombre del directorio a eliminar","carpeta");
				if (!(nombreCarpeta==null)) 
				{
					String directorio = direcSelec;
					if (!direcSelec.equals("/"))
						directorio = directorio + "/";
					//nombre del directorio a eliminar
					directorio += nombreCarpeta.trim(); //quita blancos a derecha y a izquierda
					try 
					{
						if(cliente.removeDirectory(directorio)) 
						{
							String m = nombreCarpeta.trim()+" => Se ha eliminado correctamente ...";
							JOptionPane.showMessageDialog(null, m);
							txtFicheroSeleccionado.setText(m);
							//directorio de trabajo actual
							cliente.changeWorkingDirectory(direcSelec);
							FTPFile[] ff2 = null;
							//obtener ficheros del directorio actual
							ff2 = cliente.listFiles();
							//llenar la lista
							llenarLista(ff2, direcSelec);
						}
						else
							JOptionPane.showMessageDialog(null, nombreCarpeta.trim() + " => No se ha podido eliminar ...");
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				} 
				// final del if
			}
		});
		//final del botón Eliminar Carpeta

		botonRenombrarDirectorio.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{


			}
		});

		botonSubirFichero.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser f;
				File file;
				f = new JFileChooser();
				//Solo se pueden seleccionar ficheros
				f.setFileSelectionMode(JFileChooser.FILES_ONLY);
				//Título de la ventana
				f.setDialogTitle("Selecciona el fichero a subir al servidor FTP");
				//Se muestra la ventana
				int returnVal = f.showDialog(f, "Cargar");
				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
					//Fichero seleccionado
					file = f.getSelectedFile();
					//Nombre completo del fichero
					String archivo = file.getAbsolutePath();
					//Solo nombre del fichero
					String nombreArchivo = file.getName();
					try 
					{
						SubirFichero(archivo, nombreArchivo);
					}
					catch (IOException e1) 
					{
						e1.printStackTrace(); 
					}
				}
			}
		}); //Fin botón subir

		botonDescargarFichero.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String directorio = direcSelec;
				if (!direcSelec.equals("/"))
					directorio = directorio + "/";
				if (!direcSelec.equals("")) 
				{
					DescargarFichero(directorio + ficheroSelec, ficheroSelec);
				}
			}
		}); // Fin botón descargar

		botonEliminarFichero.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String directorio = direcSelec;
				if (!direcSelec.equals("/"))
					directorio = directorio + "/";
				if (!direcSelec.equals("")) 
				{
					BorrarFichero(directorio + ficheroSelec,ficheroSelec);
				}
			}
		});

		botonRenombrarFichero.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

			}
		});
	} // fin constructor

	private static void llenarLista(FTPFile[] files,String direc2) 
	{
		if (files == null)
			return;
		//se crea un objeto DefaultListModel
		DefaultListModel<String> modeloLista = new DefaultListModel<String>();
		modeloLista = new DefaultListModel<String>();
		//se definen propiedades para la lista, color y tipo de fuente

		ListaStringArchivos.setForeground(Color.blue);
		Font fuente = new Font("Courier", Font.PLAIN, 12);
		ListaStringArchivos.setFont(fuente);
		//se eliminan los elementos de la lista
		ListaStringArchivos.removeAll();
		try 
		{
			//se establece el directorio de trabajo actual
			cliente.changeWorkingDirectory(direc2);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		direcSelec = direc2; //directorio actual
		//se añade el directorio de trabajo al listmodel, primerelementomodeloLista.addElement(direc2);
		//se recorre el array con los ficheros y directorios
		for (int i = 0; i < files.length; i++) 
		{
			if (!(files[i].getName()).equals(".") && !(files[i].getName()).equals("..")) 
			{
				//nos saltamos los directorios . y ..
				//Se obtiene el nombre del fichero o directorio
				String f = files[i].getName();
				//Si es directorio se añade al nombre (DIR)
				if (files[i].isDirectory()) f = "(DIR) " + f;
				//se añade el nombre del fichero o directorio al listmodel
				modeloLista.addElement(f);
			}//fin if
		}//fin for
		try 
		{
			//se asigna el listmodel al JList,
			//se muestra en pantalla la lista de ficheros y direc
			ListaStringArchivos.setModel(modeloLista);
		}
		catch (NullPointerException n) 
		{
			; //Se produce al cambiar de directorio
		}
	}//Fin llenarLista

	private boolean SubirFichero(String archivo, String soloNombre) throws IOException 
	{
		cliente.setFileType(FTP.BINARY_FILE_TYPE);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(archivo));
		boolean ok = false;
		//directorio de trabajo actual
		cliente.changeWorkingDirectory(direcSelec);
		if (cliente.storeFile(soloNombre, in)) 
		{
			String s = " " + soloNombre + " => Subido correctamente...";
			txtFicheroSeleccionado.setText(s);
			txtDirectorioActual.setText("Se va a actualizar el árbol de directorios...");
			JOptionPane.showMessageDialog(null, s);
			FTPFile[] ff2 = null;
			//obtener ficheros del directorio actual
			ff2 = cliente.listFiles();
			//llenar la lista con los ficheros del directorio actual
			llenarLista(ff2,direcSelec);
			ok = true;
		}
		else
			txtFicheroSeleccionado.setText("No se ha podido subir... " + soloNombre);
		return ok;
	}// final de SubirFichero

	private void DescargarFichero(String NombreCompleto, String nombreFichero) 
	{
		File file;
		String archivoyCarpetaDestino = "";
		String carpetaDestino = "";
		JFileChooser f = new JFileChooser();
		//solo se pueden seleccionar directorios
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//título de la ventana
		f.setDialogTitle("Selecciona el Directorio donde Descargar el Fichero");
		int returnVal = f.showDialog(null, "Descargar");
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			file = f.getSelectedFile();
			//obtener carpeta de destino
			carpetaDestino = (file.getAbsolutePath()).toString();
			//construimos el nombre completo que se creará en nuestro disco
			archivoyCarpetaDestino = carpetaDestino + File.separator + nombreFichero;
			try 
			{
				cliente.setFileType(FTP.BINARY_FILE_TYPE);
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(archivoyCarpetaDestino));
				if (cliente.retrieveFile(NombreCompleto, out))
					JOptionPane.showMessageDialog(null,	nombreFichero + " => Se ha descargado correctamente ...");
				else
					JOptionPane.showMessageDialog(null,	nombreFichero + " => No se ha podido descargar ...");
				out.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	} // Final de DescargarFichero

	private void BorrarFichero(String NombreCompleto, String nombreFichero) 
	{
		//pide confirmación
		int seleccion = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el fichero seleccionado?");
		if (seleccion == JOptionPane.OK_OPTION) 
		{
			try 
			{
				if (cliente.deleteFile(NombreCompleto)) 
				{
					String m = nombreFichero + " => Eliminado correctamente... ";
					JOptionPane.showMessageDialog(null, m);
					txtFicheroSeleccionado.setText(m);
					//directorio de trabajo actual
					cliente.changeWorkingDirectory(direcSelec);
					FTPFile[] ff2 = null;
					//obtener ficheros del directorio actual
					ff2 = cliente.listFiles();
					//llenar la lista con los ficheros del directorio actual
					llenarLista(ff2, direcSelec);
				}
				else
					JOptionPane.showMessageDialog(null, nombreFichero + " => No se ha podido eliminar ...");
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}// Final de BorrarFichero
}// Final de la clase ClienteFTPBasico