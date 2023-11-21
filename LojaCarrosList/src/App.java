import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int opcao = 1;
        String marca = "", modelo, ano, cor;
        String listar = "";
        boolean rodando = true;
        String pesquisa = "";

        // ARRAYLIST
        List<Carros> listaCarros = new ArrayList<>();

        while (rodando) {
            System.out.println(
                    "Bem vindo à loja de carros do Carlos \n O que deseja? \n 1- Cadastrar novo carro. \n 2- Listar todos os carros cadastrados \n 3- Consultar um carro cadastrado. \n 4- Apagar um carro cadastrado");

            do {
                opcao = sc.nextInt();

                if (opcao > 4 || opcao < 1) {
                    System.out.println("Operação inválida! Escolha outra opção");
                }
            } while (opcao > 4 || opcao < 1);

            switch (opcao) {
                case 1:
                    System.out.println("Insira a marca do carro");
                    marca = sc.next();
                    System.out.println("Insira o modelo do carro");
                    modelo = sc.next();
                    System.out.println("Insira o ano do carro");
                    ano = sc.next();
                    System.out.println("Insira a cor do carro");
                    cor = sc.next();

                    Carros c = new Carros(marca, modelo, ano, cor);

                    listaCarros.add(c);

                    System.out.println("Carro registrado!");

                    break;

                case 2:
                    for (Carros carros : listaCarros) {
                        listar += "-" + carros.getMarca() + " " + carros.getModelo() + " " + carros.getAno() + " "
                                + carros.getCor();
                        System.out.println(listar);
                        listar = "";
                    }
                case 3:
                    System.out.println("Insira a marca do carro que deseja consultar.");
                    pesquisa = sc.next();

                    for (Carros carros : listaCarros) {
                        if (pesquisa.equals(carros.getMarca())) {
                            listar += "-" + carros.getMarca() + " " + carros.getModelo() + " " + carros.getAno() + " "
                                    + carros.getCor();
                            System.out.println(listar);
                            listar = "";
                        }
                    }
                default:
                    break;
            }
        }
        sc.close();
    }
}
