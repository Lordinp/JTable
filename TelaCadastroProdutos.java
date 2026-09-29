package view;

import model.Produto;
import model.TabelaProdutosDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCadastroProdutos extends JFrame {
    
    private JTextField txtId, txtNome, txtValorUnitario, txtQtdEstoque;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private TabelaProdutosDAO dao;
    
    // Botões
    private JButton btnNovo, btnSalvar, btnAlterar, btnExcluir, btnLimpar;

    public TelaCadastroProdutos() {
        dao = new TabelaProdutosDAO();
        dao.criarTabelaSeNaoExistir();
        
        inicializarComponentes();
        configurarLayout();
        carregarTabela();
    }

    private void inicializarComponentes() {
        setTitle("Cadastro de Produtos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Campos de texto
        txtId = new JTextField(15);
        txtId.setEditable(false); // ID gerado automaticamente
        
        txtNome = new JTextField(20);
        txtValorUnitario = new JTextField(10);
        txtQtdEstoque = new JTextField(10);
        
        // Tabela
        String[] colunas = {"ID", "Nome", "Valor Unitário", "Qtde Estoque"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela somente leitura
            }
        };
        tabelaProdutos = new JTable(modeloTabela);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Botões
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");
        
        // Ações dos botões
        btnNovo.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> salvarProduto());
        btnAlterar.addActionListener(e -> alterarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel superior - Formulário
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Linha 1: ID e Nome
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtId, gbc);
        
        gbc.gridx = 2;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        painelFormulario.add(txtNome, gbc);
        
        // Linha 2: Valor Unitário e Quantidade
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painelFormulario.add(new JLabel("Valor Unitário (R$):"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtValorUnitario, gbc);
        
        gbc.gridx = 2;
        painelFormulario.add(new JLabel("Qtde Estoque:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(txtQtdEstoque, gbc);
        
        // Painel central - Tabela
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Produtos"));
        
        // Painel inferior - Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);
        
        // Adicionar painéis ao frame
        add(painelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0); // Limpa a tabela
        List<Produto> produtos = dao.listarTodos();
        
        for (Produto p : produtos) {
            Object[] linha = {
                p.getId(),
                p.getNome(),
                String.format("R$ %.2f", p.getValorUnitario()),
                p.getQtdEstoque()
            };
            modeloTabela.addRow(linha);
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtValorUnitario.setText("");
        txtQtdEstoque.setText("");
        txtNome.requestFocus();
    }

    private Produto obterProdutoDosCampos() {
        try {
            Produto p = new Produto();
            
            if (!txtId.getText().isEmpty()) {
                p.setId(Integer.parseInt(txtId.getText()));
            }
            
            p.setNome(txtNome.getText().trim());
            p.setValorUnitario(Float.parseFloat(txtValorUnitario.getText().replace(",", ".")));
            p.setQtdEstoque(Integer.parseInt(txtQtdEstoque.getText()));
            
            return p;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro: Verifique os valores numéricos!", 
                "Erro de Validação", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void salvarProduto() {
        Produto produto = obterProdutoDosCampos();
        if (produto == null) return;
        
        if (produto.getNome().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Preencha o campo Nome!", 
                "Validação", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (dao.inserir(produto)) {
            JOptionPane.showMessageDialog(this, 
                "Produto cadastrado com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarTabela();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erro ao cadastrar produto!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um produto na tabela para alterar!", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Produto produto = obterProdutoDosCampos();
        if (produto == null) return;
        
        if (dao.atualizar(produto)) {
            JOptionPane.showMessageDialog(this, 
                "Produto atualizado com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarTabela();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar produto!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um produto na tabela para excluir!", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        String nome = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Deseja realmente excluir o produto \"" + nome + "\"?",
            "Confirmação de Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            if (dao.deletar(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Produto excluído com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir produto!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}