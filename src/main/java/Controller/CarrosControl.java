package Controller;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Model.Carros;

public class CarrosControl {

    // Lista de carros
    private List<Carros> carros; 

    // Modelo de tabela padrão para a GUI
    private DefaultTableModel tableModel; 

    // Tabela associada na GUI
    private JTable table; 

    // Construtor da classe CarrosControl
    public CarrosControl(List<Carros> carros, DefaultTableModel tableModel, JTable table) {
        this.carros = carros; 
        this.tableModel = tableModel; 
        this.table = table; 
    }

    // Método privado para atualizar a tabela na GUI com os dados da lista de carros
    private void updateTable() {
        // Limpa todas as linhas existentes na tabela
        tableModel.setRowCount(0); 

        // Obtém a lista atualizada de carros do banco de dados
        carros = new CarrosDAO().listAll(); 

        // Adiciona cada carro na lista à tabela na GUI
        for (Carros carro : carros) {
            tableModel.addRow(new Object[] { 
                carro.getMarca(), 
                carro.getModelo(), 
                carro.getAno(), 
                carro.getPlaca(), 
                carro.getValor() 
            });
        }
    }

    // Método público para cadastrar um novo carro
    public void cadastrar(String marca, String modelo, String ano, String placa, String valor) {
        // Chama o método de cadastro na classe DAO para inserir o carro no banco de dados
        new CarrosDAO().cadastrar(marca, modelo, ano, placa, valor);
        
        // Atualiza a tabela na GUI com os dados atualizados do banco de dados
        updateTable(); 
    }

    // Método público para atualizar informações de um carro existente
    public void atualizar(String marca, String modelo, String ano, String placa, String valor) {
        // Chama o método de atualização na classe DAO para modificar os dados no banco de dados
        new CarrosDAO().update(marca, modelo, ano, placa, valor); 
        
        // Atualiza a tabela na GUI com os dados atualizados do banco de dados
        updateTable(); 
    }

    // Método público para apagar um carro existente
    public void apagar(String placa) {
        // Chama o método de exclusão na classe DAO para remover o carro do banco de dados
        new CarrosDAO().delete(placa);
        
        // Atualiza a tabela na GUI com os dados atualizados do banco de dados
        updateTable(); 
    }

}
