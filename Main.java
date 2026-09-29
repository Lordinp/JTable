import view.TelaCadastroProdutos;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            TelaCadastroProdutos tela = new TelaCadastroProdutos();
            tela.setVisible(true);
        });
    }
}