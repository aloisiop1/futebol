package br.com.etecmam.cartolafc.controle;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.etecmam.cartolafc.apresentacao.JogadorUI;

public class CartolaFC {
	
	//comentário exemplo
	
	public static void main(String[] args)
			throws ClassNotFoundException, 
			InstantiationException, 
			IllegalAccessException, 
			UnsupportedLookAndFeelException {
			
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		
		///INSTANCIAR OBJETO
		JogadorUI jogadorUI = new JogadorUI();

		//DEFINIR COMO CENTRO DA TELA
		jogadorUI.setLocationRelativeTo(null);
		
		
		jogadorUI.popularGrid();

		//TORNAR FORMULÁRIO VISÍVEL
		jogadorUI.setVisible(true);		

				
		
	}

}


