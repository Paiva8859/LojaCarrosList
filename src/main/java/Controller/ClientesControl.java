package Controller;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Model.Carros;
import Model.Clientes;

public class ClientesControl {
    // Lista de clientes
    private List<Clientes> clientes;

    // Modelo de tabela padrão para a GUI
    private DefaultTableModel tableModel;

    // Tabela associada na GUI
    private JTable table;

    // Construtor da classe ClientesControl
    public ClientesControl(List<Clientes> clientes, DefaultTableModel tableModel, JTable table) {
        this.clientes = clientes;
        this.tableModel = tableModel;
        this.table = table;
    }

    // Método privado para atualizar a tabela na GUI com os dados da lista de clientes
    private void updateTable() {
        // Limpa todas as linhas existentes na tabela
        tableModel.setRowCount(0);

        // Obtém a lista atualizada de clientes do banco de dados
        clientes = new ClientesDAO().listAll();

        // Adiciona cada cliente na lista à tabela na GUI
        for (Clientes cliente : clientes) {
            tableModel.addRow(
                new Object[] { cliente.getCpf(), cliente.getNome(), cliente.getTelefone(), cliente.getCidade() });
        }
    }

    // Método público para cadastrar um novo cliente
    public void cadastrar(String cpf, String nome, String telefone, String cidade) {
        // Chama o método de cadastro na classe DAO para inserir o cliente no banco de dados
        new ClientesDAO().cadastrar(cpf, nome, telefone, cidade);

        // Atualiza a tabela na GUI com os dados atualizados do banco de dados
        updateTable();
    }

    // Método público para atualizar informações de um cliente existente
    public void update(String cpf, String nome, String telefone, String cidade) {
        // Chama o método de atualização na classe DAO para modificar os dados no banco de dados
        new ClientesDAO().update(cpf, nome, telefone, cidade);

        // Atualiza a tabela na GUI com os dados atualizados do banco de dados
        updateTable();
    }

    // Método público para apagar um cliente existente
    public void delete(String cpf) {
        // Chama o método de exclusão na classe DAO para remover o cliente do banco de dados
        new ClientesDAO().delete(cpf);

        // Atualiza a tabela na GUI com os dados atualizados do banco de dados
        updateTable();
    }
}
