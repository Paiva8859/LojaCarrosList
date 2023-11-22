package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Connection.ConnectionFactory;
import Model.Carros;

public class CarrosDAO {

    // Conexão com o banco de dados
    private Connection connection;

    // Lista de carros
    private List<Carros> carros;

    // Construtor que obtém uma conexão com o banco de dados
    public CarrosDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    // Método para criar a tabela no banco de dados se não existir
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS carros_lojacarros (MARCA VARCHAR(255),MODELO VARCHAR(255),ANO VARCHAR(255),PLACA VARCHAR(255) PRIMARY KEY, VALOR VARCHAR(255))";
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

    // Método para listar todos os carros no banco de dados
    public List<Carros> listAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        carros = new ArrayList<>();

        try {
            stmt = connection.prepareStatement("SELECT * FROM carros_lojacarros");
            rs = stmt.executeQuery();

            // Preenche a lista de carros com os dados do banco de dados
            while (rs.next()) {
                Carros carro = new Carros(
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("ano"),
                    rs.getString("placa"),
                    rs.getString("valor")
                );
                carros.add(carro);
            }
        } catch (SQLException ex) {
            // Exibe uma mensagem de erro em caso de exceção SQL
            System.out.println(ex);
        } finally {
            // Fecha a conexão e os recursos do banco de dados
            ConnectionFactory.closeConnection(connection, stmt, rs);
        }
        // Retorna a lista de carros
        return carros;
    }

    // Método para cadastrar um novo carro no banco de dados
    public void cadastrar(String marca, String modelo, String ano, String placa, String valor) {
        PreparedStatement stmt = null;

        // Adicione validação para o formato da placa
        if (!placa.matches("[a-zA-Z]{3}\\d{4}") && !placa.matches("[a-zA-Z]{3}[0-9][a-zA-Z]\\d{2}")) {
            JOptionPane.showMessageDialog(null, "Erro: A placa inserida não está no formato correto (LLLNNN ou LLLNLNN).");
            return;
        }

        // Adicione validação para o intervalo de anos
        int anoInt;
        try {
            anoInt = Integer.parseInt(ano);
            if (anoInt < 1886 || anoInt > 2023) {
                JOptionPane.showMessageDialog(null, "Erro: O ano do carro deve estar entre 1886 e 2023.");
                return; // Saia do método se o ano estiver fora do intervalo desejado
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: O ano do carro deve ser um número válido.");
            return; // Saia do método se o ano não for um número válido
        }

        String sql = "INSERT INTO carros_lojacarros (marca, modelo, ano, placa, valor) VALUES (?, ?, ?, ?, ?)";
        try {
            // Executa a inserção no banco de dados
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, marca);
            stmt.setString(2, modelo);
            stmt.setString(3, ano);
            stmt.setString(4, placa);
            stmt.setString(5, valor);
            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso");
            JOptionPane.showMessageDialog(null, "Anúncio registrado");
        } catch (SQLException e) {
            // Trata exceções específicas (por exemplo, violação de chave única) e exibe mensagens adequadas
            if (e.getSQLState().equals("23505")) {
                JOptionPane.showMessageDialog(null, "Erro: A placa inserida já existe na tabela.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao inserir dados no banco de dados: " + e.getMessage());
            }
        } finally {
            // Fecha a conexão
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    // Método para atualizar dados de um carro no banco de dados
    public void update(String marca, String modelo, String ano, String placa, String valor) {
        PreparedStatement stmt = null;

        String sql = "UPDATE carros_lojacarros SET marca = ?, modelo = ?, ano = ?, valor = ? WHERE placa = ?";
        try {
            // Executa a atualização no banco de dados
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, marca);
            stmt.setString(2, modelo);
            stmt.setString(3, ano);
            stmt.setString(4, valor);
            stmt.setString(5, placa);
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

    // Método para apagar um carro do banco de dados
    public void delete(String placa) {
        PreparedStatement stmt = null;
        String sql = "DELETE FROM carros_lojacarros WHERE placa = ?";
        try {
            // Executa a exclusão no banco de dados
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, placa);
            stmt.executeUpdate();
            System.out.println("Dado apagado com sucesso");
        } catch (SQLException e) {
            // Lança uma exceção em caso de erro na exclusão
            throw new RuntimeException("Erro ao apagar dados no banco de dados.", e);
        } finally {
            // Fecha a conexão
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }
}
