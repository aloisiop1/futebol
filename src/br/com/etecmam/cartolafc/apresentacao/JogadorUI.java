package br.com.etecmam.cartolafc.apresentacao;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.SpinnerDateModel;

import br.com.etecmam.cartolafc.negocios.Jogador;
import br.com.etecmam.cartolafc.persistencia.CartolaDB;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class JogadorUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtCodigo;
	private JTextField txtNome;
	private JTextField txtIdade;
	private JTable tabela;
	
	
	private CartolaDB banco = CartolaDB.getInstancia();
	
	private String caminhoImagem;
	
	private String[] colunas = {"ID", "NOME", "POSIÇÃO", "DATA NASC.","IDADE"};
	private JSpinner dataNascimento;
	private JLabel lblImagem;
	private JTextField txtPosicao;
	
	
	private BufferedImage redimencionarImagem(BufferedImage imagemOriginal, int largura, int altura, int tipo) throws IOException {  
		
		BufferedImage resizedImage = new BufferedImage(largura, altura, tipo);  
		Graphics2D g = resizedImage.createGraphics();  
		g.drawImage(imagemOriginal, 0, 0, largura, altura, null);  
		g.dispose();
		
		return resizedImage;
		
	}  
		
	  
	   public String escolherImagens() throws NullPointerException {
		   
		   String imagem;


		   JFileChooser fc = new JFileChooser();
		   fc.setCurrentDirectory(new File(System.getProperty("user.home")));

		   FileFilter imageFilter = new FileNameExtensionFilter(
				   "Image files", ImageIO.getReaderFileSuffixes());

		   fc.addChoosableFileFilter( imageFilter );
		   fc.setAcceptAllFileFilterUsed(false);
		   fc.showOpenDialog(this);  

		   File file = fc.getSelectedFile();

		   
		   try {				   

			   imagem = file.getAbsolutePath();

		   } catch (NullPointerException e) {
			   imagem = null;
		   }


		   return imagem;  
	   }  
	  
	   
	public void limparCampos(){
		
		txtCodigo.setText("");
		txtNome.setText("");
		txtIdade.setText("");
		txtPosicao.setText("");
		
		dataNascimento.setValue(new Date());
		
		lblImagem.setIcon(null);
		
		
		//LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();		
		//Date dt = new Date(ZonedDateTime.now().toInstant().toEpochMilli());				
		
	}
	
	
	public  void popularGrid(){
			
		List<Jogador> jogadores = banco.getJogadores();
		
		String [][] dados = new String [ jogadores.size() ] [ 5 ];
		
		for(int i = 0; i < jogadores.size(); i++ ){
			
			dados[i][0] = String.valueOf( jogadores.get(i).getId() );
			dados[i][1] = String.valueOf( jogadores.get(i).getNome() );
			dados[i][2] = String.valueOf( jogadores.get(i).getPosicao() );			
			dados[i][3] = String.valueOf( jogadores.get(i).getDataNascimento().toString() );
			dados[i][4] = String.valueOf( jogadores.get(i).getIdade() );
			
		}			
		
		tabela.setModel(new DefaultTableModel(dados, colunas));
										
		
	}
	
	private void popularDados() {
		int linha = tabela.getSelectedRow();	
								
		String id = String.valueOf( tabela.getValueAt(linha, 0) );				
		Jogador jogador = banco.getJogador(Integer.valueOf(id));
		
		try {
			
			preencherCampos(jogador);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void preencherCampos(Jogador jogador) throws IOException {

		//DADOS ///////////////////////////////////////////////////////////////////////////////////////
		txtCodigo.setText( String.valueOf( jogador.getId() ) );
		txtNome.setText( String.valueOf( jogador.getNome() ) );
		txtPosicao.setText(jogador.getPosicao());
		
		
		//DATA /////////////////////////////////////////////////////////////////////////////////////////

		Date data = Date.from(jogador.getDataNascimento()
					.atStartOfDay(ZoneId.systemDefault())
					.toInstant());
		
		dataNascimento.setValue(data);
		
		
		//FOTO /////////////////////////////////////////////////////////////////////////////////////////
		byte[] fotoEmBytes = jogador.getImage();
						
		
		if(fotoEmBytes != null){										
			
			InputStream in = new ByteArrayInputStream(fotoEmBytes);
		
			BufferedImage imagemOriginal       = ImageIO.read(in);
			
			BufferedImage imagemRedimensionada = 
					redimencionarImagem(imagemOriginal, lblImagem.getWidth(), lblImagem.getHeight(), 1);

			ImageIcon imageIcon = new ImageIcon(imagemRedimensionada);

			lblImagem.setIcon(imageIcon);
			
		}else{
			lblImagem.setIcon(null);
		}
		
		
							
		
	}	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JogadorUI frame = new JogadorUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JogadorUI() {
		setTitle("CADASTRO DE JOGADORES");
		setIconImage(Toolkit.getDefaultToolkit().getImage(JogadorUI.class.getResource("/br/com/etecmam/cartolafc/images/Football-52.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 632, 433);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 182, 601, 148);
		contentPane.add(panel);
		panel.setLayout(null);
		
		txtCodigo = new JTextField();
		txtCodigo.setEditable(false);
		txtCodigo.setBounds(288, 11, 68, 20);
		panel.add(txtCodigo);
		txtCodigo.setColumns(10);
		
		JLabel lblId = new JLabel("C\u00F3digo");
		lblId.setBounds(230, 14, 55, 14);
		panel.add(lblId);
		
		txtNome = new JTextField();
		
		txtNome.setBounds(288, 42, 271, 20);
		panel.add(txtNome);
		txtNome.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(230, 45, 48, 14);
		panel.add(lblNome);
		
		dataNascimento = new JSpinner();
		dataNascimento.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				
				Date data = (Date) dataNascimento.getValue();				
				LocalDate dtn = data.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();		
				
				Jogador j = new Jogador();
				j.setDataNascimento(dtn);
				
				txtIdade.setText( String.valueOf( j.getIdade() ));				
			}
		});
		
		
		dataNascimento.setModel(new SpinnerDateModel(new Date(1463022000000L), null, null, Calendar.DAY_OF_YEAR));
		dataNascimento.setBounds(288, 99, 68, 20);
		panel.add(dataNascimento);
		
		JLabel lblDtNascimento = new JLabel("Dt. Nasc.");
		lblDtNascimento.setBounds(230, 102, 64, 14);
		panel.add(lblDtNascimento);
		
		txtIdade = new JTextField();
		txtIdade.setEditable(false);
		txtIdade.setBounds(491, 99, 68, 20);
		panel.add(txtIdade);
		txtIdade.setColumns(10);
		
		JLabel lblIdade = new JLabel("Idade");
		lblIdade.setBounds(433, 102, 48, 14);
		panel.add(lblIdade);
		
		JButton btnFoto = new JButton("");
		btnFoto.setIcon(new ImageIcon(JogadorUI.class.getResource("/br/com/etecmam/cartolafc/images/Search-22.png")));
		btnFoto.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent arg0) {
			
				
				caminhoImagem = escolherImagens();
					
			    System.out.println(caminhoImagem);
			    		    
			    /*
			    ImageIcon imageIcon = new ImageIcon(caminhoImagem);
			    
			    lblImagem.setIcon(imageIcon);
			    
			    //////////////////////////////////////////////////////////////////////////////
 			    
			    */
			    
		
			    try {
				
			    	
			    	BufferedImage imagemOriginal = ImageIO.read(new File(caminhoImagem));
			    	
			    	BufferedImage imagemRedimensionada = redimencionarImagem(imagemOriginal, lblImagem.getWidth(), lblImagem.getHeight(), 1);	
			    	
			    	ImageIcon imageIcon = new ImageIcon(imagemRedimensionada);
				    
				    lblImagem.setIcon(imageIcon);
				    
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			    
			    
			  //  super.paintComponent(g);
		 //       g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameter
			    
			   
							
				
			}
		});
		
		
		
		
		
		btnFoto.setBounds(148, 4, 40, 40);
		panel.add(btnFoto);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_2.setBounds(5, 5, 133, 140);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		lblImagem = new JLabel("");
		lblImagem.setBounds(3, 3, 127, 133);
		panel_2.add(lblImagem);
		lblImagem.setIcon(new ImageIcon(JogadorUI.class.getResource("/br/com/etecmam/cartolafc/images/Football-52.png")));
		lblImagem.setBackground(Color.BLACK);
		
		JLabel lblPosio = new JLabel("Posi\u00E7\u00E3o");
		lblPosio.setBounds(230, 74, 48, 14);
		panel.add(lblPosio);
		
		txtPosicao = new JTextField();
		txtPosicao.setColumns(10);
		txtPosicao.setBounds(288, 71, 271, 20);
		panel.add(txtPosicao);
		
		JScrollPane painelRolavel = new JScrollPane();
		painelRolavel.setBounds(10, 11, 601, 166);
		contentPane.add(painelRolavel);
		
		tabela = new JTable();
		tabela.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				popularDados();
			}
		});
		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {				
				popularDados();
			}
			
		});
		
		painelRolavel.setViewportView(tabela);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_1.setBounds(10, 335, 601, 57);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.setIcon(new ImageIcon(JogadorUI.class.getResource("/br/com/etecmam/cartolafc/images/Plus-26.png")));
		btnNovo.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				limparCampos();
				txtNome.requestFocus();

				
			}
		});
		btnNovo.setBounds(10, 11, 105, 35);
		panel_1.add(btnNovo);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setIcon(new ImageIcon(JogadorUI.class.getResource("/br/com/etecmam/cartolafc/images/Save-26.png")));
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Jogador jogador = new Jogador();
				
				// NOME, POSIÇÃO, DATA NASCIMENTO ///////////////////////////////////////////////
				jogador.setNome(txtNome.getText());
				jogador.setPosicao(txtPosicao.getText());
				
				Date data = (Date) dataNascimento.getValue();				
				LocalDate dtn = data.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
				jogador.setDataNascimento(dtn);				
				
				//FOTO /////////////////////////////////////////////////////////////////////////
				
				if(caminhoImagem != null){
				
					File arquivoDeImagem = new File(caminhoImagem);
			        byte[] arquivoEmBytes = new byte[(int) arquivoDeImagem.length()];
			        
			        try {
				        FileInputStream fileInputStream = new FileInputStream(arquivoDeImagem);
				        fileInputStream.read(arquivoEmBytes);
				        fileInputStream.close();
			        } catch (Exception e) {
				        e.printStackTrace();
			        }	        
			        		        
			        jogador.setImage(arquivoEmBytes);
					
				}
					
		        ////////////////////////////////////////////////////////////////////////////////
								
				if(txtCodigo.getText().equals("") ){
					banco.addJogador(jogador);					
					JOptionPane.showMessageDialog(JogadorUI.this, "JOGADOR ADICIONADO !!!" );										
					
				}else{					
					
					banco.updateJogador(Integer.valueOf(txtCodigo.getText()), jogador);
					JOptionPane.showMessageDialog(JogadorUI.this, "JOGADOR ATUALIZADO !!!" );
										
				}				
				
				txtIdade.setText( String.valueOf( jogador.getIdade() ) );
								
				popularGrid();								
				
			}
		});
		
		
		btnSalvar.setBounds(125, 11, 105, 35);
		panel_1.add(btnSalvar);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setIcon(new ImageIcon(JogadorUI.class.getResource("/br/com/etecmam/cartolafc/images/Minus-26.png")));
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if( ! txtCodigo.getText().equals("") ){
				
					int id = Integer.valueOf(txtCodigo.getText());
										
					int resp = JOptionPane.showConfirmDialog(JogadorUI.this, "EXCLUIR JOGADOR " + txtNome.getText());
					
					if(resp == 0){			
						
						banco.deleteJogador(id);
						
						popularGrid();
						
						if (tabela.getRowCount() > 0) {
							tabela.setRowSelectionInterval(0, 0);							
							popularDados();							
						}else{
							limparCampos();
						}						
												
						JOptionPane.showMessageDialog(JogadorUI.this, "JOGADOR EXCLUÍDO CON SUCESSO!!!");						
					}
					
				}								
				
			}
		});
		
		
		btnExcluir.setBounds(362, 11, 105, 35);
		panel_1.add(btnExcluir);
		
		JButton btnSair = new JButton("Sair");
		btnSair.setIcon(new ImageIcon(JogadorUI.class.getResource("/br/com/etecmam/cartolafc/images/Circled Right-26.png")));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {			
				System.exit(0);
			}
		});
		btnSair.setBounds(491, 11, 105, 35);
		panel_1.add(btnSair);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (tabela.getRowCount() > 0) {
					tabela.setRowSelectionInterval(0, 0);							
					popularDados();							
				}else{
					limparCampos();
				}	
				
			}
		});
		btnCancelar.setIcon(new ImageIcon(JogadorUI.class.getResource("/br/com/etecmam/cartolafc/images/Undo-26.png")));
		btnCancelar.setBounds(240, 11, 105, 35);
		panel_1.add(btnCancelar);
	}
}
