package View;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Controller.CarrosControl;
import Controller.CarrosDAO;

import java.awt.AWTException;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import Model.Carros;

import java.lang.Object;

public class CarrosPainel extends JPanel {

    private JButton cadastrar, apagar, editar;
    private JTextField carMarcaField, carModeloField, carAnoField, carPlacaField, carValorField;
    private List<Carros> carros;
    private JTable table;
    private DefaultTableModel tableModel;
    private int linhaSelecionada = -1;

    public CarrosPainel() {
        super();

        // Configuração do layout principal
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Cadastro Carros"));

        // Configuração do painel de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Marca"));
        carMarcaField = new JTextField(20);
        inputPanel.add(carMarcaField);
        inputPanel.add(new JLabel("Modelo"));
        carModeloField = new JTextField(20);
        inputPanel.add(carModeloField);
        inputPanel.add(new JLabel("Ano"));
        carAnoField = new JTextField(20);
        inputPanel.add(carAnoField);
        inputPanel.add(new JLabel("Placa"));
        carPlacaField = new JTextField(20);
        inputPanel.add(carPlacaField);
        inputPanel.add(new JLabel("Valor"));
        carValorField = new JTextField(20);
        inputPanel.add(carValorField);
        add(inputPanel);

        // Configuração dos botões
        JPanel botoes = new JPanel();
        botoes.add(cadastrar = new JButton("Cadastrar"));
        botoes.add(editar = new JButton("Editar"));
        botoes.add(apagar = new JButton("Apagar"));
        add(botoes);

        // Configuração da tabela
        JScrollPane jSPane = new JScrollPane();
        add(jSPane);
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "Marca", "Modelo", "Ano", "Placa", "Valor" });
        table = new JTable(tableModel);
        jSPane.setViewportView(table);

        // Criação da tabela no banco de dados
        new CarrosDAO().createTable();

        // Atualização inicial da tabela
        atualizarTabela();

        // Configuração de evento de clique na tabela
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                linhaSelecionada = table.rowAtPoint(evt.getPoint());
                if (linhaSelecionada != -1) {
                    carMarcaField.setText((String) table.getValueAt(linhaSelecionada, 0));
                    carModeloField.setText((String) table.getValueAt(linhaSelecionada, 1));
                    carAnoField.setText((String) table.getValueAt(linhaSelecionada, 2));
                    carPlacaField.setText((String) table.getValueAt(linhaSelecionada, 3));
                    carValorField.setText((String) table.getValueAt(linhaSelecionada, 4));
                }
            }
        });

        // Configuração de operações de controle
        CarrosControl operacoes = new CarrosControl(carros, tableModel, table);

        // Configuração de ações para botões
        cadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica se os campos estão preenchidos
                if (carMarcaField.getText().isEmpty() || carModeloField.getText().isEmpty()
                        || carAnoField.getText().isEmpty()
                        || carPlacaField.getText().isEmpty() || carValorField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "ATENÇÃO! \nExiste campos em branco");
                } else {
                    // Verifica se o ano e o valor são numéricos
                    if (!carAnoField.getText().matches("[0-9]+")) {
                        JOptionPane.showMessageDialog(null, "O campo 'Ano' deve obter apenas números.");
                    } else if (!carValorField.getText().matches("[0-9]+")) {
                        JOptionPane.showMessageDialog(null, "O campo 'Valor' deve obter apenas números.");
                    } else {
                        // Executa a operação de cadastrar
                        operacoes.cadastrar(carMarcaField.getText(), carModeloField.getText(), carAnoField.getText(),
                                carPlacaField.getText(), carValorField.getText());

                        // Limpa os campos após o cadastro
                        carMarcaField.setText("");
                        carModeloField.setText("");
                        carAnoField.setText("");
                        carPlacaField.setText("");
                        carValorField.setText("");
                    }
                }
            }
        });

        editar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica se a placa está vazia
                if (carPlacaField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Selecione algo para editar");
                } else {
                    // Executa a operação de atualizar
                    operacoes.atualizar(carMarcaField.getText(), carModeloField.getText(), carAnoField.getText(),
                            carPlacaField.getText(), carValorField.getText());

                    // Limpa os campos após a edição
                    carMarcaField.setText("");
                    carModeloField.setText("");
                    carAnoField.setText("");
                    carPlacaField.setText("");
                    carValorField.setText("");
                    JOptionPane.showMessageDialog(null, "Informação editada com Sucesso!");
                }
            }
        });

        apagar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica se a placa está vazia
                if (carPlacaField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Selecione um carro para apagar.");
                } else {
                    // Pede confirmação ao usuário
                    int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja apagar o anúncio?",
                            "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (resposta == JOptionPane.YES_OPTION) {
                        // Executa a operação de apagar
                        operacoes.apagar(carPlacaField.getText());
                        JOptionPane.showMessageDialog(null, "O Carro " + carModeloField.getText() + " de placa "
                                + carPlacaField.getText() + " foi excluído!");

                        // Limpa os campos após a exclusão
                        carMarcaField.setText("");
                        carModeloField.setText("");
                        carAnoField.setText("");
                        carPlacaField.setText("");
                        carValorField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "O carro não foi excluído!");
                    }
                }
            }
        });
    }

    // Método para atualizar a tabela de carros
    private void atualizarTabela() {
        // Atualiza a tabela com os dados do banco de dados
        tableModel.setRowCount(0);
        carros = new CarrosDAO().listAll();
        for (Carros carro : carros) {
            tableModel.addRow(new Object[] { carro.getMarca(), carro.getModelo(), carro.getAno(), carro.getPlaca(),
                    carro.getValor() });
        }
    }
}
