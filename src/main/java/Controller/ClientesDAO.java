package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Model.Clientes;
import Connection.ConnectionFactory;

public class ClientesDAO {

    // Conexão com o banco de dados
    private Connection connection;

    // Lista de clientes
    private List<Clientes> clientes;

    // Construtor que obtém uma conexão com o banco de dados
    public ClientesDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    // Método para criar a tabela no banco de dados se não existir
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes_lojacarros (NOME VARCHAR(255),TELEFONE VARCHAR(255),CIDADE VARCHAR(255),CPF VARCHAR(255) PRIMARY KEY)";
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela criada com sucesso.");
        } catch (SQLException e) {
            // Lança uma exceção em caso de erro na criação da tabela
            throw new RuntimeException("Erro ao criar a tabela: " + e.getMessage(), e);
        } finally {
            // Fecha a conexão
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    // Método para listar todos os clientes no banco de dados
    public List<Clientes> listAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        clientes = new ArrayList<>();

        try {
            stmt = connection.prepareStatement("SELECT * FROM clientes_lojacarros");
            rs = stmt.executeQuery();

            // Preenche a lista de clientes com os dados do banco de dados
            while (rs.next()) {
                Clientes cliente = new Clientes(
                    rs.getString("CPF"),
                    rs.getString("Nome"),
                    rs.getString("Telefone"),
                    rs.getString("Cidade"));
                clientes.add(cliente);
            }
        } catch (SQLException ex) {
            // Exibe uma mensagem de erro em caso de exceção SQL
            System.out.println(ex);
        } finally {
            // Fecha a conexão e os recursos do banco de dados
            ConnectionFactory.closeConnection(connection, stmt, rs);
        }
        // Retorna a lista de clientes
        return clientes;
    }

    // Método para cadastrar um novo cliente no banco de dados
    public void cadastrar(String cpf, String nome, String telefone, String cidade) {
        PreparedStatement stmt = null;

        String sql = "INSERT INTO clientes_lojacarros (cpf, nome, telefone, cidade) VALUES (?, ?, ?, ?)";
        try {
            // Executa a inserção no banco de dados
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.setString(3, telefone);
            stmt.setString(4, cidade);
            stmt.executeUpdate();
            System.out.println("Dados inseridos");
            JOptionPane.showMessageDialog(null, "Cliente cadastrado");
        } catch (SQLException e) {
            // Trata exceções específicas (por exemplo, violação de chave única) e exibe mensagens adequadas
            if (e.getSQLState().equals("23505")) {
                JOptionPane.showMessageDialog(null, "Erro: O CPF inserido já existe.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao inserir dados: " + e.getMessage());
            }
        } finally {
            // Fecha a conexão
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    // Método para atualizar dados de um cliente no banco de dados
    public void update(String nome, String telefone, String cidade, String cpf) {
        PreparedStatement stmt = null;

        String sql = "UPDATE clientes_lojacarros SET nome = ?, telefone = ?, cidade = ? WHERE cpf = ?";
        try {
            // Executa a atualização no banco de dados
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, cidade);
            stmt.setString(4, cpf);
            stmt.executeUpdate();
            System.out.println("Dados atualizados");
        } catch (SQLException e) {
            // Lança uma exceção em caso de erro na atualização
            throw new RuntimeException("Erro ao atualizar dados.", e);
        } finally {
            // Fecha a conexão
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    // Método para apagar um cliente do banco de dados
    public void delete(String cpf) {
        PreparedStatement stmt = null;

        String sql = "DELETE FROM clientes_lojacarros WHERE cpf = ?";
        try {
            // Executa a exclusão no banco de dados
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cpf);
            stmt.executeUpdate();
            System.out.println("Dado apagado.");
        } catch (SQLException e) {
            // Lança uma exceção em caso de erro na exclusão
            throw new RuntimeException("Erro ao apagar dados.", e);
        } finally {
            // Fecha a conexão
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }
}
