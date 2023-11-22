package View;

import java.util.ArrayList;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import Controller.CarrosDAO;
import Controller.ClientesControl;
import Controller.ClientesDAO;
import Model.Carros;
import Model.Clientes;

public class ClientesPainel extends JPanel {

    // Campos de entrada de texto
    private JTextField inputCpf;
    private JTextField inputNome;
    private JTextField inputTelefone;
    private JTextField inputCidade;

    // Rótulos para os campos de entrada
    private JLabel labelCpf;
    private JLabel labelNome;
    private JLabel labelTelefone;
    private JLabel labelCidade;

    // Modelo e tabela para exibir os dados dos clientes
    private DefaultTableModel tableModel;
    private JTable table;

    // Lista de clientes
    private List<Clientes> clientes = new ArrayList<>();

    // Índice da linha selecionada na tabela
    private int linhaSelecionada = -1;

    // Botões para interação
    private JButton cadastrarButton, apagarButton, editarButton;

    // Construtor
    public ClientesPainel() {
        // Painéis para organização
        JPanel painel1 = new JPanel();
        JPanel inputPanel = new JPanel();
        JPanel buttons = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        painel1.setLayout(new BorderLayout());

        // Configuração da tabela
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Cpf");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Telefone");
        tableModel.addColumn("Cidade");
        table = new JTable(tableModel);
        table.setBackground(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);

        // Configuração dos campos de entrada
        inputCpf = new JTextField(20);
        inputCpf.setFont(new Font("Arial", Font.PLAIN, 16));

        inputNome = new JTextField(20);
        inputNome.setFont(new Font("Arial", Font.PLAIN, 16));

        inputTelefone = new JTextField(20);
        inputTelefone.setFont(new Font("Arial", Font.PLAIN, 16));

        inputCidade = new JTextField(20);
        inputCidade.setFont(new Font("Arial", Font.PLAIN, 16));

        // Configuração dos rótulos
        labelCpf = new JLabel("CPF");
        labelCpf.setFont(new Font("Arial", Font.PLAIN, 16));

        labelNome = new JLabel("Nome");
        labelNome.setFont(new Font("Arial", Font.PLAIN, 16));

        labelTelefone = new JLabel("Telefone");
        labelTelefone.setFont(new Font("Arial", Font.PLAIN, 16));

        labelCidade = new JLabel("Cidade");
        labelCidade.setFont(new Font("Arial", Font.PLAIN, 16));

        // Configuração dos botões
        cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.setFont(new Font("Arial", Font.PLAIN, 16));
        cadastrarButton.setBackground(Color.GREEN);

        editarButton = new JButton("Editar");
        editarButton.setFont(new Font("Arial", Font.PLAIN, 16));

        apagarButton = new JButton("Apagar");
        apagarButton.setFont(new Font("Arial", Font.PLAIN, 16));
        apagarButton.setBackground(Color.RED);

        // Adicionar configurações semelhantes para os outros botões

        // Adicionar elementos aos painéis
        inputPanel.add(labelCpf);
        inputPanel.add(inputCpf);
        inputPanel.add(labelNome);
        inputPanel.add(inputNome);
        inputPanel.add(labelTelefone);
        inputPanel.add(inputTelefone);
        inputPanel.add(labelCidade);
        inputPanel.add(inputCidade);

        buttons.add(cadastrarButton);
        buttons.add(editarButton);
        buttons.add(apagarButton);

        this.add(painel1);
        painel1.add(scrollPane, BorderLayout.CENTER);
        painel1.add(inputPanel, BorderLayout.NORTH);
        painel1.add(buttons, BorderLayout.SOUTH);

        // Inicialização da tabela e atualização dos dados
        new ClientesDAO().createTable();
        atualizarTabela();

        // Configuração do ouvinte de eventos do mouse para a tabela
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                linhaSelecionada = table.rowAtPoint(evt.getPoint());
                if (linhaSelecionada != -1) {
                    inputCpf.setText((String) table.getValueAt(linhaSelecionada, 0));
                    inputNome.setText((String) table.getValueAt(linhaSelecionada, 1));
                    inputTelefone.setText((String) table.getValueAt(linhaSelecionada, 2));
                    inputCidade.setText((String) table.getValueAt(linhaSelecionada, 3));
                }
            }
        });

        // Instância do controlador para operações nos clientes
        ClientesControl operacoes = new ClientesControl(clientes, tableModel, table);

        // Configuração do ouvinte de eventos para o botão de cadastrar
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verificação e validação dos campos antes de cadastrar
                if (inputCpf.getText().isEmpty() || inputNome.getText().isEmpty()
                        || inputTelefone.getText().isEmpty() || inputCidade.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "ATENÇÃO! \nPreencha todos os campos!");
                } else {
                    if (!validarFormatoCPF(inputCpf.getText())) {
                        JOptionPane.showMessageDialog(null,
                                "CPF inválido! O CPF deve conter apenas números e ter 11 dígitos.");
                    } else if (!validarFormatoTelefone(inputTelefone.getText())) {
                        JOptionPane.showMessageDialog(null,
                                "Número de telefone inválido! O formato correto é DDDxxxxxxxxx.");
                    } else {
                        // Chamada do método para cadastrar o cliente
                        operacoes.cadastrar(inputCpf.getText(), inputNome.getText(), inputTelefone.getText(),
                                inputCidade.getText());

                        // Limpar campos após cadastrar
                        inputCpf.setText("");
                        inputNome.setText("");
                        inputTelefone.setText("");
                        inputCidade.setText("");
                    }
                }
            }
        });

        // Configuração do ouvinte de eventos para o botão de editar
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verificação antes de editar
                if (inputCpf.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Selecione algo para editar");
                } else {
                    // Chamada do método para editar
                    operacoes.update(labelCpf.getText(), labelNome.getText(), labelTelefone.getText(),
                            labelCidade.getText());

                    // Limpar campos após editar
                    inputCpf.setText("");
                    inputNome.setText("");
                    inputTelefone.setText("");
                    inputCidade.setText("");
                    JOptionPane.showMessageDialog(null, "Informação editada com Sucesso!");
                }
            }
        });

        // Configuração do ouvinte de eventos para o botão de apagar
        apagarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verificação antes de apagar
                if (inputCpf.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Selecione um cliente para apagar.");
                } else {
                    // Confirmação antes de apagar
                    int resposta = JOptionPane.showConfirmDialog(null,
                            "Tem certeza de que deseja apagar o cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (resposta == JOptionPane.YES_OPTION) {
                        // Chamada do método para apagar
                        operacoes.delete(inputCpf.getText());
                        JOptionPane.showMessageDialog(null,
                                "O Cliente " + inputNome.getText() + " de CPF " + inputCpf.getText() + " foi apagado!");
                        // Limpar campos após apagar
                        inputCpf.setText("");
                        inputNome.setText("");
                        inputTelefone.setText("");
                        inputCidade.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "O cliente não foi apagado!");
                    }
                }
            }
        });
    }

    // Método para atualizar a tabela com os dados mais recentes
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        clientes = new ClientesDAO().listAll();
        for (Clientes cliente : clientes) {
            tableModel.addRow(
                    new Object[] { cliente.getCpf(), cliente.getNome(), cliente.getTelefone(), cliente.getCidade() });
        }
    }

    // Método para validar o formato do CPF
    private boolean validarFormatoCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.length() == 11;
    }

    // Método para validar o formato do telefone
    private boolean validarFormatoTelefone(String telefone) {
        telefone = telefone.replaceAll("[^0-9]", "");
        return telefone.length() == 11;
    }
}
