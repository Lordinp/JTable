package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TabelaProdutosDAO {
    
    private static final String URL = "jdbc:h2:~/cadastro_produtos";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Conectar ao banco
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Criar tabela se não existir
    public void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS PRODUTO (" +
                     "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                     "NOME VARCHAR(100) NOT NULL, " +
                     "VALOR_UNITARIO FLOAT NOT NULL, " +
                     "QTD_ESTOQUE INT NOT NULL)";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INSERT - Cadastrar produto
    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO PRODUTO (NOME, VALOR_UNITARIO, QTD_ESTOQUE) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, produto.getNome());
            pstmt.setFloat(2, produto.getValorUnitario());
            pstmt.setInt(3, produto.getQtdEstoque());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // SELECT - Listar todos os produtos
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM PRODUTO ORDER BY ID";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("ID"));
                p.setNome(rs.getString("NOME"));
                p.setValorUnitario(rs.getFloat("VALOR_UNITARIO"));
                p.setQtdEstoque(rs.getInt("QTD_ESTOQUE"));
                produtos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return produtos;
    }

    // UPDATE - Atualizar produto
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE PRODUTO SET NOME = ?, VALOR_UNITARIO = ?, QTD_ESTOQUE = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, produto.getNome());
            pstmt.setFloat(2, produto.getValorUnitario());
            pstmt.setInt(3, produto.getQtdEstoque());
            pstmt.setInt(4, produto.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Remover produto
    public boolean deletar(int id) {
        String sql = "DELETE FROM PRODUTO WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}